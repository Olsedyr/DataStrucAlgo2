package MoreQA.Trees;

import java.util.*;

public class MaxDepthBinaryTree {

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

        // Approach 1: Using Recursive Depth First Search (DFS)
        System.out.println("Approach 1: Using Recursive DFS to find the maximum depth:");
        System.out.println("Max Depth (DFS): " + findMaxDepthDFS(root));

        // Approach 2: Using Level Order Traversal (BFS)
        System.out.println("Approach 2: Using Level Order Traversal (BFS) to find the maximum depth:");
        System.out.println("Max Depth (BFS): " + findMaxDepthBFS(root));
    }

    // Approach 1: Using Recursive Depth First Search (DFS)
    // Time Complexity: O(n), where n is the number of nodes in the tree.
    // Space Complexity: O(h), where h is the height of the tree due to the recursive call stack.
    public static int findMaxDepthDFS(Node root) {
        if (root == null) {
            return 0;
        }
        // Compute the depth of each subtree
        int leftDepth = findMaxDepthDFS(root.left);
        int rightDepth = findMaxDepthDFS(root.right);

        // The depth of the current node is the maximum of the left and right subtrees' depth plus one
        return Math.max(leftDepth, rightDepth) + 1;
    }

    // Approach 2: Using Level Order Traversal (BFS)
    // Time Complexity: O(n), where n is the number of nodes in the tree.
    // Space Complexity: O(n), due to the space used by the queue.
    public static int findMaxDepthBFS(Node root) {
        if (root == null) {
            return 0;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            // Process each level
            for (int i = 0; i < size; i++) {
                Node currentNode = queue.poll();

                // Add left and right children to the queue
                if (currentNode.left != null) {
                    queue.add(currentNode.left);
                }
                if (currentNode.right != null) {
                    queue.add(currentNode.right);
                }
            }
            // Increase the depth after processing each level
            depth++;
        }
        return depth;
    }
}
