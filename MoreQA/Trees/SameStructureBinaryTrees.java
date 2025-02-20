package MoreQA.Trees;

public class SameStructureBinaryTrees {

    static class Node {
        int data;
        Node left, right;

        // Constructor
        public Node(int data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    public static void main(String[] args) {
        // Create two binary trees with the same structure
        Node root1 = new Node(1);
        root1.left = new Node(2);
        root1.right = new Node(3);
        root1.left.left = new Node(4);
        root1.left.right = new Node(5);

        Node root2 = new Node(10);
        root2.left = new Node(20);
        root2.right = new Node(30);
        root2.left.left = new Node(40);
        root2.left.right = new Node(50);

        // Check if both trees have the same structure
        System.out.println("Do the trees have the same structure?");
        System.out.println("Result (same structure): " + haveSameStructure(root1, root2));

        // Modify the structure of the second tree
        root2.right.right = new Node(60);
        System.out.println("\nAfter modifying tree 2, do they have the same structure?");
        System.out.println("Result (same structure): " + haveSameStructure(root1, root2));
    }

    // Function to check if two binary trees have the same structure
    // Time Complexity: O(n), where n is the number of nodes in the trees.
    // Space Complexity: O(h), where h is the height of the tree due to recursion stack.
    public static boolean haveSameStructure(Node root1, Node root2) {
        // If both nodes are null, they are structurally the same (base case)
        if (root1 == null && root2 == null) {
            return true;
        }

        // If one of the nodes is null and the other is not, the trees have different structures
        if (root1 == null || root2 == null) {
            return false;
        }

        // Recursively check if the left and right subtrees have the same structure
        return haveSameStructure(root1.left, root2.left) && haveSameStructure(root1.right, root2.right);
    }
}
