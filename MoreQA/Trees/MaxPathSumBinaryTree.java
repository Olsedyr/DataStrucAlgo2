package MoreQA.Trees;

public class MaxPathSumBinaryTree {

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
        Node root = new Node(-10);
        root.left = new Node(9);
        root.right = new Node(20);
        root.right.left = new Node(15);
        root.right.right = new Node(7);

        // Finding the maximum path sum
        System.out.println("Maximum Path Sum: " + findMaxPathSum(root));
    }

    // Function to find the maximum path sum
    // Time Complexity: O(n), where n is the number of nodes in the tree.
    // Space Complexity: O(h), where h is the height of the tree (due to recursion stack).
    public static int findMaxPathSum(Node root) {
        int[] maxSum = new int[1];  // Initialize to store the result
        maxSum[0] = Integer.MIN_VALUE;  // Set initial max sum to the smallest possible value

        // Call the helper function to compute the maximum path sum
        maxPathSumHelper(root, maxSum);

        return maxSum[0];
    }

    // Helper function to find the maximum path sum with recursion
    public static int maxPathSumHelper(Node node, int[] maxSum) {
        // Base case: if the node is null, return 0
        if (node == null) {
            return 0;
        }

        // Recursively find the maximum path sum for the left and right subtrees
        int leftSum = Math.max(0, maxPathSumHelper(node.left, maxSum));  // Only consider positive sums
        int rightSum = Math.max(0, maxPathSumHelper(node.right, maxSum));  // Only consider positive sums

        // Calculate the maximum path sum that passes through the current node
        int currentPathSum = node.data + leftSum + rightSum;

        // Update the global maximum path sum if the current path sum is greater
        maxSum[0] = Math.max(maxSum[0], currentPathSum);

        // Return the maximum path sum extending from the current node
        return node.data + Math.max(leftSum, rightSum);
    }
}
