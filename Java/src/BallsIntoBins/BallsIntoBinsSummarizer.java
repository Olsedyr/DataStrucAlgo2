package BallsIntoBins;

import java.util.*;

public class BallsIntoBinsSummarizer {

    // ========== KONFIGURATION - ÆNDR HER ==========
    private static final int[] BIN_SIZES = {10007, 32749};  // Test disse størrelser
    private static final int TRIALS = 100;                  // Antal gentagelser per eksperiment
    private static final boolean VERBOSE = true;           // Vis detaljeret output

    // Teoretisk analyse parametre
    private static final int[] THEORETICAL_TEST_SIZES = {100, 1000, 10000, 32749, 100000};

    // Theorem 5.2 test parametre
    private static final int[] THEOREM_TEST_SIZES = {50, 100, 200};

    // Belastningsfordeling parametre
    private static final int DISTRIBUTION_N = 10000;       // N for fordelingstest
    private static final int DISTRIBUTION_TRIALS = 100;    // Trials for fordelingstest
    // ========== SLUT KONFIGURATION ==========

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║             BALLS'N BINS KOMPLET ANALYSE                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        System.out.println("Konfiguration:");
        System.out.println("  Test størrelser: " + Arrays.toString(BIN_SIZES));
        System.out.println("  Antal forsøg per test: " + TRIALS);
        System.out.println("  Detaljeret output: " + (VERBOSE ? "Ja" : "Nej"));
        System.out.println();

