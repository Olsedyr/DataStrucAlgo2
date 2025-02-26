package MoreQA.Trees;


import java.util.*;

public class InOrderAndLevelOrderTraversalAndPostOrderAndPreOrder {

    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
            left = right = null;
        }
    }

    // In-order traversal
    public static void inOrderTraversal(TreeNode root) {
        if (root != null) {
            inOrderTraversal(root.left);
            System.out.print(root.val + " ");
            inOrderTraversal(root.right);
        }
    }

    // Level-order traversal
    public static void levelOrderTraversal(TreeNode root) {
        if (root == null) return;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.print(node.val + " ");

            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }
    }

    // Post-order traversal
    public static void postOrderTraversal(TreeNode root) {
        if (root != null) {
            postOrderTraversal(root.left);
            postOrderTraversal(root.right);
            System.out.print(root.val + " ");
        }
    }

    // In-order traversal is a depth-first traversal method
    // used primarily for binary trees. The nodes are visited
    // in the following order:
    //
    //    Left Subtree: Traverse the left subtree.
    //    Root Node: Visit the root node.
    //    Right Subtree: Traverse the right subtree.
    //
    // This traversal method ensures that the nodes are visited
    // in non-decreasing order for binary search trees (BSTs).

    // Level-order traversal is a breadth-first traversal method
    // where nodes are visited level by level from left to right.
    // This traversal uses a queue to keep track of the nodes at
    // each level. The process is as follows:
    //
    //    Root Node: Start with the root node.
    //    Level by Level: Visit all nodes at the current level
    //    before moving to the next level.

    // Post-order traversal is a depth-first traversal method
    // where nodes are visited in the following order:
    //
    //    Left Subtree: Traverse the left subtree.
    //    Right Subtree: Traverse the right subtree.
    //    Root Node: Visit the root node.

    public static void main(String[] args) {
        // Example tree
        TreeNode root = new TreeNode(11);

        //Right Side of root
        root.right = new TreeNode(13);
        root.right.right = new TreeNode(57);
        root.right.right.right = new TreeNode(90);
        root.right.right.left = new TreeNode(25);
        root.right.right.left.left = new TreeNode(17);

        //Left side of root
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(9);
        root.left.right.left = new TreeNode(3);




        System.out.print("In-order traversal: ");
        inOrderTraversal(root);
        System.out.println();

        System.out.print("Level-order traversal: ");
        levelOrderTraversal(root);
        System.out.println();

        System.out.print("Post-order traversal: ");
        postOrderTraversal(root);
        System.out.println();
    }
}