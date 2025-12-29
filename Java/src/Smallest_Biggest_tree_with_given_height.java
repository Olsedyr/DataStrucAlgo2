import java.util.LinkedList;
import java.util.Queue;

class Node2 {
    int value;
    Node left, right;

    public Node2(int value) {
        this.value = value;
        left = right = null;
    }
}

public class Smallest_Biggest_tree_with_given_height {

    // Level-order traversal (BFS) - printer træet niveau for niveau
    public static void printLevelOrder(Node root) {
        if (root == null) {
            System.out.println("Træet er tomt");
            return;
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                Node node = queue.poll();
                if (node != null) {
                    System.out.print(node.value + " ");
                    queue.add(node.left);
                    queue.add(node.right);
                } else {
                    System.out.print("null ");
                }
            }
            System.out.println(); // Ny linje efter hvert niveau
        }
        System.out.println();
    }

    // Beregner højden af et træ
    public static int height(Node root) {
        if (root == null) return 0;
        return 1 + Math.max(height(root.left), height(root.right));
    }

    // Tæller antal noder
    public static int countNodes(Node root) {
        if (root == null) return 0;
        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    // Det STØRSTE balancerede BST med højde h: Perfekt træ
    // Fylder med værdier 1 til (2^h - 1)
    public static Node createPerfectTree(int h, int start, int end) {
        if (start > end) return null;
        int mid = start + (end - start) / 2;
        Node node = new Node(mid);
        node.left = createPerfectTree(h, start, mid - 1);
        node.right = createPerfectTree(h, mid + 1, end);
        return node;
    }

    public static Node createLargestBalancedBST(int h) {
        if (h <= 0) return null;
        return createPerfectTree(h, 1, (1 << h) - 1); // 2^h - 1
    }

    // Det MINDSTE balancerede BST med præcis højde h
    // Vi bygger et træ, hvor venstre undertræ har højde h-1, og højre har højde h-2 eller lavere
    // Dette giver det minimale antal noder for højde h
    public static Node createMinimalHeightTree(int h, int[] counter) {
        if (h == 0) return null;

        Node node = new Node(counter[0]++);

        // Venstre undertræ skal have højde h-1 for at sikre total højde h
        node.left = createMinimalHeightTree(h - 1, counter);

        // Højre undertræ kan have højde højst h-2 (for at holde balancen minimal)
        // For minimalt antal noder: vi sætter højre til højde h-2 hvis h > 1
        if (h > 1) {
            node.right = createMinimalHeightTree(h - 2, counter);
        } else {
            node.right = null;
        }

        return node;
    }

    public static Node createSmallestBalancedBST(int h) {
        if (h <= 0) return null;
        int[] counter = {1}; // Starter værdier fra 1
        return createMinimalHeightTree(h, counter);
    }

    public static void main(String[] args) {
        int h = 4; // Du kan ændre denne værdi til den ønskede højde (>0)

        System.out.println("=== Balancerede binære søgetræer med højde " + h + " ===\n");

        // Det mindste træ med højde h
        Node smallest = createSmallestBalancedBST(h);
        System.out.println("1. MINDSTE balancerede BST med højde " + h + ":");
        System.out.println("Antal noder: " + countNodes(smallest));
        System.out.println("Faktisk højde: " + height(smallest));
        System.out.println("Level-order udskrift:");
        printLevelOrder(smallest);

        // Det største træ med højde h (perfekt træ)
        Node largest = createLargestBalancedBST(h);
        System.out.println("2. STØRSTE balancerede BST med højde " + h + " (perfekt træ):");
        System.out.println("Antal noder: " + countNodes(largest));
        System.out.println("Faktisk højde: " + height(largest));
        System.out.println("Level-order udskrift:");
        printLevelOrder(largest);
    }
}