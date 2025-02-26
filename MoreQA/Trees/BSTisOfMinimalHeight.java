package MoreQA.Trees;

import java.util.*;

public class BSTisOfMinimalHeight {
    static class Node {
        int data;
        Node left, right;

        public Node(int item) {
            data = item;
            left = right = null;
        }
    }

    Node root;

    // Method to calculate the height of the tree
    public int height(Node node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        return Math.max(leftHeight, rightHeight) + 1;
    }

    // Method to calculate the number of nodes in the tree
    public int countNodes(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    // Method to calculate the minimal height based on the number of nodes
    public int minimalHeight(int nodeCount) {
        return (int) Math.ceil(Math.log(nodeCount + 1) / Math.log(2)) - 1;
    }

    // Method to check if the tree is of minimal height
    public boolean minimalHeight() {
        int nodeCount = countNodes(root);
        int actualHeight = height(root);
        int minHeight = minimalHeight(nodeCount);
        return actualHeight == minHeight;
    }

    // In-order traversal
    public void inOrderTraversal(Node root) {
        if (root != null) {
            inOrderTraversal(root.left);
            System.out.print(root.data + " ");
            inOrderTraversal(root.right);
        }
    }

    // Level-order traversal
    public void levelOrderTraversal(Node root) {
        if (root == null) return;

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            System.out.print(node.data + " ");

            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
    }

    // Post-order traversal
    public void postOrderTraversal(Node root) {
        if (root != null) {
            postOrderTraversal(root.left);
            postOrderTraversal(root.right);
            System.out.print(root.data + " ");
        }
    }

    // Pre-order traversal
    public void preOrderTraversal(Node root) {
        if (root != null) {
            System.out.print(root.data + " ");
            preOrderTraversal(root.left);
            preOrderTraversal(root.right);
        }
    }

    public static void main(String[] args) {
        BSTisOfMinimalHeight bst = new BSTisOfMinimalHeight();

        // Manually construct the tree using root.left, root.right, etc.
        bst.root = new Node(11);

        // Right side of root
        bst.root.right = new Node(13);
        bst.root.right.right = new Node(57);
        bst.root.right.right.right = new Node(90);
        bst.root.right.right.left = new Node(25);
        bst.root.right.right.left.left = new Node(17);

        // Left side of root
        bst.root.left = new Node(2);
        bst.root.left.left = new Node(1);
        bst.root.left.right = new Node(9);
        bst.root.left.right.left = new Node(3);

        // Check if the tree is of minimal height
        System.out.println("Is the tree of minimal height? " + bst.minimalHeight());

        // Perform traversals
        System.out.print("In-order traversal: ");
        bst.inOrderTraversal(bst.root);
        System.out.println();

        System.out.print("Level-order traversal: ");
        bst.levelOrderTraversal(bst.root);
        System.out.println();

        System.out.print("Post-order traversal: ");
        bst.postOrderTraversal(bst.root);
        System.out.println();

        System.out.print("Pre-order traversal: ");
        bst.preOrderTraversal(bst.root);
        System.out.println();
    }
}