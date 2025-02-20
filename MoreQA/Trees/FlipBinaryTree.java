package MoreQA.Trees;

public class FlipBinaryTree {

    static class Node {
        int data;
        Node left, right;

        // Constructor to create a new node
        public Node(int data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    public static void main(String[] args) {
        // Creating a sample binary tree
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);

        System.out.println("Original Tree:");
        printInOrder(root);

        // Flip the binary tree
        flipTree(root);

        System.out.println("\nFlipped Tree:");
        printInOrder(root);
    }

    // Function to flip the binary tree (mirror it)
    // Time Complexity: O(n), where n is the number of nodes.
    // Space Complexity: O(h), where h is the height of the tree (due to recursion stack).
    public static void flipTree(Node root) {
        if (root == null) {
            return;
        }

        // Recursively flip the left and right subtrees
        flipTree(root.left);
        flipTree(root.right);

        // Swap the left and right children of the current node
        Node temp = root.left;
        root.left = root.right;
        root.right = temp;
    }

    // Function to print the tree in inorder traversal
    public static void printInOrder(Node root) {
        if (root == null) {
            return;
        }
        printInOrder(root.left);
        System.out.print(root.data + " ");
        printInOrder(root.right);
    }
}
