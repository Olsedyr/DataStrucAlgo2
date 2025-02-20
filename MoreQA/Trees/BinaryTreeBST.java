public class BinaryTreeBST {

    static class Node {
        int data;
        Node left, right;

        public Node(int data) {
            this.data = data;
            left = right = null;
        }
    }

    public static void main(String[] args) {
        // Create the binary tree
        Node root = new Node(10);
        root.left = new Node(5);
        root.right = new Node(20);
        root.left.left = new Node(3);
        root.left.right = new Node(7);
        root.right.left = new Node(15);
        root.right.right = new Node(25);

        // Check if the binary tree is a BST
        if (isBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE)) {
            System.out.println("The binary tree is a BST.");
        } else {
            System.out.println("The binary tree is not a BST.");
        }
    }

    // Function to check if the binary tree is a BST
    // Time Complexity: O(n), where n is the number of nodes in the tree
    // Space Complexity: O(h), where h is the height of the tree (for the recursion stack)
    public static boolean isBST(Node node, int min, int max) {
        // Base case: if the node is null, it is a valid BST by definition
        if (node == null) {
            return true;
        }

        // Check if the current node's data is within the allowed range (min, max)
        if (node.data <= min || node.data >= max) {
            return false;
        }

        // Recursively check the left and right subtrees with updated ranges
        return isBST(node.left, min, node.data) && isBST(node.right, node.data, max);
    }
}
