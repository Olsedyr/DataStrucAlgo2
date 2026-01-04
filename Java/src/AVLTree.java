import java.util.ArrayList;
import java.util.List;

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
class AVLTreeExamReady {
    AVLNode root;

    AVLTreeExamReady() {
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
    AVLNode rightRotate(AVLNode z) {
        AVLNode y = z.left;
        AVLNode T3 = y.right;

        y.right = z;
        z.left = T3;

        updateHeight(z);
        updateHeight(y);

        return y;
    }

    // LEFT ROTATION (for Right-Right case)
    AVLNode leftRotate(AVLNode z) {
        AVLNode y = z.right;
        AVLNode T2 = y.left;

        y.left = z;
        z.right = T2;

        updateHeight(z);
        updateHeight(y);

        return y;
    }

    // INSERT with auto-balancing
    void insert(int value) {
        root = insertRec(root, value);
    }

    // INSERT with detection for exam demo
    void insertWithDetection(int value) {
        System.out.println("Inserting " + value + "...");
        root = insertRecWithDetection(root, value, 0);
        System.out.println();
    }

    AVLNode insertRecWithDetection(AVLNode node, int value, int depth) {
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
            System.out.println("  ".repeat(depth) + "Duplicate " + value + " ignored");
            return node;
        }

        updateHeight(node);

        int balance = getBalance(node);

        System.out.println("  ".repeat(depth) + "↑ Node " + node.value + ": height=" + node.height + ", balance=" + balance);

        if (Math.abs(balance) > 1) {
            System.out.println("  ".repeat(depth) + "⚠ VIOLATION! Node " + node.value + " has balance=" + balance);
        }

        // Fix imbalances
        if (balance > 1 && getBalance(node.left) >= 0) {
            System.out.println("  ".repeat(depth) + "→ Fixing LL with RIGHT rotation at " + node.value);
            return rightRotate(node);
        }
        if (balance > 1 && getBalance(node.left) < 0) {
            System.out.println("  ".repeat(depth) + "→ Fixing LR with LEFT-RIGHT rotation");
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) {
            System.out.println("  ".repeat(depth) + "→ Fixing RR with LEFT rotation at " + node.value);
            return leftRotate(node);
        }
        if (balance < -1 && getBalance(node.right) > 0) {
            System.out.println("  ".repeat(depth) + "→ Fixing RL with RIGHT-LEFT rotation");
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    AVLNode insertRec(AVLNode node, int value) {
        if (node == null) return new AVLNode(value);

        if (value < node.value) {
            node.left = insertRec(node.left, value);
        } else if (value > node.value) {
            node.right = insertRec(node.right, value);
        } else {
            return node;
        }

        updateHeight(node);

        int balance = getBalance(node);

        if (balance > 1 && value < node.left.value) return rightRotate(node);
        if (balance < -1 && value > node.right.value) return leftRotate(node);
        if (balance > 1 && value > node.left.value) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && value < node.right.value) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // DELETE with auto-balancing
    void delete(int value) {
        root = deleteRec(root, value);
    }

    // DELETE with detection for exam demo
    void deleteWithDetection(int value) {
        System.out.println("Deleting " + value + "...");
        root = deleteRecWithDetection(root, value, 0);
        System.out.println();
    }

    AVLNode deleteRecWithDetection(AVLNode node, int value, int depth) {
        if (node == null) {
            System.out.println("  ".repeat(depth) + "Value " + value + " not found");
            return node;
        }

        System.out.println("  ".repeat(depth) + "At node " + node.value);

        if (value < node.value) {
            System.out.println("  ".repeat(depth) + "Go LEFT");
            node.left = deleteRecWithDetection(node.left, value, depth + 1);
        } else if (value > node.value) {
            System.out.println("  ".repeat(depth) + "Go RIGHT");
            node.right = deleteRecWithDetection(node.right, value, depth + 1);
        } else {
            System.out.println("  ".repeat(depth) + "Found node to delete");
            if (node.left == null || node.right == null) {
                AVLNode temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    return null;
                } else {
                    return temp;
                }
            } else {
                AVLNode temp = minValueNode(node.right);
                System.out.println("  ".repeat(depth) + "Replace with min from right: " + temp.value);
                node.value = temp.value;
                node.right = deleteRecWithDetection(node.right, temp.value, depth + 1);
            }
        }

        updateHeight(node);

        int balance = getBalance(node);

        System.out.println("  ".repeat(depth) + "↑ Node " + node.value + ": height=" + node.height + ", balance=" + balance);

        if (Math.abs(balance) > 1) {
            System.out.println("  ".repeat(depth) + "⚠ VIOLATION! Node " + node.value + " has balance=" + balance);
        }

        // Fix imbalances
        if (balance > 1 && getBalance(node.left) >= 0) {
            System.out.println("  ".repeat(depth) + "→ Fixing LL with RIGHT rotation at " + node.value);
            return rightRotate(node);
        }
        if (balance > 1 && getBalance(node.left) < 0) {
            System.out.println("  ".repeat(depth) + "→ Fixing LR with LEFT-RIGHT rotation");
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) {
            System.out.println("  ".repeat(depth) + "→ Fixing RR with LEFT rotation at " + node.value);
            return leftRotate(node);
        }
        if (balance < -1 && getBalance(node.right) > 0) {
            System.out.println("  ".repeat(depth) + "→ Fixing RL with RIGHT-LEFT rotation");
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    AVLNode deleteRec(AVLNode node, int value) {
        if (node == null) return node;

        if (value < node.value) {
            node.left = deleteRec(node.left, value);
        } else if (value > node.value) {
            node.right = deleteRec(node.right, value);
        } else {
            if (node.left == null || node.right == null) {
                return (node.left != null) ? node.left : node.right;
            }
            AVLNode temp = minValueNode(node.right);
            node.value = temp.value;
            node.right = deleteRec(node.right, temp.value);
        }

        updateHeight(node);

        int balance = getBalance(node);

        if (balance > 1 && getBalance(node.left) >= 0) return rightRotate(node);
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) return leftRotate(node);
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    AVLNode minValueNode(AVLNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // SEARCH
    boolean search(int value) {
        AVLNode result = searchRec(root, value);
        if (result != null) {
            System.out.println("Found " + value);
            return true;
        } else {
            System.out.println(value + " not found");
            return false;
        }
    }

    AVLNode searchRec(AVLNode node, int value) {
        if (node == null || node.value == value) return node;
        if (value < node.value) return searchRec(node.left, value);
        return searchRec(node.right, value);
    }

    // UTILITY METHODS (same as original)
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
        printTreeVisual(root, "", true);
    }

    // Improved tree visualization with more intuitive formatting
    void printTreeVisual(AVLNode node, String prefix, boolean isLeft) {
        if (node != null) {
            System.out.println(prefix + (isLeft ? "├── " : "└── ") +
                    node.value + " (H:" + node.height + ", B:" + getBalance(node) + ")");
            printTreeVisual(node.left, prefix + (isLeft ? "│   " : "    "), true);
            printTreeVisual(node.right, prefix + (isLeft ? "│   " : "    "), false);
        }
    }

    // NEW: Graphical tree representation using ASCII art
    void printTreeGraphical() {
        if (root == null) {
            System.out.println("(empty tree)");
            return;
        }

        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║      AVL TREE VISUALIZATION      ║");
        System.out.println("╚═══════════════════════════════════╝");

        List<List<String>> lines = new ArrayList<>();
        List<AVLNode> level = new ArrayList<>();
        List<AVLNode> next = new ArrayList<>();

        level.add(root);
        int nn = 1;
        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<>();
            nn = 0;
            for (AVLNode n : level) {
                if (n == null) {
                    line.add(null);
                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.value + "(H:" + n.height + ")";
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.left);
                    next.add(n.right);

                    if (n.left != null) nn++;
                    if (n.right != null) nn++;
                }
            }

            if (widest % 2 == 1) widest++;
            lines.add(line);

            List<AVLNode> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 1).size() * (widest + 4);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {
                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of numbers
            for (int j = 0; j < line.size(); j++) {
                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
        }

        // Display balance summary
        System.out.println("\n═══════════════════════════════════");
        System.out.println("Balance Factors Summary:");
        showAllBalanceFactors();
        System.out.println("═══════════════════════════════════\n");
    }

    // Helper for printing tree
    void printTreeHelper(AVLNode node, String prefix, boolean isTail) {
        if (node == null) return;
        System.out.println(prefix + (isTail ? "└── " : "├── ") + node.value);
        printTreeHelper(node.left, prefix + (isTail ? "    " : "│   "), node.right == null);
        printTreeHelper(node.right, prefix + (isTail ? "    " : "│   "), true);
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

    // MAIN for exam demo - Ændr values og operations her!
    public static void main(String[] args) {
        AVLTreeExamReady avl = new AVLTreeExamReady();

        // Ændr dette array for dit eksamensspørgsmål
        int[] values = {11, 2, 13, 1, 9, 57, 3, 25, 90, 17};

        System.out.println("Inserting initial values: " + java.util.Arrays.toString(values));
        for (int value : values) {
            avl.insertWithDetection(value);
            System.out.println("Current tree after insert " + value + ":");
            avl.printTreeGraphical();  // Using the new graphical method
            System.out.println();
        }

        // Demo delete - Ændr værdien her for eksamen
        int deleteValue = 25; // Eksempel
        avl.deleteWithDetection(deleteValue);
        System.out.println("Tree after delete " + deleteValue + ":");
        avl.printTreeGraphical();  // Using the new graphical method
        System.out.println("Is balanced: " + avl.isBalanced());
        System.out.println("Height: " + avl.getHeight());
        System.out.println("Nodes: " + avl.countNodes());
        System.out.println();

        // Demo search - Ændr værdien her for eksamen
        int searchValue = 9; // Eksempel
        avl.search(searchValue);

        // Final traversals and balances
        System.out.println("\nFinal In-order: ");
        avl.inOrder();
        System.out.println("Final Pre-order: ");
        avl.preOrder();
    }
}