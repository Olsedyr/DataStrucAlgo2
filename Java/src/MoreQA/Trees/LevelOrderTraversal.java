package MoreQA.Trees;

import java.util.LinkedList;
import java.util.Queue;

public class LevelOrderTraversal {

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

        // Performing level-order traversal
        System.out.println("Level Order Traversal:");
        levelOrder(root);
    }

    // Function to perform level-order traversal
    // Time Complexity: O(n), where n is the number of nodes in the tree.
    // Space Complexity: O(n), for storing nodes in the queue.
    public static void levelOrder(Node root) {
        if (root == null) {
            return;
        }

        // Initialize a queue to store nodes
        Queue<Node> queue = new LinkedList<>();

        // Enqueue the root node
        queue.add(root);

        while (!queue.isEmpty()) {
            // Dequeue the node from the queue
            Node currentNode = queue.poll();

            // Print the data of the current node
            System.out.print(currentNode.data + " ");

            // Enqueue the left child if it exists
            if (currentNode.left != null) {
                queue.add(currentNode.left);
            }

            // Enqueue the right child if it exists
            if (currentNode.right != null) {
                queue.add(currentNode.right);
            }
        }
    }
}
