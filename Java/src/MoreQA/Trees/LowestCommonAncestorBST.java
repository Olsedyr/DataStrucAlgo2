package MoreQA.Trees;

public class LowestCommonAncestorBST {

    static class Node {
        int data;
        Node left, right;

        public Node(int data) {
            this.data = data;
            left = right = null;
        }
    }

    public static void main(String[] args) {
        // Create the binary search tree
        Node root = new Node(50);
        root.left = new Node(30);
        root.right = new Node(70);
        root.left.left = new Node(20);
        root.left.right = new Node(40);
        root.right.left = new Node(60);
        root.right.right = new Node(80);

        // Define the nodes to find the LCA
        Node p = root.left.left; // Node with value 20
        Node q = root.left.right; // Node with value 40

        // Find the LCA of the nodes p and q
        Node lca = findLCA(root, p, q);
        if (lca != null) {
            System.out.println("The Lowest Common Ancestor of " + p.data + " and " + q.data + " is: " + lca.data);
        } else {
            System.out.println("No common ancestor found.");
        }
    }

    // Function to find the LCA of two nodes in a BST
    // Time Complexity: O(h), where h is the height of the tree (in the worst case, O(n) for a skewed tree)
    // Space Complexity: O(1), no extra space is used other than the recursion stack
    public static Node findLCA(Node root, Node p, Node q) {
        // Base case: If the root is null, return null
        if (root == null) {
            return null;
        }

        // If both p and q are smaller than root, the LCA must be in the left subtree
        if (p.data < root.data && q.data < root.data) {
            return findLCA(root.left, p, q);
        }

        // If both p and q are greater than root, the LCA must be in the right subtree
        if (p.data > root.data && q.data > root.data) {
            return findLCA(root.right, p, q);
        }

        // If one of p or q is smaller and the other is greater, root is the LCA
        return root;
    }
}
