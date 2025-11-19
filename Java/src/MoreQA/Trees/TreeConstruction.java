package MoreQA.Trees;

import java.util.HashMap;

public class TreeConstruction {

    static class Node {
        int data;
        Node left, right;

        public Node(int data) {
            this.data = data;
            left = right = null;
        }
    }

    // A map to store the indices of elements in inorder traversal for quick look-up
    static HashMap<Integer, Integer> inorderIndexMap = new HashMap<>();
    static int preorderIndex = 0; // To keep track of the current index in preorder traversal

    public static void main(String[] args) {
        // Sample preorder and inorder traversals
        int[] preorder = {1, 2, 4, 5, 3, 6, 7};
        int[] inorder = {4, 2, 5, 1, 6, 3, 7};

        int n = preorder.length;

        // Construct the tree from preorder and inorder traversals
        Node root = buildTree(preorder, inorder, 0, n - 1);

        // Print the inorder traversal of the constructed tree (should match the input inorder)
        System.out.print("Inorder of constructed tree: ");
        inorderTraversal(root);
    }

    // Function to construct the binary tree from preorder and inorder traversals
    // Time Complexity: O(n), where n is the number of nodes (size of the traversal arrays)
    // Space Complexity: O(n), for the map and recursion stack
    public static Node buildTree(int[] preorder, int[] inorder, int inorderStart, int inorderEnd) {
        if (inorderStart > inorderEnd) {
            return null;
        }

        // Pick the current node from preorder using preorderIndex and increment it
        int currentNodeData = preorder[preorderIndex++];
        Node node = new Node(currentNodeData);

        // If this node has no children (leaf node), return the node
        if (inorderStart == inorderEnd) {
            return node;
        }

        // Find the index of this node in inorder traversal
        int inorderIndex = inorderIndexMap.get(currentNodeData);

        // Recursively build the left subtree and right subtree
        node.left = buildTree(preorder, inorder, inorderStart, inorderIndex - 1);
        node.right = buildTree(preorder, inorder, inorderIndex + 1, inorderEnd);

        return node;
    }

    // Function to fill the hashmap with inorder traversal elements and their indices
    // Time Complexity: O(n)
    public static void buildInorderIndexMap(int[] inorder) {
        for (int i = 0; i < inorder.length; i++) {
            inorderIndexMap.put(inorder[i], i);
        }
    }

    // Function to print inorder traversal of the tree
    // Time Complexity: O(n)
    public static void inorderTraversal(Node node) {
        if (node == null) {
            return;
        }

        inorderTraversal(node.left);
        System.out.print(node.data + " ");
        inorderTraversal(node.right);
    }
}
