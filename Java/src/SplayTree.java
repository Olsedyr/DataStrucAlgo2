import java.util.LinkedList;
import java.util.Queue;

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
        if (root == null) {
            root = new SplayNode(value);
            return;
        }
        SplayNode node = insertRec(root, value);
        splay(node);
    }

    private SplayNode insertRec(SplayNode root, int value) {
        if (root == null) {
            return new SplayNode(value);
        }
        if (value < root.value) {
            SplayNode leftChild = insertRec(root.left, value);
            leftChild.parent = root;
            root.left = leftChild;
        } else if (value > root.value) {
            SplayNode rightChild = insertRec(root.right, value);
            rightChild.parent = root;
            root.right = rightChild;
        }
        return root;
    }

    public void splay(int value) {
        SplayNode node = findNode(root, value);
        if (node == null) {
            System.out.println("Værdi " + value + " findes ikke i træet.");
            return;
        }
        System.out.println("\n=== Splay på værdi: " + value + " ===");
        splay(node);
        System.out.println("Færdig – " + value + " er nu roden.\n");
    }

    private void splay(SplayNode node) {
        while (node.parent != null) {
            SplayNode parent = node.parent;
            SplayNode grandparent = parent.parent;

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

            // Vis træet efter hver rotation (valgfrit – fjern hvis for meget output)
            printLevelOrder();
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

    private SplayNode findNode(SplayNode root, int value) {
        if (root == null || root.value == value) return root;
        if (value < root.value) return findNode(root.left, value);
        return findNode(root.right, value);
    }

    // RETTET printLevelOrder – stopper uendelig løkke!
    public void printLevelOrder() {
        if (root == null) {
            System.out.println("Træet er tomt.\n");
            return;
        }

        Queue<SplayNode> queue = new LinkedList<>();
        queue.add(root);
        System.out.println("Træ (level-order):");

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                SplayNode node = queue.poll();
                if (node != null) {
                    System.out.print(node.value + " ");
                    // Kun tilføj børn hvis noden eksisterer
                    queue.add(node.left);
                    queue.add(node.right);
                } else {
                    System.out.print("null ");
                    // Tilføj IKKE børn af null – det var problemet!
                }
            }
            System.out.println(); // Ny linje efter hvert niveau
        }
        System.out.println(); // Ekstra linje for overskuelighed
    }

    public static void main(String[] args) {
        SplayTree tree = new SplayTree();

        int[] values = {10, 20, 30, 5, 4, 7, 25};
        System.out.println("Indsætter: " + java.util.Arrays.toString(values));
        for (int v : values) {
            tree.insert(v);
        }

        System.out.println("Træ efter indsættelser:");
        tree.printLevelOrder();

        tree.splay(4);
        tree.printLevelOrder();

        tree.splay(30);
        tree.printLevelOrder();
    }
}