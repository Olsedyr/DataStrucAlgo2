import java.util.*;

class SplayNode {
    int value;
    SplayNode left, right, parent;

    public SplayNode(int value) {
        this.value = value;
        this.left = this.right = this.parent = null;
    }
}

public class SplayTree {
    private SplayNode root;

    public SplayTree() {
        root = null;
    }

    public void insert(int value) {
        System.out.println("\n═══════════════════════════════════");
        System.out.println("INSERT: " + value);
        System.out.println("═══════════════════════════════════");

        if (root == null) {
            root = new SplayNode(value);
            printTreeGraphical("Efter indsættelse af " + value);
            return;
        }

        SplayNode node = insertRec(root, value);
        splay(node);
        printTreeGraphical("Efter indsættelse af " + value);
    }

    private SplayNode insertRec(SplayNode current, int value) {
        if (current == null) {
            return new SplayNode(value);
        }

        if (value < current.value) {
            if (current.left == null) {
                current.left = new SplayNode(value);
                current.left.parent = current;
                return current.left;
            } else {
                return insertRec(current.left, value);
            }
        } else if (value > current.value) {
            if (current.right == null) {
                current.right = new SplayNode(value);
                current.right.parent = current;
                return current.right;
            } else {
                return insertRec(current.right, value);
            }
        }
        // Duplicate - return existing node
        return current;
    }

    public void splay(int value) {
        System.out.println("\n═══════════════════════════════════");
        System.out.println("=== SPLAY OPERATION: " + value + " ===");
        System.out.println("═══════════════════════════════════\n");

        SplayNode node = findNode(root, value);
        if (node == null) {
            System.out.println("Værdi " + value + " findes ikke i træet.");
            return;
        }

        System.out.println("Finder node " + value + "...");
        printTreeGraphical("Før splay operation");

        int step = 1;
        while (node.parent != null) {
            SplayNode parent = node.parent;
            SplayNode grandparent = parent.parent;

            System.out.println("\n─── Trin " + step + " ───");

            if (grandparent == null) {
                // Zig
                if (node == parent.left) {
                    System.out.println("Zig: Højre-rotation på " + parent.value);
                    rotateRight(parent);
                } else {
                    System.out.println("Zig: Venstre-rotation på " + parent.value);
                    rotateLeft(parent);
                }
            } else if (node == parent.left && parent == grandparent.left) {
                // Zig-zig
                System.out.println("Zig-Zig: Højre-rotation på " + grandparent.value + " og " + parent.value);
                rotateRight(grandparent);
                rotateRight(parent);
            } else if (node == parent.right && parent == grandparent.right) {
                // Zig-zig
                System.out.println("Zig-Zig: Venstre-rotation på " + grandparent.value + " og " + parent.value);
                rotateLeft(grandparent);
                rotateLeft(parent);
            } else if (node == parent.right && parent == grandparent.left) {
                // Zig-zag
                System.out.println("Zig-Zag: Venstre-rotation på " + parent.value + ", højre-rotation på " + grandparent.value);
                rotateLeft(parent);
                rotateRight(grandparent);
            } else {
                // Zig-zag
                System.out.println("Zig-Zag: Højre-rotation på " + parent.value + ", venstre-rotation på " + grandparent.value);
                rotateRight(parent);
                rotateLeft(grandparent);
            }

            printTreeGraphical("Efter rotation " + step);
            step++;
        }

        System.out.println("\n✅ SPLAY FÆRDIG - " + value + " er nu roden");
    }

    // Private hjælpemetode til at splay en node (bruges i insert)
    private void splay(SplayNode node) {
        while (node.parent != null) {
            SplayNode parent = node.parent;
            SplayNode grandparent = parent.parent;

            if (grandparent == null) {
                // Zig
                if (node == parent.left) {
                    rotateRight(parent);
                } else {
                    rotateLeft(parent);
                }
            } else if (node == parent.left && parent == grandparent.left) {
                // Zig-zig
                rotateRight(grandparent);
                rotateRight(parent);
            } else if (node == parent.right && parent == grandparent.right) {
                // Zig-zig
                rotateLeft(grandparent);
                rotateLeft(parent);
            } else if (node == parent.right && parent == grandparent.left) {
                // Zig-zag
                rotateLeft(parent);
                rotateRight(grandparent);
            } else {
                // Zig-zag
                rotateRight(parent);
                rotateLeft(grandparent);
            }
        }
    }

