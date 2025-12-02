import java.awt.*;
import javax.swing.*;

public class BranchCounter {

    private class BinaryNode {
        int element;
        BinaryNode left;
        BinaryNode right;
        BinaryNode parent;

        BinaryNode(int element, BinaryNode parent) {
            this.element = element;
            this.parent = parent;
        }
    }

    private BinaryNode root;

    public BranchCounter() {
        root = null;
    }

    private BinaryNode getOnlyChild(BinaryNode node) {
        if (node == null) return null;
        boolean hasLeft = node.left != null;
        boolean hasRight = node.right != null;
        if (hasLeft == hasRight) return null;
        return hasLeft ? node.left : node.right;
    }

    private int countBranchesInSubtree(BinaryNode node) {
        if (node == null) return 0;

        int count = countBranchesInSubtree(node.left) +
                countBranchesInSubtree(node.right);

        if (node.parent != null) {
            if (getOnlyChild(node.parent) == node) {
                BinaryNode y = getOnlyChild(node);
                if (y != null) {
                    BinaryNode z = getOnlyChild(y);
                    if (z != null && z.left == null && z.right == null) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    public int countBranches() {
        return countBranchesInSubtree(root);
    }

    public int size() { return size(root); }
    private int size(BinaryNode node) {
        if (node == null) return 0;
        return 1 + size(node.left) + size(node.right);
    }

    public int height() { return height(root); }
    private int height(BinaryNode node) {
        if (node == null) return -1;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public int leafCount() { return leafCount(root); }
    private int leafCount(BinaryNode node) {
        if (node == null) return 0;
        if (node.left == null && node.right == null) return 1;
        return leafCount(node.left) + leafCount(node.right);
    }

    public boolean isBST() {
        return isBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    private boolean isBST(BinaryNode node, int min, int max) {
        if (node == null) return true;
        if (node.element <= min || node.element >= max) return false;
        return isBST(node.left, min, node.element) &&
                isBST(node.right, node.element, max);
    }

    public int optimalHeight() {
        int n = size();
        return (int) Math.ceil(Math.log(n + 1) / Math.log(2)) - 1;
    }

    public void prettyPrint() {
        prettyPrint(root, "", true);
    }
    private void prettyPrint(BinaryNode node, String prefix, boolean isTail) {
        if (node == null) return;
        System.out.println(prefix + (isTail ? "└── " : "├── ") + String.format("%03d", node.element));
        if (node.left != null || node.right != null) {
            if (node.left != null) prettyPrint(node.left, prefix + (isTail ? "    " : "│   "), node.right == null);
            if (node.right != null) prettyPrint(node.right, prefix + (isTail ? "    " : "│   "), true);
        }
    }

    // ------------------- SIMPEL OG FUNKTIONEL VISUALISERING -------------------
    public void visualize() {
        JFrame frame = new JFrame("Binary Tree Visualization - Fit to Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ScalingTreePanel panel = new ScalingTreePanel();

        // Tilføj mouse wheel til at zoome
        panel.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                double zoomFactor = e.getWheelRotation() < 0 ? 1.1 : 0.9;
                panel.setZoom(panel.getZoom() * zoomFactor);
                panel.repaint();
                e.consume();
            }
        });

        frame.add(panel);

        // Sæt størrelse til at passe skærmen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int)(screenSize.width * 0.9), (int)(screenSize.height * 0.8));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class ScalingTreePanel extends JPanel {
        private static final int BASE_NODE_SIZE = 25;
        private static final int BASE_VERTICAL_GAP = 60;
        private static final int BASE_HORIZONTAL_GAP = 40;
        private double zoom = 1.0;

        public void setZoom(double zoom) {
            this.zoom = Math.max(0.3, Math.min(3.0, zoom));
        }

        public double getZoom() {
            return zoom;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);

            if (root == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Beregn træets højde
            int treeHeight = height();

            // Beregn optimale parametre baseret på vinduets størrelse
            int availableWidth = getWidth();
            int availableHeight = getHeight();

            // Beregn skalering baseret på vinduets størrelse og træets højde
            double widthScale = (double) availableWidth / (treeHeight * BASE_HORIZONTAL_GAP * 3);
            double heightScale = (double) availableHeight / ((treeHeight + 1) * BASE_VERTICAL_GAP);
            double autoScale = Math.min(widthScale, heightScale);

            // Anvend både auto-scaling og manual zoom
            double totalScale = autoScale * zoom;

            // Beregn faktiske størrelser
            int nodeRadius = (int)(BASE_NODE_SIZE * totalScale);
            int verticalGap = (int)(BASE_VERTICAL_GAP * totalScale);
            int horizontalGap = (int)(BASE_HORIZONTAL_GAP * totalScale);

            // Sørg for minimumsstørrelser
            nodeRadius = Math.max(10, nodeRadius);
            verticalGap = Math.max(30, verticalGap);
            horizontalGap = Math.max(20, horizontalGap);

            // Startposition (centreret horisontalt)
            int startX = getWidth() / 2;
            int startY = 50;

            // Tegn træet
            drawTree(g2d, root, startX, startY, horizontalGap, verticalGap, nodeRadius, availableWidth / 4);

            // Vis zoom niveau
            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(String.format("Zoom: %.0f%% | Ctrl+Scroll to zoom", zoom * 100), 10, 20);
        }

        private void drawTree(Graphics2D g2d, BinaryNode node,
                              int x, int y,
                              int horizontalGap, int verticalGap,
                              int nodeRadius, int xOffset) {
            if (node == null) return;

            // Tegn forbindelser til børn
            if (node.left != null) {
                int childX = x - xOffset;
                int childY = y + verticalGap;
                g2d.setColor(Color.GRAY);
                g2d.setStroke(new BasicStroke((float) Math.max(1.0f, 2.0f * zoom)));
                g2d.drawLine(x, y + nodeRadius, childX, childY - nodeRadius);
                drawTree(g2d, node.left, childX, childY,
                        horizontalGap, verticalGap, nodeRadius, xOffset / 2);
            }

            if (node.right != null) {
                int childX = x + xOffset;
                int childY = y + verticalGap;
                g2d.setColor(Color.GRAY);
                g2d.setStroke(new BasicStroke((float) Math.max(1.0f, 2.0f * zoom)));
                g2d.drawLine(x, y + nodeRadius, childX, childY - nodeRadius);
                drawTree(g2d, node.right, childX, childY,
                        horizontalGap, verticalGap, nodeRadius, xOffset / 2);
            }

            // Tegn noden
            Color nodeColor = new Color(70, 130, 180); // Steel blue
            g2d.setColor(nodeColor);
            g2d.fillOval(x - nodeRadius, y - nodeRadius,
                    2 * nodeRadius, 2 * nodeRadius);

            // Tegn node kant
            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawOval(x - nodeRadius, y - nodeRadius,
                    2 * nodeRadius, 2 * nodeRadius);

            // Tegn node tekst
            g2d.setColor(Color.WHITE);
            int fontSize = Math.max(8, (int)(12 * zoom));
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
            String text = String.valueOf(node.element);
            FontMetrics fm = g2d.getFontMetrics();
            int textX = x - fm.stringWidth(text) / 2;
            int textY = y + fm.getHeight() / 3;
            g2d.drawString(text, textX, textY);
        }
    }

    // ------------------- ALTERNATIV: HORISONTAL VISNING -------------------
    public void visualizeHorizontal() {
        JFrame frame = new JFrame("Binary Tree - Horizontal Layout");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        HorizontalTreePanel panel = new HorizontalTreePanel();

        frame.add(panel);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private class HorizontalTreePanel extends JPanel {
        private static final int NODE_WIDTH = 40;
        private static final int NODE_HEIGHT = 30;
        private static final int H_SPACING = 50;
        private static final int V_SPACING = 70;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            if (root == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int treeHeight = height();
            int maxNodesAtBottom = (int) Math.pow(2, treeHeight);

            // Beregn hvor meget plads vi har
            int availableWidth = getWidth();
            int availableHeight = getHeight();

            // Beregn skalering
            double widthScale = (double) availableWidth / (maxNodesAtBottom * (NODE_WIDTH + H_SPACING));
            double heightScale = (double) availableHeight / ((treeHeight + 1) * V_SPACING);
            double scale = Math.min(widthScale, heightScale) * 0.9; // 10% margin

            // Beregn faktiske størrelser
            int nodeWidth = (int)(NODE_WIDTH * scale);
            int nodeHeight = (int)(NODE_HEIGHT * scale);
            int hSpacing = (int)(H_SPACING * scale);
            int vSpacing = (int)(V_SPACING * scale);

            // Start i midten
            int startX = availableWidth / 2;
            int startY = 50;

            // Tegn træet
            drawHorizontalTree(g2d, root, startX, startY,
                    availableWidth / 4, nodeWidth, nodeHeight,
                    hSpacing, vSpacing, 0);
        }

        private void drawHorizontalTree(Graphics2D g2d, BinaryNode node,
                                        int x, int y,
                                        int xOffset,
                                        int nodeWidth, int nodeHeight,
                                        int hSpacing, int vSpacing, int depth) {
            if (node == null) return;

            // Tegn forbindelser
            if (node.left != null) {
                int childX = x - xOffset;
                int childY = y + vSpacing;
                g2d.setColor(Color.GRAY);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawLine(x + nodeWidth/2, y + nodeHeight,
                        childX + nodeWidth/2, childY);
                drawHorizontalTree(g2d, node.left, childX, childY,
                        xOffset/2, nodeWidth, nodeHeight,
                        hSpacing, vSpacing, depth + 1);
            }

            if (node.right != null) {
                int childX = x + xOffset;
                int childY = y + vSpacing;
                g2d.setColor(Color.GRAY);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawLine(x + nodeWidth/2, y + nodeHeight,
                        childX + nodeWidth/2, childY);
                drawHorizontalTree(g2d, node.right, childX, childY,
                        xOffset/2, nodeWidth, nodeHeight,
                        hSpacing, vSpacing, depth + 1);
            }

            // Tegn node
            Color nodeColor = new Color(70, 130, 180);
            g2d.setColor(nodeColor);
            g2d.fillRoundRect(x, y, nodeWidth, nodeHeight, 8, 8);

            g2d.setColor(Color.DARK_GRAY);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawRoundRect(x, y, nodeWidth, nodeHeight, 8, 8);

            // Tegn tekst
            g2d.setColor(Color.WHITE);
            int fontSize = Math.max(8, (int)(11 * Math.min(1.0, nodeWidth / 40.0)));
            g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
            String text = String.valueOf(node.element);
            FontMetrics fm = g2d.getFontMetrics();
            int textX = x + (nodeWidth - fm.stringWidth(text)) / 2;
            int textY = y + (nodeHeight + fm.getHeight()) / 2 - 2;
            g2d.drawString(text, textX, textY);
        }
    }

    // ------------------- Dit eksakte træ -------------------
    public static void main(String[] args) {
        BranchCounter tree = new BranchCounter();

        tree.root = tree.new BinaryNode(7, null);

        BinaryNode n4  = tree.new BinaryNode(4, tree.root);
        BinaryNode n28 = tree.new BinaryNode(28, tree.root);
        tree.root.left  = n4;
        tree.root.right = n28;

        BinaryNode n3 = tree.new BinaryNode(3, n4);
        n4.left = n3;

        BinaryNode n2 = tree.new BinaryNode(2, n3);
        n3.left = n2;

        BinaryNode n1 = tree.new BinaryNode(1, n2);
        n2.left = n1;

        BinaryNode n52 = tree.new BinaryNode(52, n28);
        n28.right = n52;

        BinaryNode n51 = tree.new BinaryNode(51, n52);
        n52.left = n51;

        BinaryNode n48 = tree.new BinaryNode(48, n51);
        n51.left = n48;

        BinaryNode n40 = tree.new BinaryNode(40, n48);
        n48.left = n40;

        BinaryNode n36 = tree.new BinaryNode(36, n40);
        n40.left = n36;

        BinaryNode n60 = tree.new BinaryNode(60, n52);
        n52.right = n60;

        BinaryNode n58 = tree.new BinaryNode(58, n60);
        n60.left = n58;

        BinaryNode n57 = tree.new BinaryNode(57, n58);
        n58.left = n57;

        BinaryNode n69 = tree.new BinaryNode(69, n60);
        n60.right = n69;

        System.out.println("=== TRÆ-KARAKTERISTIKA ===");
        System.out.println("Antal noder         : " + tree.size());
        System.out.println("Højde               : " + tree.height());
        System.out.println("Antal blade         : " + tree.leafCount());
        System.out.println("Antal grene         : " + tree.countBranches());
        System.out.println("Er binært søgetræ   : " + tree.isBST());
        System.out.println("Optimal højde       : " + tree.optimalHeight());
        System.out.println();

        System.out.println("=== PRETTY PRINT ===");
        tree.prettyPrint();

        System.out.println("\n=== VISUALISERING ===");
        System.out.println("Kører auto-skalering visualisering...");

        // Prøv først med auto-skalering
        tree.visualize();

        // Hvis du vil prøve den horisontale version:
        // tree.visualizeHorizontal();
    }
}