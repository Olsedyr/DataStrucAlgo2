package MoreQA.Trees;

public class BSTisOfMinimalHeight {
    class Node {
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

    // Method to insert a new node in the BST
    public void insert(int data) {
        root = insertRec(root, data);
    }

    // Recursive method to insert a new node in the BST
    private Node insertRec(Node root, int data) {
        if (root == null) {
            root = new Node(data);
            return root;
        }
        if (data < root.data) {
            root.left = insertRec(root.left, data);
        } else if (data > root.data) {
            root.right = insertRec(root.right, data);
        }
        return root;
    }

    public static void main(String[] args) {
        BSTisOfMinimalHeight bst = new BSTisOfMinimalHeight();
        bst.insert(15);
        bst.insert(10);
        bst.insert(20);
        bst.insert(8);
        bst.insert(12);
        bst.insert(17);
        bst.insert(25);

        System.out.println("Is the tree of minimal height? " + bst.minimalHeight());
    }
}