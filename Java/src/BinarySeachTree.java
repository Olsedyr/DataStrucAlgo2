// Class representing a node in the binary search tree
class Node {
    int value; // Value of the node
    Node left, right; // Left and right children of the node

    // Constructor to create a new node with a given value
    public Node(int item) {
        value = item;
        left = right = null; // Initialize left and right children as null
    }
}

// Class representing the binary search tree
class BinarySearchTree {
    Node root; // Root of the binary search tree

    // Constructor to initialize the binary search tree
    BinarySearchTree() {
        root = null; // Initially, the tree is empty
    }

    // Method to insert a new value into the binary search tree
    void insert(int value) {
        root = insertRec(root, value); // Call the recursive insert method
    }

    // Recursive method to insert a new value into the binary search tree
    Node insertRec(Node root, int value) {
        // If the tree is empty, create a new node and return it
        if (root == null) {
            root = new Node(value);
            return root;
        }

        // Otherwise, recur down the tree
        if (value < root.value) {
            root.left = insertRec(root.left, value); // Insert in the left subtree
        } else if (value > root.value) {
            root.right = insertRec(root.right, value); // Insert in the right subtree
        }

        // Return the (unchanged) node pointer
        return root;
    }

    // Method to perform an in-order traversal of the binary search tree
    void inOrder() {
        inOrderRec(root); // Call the recursive in-order traversal method
    }

    // Recursive method for in-order traversal of the binary search tree
    void inOrderRec(Node root) {
        if (root != null) {
            inOrderRec(root.left); // Traverse the left subtree
            System.out.print(root.value + " "); // Print the value of the node
            inOrderRec(root.right); // Traverse the right subtree
        }
    }

    // Main method to demonstrate the binary search tree operations
    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree(); // Create a new binary search tree

        // Insert nodes into the binary search tree
        tree.insert(50);
        tree.insert(30);
        tree.insert(20);
        tree.insert(40);
        tree.insert(70);
        tree.insert(60);
        tree.insert(80);

        // Print the in-order traversal of the binary search tree
        tree.inOrder(); // Output should be: 20 30 40 50 60 70 80
    }
}