package MoreQA.StackAndQueue;

import java.util.*;

public class RightViewBinaryTree {

    static class Node {
        int data;
        Node left, right;

        // Constructor
        public Node(int data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    public static void main(String[] args) {
        // Create a binary tree
        Node root = new Node(1);
        root.left = new Node(2);
        root.right = new Node(3);
        root.left.left = new Node(4);
        root.left.right = new Node(5);
        root.right.right = new Node(6);
        root.left.left.left = new Node(7);
        root.left.right.right = new Node(8);

        // Approach 1: Using Level Order Traversal (BFS)
        System.out.println("Approach 1: Using Level Order Traversal (BFS) to print right view:");
        printRightViewBFS(root);

        // Approach 2: Using Recursive DFS Traversal
        System.out.println("Approach 2: Using Recursive DFS Traversal to print right view:");
        printRightViewDFS(root);
    }

    // Approach 1: Using Level Order Traversal (BFS)
    // Time Complexity: O(n), where n is the number of nodes in the tree.
    // Space Complexity: O(n), due to the space used by the queue.
    public static void printRightViewBFS(Node root) {
        if (root == null) {
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int size = queue.size();

            // Traverse the current level
            for (int i = 0; i < size; i++) {
                Node currentNode = queue.poll();

                // If this is the last node of the level, print it
                if (i == size - 1) {
                    System.out.print(currentNode.data + " ");
                }

                // Enqueue left and right children
                if (currentNode.left != null) {
                    queue.add(currentNode.left);
                }
                if (currentNode.right != null) {
                    queue.add(currentNode.right);
                }
            }
        }
        System.out.println();
    }

    // Approach 2: Using Recursive DFS Traversal
    // Time Complexity: O(n), where n is the number of nodes in the tree.
    // Space Complexity: O(h), where h is the height of the tree due to the recursive call stack.
    public static void printRightViewDFS(Node root) {
        // We need to keep track of the current level of the tree while traversing
        Map<Integer, Integer> rightView = new HashMap<>();
        int maxLevel = -1;
        rightViewDFS(root, 0, rightView, maxLevel);

        // Print the right view using the map
        for (int level : rightView.keySet()) {
            System.out.print(rightView.get(level) + " ");
        }
        System.out.println();
    }

    // Helper function for DFS approach
    public static void rightViewDFS(Node node, int level, Map<Integer, Integer> rightView, int maxLevel) {
        if (node == null) {
            return;
        }

        // If this level has not been encountered yet, add the node's value
        if (level > maxLevel) {
            rightView.put(level, node.data);
            maxLevel = level;
        }

        // First visit the right subtree to get the rightmost node at each level
        rightViewDFS(node.right, level + 1, rightView, maxLevel);
        // Then visit the left subtree
        rightViewDFS(node.left, level + 1, rightView, maxLevel);
    }
}
