package MoreQA.Trees;

public class KthSmallestElementInBST {

    static class Node {
        int data;
        Node left, right;

        public Node(int data) {
            this.data = data;
            left = right = null;
        }
    }

    // This will keep track of the current count of visited nodes
    static int count = 0;
    static int kthSmallest = -1;

    public static void main(String[] args) {
        // Create the binary search tree
        Node root = new Node(50);
        root.left = new Node(30);
        root.right = new Node(70);
        root.left.left = new Node(20);
        root.left.right = new Node(40);
        root.right.left = new Node(60);
        root.right.right = new Node(80);

        // Find the 3rd smallest element in the BST
        int k = 2;
        kthSmallestElement(root, k);
        if (kthSmallest != -1) {
            System.out.println("The " + k + "-th smallest element in the BST is: " + kthSmallest);
        } else {
            System.out.println("The tree doesn't have " + k + " elements.");
        }
    }

    // Function to perform the in-order traversal and find the k-th smallest element
    // Time Complexity: O(n), where n is the number of nodes in the tree
    // Space Complexity: O(h), where h is the height of the tree (due to recursion stack)
    public static void kthSmallestElement(Node root, int k) {
        if (root == null || count >= k) {
            return; // Base case: null node or already found k-th smallest element
        }

        // Traverse the left subtree
        kthSmallestElement(root.left, k);

        // Increment the count for the visited node
        count++;

        // If the count is equal to k, this is the k-th smallest element
        if (count == k) {
            kthSmallest = root.data;
            return; // We found the k-th smallest element, no need to continue
        }

        // Traverse the right subtree
        kthSmallestElement(root.right, k);
    }
}
