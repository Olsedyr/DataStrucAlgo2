import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Arrays;

public class BigOEstimator extends JPanel {

    interface TestFunction {
        void run(int n);
    }

    // --- Parametre ---
    private static final int WARMUP_ROUNDS = 15;
    private static final int REPEAT = 15;
    private static final int INNER_REPEAT = 100; // Run function multiple times per measurement
    private static final int[] TEST_SIZES_USED = {100, 200, 500, 1000, 2000, 3000, 4000, 6000, 8000, 10000};
    private static final int[] TEST_SIZES_SMALL = {10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100};
    private static final int[] Medium = {100, 200, 500, 1000, 2000, 3000, 4000, 6000, 8000, 10000};
    private static final int[] TEST_SIZES_BIG = {100, 200, 500, 1000, 2000, 3000, 4000, 6000, 8000, 10000, 32000, 64000};
    private static final int PADDING = 60;

    // Data arrays
    private final int[] ns;
    private final double[] times;

    // Models - nu med 6 modeller (inklusive O(n³))
    private final double[] a = new double[6];
    private final double[] b = new double[6];
    private final double[] r2 = new double[6];
    private final double[] adjR2 = new double[6];

    private double logLogA, logLogB, logLogR2;

    private final TestFunction func;

    public BigOEstimator(TestFunction func) {
        this.func = func;
        this.ns = TEST_SIZES_USED;
        this.times = new double[ns.length];

        measureTimes();
        fitModels();
        fitLogLogModel();
    }

    private void measureTimes() {
        System.out.println("Måler tider (med warmup og median)...");

        // Warmup JIT compiler
        System.out.println("Warming up JIT...");
        for (int w = 0; w < WARMUP_ROUNDS; w++) {
            for (int r = 0; r < INNER_REPEAT; r++) {
                func.run(ns[ns.length / 2]);
            }
        }

        for (int i = 0; i < ns.length; i++) {
            int n = ns[i];
            double[] measurements = new double[REPEAT];

            for (int r = 0; r < REPEAT; r++) {
                System.gc();
                try { Thread.sleep(50); } catch (InterruptedException e) {}

                long start = System.nanoTime();
                // Run multiple times to get measurable time
                for (int inner = 0; inner < INNER_REPEAT; inner++) {
                    func.run(n);
                }
                long end = System.nanoTime();
                measurements[r] = (end - start) / 1_000_000.0 / INNER_REPEAT; // Average per run
            }

            // Use median to reduce outlier impact
            Arrays.sort(measurements);
            times[i] = measurements[REPEAT / 2];

            System.out.printf("n=%d, median tid=%.6f ms%n", n, times[i]);
        }
    }

    private void fitModels() {
        for (int model = 0; model < 6; model++) {
            double[] x = new double[ns.length];
            for (int i = 0; i < ns.length; i++) {
                int n = ns[i];
                switch (model) {
                    case 0 -> x[i] = 1.0;
                    case 1 -> x[i] = Math.log(n);
                    case 2 -> x[i] = n;
                    case 3 -> x[i] = n * Math.log(n);
                    case 4 -> x[i] = (double) n * n;
                    case 5 -> x[i] = (double) n * n * n; // O(n³)
                }
            }
            double[] fit = linearRegression(x, times);
            a[model] = fit[0];
            b[model] = fit[1];
            r2[model] = fit[2];

            int p = 1;
            int nPoints = ns.length;
            adjR2[model] = 1 - (1 - r2[model]) * (nPoints - 1) / (nPoints - p - 1);
        }

        System.out.println();
        System.out.println("Resultater for modellerne:");
        String[] modelNames = {"Konstant O(1)", "Logaritmisk O(log n)", "Lineær O(n)",
                "N log n O(n log n)", "Kvadratisk O(n²)", "Kubisk O(n³)"};
        for (int i = 0; i < 6; i++) {
            System.out.printf("%s: a=%.8f, b=%.6f, R²=%.6f, Adj R²=%.6f%n",
                    modelNames[i], a[i], b[i], r2[i], adjR2[i]);
        }
    }

