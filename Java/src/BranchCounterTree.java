import java.util.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Arrays;

public class BranchCounterTree {

    static class BinaryNode {
        int key;
        BinaryNode left;
        BinaryNode right;

        BinaryNode(int key) {
            this.key = key;
        }
    }

    // Indsætter noder niveauvis (fra en liste, null = tom plads)
    public static BinaryNode buildTreeFromList(List<Integer> values) {
        if (values == null || values.isEmpty() || values.get(0) == null) {
            return null;
        }

        BinaryNode root = new BinaryNode(values.get(0));
        Queue<BinaryNode> queue = new LinkedList<BinaryNode>();  // <-- her

        queue.add(root);

        int i = 1;
        while (i < values.size()) {
            BinaryNode current = queue.poll();

            if (i < values.size() && values.get(i) != null) {
                current.left = new BinaryNode(values.get(i));
                queue.add(current.left);
            }
            i++;

            if (i < values.size() && values.get(i) != null) {
                current.right = new BinaryNode(values.get(i));
                queue.add(current.right);
            }
            i++;
        }

        return root;
    }

    public static void printTree(BinaryNode root) {
        if (root == null) {
            System.out.println("Træet er tomt.");
            return;
        }

        Queue<BinaryNode> queue = new LinkedList<BinaryNode>();  // <-- her
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                BinaryNode node = queue.poll();
                if (node == null) {
                    System.out.print("_ ");
                    queue.add(null);
                    queue.add(null);
                } else {
                    System.out.print(node.key + " ");
                    queue.add(node.left);
                    queue.add(node.right);
                }
            }
            System.out.println();

            // Check om næste niveau kun indeholder nulls, så kan vi stoppe
            boolean allNull = true;
            for (BinaryNode n : queue) {
                if (n != null) {
                    allNull = false;
                    break;
                }
            }
            if (allNull) break;
        }
    }

    // Hjælpemetode: Returnerer nodens eneste barn, ellers null
    public static BinaryNode getOnlyChild(BinaryNode node) {
        if (node == null) return null;
        if (node.left != null && node.right == null) return node.left;
        if (node.left == null && node.right != null) return node.right;
        return null;
    }

    // Tjek om node har søskende
    public static boolean hasSibling(BinaryNode parent, BinaryNode node) {
        if (parent == null) return false;
        if (parent.left != null && parent.left != node) return true;
        if (parent.right != null && parent.right != node) return true;
        return false;
    }

    // Tjek om node er blad
    public static boolean isLeaf(BinaryNode node) {
        return node != null && node.left == null && node.right == null;
    }

    // Tæl antal grene i træet
    public static int countBranches(BinaryNode root) {
        if (root == null) return 0;
        return countBranchesHelper(root, null);
    }

    private static int countBranchesHelper(BinaryNode node, BinaryNode parent) {
        if (node == null) return 0;

        int total = 0;

        if (parent != null) {
            BinaryNode child = getOnlyChild(node);
            if (child != null) {
                if (!hasSibling(parent, node)) {
                    if (!hasSibling(node, child)) {
                        BinaryNode grandChild = getOnlyChild(child);
                        if (grandChild != null && isLeaf(grandChild)) {
                            total++;
                        }
                    }
                }
            }
        }

        total += countBranchesHelper(node.left, node);
        total += countBranchesHelper(node.right, node);

        return total;
    }



    public static void main(String[] args) {
        // Indsæt dit træ her i niveauorden
        // Eksempel: rod=10, venstre=3, højre=48, venstre til venstre=1, højre til højre=66, osv.
        // Brug null hvis node mangler (så træets form bevares)
        List<Integer> nodeValues = Arrays.asList(10, 3, 48, 1, null, null, 66);

        System.out.println("Originalt træ (niveauorden):");
        BinaryNode root = buildTreeFromList(nodeValues);
        printTree(root);

        int antalGrener = countBranches(root);
        System.out.println("\nAntal grene i træet ifølge definitionen: " + antalGrener);
    }
}
