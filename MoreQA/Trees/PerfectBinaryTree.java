package MoreQA.Trees;

public class PerfectBinaryTree {
    static class TreeNode {
        int val;
        TreeNode left, right;

        TreeNode(int val) {
            this.val = val;
            left = right = null;
        }
    }

    // Metode til at skabe et perfekt binært træ med højden H
    public static TreeNode createPerfectBinaryTree(int H) {
        return createPerfectBinaryTreeHelper(1, H);
    }

    // Hjælpemetode til rekursivt at skabe et perfekt binært træ
    private static TreeNode createPerfectBinaryTreeHelper(int currentLevel, int maxLevel) {
        if (currentLevel > maxLevel) {
            return null;
        }

        TreeNode node = new TreeNode(currentLevel);
        node.left = createPerfectBinaryTreeHelper(currentLevel + 1, maxLevel);
        node.right = createPerfectBinaryTreeHelper(currentLevel + 1, maxLevel);
        return node;
    }

    // In-order traversal for at vise træet
    public static void inOrderTraversal(TreeNode root) {
        if (root != null) {
            inOrderTraversal(root.left);
            System.out.print(root.val + " ");
            inOrderTraversal(root.right);
        }
    }

    public static void main(String[] args) {
        int H = 3; // Eksempelværdi for højden

        TreeNode root = createPerfectBinaryTree(H);

        System.out.print("In-order traversal af det perfekte binære træ: ");
        inOrderTraversal(root);
        System.out.println();
    }
}