        // Kør alle analyser
        runAllAnalyses();
    }

    private static void runAllAnalyses() {
        // 1. Klassisk Balls into Bins
        System.out.println("1. KLASSISK BALLS INTO BINS ANALYSE");
        System.out.println("======================================");
        for (int n : BIN_SIZES) {
            runClassicalAnalysis(n, "N = " + n);
        }

        // 2. Power of Two Choices
        System.out.println("\n\n2. POWER OF TWO CHOICES ANALYSE");
        System.out.println("=================================");
        for (int n : BIN_SIZES) {
            runTwoChoicesAnalysis(n, "N = " + n);
        }

        // 3. Sammenligning klassisk vs two choices
        System.out.println("\n\n3. SAMMENLIGNING: KLASSISK VS TWO CHOICES");
        System.out.println("============================================");
        for (int n : BIN_SIZES) {
            compareMethods(n, "N = " + n);
        }

        // 4. Teoretiske estimater
        System.out.println("\n\n4. BEKRÆFTELSE AF TEORETISKE ESTIMATER");
        System.out.println("========================================");
        verifyTheoreticalEstimates();

        // 5. Theorem 5.2
        System.out.println("\n\n5. THEOREM 5.2 TEST");
        System.out.println("====================");
        testTheorem52();

        // 6. Detaljeret belastningsfordeling
        if (VERBOSE) {
            System.out.println("\n\n6. DETALJERET BELASTNINGSFORDELING");
            System.out.println("==================================");
            analyzeLoadDistribution(DISTRIBUTION_N, DISTRIBUTION_TRIALS);
        }

        // 7. Kapacitetsanbefalinger
        System.out.println("\n\n7. KAPACITETSRÅDGIVNING");
        System.out.println("========================");
        provideCapacityRecommendations();
    }

    // 1. Klassisk Balls into Bins
    private static void runClassicalAnalysis(int n, String label) {
        System.out.println("\n--- " + label + " ---");

        double[] results = runTrials(n, TRIALS, false);
        double avgMax = results[0];
        double maxOfMax = results[1];
        double stdDev = results[2];
        double theoretical = theoreticalMaxClassical(n);

        System.out.printf("Gennemsnitligt maksimum: %.2f%n", avgMax);
        System.out.printf("Teoretisk estimat (log N / log log N): %.2f%n", theoretical);
        System.out.printf("Ratio empirisk/teoretisk: %.3f%n", avgMax / theoretical);

        // Anbefalinger
        System.out.println("\nAnbefalet beholder kapacitet:");
        System.out.printf("  - Minimum: %.0f (for at dække gennemsnit)%n", Math.ceil(avgMax));
        System.out.printf("  - Anbefalet: %.0f (95%% sikkerhed)%n", Math.ceil(avgMax + 1.96 * stdDev));
        System.out.printf("  - Ekstremt sikkert: %.0f (max observeret)%n", maxOfMax);
    }

    // 2. Power of Two Choices
    private static void runTwoChoicesAnalysis(int n, String label) {
        System.out.println("\n--- " + label + " ---");

        double[] results = runTrials(n, TRIALS, true);
        double avgMax = results[0];
        double maxOfMax = results[1];
        double stdDev = results[2];
        double theoretical = theoreticalMaxTwoChoices(n);

        System.out.printf("Gennemsnitligt maksimum: %.2f%n", avgMax);
        System.out.printf("Teoretisk estimat (log log N): %.2f%n", theoretical);
        System.out.printf("Ratio empirisk/teoretisk: %.3f%n", avgMax / theoretical);
        System.out.printf("Standardafvigelse: %.3f%n", stdDev);
    }

    // 3. Sammenligning
    private static void compareMethods(int n, String label) {
        System.out.println("\n--- " + label + " ---");

        double[] classical = runTrials(n, TRIALS, false);
        double[] twoChoices = runTrials(n, TRIALS, true);

        System.out.println("                     Klassisk    Two Choices   Forbedring");
        System.out.println("                     --------    -----------   ----------");
        System.out.printf("Gennemsnit max      %8.2f      %8.2f      %.1f%%%n",
                classical[0], twoChoices[0],
                (1 - twoChoices[0]/classical[0]) * 100);
        System.out.printf("Største max         %8.2f      %8.2f      %.1f%%%n",
                classical[1], twoChoices[1],
                (1 - twoChoices[1]/classical[1]) * 100);
        System.out.printf("Standardafvigelse   %8.2f      %8.2f      %.1f%%%n",
                classical[2], twoChoices[2],
                (1 - twoChoices[2]/classical[2]) * 100);
    }

    // 4. Teoretiske estimater
    private static void verifyTheoreticalEstimates() {
        System.out.println("\nTeoretiske vs Empiriske Maksimum Værdier:");
        System.out.println("N\t\tKlassisk\tTeori\t\tTwoChoices\tTeori\t\tForbedring");
        System.out.println("-------------------------------------------------------------------------------------");

        for (int n : THEORETICAL_TEST_SIZES) {
            double classicalEmpirical = runSingleTrial(n, 10, false);
            double twoChoicesEmpirical = runSingleTrial(n, 10, true);
            double classicalTheoretical = theoreticalMaxClassical(n);
            double twoChoicesTheoretical = theoreticalMaxTwoChoices(n);

            System.out.printf("%-10d\t%-8.2f\t%-8.2f\t%-8.2f\t%-8.2f\t%.1fx%n",
                    n, classicalEmpirical, classicalTheoretical,
                    twoChoicesEmpirical, twoChoicesTheoretical,
                    classicalEmpirical / twoChoicesEmpirical);
        }
    }

    // 5. Theorem 5.2 test
    private static void testTheorem52() {
        System.out.println("\nTheorem 5.2: N bolde i N² beholdere");
        System.out.println("Sandsynlighed for ingen kollisioner:");
        System.out.println("N\tM=N²\t\tP(ingen kollision)\t< 0.5?");
        System.out.println("------------------------------------------------------");

        for (int n : THEOREM_TEST_SIZES) {
            int m = n * n;
            int trials = 1000;
            int success = 0;

            for (int t = 0; t < trials; t++) {
                if (runTheoremTrial(n, m)) success++;
            }

            double probability = (double) success / trials;
            System.out.printf("%d\t%d\t\t%.4f\t\t\t%s%n",
                    n, m, probability, probability < 0.5 ? "✓" : "✗");
        }

        // Teoretisk beregning for større N
        System.out.println("\nTeoretisk beregning:");
        System.out.println("For N → ∞: P ≈ e^(-N²/2M) = e^(-1/2) ≈ 0.6065");
        System.out.println("Konklusion: Theorem gælder kun for tilstrækkeligt store N");
    }

    // 6. Belastningsfordeling
    private static void analyzeLoadDistribution(int n, int trials) {
        System.out.println("\nBelastningsfordeling for N = " + n);

        // Klassisk
        Map<Integer, Integer> classicalDist = getLoadDistribution(n, trials, false);
        System.out.println("\nKlassisk (1 valg):");
        printDistribution(classicalDist, trials);

        // Two choices
        Map<Integer, Integer> twoChoicesDist = getLoadDistribution(n, trials, true);
        System.out.println("\nTwo Choices (2 valg):");
        printDistribution(twoChoicesDist, trials);
    }

    // 7. Kapacitetsrådgivning
    private static void provideCapacityRecommendations() {
        System.out.println("Praktiske anbefalinger:");
        System.out.println("=======================");
        System.out.println();

        for (int n : BIN_SIZES) {
            System.out.println("For N = " + n + ":");

            double[] classical = runTrials(n, TRIALS, false);
            double[] twoChoices = runTrials(n, TRIALS, true);

            int classicalRec = (int) Math.ceil(classical[0] + 2 * classical[2]);
            int twoChoicesRec = (int) Math.ceil(twoChoices[0] + 2 * twoChoices[2]);

            System.out.printf("  • Klassisk: Anbefalet kapacitet = %d (%.1fx gennemsnit)%n",
                    classicalRec, classicalRec / classical[0]);
            System.out.printf("  • Two Choices: Anbefalet kapacitet = %d (%.1fx gennemsnit)%n",
                    twoChoicesRec, twoChoicesRec / twoChoices[0]);
            System.out.printf("  • Besparelse med Two Choices: %.1f%% mindre kapacitet nødvendig%n%n",
                    (1 - (double)twoChoicesRec/classicalRec) * 100);
        }

        System.out.println("Generelle regler:");
        System.out.println("• Klassisk: Kapacitet skal være ~log N / log log N");
        System.out.println("• Two Choices: Kapacitet skal være ~log log N");
        System.out.println("• Two Choices reducerer nødvendig kapacitet med faktor O(log N)");
    }

    // ========== HJÆLPE METODER ==========

    private static double[] runTrials(int n, int trials, boolean useTwoChoices) {
        List<Double> maxValues = new ArrayList<>();

        for (int t = 0; t < trials; t++) {
            int[] bins = new int[n];

            for (int i = 0; i < n; i++) {
                if (useTwoChoices) {
                    int bin1 = RANDOM.nextInt(n);
                    int bin2 = RANDOM.nextInt(n);
                    if (bins[bin1] <= bins[bin2]) {
                        bins[bin1]++;
                    } else {
                        bins[bin2]++;
                    }
                } else {
                    bins[RANDOM.nextInt(n)]++;
                }
            }

            int max = Arrays.stream(bins).max().orElse(0);
            maxValues.add((double) max);
        }

        double avg = maxValues.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double max = maxValues.stream().mapToDouble(Double::doubleValue).max().orElse(0);
        double stdDev = calculateStdDev(maxValues);

        return new double[]{avg, max, stdDev};
    }

    private static double runSingleTrial(int n, int numTrials, boolean useTwoChoices) {
        double total = 0;
        for (int t = 0; t < numTrials; t++) {
            int[] bins = new int[n];

            for (int i = 0; i < n; i++) {
                if (useTwoChoices) {
                    int bin1 = RANDOM.nextInt(n);
                    int bin2 = RANDOM.nextInt(n);
                    if (bins[bin1] <= bins[bin2]) {
                        bins[bin1]++;
                    } else {
                        bins[bin2]++;
                    }
                } else {
                    bins[RANDOM.nextInt(n)]++;
                }
            }

            total += Arrays.stream(bins).max().orElse(0);
        }
        return total / numTrials;
    }

    private static boolean runTheoremTrial(int n, int m) {
        boolean[] occupied = new boolean[m];
        for (int i = 0; i < n; i++) {
            int bin = RANDOM.nextInt(m);
            if (occupied[bin]) return false;
            occupied[bin] = true;
        }
        return true;
    }

    private static double theoreticalMaxClassical(int n) {
        if (n <= 1) return 1;
        return Math.log(n) / Math.log(Math.log(n));
    }

    private static double theoreticalMaxTwoChoices(int n) {
        if (n <= 1) return 1;
        return Math.log(Math.log(n)) / Math.log(2);
    }

    private static double calculateStdDev(List<Double> values) {
        double mean = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        double variance = values.stream()
                .mapToDouble(x -> Math.pow(x - mean, 2))
                .average().orElse(0);
        return Math.sqrt(variance);
    }

    private static Map<Integer, Integer> getLoadDistribution(int n, int trials, boolean useTwoChoices) {
        Map<Integer, Integer> distribution = new HashMap<>();

        for (int t = 0; t < trials; t++) {
            int[] bins = new int[n];

            for (int i = 0; i < n; i++) {
                if (useTwoChoices) {
                    int bin1 = RANDOM.nextInt(n);
                    int bin2 = RANDOM.nextInt(n);
                    if (bins[bin1] <= bins[bin2]) {
                        bins[bin1]++;
                    } else {
                        bins[bin2]++;
                    }
                } else {
                    bins[RANDOM.nextInt(n)]++;
                }
            }

            int max = Arrays.stream(bins).max().orElse(0);
            distribution.put(max, distribution.getOrDefault(max, 0) + 1);
        }

        return distribution;
    }

    private static void printDistribution(Map<Integer, Integer> distribution, int trials) {
        List<Integer> sortedKeys = new ArrayList<>(distribution.keySet());
        Collections.sort(sortedKeys);

        for (int key : sortedKeys) {
            int count = distribution.get(key);
            double percentage = (count * 100.0) / trials;
            int bars = (int) (percentage / 2);
            System.out.printf("  %2d: %3d gange (%5.1f%%) %s%n",
                    key, count, percentage, "█".repeat(Math.max(0, bars)));
        }
    }
}