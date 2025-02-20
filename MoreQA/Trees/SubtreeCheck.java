package MoreQA.Trees;

public class SubtreeCheck {

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
        // Creating a sample binary tree T1
        Node T1 = new Node(1);
        T1.left = new Node(2);
        T1.right = new Node(3);
        T1.left.left = new Node(4);
        T1.left.right = new Node(5);
        T1.right.left = new Node(6);
        T1.right.right = new Node(7);

        // Creating a sample binary tree T2 (which is a subtree of T1)
        Node T2 = new Node(2);
        T2.left = new Node(4);
        T2.right = new Node(5);

        // Check if T2 is a subtree of T1
        if (isSubtree(T1, T2)) {
            System.out.println("T2 is a subtree of T1.");
        } else {
            System.out.println("T2 is NOT a subtree of T1.");
        }
    }

    // Function to check if tree T2 is a subtree of tree T1
    // Time Complexity: O(n * m), where n is the number of nodes in T1 and m is the number of nodes in T2.
    // Space Complexity: O(h), where h is the height of the recursion stack.
    public static boolean isSubtree(Node T1, Node T2) {
        if (T2 == null) {
            return true; // An empty tree is always a subtree
        }
        if (T1 == null) {
            return false; // A non-empty tree can't be a subtree of a null tree
        }

        // Check if the trees rooted at T1 and T2 are identical
        if (isIdentical(T1, T2)) {
            return true;
        }

        // Recursively check the left and right subtrees of T1
        return isSubtree(T1.left, T2) || isSubtree(T1.right, T2);
    }

    // Helper function to check if two trees are identical
    // Time Complexity: O(n), where n is the number of nodes in the tree
    // Space Complexity: O(h), where h is the height of the recursion stack
    public static boolean isIdentical(Node T1, Node T2) {
        // If both trees are empty, they are identical
        if (T1 == null && T2 == null) {
            return true;
        }

        // If one of the trees is empty or the data doesn't match, they are not identical
        if (T1 == null || T2 == null || T1.data != T2.data) {
            return false;
        }

        // Recursively check the left and right subtrees
        return isIdentical(T1.left, T2.left) && isIdentical(T1.right, T2.right);
    }
}
