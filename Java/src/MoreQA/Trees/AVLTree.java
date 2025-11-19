package MoreQA.Trees;

import java.util.LinkedList;
import java.util.Queue;

public class AVLTree {

    static class Node {
        int data, height;
        Node left, right;

        public Node(int data) {
            this.data = data;
            this.height = 1;
            left = null;
            right = null;
        }
    }

    public static void main(String[] args) {
        Node root = new Node(1);
        root.left = new Node(3);
        root.right = new Node(2);
        root.left.left = new Node(4);
        root.left.left.left = new Node(3);
        root.left.left.left.left = new Node(3);
        root.left.right = new Node(5);
        root.right.left = new Node(6);
        root.right.right = new Node(7);

        System.out.println("Level Order Traversal:");
        levelOrder(root);

        if (isAVL(root)) {
            System.out.println("\nThe tree is already an AVL tree.");
        } else {
            System.out.println("\nThe tree is not an AVL tree. Converting it to an AVL tree...");
            root = makeAVL(root);
            System.out.println("\nTree structure after balancing:");
            printTreeStructure(root, "root");
        }
    }

    public static void levelOrder(Node root) {
        if (root == null) return;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.print(current.data + " ");
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
    }

    public static boolean isAVL(Node root) {
        return checkBalance(root) != -1;
    }

    private static int checkBalance(Node node) {
        if (node == null) return 0;
        int leftHeight = checkBalance(node.left);
        int rightHeight = checkBalance(node.right);
        if (leftHeight == -1 || rightHeight == -1 || Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }
        return Math.max(leftHeight, rightHeight) + 1;
    }

    public static Node makeAVL(Node root) {
        if (root == null) return null;
        root.left = makeAVL(root.left);
        root.right = makeAVL(root.right);
        root.height = 1 + Math.max(height(root.left), height(root.right));
        int balance = getBalance(root);

        if (balance > 1 && getBalance(root.left) >= 0) return rightRotate(root);
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }
        if (balance < -1 && getBalance(root.right) <= 0) return leftRotate(root);
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }
        return root;
    }

    public static Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    public static Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    public static int height(Node node) {
        return (node == null) ? 0 : node.height;
    }

    public static int getBalance(Node node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    public static void printTreeStructure(Node node, String position) {
        if (node != null) {
            System.out.println(position + " = " + node.data);
            printTreeStructure(node.left, position + ".left");
            printTreeStructure(node.right, position + ".right");
        }
    }
}