    private void rotateRight(SplayNode x) {
        SplayNode y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.right) x.parent.right = y;
        else x.parent.left = y;
        y.right = x;
        x.parent = y;
    }

    private void rotateLeft(SplayNode x) {
        SplayNode y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private SplayNode findNode(SplayNode current, int value) {
        if (current == null || current.value == value) return current;
        if (value < current.value) return findNode(current.left, value);
        return findNode(current.right, value);
    }

    // ====== VISUALISERING ======
    public void printTreeGraphical(String title) {
        if (root == null) {
            System.out.println("╔═══════════════════════════════════╗");
            System.out.println("║        " + title + "         ║");
            System.out.println("╚═══════════════════════════════════╝");
            System.out.println("(empty tree)\n");
            return;
        }

        System.out.println("\n╔═══════════════════════════════════╗");
        System.out.println("║        " + title + "         ║");
        System.out.println("╚═══════════════════════════════════╝");

        List<List<String>> lines = new ArrayList<>();
        List<SplayNode> level = new ArrayList<>();
        List<SplayNode> next = new ArrayList<>();

        level.add(root);
        int nn = 1;
        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<>();
            nn = 0;
            for (SplayNode n : level) {
                if (n == null) {
                    line.add(null);
                    next.add(null);
                    next.add(null);
                } else {
                    String aa = n.value + (n == root ? " (ROOT)" : "");
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

            List<SplayNode> tmp = level;
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

        // Vis parent information
        System.out.println("\n═══════════════════════════════════");
        System.out.println("Node Information:");
        printNodeInfo(root, "");
        System.out.println("═══════════════════════════════════\n");
    }

    private void printNodeInfo(SplayNode node, String indent) {
        if (node == null) return;

        String parentInfo = (node.parent == null) ? "Ingen (ROOT)" : String.valueOf(node.parent.value);
        String leftInfo = (node.left == null) ? "null" : String.valueOf(node.left.value);
        String rightInfo = (node.right == null) ? "null" : String.valueOf(node.right.value);

        System.out.println(indent + "Node " + node.value + ":");
        System.out.println(indent + "  Parent = " + parentInfo);
        System.out.println(indent + "  Left = " + leftInfo);
        System.out.println(indent + "  Right = " + rightInfo);

        if (node.left != null || node.right != null) {
            printNodeInfo(node.left, indent + "  ");
            printNodeInfo(node.right, indent + "  ");
        }
    }

    // ====== TRAVERSAL METODER ======
    public void printInOrder() {
        System.out.print("In-Order Traversal: ");
        inOrderRec(root);
        System.out.println("\n");
    }

    private void inOrderRec(SplayNode node) {
        if (node != null) {
            inOrderRec(node.left);
            System.out.print(node.value + " ");
            inOrderRec(node.right);
        }
    }

    public void printPreOrder() {
        System.out.print("Pre-Order Traversal: ");
        preOrderRec(root);
        System.out.println("\n");
    }

    private void preOrderRec(SplayNode node) {
        if (node != null) {
            System.out.print(node.value + " ");
            preOrderRec(node.left);
            preOrderRec(node.right);
        }
    }

    // ====== TREE VALIDATION ======
    public void validateTree() {
        System.out.println("\n═══════════════════════════════════");
        System.out.println("TREE VALIDATION");
        System.out.println("═══════════════════════════════════");

        if (root == null) {
            System.out.println("✅ Træet er tomt (korrekt)");
            return;
        }

        // Check BST property
        boolean isBST = isValidBST(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println("✅ BST property: " + (isBST ? "OK" : "FAILED"));

        // Check parent pointers
        boolean parentOK = checkParentPointers(root, null);
        System.out.println("✅ Parent pointers: " + (parentOK ? "OK" : "FAILED"));

        // Check no cycles
        boolean noCycles = !hasCycle(root);
        System.out.println("✅ No cycles: " + (noCycles ? "OK" : "FAILED"));

        System.out.println("Tree height: " + getHeight(root));
        System.out.println("Node count: " + countNodes(root));
        System.out.println("═══════════════════════════════════\n");
    }

    private boolean isValidBST(SplayNode node, int min, int max) {
        if (node == null) return true;
        if (node.value <= min || node.value >= max) return false;
        return isValidBST(node.left, min, node.value) &&
                isValidBST(node.right, node.value, max);
    }

    private boolean checkParentPointers(SplayNode node, SplayNode parent) {
        if (node == null) return true;
        if (node.parent != parent) return false;
        return checkParentPointers(node.left, node) &&
                checkParentPointers(node.right, node);
    }

    private boolean hasCycle(SplayNode root) {
        Set<SplayNode> visited = new HashSet<>();
        return hasCycleUtil(root, visited);
    }

    private boolean hasCycleUtil(SplayNode node, Set<SplayNode> visited) {
        if (node == null) return false;
        if (visited.contains(node)) return true;
        visited.add(node);
        return hasCycleUtil(node.left, visited) || hasCycleUtil(node.right, visited);
    }

    private int getHeight(SplayNode node) {
        if (node == null) return 0;
        return 1 + Math.max(getHeight(node.left), getHeight(node.right));
    }

    private int countNodes(SplayNode node) {
        if (node == null) return 0;
        return 1 + countNodes(node.left) + countNodes(node.right);
    }

    // ====== MAIN ======
    public static void main(String[] args) {
        SplayTree tree = new SplayTree();

        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║       SPLAY TREE DEMONSTRATION           ║");
        System.out.println("╚══════════════════════════════════════════╝");

        int[] values = {10, 20, 30, 5, 4, 7, 25};
        System.out.println("\nIndsætter værdier: " + Arrays.toString(values));
        System.out.println("Forventet in-order efter alle indsættelser: 4 5 7 10 20 25 30");

        for (int v : values) {
            tree.insert(v);
            tree.validateTree(); // Valider efter hver indsættelse
        }

        System.out.println("\n══════════════════════════════════════════");
        System.out.println("FÆRDIG MED INDSÆTTELSER");
        System.out.println("══════════════════════════════════════════\n");

        tree.printInOrder();
        tree.printPreOrder();

        // Test splay operation på 4
        tree.splay(4);
        tree.validateTree();

        // Test splay på 30
        tree.splay(30);
        tree.validateTree();

        // Test splay på ikke-eksisterende værdi
        System.out.println("\n═══════════════════════════════════");
        System.out.println("TEST: SPLAY NON-EXISTENT VALUE");
        System.out.println("═══════════════════════════════════");
        tree.splay(100);

        // Final tree state
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("FINAL TREE STATE");
        System.out.println("══════════════════════════════════════════");
        tree.printTreeGraphical("FINAL SPLAY TREE");
        tree.validateTree();
    }
}