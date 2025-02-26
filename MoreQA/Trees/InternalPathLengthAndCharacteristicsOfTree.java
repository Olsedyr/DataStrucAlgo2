package MoreQA.Trees;

import java.util.LinkedList;
import java.util.Queue;

public class InternalPathLengthAndCharacteristicsOfTree {
    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
            left = right = null;
        }
    }

    // Calculate internal path length
    public static int internalPathLength(TreeNode root, int depth) {
        if (root == null) return 0;
        return depth + internalPathLength(root.left, depth + 1) + internalPathLength(root.right, depth + 1);
    }

    // Check if the tree is a full binary tree
    public static boolean isFullBinaryTree(TreeNode root) {
        if (root == null) return true;
        if ((root.left == null && root.right != null) || (root.left != null && root.right == null)) return false;
        return isFullBinaryTree(root.left) && isFullBinaryTree(root.right);
    }

    // Check if the tree is a complete binary tree
    public static boolean isCompleteBinaryTree(TreeNode root) {
        if (root == null) return true;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        boolean flag = false;
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node.left != null) {
                if (flag) return false;
                queue.add(node.left);
            } else {
                flag = true;
            }
            if (node.right != null) {
                if (flag) return false;
                queue.add(node.right);
            } else {
                flag = true;
            }
        }
        return true;
    }

    // Check if the tree is balanced
    public static boolean isBalanced(TreeNode root) {
        return checkHeight(root) != -1;
    }

    private static int checkHeight(TreeNode root) {
        if (root == null) return 0;
        int leftHeight = checkHeight(root.left);
        if (leftHeight == -1) return -1;
        int rightHeight = checkHeight(root.right);
        if (rightHeight == -1) return -1;
        if (Math.abs(leftHeight - rightHeight) > 1) return -1;
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public static void main(String[] args) {
        // Example tree
        TreeNode root = new TreeNode(50);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);


        int internalPathLength = internalPathLength(root, 0);
        System.out.println("Internal path length of the tree: " + internalPathLength);

        // Other characteristics
        System.out.println("Other characteristics of the tree:");
        System.out.println("1. Binary Tree: Each node has at most two children.");
        System.out.println("2. Balanced Tree: " + (isBalanced(root) ? "Yes" : "No"));
        System.out.println("3. Full Binary Tree: " + (isFullBinaryTree(root) ? "Yes" : "No"));
        System.out.println("4. Complete Binary Tree: " + (isCompleteBinaryTree(root) ? "Yes" : "No"));
    }
}