    private void fitLogLogModel() {
        double[] logN = new double[ns.length];
        double[] logT = new double[ns.length];
        int count = 0;

        // Only use data points where we have positive measurements
        for (int i = 0; i < ns.length; i++) {
            if (times[i] > 1e-6) { // Filter out near-zero times
                logN[count] = Math.log(ns[i]);
                logT[count] = Math.log(times[i]);
                count++;
            }
        }

        if (count < 3) {
            logLogA = 0;
            logLogB = 0;
            logLogR2 = 0;
            System.out.println("\nInsufficient data for log-log fit");
            return;
        }

        logN = Arrays.copyOf(logN, count);
        logT = Arrays.copyOf(logT, count);

        double[] fit = linearRegression(logN, logT);
        logLogA = fit[0];
        logLogB = fit[1];
        logLogR2 = fit[2];

        System.out.println();
        System.out.printf("Log-log fit: k=%.5f, c=%.5f, R²=%.5f%n", logLogA, logLogB, logLogR2);
        System.out.printf("Estimeret kompleksitet: O(n^%.3f)%n", logLogA);

        // Interpret the exponent with tighter bounds - tilføj kubisk
        String interpretation;
        if (logLogA < 0.3) interpretation = "konstant eller sub-logaritmisk";
        else if (logLogA < 0.7) interpretation = "logaritmisk O(log n)";
        else if (logLogA < 1.05) interpretation = "lineær O(n)";
        else if (logLogA < 1.40) interpretation = "n log n - O(n log n)";
        else if (logLogA < 1.7) interpretation = "superlineær, under kvadratisk";
        else if (logLogA < 2.2) interpretation = "kvadratisk O(n²)";
        else if (logLogA < 3.0) interpretation = "kubisk eller mellem kvadratisk og kubisk";
        else interpretation = "højere end kubisk";

        System.out.printf("Fortolkning baseret på log-log: %s%n", interpretation);
    }

    private int bestFitIndex() {
        // Use log-log estimate as primary guide when R² is high
        int best = 0;
        double bestScore = -1;

        for (int i = 0; i < adjR2.length; i++) {
            if (a[i] < 0 && i > 0) continue; // Skip models with negative growth

            double score = adjR2[i];

            // If log-log fit is very good, use it to strongly guide selection
            if (logLogR2 > 0.95) {
                double expectedExponent = switch (i) {
                    case 0 -> 0.0;
                    case 1 -> 0.5;
                    case 2 -> 1.0;
                    case 3 -> 1.2; // n log n typically shows as ~1.1-1.3
                    case 4 -> 2.0;
                    case 5 -> 3.0; // O(n³)
                    default -> 0;
                };

                double exponentDiff = Math.abs(logLogA - expectedExponent);

                // Strong bonus for matching log-log prediction
                if (exponentDiff < 0.02) {
                    score += 0.02; // Very significant bonus
                }
            }

            if (score > bestScore) {
                bestScore = score;
                best = i;
            }
        }
        return best;
    }

    private static double[] linearRegression(double[] x, double[] y) {
        int n = x.length;
        double sumX=0, sumY=0, sumXY=0, sumX2=0;

        for (int i=0; i<n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i]*y[i];
            sumX2 += x[i]*x[i];
        }

        double denom = n*sumX2 - sumX*sumX;
        if (Math.abs(denom) < 1e-10) return new double[]{0,0,0};

        double a = (n*sumXY - sumX*sumY)/denom;
        double b = (sumY*sumX2 - sumX*sumXY)/denom;

        // Calculate R^2
        double meanY = sumY/n;
        double ssTot=0, ssRes=0;
        for (int i=0; i<n; i++) {
            double fitY = a*x[i] + b;
            ssTot += (y[i] - meanY)*(y[i] - meanY);
            ssRes += (y[i] - fitY)*(y[i] - fitY);
        }
        double r2 = Math.abs(ssTot) < 1e-10 ? 0 : 1 - ssRes/ssTot;

        // Clamp R^2 to [0, 1]
        r2 = Math.max(0, Math.min(1, r2));

