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

    // Method to add a new value into the binary search tree (renamed from insert)
    void add(int value) {
        root = addRec(root, value); // Call the recursive add method
    }

    // Recursive method to add a new value into the binary search tree
    Node addRec(Node root, int value) {
        // If the tree is empty, create a new node and return it
        if (root == null) {
            root = new Node(value);
            return root;
        }

        // Otherwise, recur down the tree
        if (value < root.value) {
            root.left = addRec(root.left, value); // Add in the left subtree
        } else if (value > root.value) {
            root.right = addRec(root.right, value); // Add in the right subtree
        }

        // Return the (unchanged) node pointer
        return root;
    }

    // Method to search for a value in the binary search tree
    boolean search(int value) {
        return searchRec(root, value) != null;
    }

    // Recursive method to search for a value
    Node searchRec(Node root, int value) {
        if (root == null || root.value == value) {
            return root;
        }
        if (value < root.value) {
            return searchRec(root.left, value);
        }
        return searchRec(root.right, value);
    }

    // Method to delete a value from the binary search tree
    void delete(int value) {
        root = deleteRec(root, value);
    }

    // Recursive method to delete a value
    Node deleteRec(Node root, int value) {
        if (root == null) {
            return root;
        }

        if (value < root.value) {
            root.left = deleteRec(root.left, value);
        } else if (value > root.value) {
            root.right = deleteRec(root.right, value);
        } else {
            // Node with only one child or no child
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }

            // Node with two children: Get the inorder successor (smallest in the right subtree)
            root.value = minValue(root.right);

            // Delete the inorder successor
            root.right = deleteRec(root.right, root.value);
        }

        return root;
    }

    // Helper method to find the minimum value in a subtree
    int minValue(Node root) {
        int minv = root.value;
        while (root.left != null) {
            minv = root.left.value;
            root = root.left;
        }
        return minv;
    }

    // Method for in-order traversal
    void inOrder() {
        inOrderRec(root);
        System.out.println();
    }

    // Recursive in-order traversal
    void inOrderRec(Node root) {
        if (root != null) {
            inOrderRec(root.left);
            System.out.print(root.value + " ");
            inOrderRec(root.right);
        }
    }

    // Method for pre-order traversal
    void preOrder() {
        preOrderRec(root);
        System.out.println();
    }

    // Recursive pre-order traversal
    void preOrderRec(Node root) {
        if (root != null) {
            System.out.print(root.value + " ");
            preOrderRec(root.left);
            preOrderRec(root.right);
        }
    }

    // Method for post-order traversal
    void postOrder() {
        postOrderRec(root);
        System.out.println();
    }

    // Recursive post-order traversal
    void postOrderRec(Node root) {
        if (root != null) {
            postOrderRec(root.left);
            postOrderRec(root.right);
            System.out.print(root.value + " ");
        }
    }

    // Method for level-order traversal (BFS)
    void levelOrder() {
        if (root == null) return;

        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node tempNode = queue.poll();
            System.out.print(tempNode.value + " ");

            if (tempNode.left != null) {
                queue.add(tempNode.left);
            }

            if (tempNode.right != null) {
                queue.add(tempNode.right);
            }
        }
        System.out.println();
    }

    // Method to calculate the height of the tree
    int height() {
        return heightRec(root);
    }

    int heightRec(Node root) {
        if (root == null) return 0;
        return 1 + Math.max(heightRec(root.left), heightRec(root.right));
    }

    // Method to count the number of nodes
    int countNodes() {
        return countNodesRec(root);
    }

    int countNodesRec(Node root) {
        if (root == null) return 0;
        return 1 + countNodesRec(root.left) + countNodesRec(root.right);
    }

    // Method to calculate internal path length (sum of depths of all nodes)
    long internalPathLength() {
        return internalPathLengthRec(root, 0);
    }

    long internalPathLengthRec(Node root, int depth) {
        if (root == null) return 0;
        return depth + internalPathLengthRec(root.left, depth + 1) + internalPathLengthRec(root.right, depth + 1);
    }

    // Method to create a perfect binary search tree of height h (>0)
    // Fills with values 1 to (2^h - 1) in sorted order
    static BinarySearchTree createPerfectTree(int h) {
        if (h <= 0) throw new IllegalArgumentException("Height must be greater than 0");
        BinarySearchTree tree = new BinarySearchTree();
        tree.root = createPerfectTreeRec(1, (int) Math.pow(2, h) - 1);
        return tree;
    }

    static Node createPerfectTreeRec(int start, int end) {
        if (start > end) return null;
        int mid = start + (end - start) / 2;
        Node node = new Node(mid);
        node.left = createPerfectTreeRec(start, mid - 1);
        node.right = createPerfectTreeRec(mid + 1, end);
        return node;
    }

    // Method to calculate sum of all node values
    int sumOfNodeValues() {
        return sumOfNodeValuesRec(root);
    }

    int sumOfNodeValuesRec(Node root) {
        if (root == null) return 0;
        return root.value + sumOfNodeValuesRec(root.left) + sumOfNodeValuesRec(root.right);
    }

    // Method to calculate sum of (level * number of nodes at level)
    // This is one interpretation of "summen af levels"
    int sumOfLevels() {
        if (root == null) return 0;

        java.util.Queue<Node> queue = new java.util.LinkedList<>();
        queue.add(root);
        int sum = 0;
        int level = 0;

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            sum += level * levelSize;

            for (int i = 0; i < levelSize; i++) {
                Node tempNode = queue.poll();

                if (tempNode.left != null) {
                    queue.add(tempNode.left);
                }

                if (tempNode.right != null) {
                    queue.add(tempNode.right);
                }
            }
            level++;
        }
        return sum;
    }

    // Main method to demonstrate all operations
    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();

        // Add nodes
        tree.add(50);
        tree.add(30);
        tree.add(20);
        tree.add(40);
        tree.add(70);
        tree.add(60);
        tree.add(80);

        // Traversals
        System.out.print("In-order traversal: ");
        tree.inOrder(); // Expected: 20 30 40 50 60 70 80

        System.out.print("Pre-order traversal: ");
        tree.preOrder(); // Expected: 50 30 20 40 70 60 80

        System.out.print("Post-order traversal: ");
        tree.postOrder(); // Expected: 20 40 30 60 80 70 50

        System.out.print("Level-order traversal: ");
        tree.levelOrder(); // Expected: 50 30 70 20 40 60 80

        // Search
        System.out.println("Search for 40: " + tree.search(40)); // true
        System.out.println("Search for 90: " + tree.search(90)); // false

        // Delete
        tree.delete(20);
        System.out.print("In-order after deleting 20: ");
        tree.inOrder(); // Expected: 30 40 50 60 70 80

        // Tree properties
        System.out.println("Height of tree: " + tree.height()); // 3
        System.out.println("Number of nodes: " + tree.countNodes()); // 6
        System.out.println("Internal path length: " + tree.internalPathLength()); // Sum of depths
        System.out.println("Sum of node values: " + tree.sumOfNodeValues());
        System.out.println("Sum of levels (level * nodes at level): " + tree.sumOfLevels());

        // Create perfect tree
        BinarySearchTree perfectTree = createPerfectTree(3);
        System.out.print("Perfect tree (height 3) in-order: ");
        perfectTree.inOrder(); // 1 2 3 4 5 6 7
        System.out.println("Perfect tree height: " + perfectTree.height()); // 3
    }
}