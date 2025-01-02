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


    public BinaryNode getOnlyChild(BinaryNode node) {
        if (node == null) return null;

        if (node.left != null && node.right == null) {
            return node.left; // Return left child if only left exists
        } else if (node.right != null && node.left == null) {
            return node.right; // Return right child if only right exists
        }

        return null; // Return null if there are two or no children
    }



    public int countBranches(BinaryNode root) {
        if (root == null) return 0;

        int branchCount = 0;

        // Check if the current node has exactly one child
        BinaryNode onlyChild = getOnlyChild(root);
        if (onlyChild != null) {
            // Check if the only child has exactly one child (a leaf node)
            BinaryNode grandchild = getOnlyChild(onlyChild);
            if (grandchild != null && grandchild.left == null && grandchild.right == null) {
                // This is a valid branch
                branchCount++;
            }
        }

        
        // Recursively check left and right subtrees
        branchCount += countBranches(root.left);
        branchCount += countBranches(root.right);

        return branchCount;
    }

    public static void main(String[] args) {
        // Sample binary tree setup for testing
        BinaryNode root = new BinaryNode(7);
        root.left = new BinaryNode(4);
        root.right = new BinaryNode(22);
        root.left.left = new BinaryNode(3);
        root.left.left.left = new BinaryNode(1);
        root.left.left.left.left = new BinaryNode(43);
        root.right.left = new BinaryNode(55);
        root.right.left.left = new BinaryNode(35);
        root.right.left.left.left = new BinaryNode(57);

        TreeSearchBranchCounting exercise = new TreeSearchBranchCounting();
        int numberOfBranches = exercise.countBranches(root);
        System.out.println("Number of branches in the tree: " + numberOfBranches);

    }
}
