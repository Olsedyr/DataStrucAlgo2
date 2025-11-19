package MoreQA.Trees;
import java.util.*;
public class LevelOrderTraversalPreferableDataStructure {


    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
            left = right = null;
        }
    }

    // Level-order traversal using a queue
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

    public static void main(String[] args) {
        // Example tree
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);

        System.out.print("Level-order traversal: ");
        levelOrderTraversal(root);
        System.out.println();

        // Call the explanation method
        explanation();
    }

    // Explanation
    public static void explanation() {
        System.out.println("A queue is the preferred data structure for level-order traversal.");
        System.out.println("Reason: A queue follows the First-In-First-Out (FIFO) principle, which is ideal for processing nodes level by level.");
        System.out.println("In level-order traversal, nodes are visited level by level from left to right.");
        System.out.println("Using a queue ensures that nodes are processed in the correct order.");
    }
}
