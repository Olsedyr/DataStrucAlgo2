// AVL Node class
class AVLNode {
    int value;
    AVLNode left, right;
    int height;

    public AVLNode(int item) {
        value = item;
        left = right = null;
        height = 1;
    }
}

// Complete AVL Tree - Exam Ready
class AVLTree {
    AVLNode root;

    AVLTree() {
        root = null;
    }

    // ========================================
    // CORE AVL METHODS - Know these for exam!
    // ========================================

    // Get height of a node
    int height(AVLNode node) {
        if (node == null) return 0;
        return node.height;
    }

    // Get balance factor of a node
    // Balance Factor = height(left subtree) - height(right subtree)
    // Valid AVL: balance factor must be -1, 0, or 1
    int getBalance(AVLNode node) {
        if (node == null) return 0;
        return height(node.left) - height(node.right);
    }

    // Update height of a node
    void updateHeight(AVLNode node) {
        if (node != null) {
            node.height = 1 + Math.max(height(node.left), height(node.right));
        }
    }

    // RIGHT ROTATION (for Left-Left case)
    // Used when left subtree is too heavy
    //       z                              y
    //      / \                            / \
    //     y   T4    Right Rotate (z)     x   z
    //    / \        --------------->    / \ / \
    //   x  T3                          T1 T2 T3 T4
    //  / \
    // T1 T2
    AVLNode rightRotate(AVLNode z) {
        AVLNode y = z.left;
        AVLNode T3 = y.right;

        // Perform rotation
        y.right = z;
        z.left = T3;

        // Update heights
        updateHeight(z);
        updateHeight(y);

        return y; // New root
    }

    // LEFT ROTATION (for Right-Right case)
    // Used when right subtree is too heavy
    //     z                              y
    //    / \                            / \
    //   T1  y      Left Rotate (z)    z   x
    //      / \     --------------->   / \ / \
    //     T2  x                      T1 T2 T3 T4
    //        / \
    //       T3 T4
    AVLNode leftRotate(AVLNode z) {
        AVLNode y = z.right;
        AVLNode T2 = y.left;

        // Perform rotation
        y.left = z;
        z.right = T2;

        // Update heights
        updateHeight(z);
        updateHeight(y);

        return y; // New root
    }

    // INSERT with auto-balancing
    // This is the MAIN method you need to understand!
    void insert(int value) {
        root = insertRec(root, value);
    }

    // Version that shows violations before fixing
    void insertWithDetection(int value) {
        System.out.println("Inserting " + value + "...");
        root = insertRecWithDetection(root, value, 0);
        System.out.println();
    }

    AVLNode insertRecWithDetection(AVLNode node, int value, int depth) {
        // STEP 1: Normal BST insertion
        if (node == null) {
            System.out.println("  ".repeat(depth) + "→ Created node " + value);
            return new AVLNode(value);
        }

        if (value < node.value) {
            System.out.println("  ".repeat(depth) + "At node " + node.value + ", go LEFT");
            node.left = insertRecWithDetection(node.left, value, depth + 1);
        } else if (value > node.value) {
            System.out.println("  ".repeat(depth) + "At node " + node.value + ", go RIGHT");
            node.right = insertRecWithDetection(node.right, value, depth + 1);
        } else {
            return node; // Duplicate not allowed
        }

        // STEP 2: Update height of current node
        updateHeight(node);

        // STEP 3: Get balance factor
        int balance = getBalance(node);

        System.out.println("  ".repeat(depth) + "↑ Node " + node.value + ": height=" + node.height + ", balance=" + balance);

        // Check for violations BEFORE fixing
        if (Math.abs(balance) > 1) {
            System.out.println("  ".repeat(depth) + "⚠ VIOLATION! Node " + node.value + " has balance=" + balance);
        }

        // STEP 4: If unbalanced, there are 4 cases:

        // Case 1: Left-Left (LL)
        if (balance > 1 && value < node.left.value) {
            System.out.println("  ".repeat(depth) + "→ Fixing with RIGHT rotation at node " + node.value);
            return rightRotate(node);
        }

        // Case 2: Right-Right (RR)
        if (balance < -1 && value > node.right.value) {
            System.out.println("  ".repeat(depth) + "→ Fixing with LEFT rotation at node " + node.value);
            return leftRotate(node);
        }

        // Case 3: Left-Right (LR)
        if (balance > 1 && value > node.left.value) {
            System.out.println("  ".repeat(depth) + "→ Fixing with LEFT-RIGHT double rotation");
            System.out.println("  ".repeat(depth) + "  Step 1: LEFT rotate on node " + node.left.value);
            node.left = leftRotate(node.left);
            System.out.println("  ".repeat(depth) + "  Step 2: RIGHT rotate on node " + node.value);
            return rightRotate(node);
        }

        // Case 4: Right-Left (RL)
        if (balance < -1 && value < node.right.value) {
            System.out.println("  ".repeat(depth) + "→ Fixing with RIGHT-LEFT double rotation");
            System.out.println("  ".repeat(depth) + "  Step 1: RIGHT rotate on node " + node.right.value);
            node.right = rightRotate(node.right);
            System.out.println("  ".repeat(depth) + "  Step 2: LEFT rotate on node " + node.value);
            return leftRotate(node);
        }

        // Return unchanged node if balanced
        return node;
    }