        return new double[]{a,b,r2};
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawPlot((Graphics2D) g);
    }

    private void drawPlot(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();

        g2.setColor(Color.white);
        g2.fillRect(0, 0, w, h);

        int left = PADDING;
        int right = w - PADDING;
        int top = PADDING;
        int bottom = h - PADDING;

        g2.setColor(Color.black);
        g2.drawLine(left, bottom, right, bottom);
        g2.drawLine(left, bottom, left, top);

        g2.drawString("Input size n", (w/2) - 30, h - 20);
        g2.drawString("Time (ms)", 10, top + 20);

        double maxN = ns[ns.length-1];
        double maxT = Arrays.stream(times).max().orElse(1) * 1.1; // Add 10% headroom

        // Plot data points
        g2.setColor(new Color(0, 0, 200));
        for (int i = 0; i < ns.length; i++) {
            int x = left + (int)((ns[i]/maxN) * (right - left));
            int y = bottom - (int)((times[i]/maxT) * (bottom - top));
            g2.fillOval(x-5, y-5, 10, 10);
            g2.setColor(Color.black);
            g2.drawOval(x-5, y-5, 10, 10);
            g2.setColor(new Color(0, 0, 200));
        }

        // Plot models - opdateret til 6 modeller
        String[] modelNames = {"O(1)", "O(log n)", "O(n)", "O(n log n)", "O(n²)", "O(n³)"};
        Color[] modelColors = {
                new Color(255, 100, 100),    // Rød - O(1)
                new Color(255, 165, 0),      // Orange - O(log n)
                new Color(255, 0, 255),      // Magenta - O(n)
                new Color(0, 150, 0),        // Grøn - O(n log n)
                new Color(0, 150, 150),      // Cyan - O(n²)
                new Color(150, 0, 150)       // Lilla - O(n³)
        };

        int best = bestFitIndex();

        for (int model = 0; model < 6; model++) {
            if (a[model] < 0 && model > 0) continue; // Skip invalid models

            g2.setColor(modelColors[model]);
            float strokeWidth = (model == best) ? 3.0f : 1.5f;
            g2.setStroke(new BasicStroke(strokeWidth));

            Path2D.Double path = new Path2D.Double();

            for (int i = 0; i < ns.length; i++) {
                double xVal = ns[i];
                double yVal = switch (model) {
                    case 0 -> a[model] + b[model];
                    case 1 -> a[model]*Math.log(xVal) + b[model];
                    case 2 -> a[model]*xVal + b[model];
                    case 3 -> a[model]*xVal*Math.log(xVal) + b[model];
                    case 4 -> a[model]*xVal*xVal + b[model];
                    case 5 -> a[model]*xVal*xVal*xVal + b[model]; // O(n³)
                    default -> 0;
                };

                int xPixel = left + (int)((xVal/maxN) * (right - left));
                int yPixel = bottom - (int)((yVal/maxT) * (bottom - top));

                if (i == 0) path.moveTo(xPixel, yPixel);
                else path.lineTo(xPixel, yPixel);
            }
            g2.draw(path);
        }

        // Draw legend - opdateret til 6 modeller
        int legendX = right - 200;
        int legendY = top + 20;
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(245, 245, 245));
        g2.fillRect(legendX - 10, legendY - 15, 190, 180);
        g2.setColor(Color.BLACK);
        g2.drawRect(legendX - 10, legendY - 15, 190, 180);

        g2.drawString("Modeller:", legendX, legendY);
        for (int i = 0; i < modelNames.length; i++) {
            if (a[i] < 0 && i > 0) continue;

            g2.setColor(modelColors[i]);
            g2.fillRect(legendX, legendY + 10 + 18*i, 14, 14);
            g2.setColor(Color.BLACK);

            String label = String.format("%s (%.3f)%s",
                    modelNames[i], adjR2[i], (i == best ? " ★" : ""));
            g2.drawString(label, legendX + 20, legendY + 22 + 18*i);
        }

        g2.setColor(Color.BLACK);
        g2.drawString(String.format("Log-Log: O(n^%.2f)", logLogA), legendX, legendY + 125);
        g2.drawString(String.format("R² = %.3f", logLogR2), legendX, legendY + 140);
    }

    public static void main(String[] args) {
        TestFunction func = n -> {
            long x = 0;
            // O(n³) algoritme - husk at reducere TEST_SIZES for eksponentiel!


            for (int i = 0; i < n; i++)
            {
                for (int j = 0; j < n; j += j+1)
                {
                    for (int k = 0; k < n; k +=(i+j+5))
                    {
                        x++;
                    }
                }
                if (i > 1024)
                    i++;
            }




            // Prevent dead code elimination
            if (x == Long.MAX_VALUE) System.out.println(x);
        };



        SwingUtilities.invokeLater(() -> {
            BigOEstimator panel = new BigOEstimator(func);

            JFrame frame = new JFrame("Big O Estimator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            int best = panel.bestFitIndex();
            String[] modelNames = {"Konstant O(1)", "Logaritmisk O(log n)", "Lineær O(n)",
                    "N log n O(n log n)", "Kvadratisk O(n²)", "Kubisk O(n³)"};
            System.out.println();
            System.out.println("═══════════════════════════════════════");
            System.out.printf("BEDSTE FIT: %s%n", modelNames[best]);
            System.out.printf("Adjusted R² = %.6f%n", panel.adjR2[best]);

            System.out.println("───────────────────────────────────────");

            // Show top 3 models for comparison
            System.out.println("Top 3 modeller efter Adj R²:");
            int[] indices = {0, 1, 2, 3, 4, 5};
            for (int i = 0; i < indices.length - 1; i++) {
                for (int j = i + 1; j < indices.length; j++) {
                    if (panel.adjR2[indices[j]] > panel.adjR2[indices[i]]) {
                        int temp = indices[i];
                        indices[i] = indices[j];
                        indices[j] = temp;
                    }
                }
            }
            for (int i = 0; i < 3; i++) {
                int idx = indices[i];
                System.out.printf("%d. %s (Adj R² = %.6f)%s%n",
                        i+1, modelNames[idx], panel.adjR2[idx],
                        idx == best ? " ← VALGT" : "");
            }
            System.out.println("═══════════════════════════════════════");
        });
    }
}