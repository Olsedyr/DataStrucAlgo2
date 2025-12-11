package MoreQA.Trees;

public class TreeSearchBranchCounting {

    public static class BinaryNode {
        int value;
        BinaryNode left;
        BinaryNode right;

        public BinaryNode(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    // ===== MAIN SOLUTION =====
    public void solveExamTree(BinaryNode root) {
        System.out.println("=== EKSAMEN SVAR - BINÆRT TRÆ ===");

        // 1. Branch count
        System.out.println("1. ANTAL GRENE I TRÆET:");
        int branches = countBranches(root, null);
        System.out.println("   RESULTAT: " + branches + " grene");
        System.out.println();

        // 2. Tree analysis
        analyzeTree(root);
        System.out.println();

        // 3. Priority queue
        transformToPriorityQueue(root);
    }

    // ===== BRANCH COUNTING - FIXED =====
    private int countBranches(BinaryNode node, BinaryNode parent) {
        if (node == null) return 0;

        int count = 0;

        // Check if current node qualifies as branch node X
        if (isBranchNode(node, parent)) {
            count++;
            BinaryNode child = getOnlyChild(node);
            BinaryNode grandchild = getOnlyChild(child);
            System.out.println("   ✓ Fundet gren: Node " + node.value +
                    " → " + child.value + " → " + grandchild.value);
        }

        return count + countBranches(node.left, node) + countBranches(node.right, node);
    }

    private boolean isBranchNode(BinaryNode node, BinaryNode parent) {
        // 1. Node X must have exactly one child
        BinaryNode child = getOnlyChild(node);
        if (child == null) return false;

        // 2. Node X must have no siblings
        if (!hasNoSiblings(node, parent)) return false;

        // 3. Node X's child must have no siblings
        if (!hasNoSiblings(child, node)) return false;

        // 4. Node X's child must have exactly one child (which is a leaf)
        BinaryNode grandchild = getOnlyChild(child);
        return grandchild != null && isLeaf(grandchild);
    }

    // ===== TREE ANALYSIS =====
    private void analyzeTree(BinaryNode root) {
        int nodes = countNodes(root);
        int height = getHeight(root);
        int leaves = countLeaves(root);

        System.out.println("2. TRÆ ANALYSE:");
        System.out.println("   - Total noder: " + nodes);
        System.out.println("   - Højde: " + height);
        System.out.println("   - Blade: " + leaves);
        System.out.println("   - Balanceret: " + (isBalanced(root) ? "JA" : "NEJ"));
        System.out.println("   - Optimal højde: ⌊log₂(" + nodes + ")⌋ = " +
                Math.floor(Math.log(nodes) / Math.log(2)));
    }

    // ===== PRIORITY QUEUE - FIXED OUTPUT =====
    private void transformToPriorityQueue(BinaryNode root) {
        System.out.println("3. TRANSFORMATION TIL PRIORITETSKØ:");

        // Get sorted elements
        java.util.List<Integer> sorted = new java.util.ArrayList<>();
        inOrderTraversal(root, sorted);
        System.out.println("   In-order traversal: " + sorted);

        // Build priority queue
        java.util.PriorityQueue<Integer> pq = new java.util.PriorityQueue<>(sorted);

        // Show priority queue operations clearly
        System.out.println("   Min-heap oprettet");
        System.out.println("   Fjern mindste element: " + pq.poll());

        // Show remaining elements in order
        java.util.List<Integer> remaining = new java.util.ArrayList<>();
        while (!pq.isEmpty()) {
            remaining.add(pq.poll());
        }
        System.out.println("   Resten af elementerne: " + remaining);

        System.out.println("   Tidskompleksitet: O(n)");
    }

    // ===== HELPER METHODS =====
    private BinaryNode getOnlyChild(BinaryNode node) {
        if (node == null) return null;
        if (node.left != null && node.right == null) return node.left;
        if (node.right != null && node.left == null) return node.right;
        return null;
    }

    private boolean isLeaf(BinaryNode node) {
        return node != null && node.left == null && node.right == null;
    }

    private boolean hasNoSiblings(BinaryNode node, BinaryNode parent) {
        if (parent == null) return true; // Root has no siblings
        return (parent.left == node && parent.right == null) ||
                (parent.right == node && parent.left == null);
    }

    private void inOrderTraversal(BinaryNode node, java.util.List<Integer> result) {
        if (node != null) {
            inOrderTraversal(node.left, result);
            result.add(node.value);
            inOrderTraversal(node.right, result);
        }
    }

    private int countNodes(BinaryNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    private int getHeight(BinaryNode node) {
        if (node == null) return -1;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private int countLeaves(BinaryNode node) {
        if (node == null) return 0;
        if (isLeaf(node)) return 1;
        return countLeaves(node.left) + countLeaves(node.right);
    }

    private boolean isBalanced(BinaryNode node) {
        if (node == null) return true;
        int left = getHeight(node.left);
        int right = getHeight(node.right);
        return Math.abs(left - right) <= 1 && isBalanced(node.left) && isBalanced(node.right);
    }

    // ===== TEST WITH PROPER BRANCH STRUCTURE =====
    public static void main(String[] args) {
        TreeSearchBranchCounting solution = new TreeSearchBranchCounting();
        BinaryNode examTree = buildTreeWithBranches(); // Use the new tree builder
        solution.solveExamTree(examTree);
    }

    // Build a tree that ACTUALLY has branches according to the definition
    private static BinaryNode buildTreeWithBranches() {
        /*
        Build a tree that guarantees branches:
        - Node X has one child
        - Node X has no siblings
        - Node X's child has no siblings
        - Node X's child has one child (leaf)
        */

        BinaryNode root = new BinaryNode(7);

        // Branch 1: 5 → 6 → 7 (leaf)
        root.left = new BinaryNode(4);
        root.left.left = new BinaryNode(3);     // Node X - has one child (6), no siblings
        root.left.left.left = new BinaryNode(2); // X's child - has one child (7), no siblings
        root.left.left.left.left = new BinaryNode(1); // Leaf - has no children

        // Branch 2: 35 → 36 → 37 (leaf)
        root.right = new BinaryNode(28);
        root.right.right = new BinaryNode(55);  // Node X - has one child (36), no siblings
        root.right.right.left = new BinaryNode(51); // X's child - has one child (37), no siblings
        root.right.right.left.left = new BinaryNode(48);
        root.right.right.left.left.left = new BinaryNode(40);
        root.right.right.left.left.left.left = new BinaryNode(36);
        root.right.right.right = new BinaryNode(60);    // Leaf - has no children
        root.right.right.right.right = new BinaryNode(69);
        root.right.right.right.left = new BinaryNode(58);
        root.right.right.right.left.left = new BinaryNode(57);

        return root;
    }
}