    AVLNode insertRec(AVLNode node, int value) {
        // STEP 1: Normal BST insertion
        if (node == null) {
            return new AVLNode(value);
        }

        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        } else {
            return node; // Duplicate not allowed
        }

        // STEP 2: Update height of current node
        updateHeight(node);

        // STEP 3: Get balance factor
        int balance = getBalance(node);

        // STEP 4: If unbalanced, there are 4 cases:

        // Case 1: Left-Left (LL)
        // Tree is left-heavy and insertion was in left subtree of left child
        if (balance > 1 && value < node.left.value) {
            return rightRotate(node);
        }

        // Case 2: Right-Right (RR)
        // Tree is right-heavy and insertion was in right subtree of right child
        if (balance < -1 && value > node.right.value) {
            return leftRotate(node);
        }

        // Case 3: Left-Right (LR)
        // Tree is left-heavy but insertion was in right subtree of left child
        // Solution: Left rotate on left child, then right rotate on node
        if (balance > 1 && value > node.left.value) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Case 4: Right-Left (RL)
        // Tree is right-heavy but insertion was in left subtree of right child
        // Solution: Right rotate on right child, then left rotate on node
        if (balance < -1 && value < node.right.value) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        // Return unchanged node if balanced
        return node;
    }

    // DELETE with auto-balancing
    void delete(int value) {
        root = deleteRec(root, value);
    }

    AVLNode deleteRec(AVLNode node, int value) {
        // STEP 1: Standard BST delete
        if (node == null) return node;

        if (value < node.value) {
            node.left = deleteRec(node.left, value);
        } else if (value > node.value) {
            node.right = deleteRec(node.right, value);
        } else {
            // Node found - delete it
            if (node.left == null || node.right == null) {
                AVLNode temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    node = null;
                } else {
                    node = temp;
                }
            } else {
                // Node with two children
                AVLNode temp = minValueNode(node.right);
                node.value = temp.value;
                node.right = deleteRec(node.right, temp.value);
            }
        }

        if (node == null) return node;

        // STEP 2: Update height
        updateHeight(node);

        // STEP 3: Get balance factor
        int balance = getBalance(node);

        // STEP 4: Balance the tree (4 cases, similar to insert)

        // Left-Left Case
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rightRotate(node);
        }

        // Left-Right Case
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Right Case
        if (balance < -1 && getBalance(node.right) <= 0) {
            return leftRotate(node);
        }

        // Right-Left Case
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // ========================================
    // UTILITY METHODS
    // ========================================

    boolean search(int value) {
        return searchRec(root, value) != null;
    }

    AVLNode searchRec(AVLNode node, int value) {
        if (node == null || node.value == value) return node;
        if (value < node.value) return searchRec(node.left, value);
        return searchRec(node.right, value);
    }

    void inOrder() {
        inOrderRec(root);
        System.out.println();
    }

    void inOrderRec(AVLNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.value + " ");
            inOrderRec(node.right);
        }
    }

    void preOrder() {
        preOrderRec(root);
        System.out.println();
    }

    void preOrderRec(AVLNode node) {
        if (node != null) {
            System.out.print(node.value + " ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }

    void printTree() {
        if (root == null) {
            System.out.println("(empty tree)");
            return;
        }
        printTreeHelper(root, "", true);
    }

    void printTreeHelper(AVLNode node, String prefix, boolean isTail) {
        if (node == null) return;
        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.value);
        if (node.left != null || node.right != null) {
            if (node.left != null) {
                printTreeHelper(node.left, prefix + (isTail ? "    " : "│   "), node.right == null);
            } else {
                System.out.println(prefix + (isTail ? "    " : "│   ") + "├── (null)");
            }
            if (node.right != null) {
                printTreeHelper(node.right, prefix + (isTail ? "    " : "│   "), true);
            } else {
                System.out.println(prefix + (isTail ? "    " : "│   ") + "└── (null)");
            }
        }
    }

    int getHeight() {
        return height(root);
    }

    int countNodes() {
        return countNodesRec(root);
    }

    int countNodesRec(AVLNode node) {
        if (node == null) return 0;
        return 1 + countNodesRec(node.left) + countNodesRec(node.right);
    }

    boolean isBalanced() {
        return checkBalanceRec(root) != -1;
    }

    int checkBalanceRec(AVLNode node) {
        if (node == null) return 0;
        int left = checkBalanceRec(node.left);
        if (left == -1) return -1;
        int right = checkBalanceRec(node.right);
        if (right == -1) return -1;
        if (Math.abs(left - right) > 1) return -1;
        return Math.max(left, right) + 1;
    }

    void showAllBalanceFactors() {
        showBalanceRec(root);
    }

    void showBalanceRec(AVLNode node) {
        if (node != null) {
            showBalanceRec(node.left);
            System.out.println("Node " + node.value + ": balance=" + getBalance(node) + ", height=" + node.height);
            showBalanceRec(node.right);
        }
    }

    // ========================================
    // EXAM DEMONSTRATION
    // ========================================

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           AVL TREE - EXAM CHARACTERISTICS                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");

        // CHANGE THESE VALUES FOR YOUR EXAM QUESTION
        int[] values = {11, 2, 13, 1, 9, 57, 3, 25, 90, 17};

        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("PART 1: AVL TREE CHARACTERISTICS (Write these in your answer)");
        System.out.println("═══════════════════════════════════════════════════════════\n");

        System.out.println("【Definition】");
        System.out.println("An AVL tree is a self-balancing Binary Search Tree where:");
        System.out.println("  • For every node, the height difference between left and");
        System.out.println("    right subtrees is at most 1");
        System.out.println("  • Balance Factor = height(left) - height(right)");
        System.out.println("  • Valid balance factors: -1, 0, or +1\n");

        System.out.println("【Key Properties】");
        System.out.println("  • Height: O(log n) - guaranteed!");
        System.out.println("  • Search: O(log n)");
        System.out.println("  • Insert: O(log n)");
        System.out.println("  • Delete: O(log n)");
        System.out.println("  • Always maintains BST property: left < root < right\n");

        System.out.println("【Four Rotation Cases】");
        System.out.println("  1. Left-Left (LL):   balance > 1, insertion in left-left");
        System.out.println("     → Fix: Single RIGHT rotation");
        System.out.println();
        System.out.println("  2. Right-Right (RR): balance < -1, insertion in right-right");
        System.out.println("     → Fix: Single LEFT rotation");
        System.out.println();
        System.out.println("  3. Left-Right (LR):  balance > 1, insertion in left-right");
        System.out.println("     → Fix: LEFT rotation on left child, then RIGHT on node");
        System.out.println();
        System.out.println("  4. Right-Left (RL):  balance < -1, insertion in right-left");
        System.out.println("     → Fix: RIGHT rotation on right child, then LEFT on node\n");

        System.out.println("【Advantages over regular BST】");
        System.out.println("  • Prevents worst-case O(n) height (like a linked list)");
        System.out.println("  • Guarantees O(log n) operations");
        System.out.println("  • Automatically maintains balance during insert/delete\n");

        System.out.println("【Disadvantages】");
        System.out.println("  • More complex implementation (needs rotations)");
        System.out.println("  • Extra storage for height in each node");
        System.out.println("  • Slightly slower insert/delete due to rebalancing\n");

        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("PART 2: DETAILED INSERTION - SEE VIOLATIONS!");
        System.out.println("═══════════════════════════════════════════════════════════\n");

        System.out.println("Values to insert: " + java.util.Arrays.toString(values));
        System.out.println("\nNow inserting with DETAILED output showing violations:\n");

        AVLTree avlDetailed = new AVLTree();

        for (int value : values) {
            avlDetailed.insertWithDetection(value);
        }

        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("PART 3: STEP-BY-STEP VISUAL");
        System.out.println("═══════════════════════════════════════════════════════════\n");

        System.out.println("Let me show you the critical moment when inserting 25:\n");

        AVLTree demo = new AVLTree();
        int[] beforeRotation = {11, 2, 13, 1, 9, 57, 3};

        for (int v : beforeRotation) {
            demo.insert(v);
        }

        System.out.println("Tree BEFORE inserting 25:");
        demo.printTree();
        System.out.println("\nBalance factors:");
        demo.showAllBalanceFactors();

        System.out.println("\n→ Now inserting 25...\n");
        demo.insertWithDetection(25);

        System.out.println("Tree AFTER rotation:");
        demo.printTree();
        System.out.println("\nBalance factors (now all valid!):");
        demo.showAllBalanceFactors();

        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("PART 4: FINAL BALANCED AVL TREE");
        System.out.println("═══════════════════════════════════════════════════════════\n");

        System.out.println("【Final Tree Structure】");
        avlDetailed.printTree();

        System.out.println("\n【Final Balance Factors - All must be -1, 0, or 1】");
        avlDetailed.showAllBalanceFactors();

        System.out.println("\n【Final Properties】");
        System.out.println("Height: " + avlDetailed.getHeight());
        System.out.println("Total nodes: " + avlDetailed.countNodes());
        System.out.println("Is balanced (AVL property satisfied): " + avlDetailed.isBalanced());

        System.out.println("\n【Traversals】");
        System.out.print("In-order (sorted): ");
        avlDetailed.inOrder();
        System.out.print("Pre-order: ");
        avlDetailed.preOrder();

        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("PART 4: FINAL BALANCED AVL TREE");
        System.out.println("═══════════════════════════════════════════════════════════\n");

        AVLTree avl = new AVLTree();
        for (int value : values) {
            avl.insert(value);
        }

        System.out.println("【Final Tree Structure】");
        avl.printTree();

        System.out.println("\n【Final Balance Factors - All must be -1, 0, or 1】");
        avl.showAllBalanceFactors();

        System.out.println("\n【Final Properties】");
        System.out.println("Height: " + avl.getHeight());
        System.out.println("Total nodes: " + avl.countNodes());
        System.out.println("Is balanced (AVL property satisfied): " + avl.isBalanced());

        System.out.println("\n【Traversals】");
        System.out.print("In-order (sorted): ");
        avl.inOrder();
        System.out.print("Pre-order: ");
        avl.preOrder();

        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("PART 5: KEY INSIGHT - ROTATION EXAMPLE");
        System.out.println("═══════════════════════════════════════════════════════════\n");

        System.out.println("When inserting 25 into the sequence " + java.util.Arrays.toString(values) + ":");
        System.out.println();
        System.out.println("BEFORE rotation (UNBALANCED):");
        System.out.println("    11");
        System.out.println("   /  \\");
        System.out.println("  2    13");
        System.out.println("       / \\");
        System.out.println("    (empty) 57");
        System.out.println("            /");
        System.out.println("          25  ← Just inserted");
        System.out.println();
        System.out.println("Node 13 has balance = -2 (VIOLATES AVL property!)");
        System.out.println("This is a Right-Left case.");
        System.out.println();
        System.out.println("ROTATION: Right rotate on 57, then Left rotate on 13");
        System.out.println();
        System.out.println("AFTER rotation (BALANCED):");
        System.out.println("    11");
        System.out.println("   /  \\");
        System.out.println("  2    25  ← 25 moved up!");
        System.out.println("      / \\");
        System.out.println("    13  57");
        System.out.println();
        System.out.println("Now all balance factors are -1, 0, or 1 ✓");

        System.out.println("\n═══════════════════════════════════════════════════════════");
        System.out.println("PART 6: CODE TO SHOW IN EXAM");
        System.out.println("═══════════════════════════════════════════════════════════\n");

        System.out.println("【Core Insert Method - This is what you write!】\n");
        System.out.println("AVLNode insertRec(AVLNode node, int value) {");
        System.out.println("    // STEP 1: Normal BST insertion");
        System.out.println("    if (node == null) return new AVLNode(value);");
        System.out.println("    ");
        System.out.println("    if (value < node.value)");
        System.out.println("        node.left = insertRec(node.left, value);");
        System.out.println("    else if (value > node.value)");
        System.out.println("        node.right = insertRec(node.right, value);");
        System.out.println("    else");
        System.out.println("        return node; // Duplicate");
        System.out.println("    ");
        System.out.println("    // STEP 2: Update height");
        System.out.println("    node.height = 1 + Math.max(height(node.left), height(node.right));");
        System.out.println("    ");
        System.out.println("    // STEP 3: Get balance factor");
        System.out.println("    int balance = getBalance(node);");
        System.out.println("    ");
        System.out.println("    // STEP 4: Fix imbalance (4 cases)");
        System.out.println("    ");
        System.out.println("    // Left-Left Case");
        System.out.println("    if (balance > 1 && value < node.left.value)");
        System.out.println("        return rightRotate(node);");
        System.out.println("    ");
        System.out.println("    // Right-Right Case");
        System.out.println("    if (balance < -1 && value > node.right.value)");
        System.out.println("        return leftRotate(node);");
        System.out.println("    ");
        System.out.println("    // Left-Right Case");
        System.out.println("    if (balance > 1 && value > node.left.value) {");
        System.out.println("        node.left = leftRotate(node.left);");
        System.out.println("        return rightRotate(node);");
        System.out.println("    }");
        System.out.println("    ");
        System.out.println("    // Right-Left Case");
        System.out.println("    if (balance < -1 && value < node.right.value) {");
        System.out.println("        node.right = rightRotate(node.right);");
        System.out.println("        return leftRotate(node);");
        System.out.println("    }");
        System.out.println("    ");
        System.out.println("    return node;");
        System.out.println("}\n");

        System.out.println("【Helper Methods】\n");
        System.out.println("int getBalance(AVLNode node) {");
        System.out.println("    if (node == null) return 0;");
        System.out.println("    return height(node.left) - height(node.right);");
        System.out.println("}\n");

        System.out.println("AVLNode rightRotate(AVLNode z) {");
        System.out.println("    AVLNode y = z.left;");
        System.out.println("    AVLNode T3 = y.right;");
        System.out.println("    y.right = z;");
        System.out.println("    z.left = T3;");
        System.out.println("    updateHeight(z);");
        System.out.println("    updateHeight(y);");
        System.out.println("    return y;");
        System.out.println("}\n");

        System.out.println("AVLNode leftRotate(AVLNode z) {");
        System.out.println("    AVLNode y = z.right;");
        System.out.println("    AVLNode T2 = y.left;");
        System.out.println("    y.left = z;");
        System.out.println("    z.right = T2;");
        System.out.println("    updateHeight(z);");
        System.out.println("    updateHeight(y);");
        System.out.println("    return y;");
        System.out.println("}\n");

        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("EXAM TIP: Remember the 4 rotation cases!");
        System.out.println("  LL → Right rotation");
        System.out.println("  RR → Left rotation");
        System.out.println("  LR → Left then Right (double rotation)");
        System.out.println("  RL → Right then Left (double rotation)");
        System.out.println();
        System.out.println("KEY POINT: The tree becomes UNBALANCED during insertion,");
        System.out.println("but rotations IMMEDIATELY fix it, so the final tree IS balanced!");
        System.out.println("═══════════════════════════════════════════════════════════");
    }
}