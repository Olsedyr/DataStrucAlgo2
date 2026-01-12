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

    //Is of minimal Height?
    String minimalHeight() {
        int n = countNodes();
        if (n == 0) return "true (height should be 0)"; // empty tree has height 0

        int minimalHeight = (int) Math.ceil(Math.log(n + 1) / Math.log(2)) - 1;
        int actualHeight = height();

        return (actualHeight == minimalHeight) + " (height should be " + minimalHeight + ")";
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

    // Public wrapper method to find path from root to a specific value
    public String findPath(int value) {
        StringBuilder path = new StringBuilder();
        boolean found = findPathRec(root, value, path);
        if (found) {
            return path.toString().trim();
        } else {
            return "Værdi " + value + " findes ikke i træet";
        }
    }

    // Private recursive method to find path to a value
    private boolean findPathRec(Node root, int value, StringBuilder path) {
        // Base case: empty tree
        if (root == null) {
            return false;
        }

        // Add current node to path
        path.append(root.value).append(" ");

        // If we found the value, return true
        if (root.value == value) {
            return true;
        }

        // Recursively search in left or right subtree based on BST property
        if (value < root.value) {
            if (findPathRec(root.left, value, path)) {
                return true;
            }
        } else {
            if (findPathRec(root.right, value, path)) {
                return true;
            }
        }

        // If value not found in this path, remove current node from path
        // Remove the last added value and space
        int lastSpace = path.lastIndexOf(" ");
        if (lastSpace > 0) {
            path.setLength(lastSpace);
        }
        return false;
    }

    // Method to check if tree is balanced (height difference <= 1 for all nodes)
    boolean isBalanced() {
        return isBalancedRec(root) != -1;
    }

    int isBalancedRec(Node root) {
        if (root == null) return 0;
        int leftHeight = isBalancedRec(root.left);
        if (leftHeight == -1) return -1;
        int rightHeight = isBalancedRec(root.right);
        if (rightHeight == -1) return -1;
        if (Math.abs(leftHeight - rightHeight) > 1) return -1;
        return Math.max(leftHeight, rightHeight) + 1;
    }

    // Method to find minimum value in the tree
    int findMin() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        return findMinRec(root);
    }

    int findMinRec(Node root) {
        if (root.left == null) return root.value;
        return findMinRec(root.left);
    }

    // Method to find maximum value in the tree
    int findMax() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        return findMaxRec(root);
    }

    int findMaxRec(Node root) {
        if (root.right == null) return root.value;
        return findMaxRec(root.right);
    }

    // Method to count leaf nodes (nodes with no children)
    int countLeaves() {
        return countLeavesRec(root);
    }

    int countLeavesRec(Node root) {
        if (root == null) return 0;
        if (root.left == null && root.right == null) return 1;
        return countLeavesRec(root.left) + countLeavesRec(root.right);
    }

    // Method to count internal nodes (nodes with at least one child)
    int countInternalNodes() {
        return countInternalNodesRec(root);
    }

    int countInternalNodesRec(Node root) {
        if (root == null || (root.left == null && root.right == null)) return 0;
        return 1 + countInternalNodesRec(root.left) + countInternalNodesRec(root.right);
    }

    // Method to find the kth smallest element (in-order traversal based)
    // Rettet metode til at finde k'te mindste element
    Integer findKthSmallest(int k) {
        if (k <= 0 || k > countNodes()) return null;
        int[] count = new int[]{0}; // Brug array for at kunne ændre i rekursion
        Integer[] result = new Integer[]{null};
        findKthSmallestRec(root, k, count, result);
        return result[0];
    }

    void findKthSmallestRec(Node root, int k, int[] count, Integer[] result) {
        if (root == null || result[0] != null) return;
        // Gå til venstre først (in-order)
        findKthSmallestRec(root.left, k, count, result);
        // Inkrementer tæller
        count[0]++;
        if (count[0] == k) {
            result[0] = root.value;
            return;
        }
        // Gå til højre
        findKthSmallestRec(root.right, k, count, result);
    }

    // Method to check if a value exists at a specific depth
    boolean existsAtDepth(int value, int targetDepth) {
        return existsAtDepthRec(root, value, targetDepth, 0);
    }

    boolean existsAtDepthRec(Node root, int value, int targetDepth, int currentDepth) {
        if (root == null) return false;
        if (root.value == value && currentDepth == targetDepth) return true;
        if (currentDepth >= targetDepth) return false;
        return existsAtDepthRec(root.left, value, targetDepth, currentDepth + 1) || existsAtDepthRec(root.right, value, targetDepth, currentDepth + 1);
    }

    // Method to find depth/level of a specific node (root is at depth 0)
    int findDepth(int value) {
        int depth = findDepthRec(root, value, 0);
        return depth == -1 ? -1 : depth;
    }

    int findDepthRec(Node root, int value, int currentDepth) {
        if (root == null) return -1;
        if (root.value == value) return currentDepth;
        if (value < root.value) {
            return findDepthRec(root.left, value, currentDepth + 1);
        } else {
            return findDepthRec(root.right, value, currentDepth + 1);
        }
    }

    // Method to check if tree is a valid BST
    boolean isBST() {
        return isBSTRec(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    boolean isBSTRec(Node root, int min, int max) {
        if (root == null) return true;
        if (root.value <= min || root.value >= max) return false;
        return isBSTRec(root.left, min, root.value) && isBSTRec(root.right, root.value, max);
    }

    // Method to find lowest common ancestor (LCA) of two nodes
    Integer findLCA(int value1, int value2) {
        // Tjek om begge værdier findes
        if (!search(value1) || !search(value2)) {
            return null;
        }
        Node lca = findLCARec(root, value1, value2);
        return lca == null ? null : lca.value;
    }

    // Hjælpemetode til LCA
    Node findLCARec(Node root, int value1, int value2) {
        if (root == null) return null;

        // Hvis begge værdier er mindre end root, LCA er i venstre subtree
        if (value1 < root.value && value2 < root.value) {
            return findLCARec(root.left, value1, value2);
        }

        // Hvis begge værdier er større end root, LCA er i højre subtree
        if (value1 > root.value && value2 > root.value) {
            return findLCARec(root.right, value1, value2);
        }

        // Ellers er root LCA
        return root;
    }

    // Method to count nodes in a given range [low, high]
    int countInRange(int low, int high) {
        return countInRangeRec(root, low, high);
    }

    int countInRangeRec(Node root, int low, int high) {
        if (root == null) return 0;
        int count = 0;
        if (root.value >= low && root.value <= high) count = 1;
        if (root.value > low) {
            count += countInRangeRec(root.left, low, high);
        }
        if (root.value < high) {
            count += countInRangeRec(root.right, low, high);
        }
        return count;
    }

    // Method to mirror/invert the tree (swap left and right children)
    void mirror() {
        mirrorRec(root);
    }

    void mirrorRec(Node root) {
        if (root == null) return;
        // Swap left and right children
        Node temp = root.left;
        root.left = root.right;
        root.right = temp;
        // Recursively mirror subtrees
        mirrorRec(root.left);
        mirrorRec(root.right);
    }

    // Method to find successor of a given value (next larger value)
    Integer findSuccessor(int value) {
        Node node = searchRec(root, value);
        if (node == null) return null;

        // Case 1: Node has right subtree - successor is leftmost in right subtree
        if (node.right != null) {
            return findMinRec(node.right);
        }

        // Case 2: No right subtree - find ancestor where node is in left subtree
        Integer successor = null;
        Node current = root;
        while (current != null) {
            if (value < current.value) {
                successor = current.value;
                current = current.left;
            } else if (value > current.value) {
                current = current.right;
            } else {
                break;
            }
        }
        return successor;
    }

    // Method to find predecessor of a given value (next smaller value)
    Integer findPredecessor(int value) {
        Node node = searchRec(root, value);
        if (node == null) return null;

        // Case 1: Node has left subtree - predecessor is rightmost in left subtree
        if (node.left != null) {
            return findMaxRec(node.left);
        }

        // Case 2: No left subtree - find ancestor where node is in right subtree
        Integer predecessor = null;
        Node current = root;
        while (current != null) {
            if (value > current.value) {
                predecessor = current.value;
                current = current.right;
            } else if (value < current.value) {
                current = current.left;
            } else {
                break;
            }
        }
        return predecessor;
    }

    // Method to check if two trees are identical
    boolean isIdentical(BinarySearchTree other) {
        return isIdenticalRec(this.root, other.root);
    }

    boolean isIdenticalRec(Node root1, Node root2) {
        if (root1 == null && root2 == null) return true;
        if (root1 == null || root2 == null) return false;
        return (root1.value == root2.value) && isIdenticalRec(root1.left, root2.left) && isIdenticalRec(root1.right, root2.right);
    }

    // Method to find distance between two nodes
    int findDistance(int value1, int value2) {
        Integer lca = findLCA(value1, value2);
        if (lca == null) return -1;
        int dist1 = findDepthRec(root, value1, 0);
        int dist2 = findDepthRec(root, value2, 0);
        int distLCA = findDepthRec(root, lca, 0);
        if (dist1 == -1 || dist2 == -1) return -1;
        return (dist1 - distLCA) + (dist2 - distLCA);
    }

    // Method to get width of tree at a given level
    int getWidth(int level) {
        return getWidthRec(root, level, 0);
    }

    int getWidthRec(Node root, int targetLevel, int currentLevel) {
        if (root == null) return 0;
        if (currentLevel == targetLevel) return 1;
        return getWidthRec(root.left, targetLevel, currentLevel + 1) + getWidthRec(root.right, targetLevel, currentLevel + 1);
    }

    // Method to find maximum width of the tree
    int getMaxWidth() {
        int maxWidth = 0;
        int h = height();
        for (int i = 0; i < h; i++) {
            int width = getWidth(i);
            maxWidth = Math.max(maxWidth, width);
        }
        return maxWidth;
    }

    // Method to print all paths from root to leaves
    void printAllPaths() {
        java.util.List<Integer> path = new java.util.ArrayList<>();
        printAllPathsRec(root, path);
    }

    void printAllPathsRec(Node root, java.util.List<Integer> path) {
        if (root == null) return;
        path.add(root.value);

        // If leaf node, print the path
        if (root.left == null && root.right == null) {
            for (int i = 0; i < path.size(); i++) {
                System.out.print(path.get(i));
                if (i < path.size() - 1) System.out.print(" -> ");
            }
            System.out.println();
        } else {
            printAllPathsRec(root.left, path);
            printAllPathsRec(root.right, path);
        }
        path.remove(path.size() - 1); // Backtrack
    }

    // Method to check if another tree is a subtree of this tree
    boolean isSubtree(BinarySearchTree other) {
        if (other.root == null) return true; // Empty tree is always a subtree
        if (root == null) return false; // Non-empty tree can't be subtree of empty tree
        return isSubtreeRec(root, other.root);
    }

    // Recursive method to check if tree rooted at node2 is subtree of tree rooted at node1
    private boolean isSubtreeRec(Node node1, Node node2) {
        if (node2 == null) return true;
        if (node1 == null) return false;

        // Check if subtrees match starting from this node
        if (node1.value == node2.value && matchTrees(node1, node2)) {
            return true;
        }

        // Otherwise, check in left or right subtree
        return isSubtreeRec(node1.left, node2) || isSubtreeRec(node1.right, node2);
    }

    // Helper method to check if two trees match exactly (structure and values)
    private boolean matchTrees(Node n1, Node n2) {
        if (n1 == null && n2 == null) return true;
        if (n1 == null || n2 == null) return false;
        return (n1.value == n2.value) &&
                matchTrees(n1.left, n2.left) &&
                matchTrees(n1.right, n2.right);
    }

    // Main method to demonstrate all operations
    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();

        //Original tree


        // Add nodes
        tree.add(60);
        tree.add(41);
        tree.add(74);
        tree.add(16);
        tree.add(53);
        tree.add(65);
        tree.add(25);
        tree.add(46);
        tree.add(55);
        tree.add(63);
        tree.add(70);
        tree.add(42);
        tree.add(62);
        tree.add(64);





        // Traversals Original
        System.out.print("In-order traversal: ");
        tree.inOrder(); // Expected: 10 12 15 20 45 50 55 79 90

        System.out.print("Pre-order traversal: ");
        tree.preOrder(); // Expected: 45 15 10 12 20 79 55 50 90

        System.out.print("Post-order traversal: ");
        tree.postOrder(); // Expected: 12 10 20 15 50 55 90 79 45

        System.out.print("Level-order traversal: ");
        tree.levelOrder(); // Expected: 45 15 79 10 20 55 90 12 50

        // Search
        System.out.println("Search for 40: " + tree.search(40)); // false
        System.out.println("Search for 90: " + tree.search(90)); // true


        // Tree properties
        System.out.println("Height of tree: " + tree.height()); // 4
        System.out.println("Number of nodes: " + tree.countNodes()); // 8 (after deleting 20)
        System.out.println("Internal path length (Number of nodes in row * the row number, do for all rows and add): " + tree.internalPathLength()); // Sum of depths
        System.out.println("Sum of node values: " + tree.sumOfNodeValues());
        System.out.println("Sum of levels (level * nodes at level): " + tree.sumOfLevels());


        // Path finding examples (works with any value)
        System.out.println("\n--- Path Finding Examples ---");
        System.out.println("Path to 10: " + tree.findPath(10)); // 45 15 10
        System.out.println("Path to 50: " + tree.findPath(50)); // 45 79 55 50
        System.out.println("Path to 45: " + tree.findPath(45)); // 45
        System.out.println("Path to 90: " + tree.findPath(90)); // 45 79 90
        System.out.println("Path to 12: " + tree.findPath(12)); // 45 15 10 12
        System.out.println("Path to 100: " + tree.findPath(100)); // Error message
        System.out.println("Path to 1: " + tree.findPath(1)); // Error message

        // Create perfect tree
        System.out.println("\n--- Perfect Tree ---");
        BinarySearchTree perfectTree = createPerfectTree(3);
        System.out.print("Perfect tree (height 3) in-order: ");
        perfectTree.inOrder(); // 1 2 3 4 5 6 7
        System.out.println("Perfect tree height: " + perfectTree.height()); // 3
        System.out.println("Path to 1 in perfect tree: " + perfectTree.findPath(1)); // 4 2 1
        System.out.println("Path to 7 in perfect tree: " + perfectTree.findPath(7)); // 4 6 7

        // Additional characteristics demonstrations
        System.out.println("\n--- Additional Tree Characteristics ---");
        System.out.println("Is tree balanced? " + tree.isBalanced()); // false (height difference > 1)
        System.out.println("Minimum value: " + tree.findMin()); // 10
        System.out.println("Maximum value: " + tree.findMax()); // 90
        System.out.println("Number of leaf nodes: " + tree.countLeaves()); // 3 (12, 20, 50)
        System.out.println("Number of internal nodes: " + tree.countInternalNodes()); // 6
        System.out.println("3rd smallest element: " + tree.findKthSmallest(3)); // 15
        System.out.println("Depth of node 50: " + tree.findDepth(50)); // 3
        System.out.println("Is valid BST? " + tree.isBST()); // true
        System.out.println("LCA of 10 and 20: " + tree.findLCA(10, 20)); // 15
        System.out.println("LCA of 12 and 50: " + tree.findLCA(12, 50)); // 45
        System.out.println("Nodes in range [15, 55]: " + tree.countInRange(15, 55)); // 5 (15, 20, 45, 50, 55)
        System.out.println("Does 50 exist at depth 3? " + tree.existsAtDepth(50, 3)); // true

        System.out.println("\n--- More Advanced Operations ---");
        System.out.println("Successor of 15: " + tree.findSuccessor(15)); // 20
        System.out.println("Predecessor of 55: " + tree.findPredecessor(55)); // 50
        System.out.println("Distance between 12 and 50: " + tree.findDistance(12, 50)); // 5
        System.out.println("Width at level 2: " + tree.getWidth(2)); // 4
        System.out.println("Maximum width: " + tree.getMaxWidth()); // 4
        System.out.println("Is tree of minimal height? " + tree.minimalHeight());

        System.out.println("\nAll paths from root to leaves:");
        tree.printAllPaths();

        // Test identical trees
        BinarySearchTree tree2 = new BinarySearchTree();
        tree2.add(45);
        tree2.add(15);
        tree2.add(79);
        System.out.println("\nAre tree and tree2 identical? " + tree.isIdentical(tree2)); // false

        // Test subtree
        System.out.println("\n--- Subtree Check Examples ---");
        BinarySearchTree subtreeTrue = new BinarySearchTree();
        subtreeTrue.add(80);
        subtreeTrue.add(85);
        subtreeTrue.add(70);
        System.out.println("Is a subtree?" + tree.isSubtree(subtreeTrue)); // Should be true


        //System.out.println("Is perfectTree a subtree? " + tree.isSubtree(perfectTree)); // Should be false


        //Operations on tree:
        System.out.println("\n--- Operations on the tree and new characterristics ---");

        tree.delete(105);
        tree.add(83);
        tree.delete(130);
        tree.add(60);
        tree.delete(95);


        // Traversals Original
        System.out.print("In-order traversal: ");
        tree.inOrder(); // Expected: 10 12 15 20 45 50 55 79 90

        System.out.print("Pre-order traversal: ");
        tree.preOrder(); // Expected: 45 15 10 12 20 79 55 50 90

        System.out.print("Post-order traversal: ");
        tree.postOrder(); // Expected: 12 10 20 15 50 55 90 79 45

        System.out.print("Level-order traversal: ");
        tree.levelOrder(); // Expected: 45 15 79 10 20 55 90 12 50

        // Search
        System.out.println("Search for 40: " + tree.search(40)); // false
        System.out.println("Search for 90: " + tree.search(90)); // true


        // Tree properties
        System.out.println("Height of tree: " + tree.height()); // 4
        System.out.println("Number of nodes: " + tree.countNodes()); // 8 (after deleting 20)
        System.out.println("Internal path length (Number of nodes in row * the row number, do for all rows and add): " + tree.internalPathLength()); // Sum of depths
        System.out.println("Sum of node values: " + tree.sumOfNodeValues());
        System.out.println("Sum of levels (level * nodes at level): " + tree.sumOfLevels());


        // Path finding examples (works with any value)
        System.out.println("\n--- Path Finding Examples ---");
        System.out.println("Path to 10: " + tree.findPath(10)); // 45 15 10
        System.out.println("Path to 50: " + tree.findPath(50)); // 45 79 55 50
        System.out.println("Path to 45: " + tree.findPath(45)); // 45
        System.out.println("Path to 90: " + tree.findPath(90)); // 45 79 90
        System.out.println("Path to 12: " + tree.findPath(12)); // 45 15 10 12
        System.out.println("Path to 100: " + tree.findPath(100)); // Error message
        System.out.println("Path to 1: " + tree.findPath(1)); // Error message

        // Create perfect tree
        System.out.println("\n--- Perfect Tree ---");
        BinarySearchTree perfectTree2 = createPerfectTree(3);
        System.out.print("Perfect tree (height 3) in-order: ");
        perfectTree.inOrder(); // 1 2 3 4 5 6 7
        System.out.println("Perfect tree height: " + perfectTree.height()); // 3
        System.out.println("Path to 1 in perfect tree: " + perfectTree.findPath(1)); // 4 2 1
        System.out.println("Path to 7 in perfect tree: " + perfectTree.findPath(7)); // 4 6 7

        // Additional characteristics demonstrations
        System.out.println("\n--- Additional Tree Characteristics ---");
        System.out.println("Is tree balanced? " + tree.isBalanced()); // false (height difference > 1)
        System.out.println("Minimum value: " + tree.findMin()); // 10
        System.out.println("Maximum value: " + tree.findMax()); // 90
        System.out.println("Number of leaf nodes: " + tree.countLeaves()); // 3 (12, 20, 50)
        System.out.println("Number of internal nodes: " + tree.countInternalNodes()); // 6
        System.out.println("3rd smallest element: " + tree.findKthSmallest(3)); // 15
        System.out.println("Depth of node 50: " + tree.findDepth(50)); // 3
        System.out.println("Is valid BST? " + tree.isBST()); // true
        System.out.println("LCA of 10 and 20: " + tree.findLCA(10, 20)); // 15
        System.out.println("LCA of 12 and 50: " + tree.findLCA(12, 50)); // 45
        System.out.println("Nodes in range [15, 55]: " + tree.countInRange(15, 55)); // 5 (15, 20, 45, 50, 55)
        System.out.println("Does 50 exist at depth 3? " + tree.existsAtDepth(50, 3)); // true

        System.out.println("\n--- More Advanced Operations ---");
        System.out.println("Successor of 15: " + tree.findSuccessor(15)); // 20
        System.out.println("Predecessor of 55: " + tree.findPredecessor(55)); // 50
        System.out.println("Distance between 12 and 50: " + tree.findDistance(12, 50)); // 5
        System.out.println("Width at level 2: " + tree.getWidth(2)); // 4
        System.out.println("Maximum width: " + tree.getMaxWidth()); // 4
        System.out.println("Is tree of minimal height? " + tree.minimalHeight());

        System.out.println("\nAll paths from root to leaves:");
        tree.printAllPaths();

        // Test identical trees
        BinarySearchTree tree3 = new BinarySearchTree();
        tree3.add(45);
        tree3.add(15);
        tree3.add(79);
        System.out.println("\nAre tree and tree2 identical? " + tree.isIdentical(tree2)); // false

        // Test subtree after operations
        System.out.println("\n--- Subtree Check Examples After Operations ---");
        BinarySearchTree subtreeTrue2 = new BinarySearchTree();
        subtreeTrue2.add(80);
        subtreeTrue2.add(70);
        subtreeTrue2.add(85);
        System.out.println("Is (80 with 70 and 85) a subtree? " + tree.isSubtree(subtreeTrue2)); // Assuming it matches after changes

        BinarySearchTree subtreeFalse2 = new BinarySearchTree();
        subtreeFalse2.add(95); // Deleted
        System.out.println("Is (95) a subtree? " + tree.isSubtree(subtreeFalse2)); // Should be false after delete
    }
}