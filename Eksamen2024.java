public class Eksamen2024 {
    // ADA Eksamen - 2. januar 2024
// Algoritmer og datastrukturer

    // Opgave 1: Big-O Tidskompleksitet
// Analyserer koden og estimerer dens Store O-tidskompleksitet.
    public static String myM(int N) {
        int x = 0, y = 0;
        for (int j = 0; j < N; j++) {
            for (int i = N; i > 0; i = i / 3) {
                for (int k = N; k > 0; k = k / 2) {
                    x++;
                }
            }
        }
        for (float v = 0; v < N; v += Math.sqrt(0.001)) {
            y++;
        }
        return x + " " + y;
    }
// Tidskompleksitet: O(N * log(N) * log(N)) for første loop og O(N^1.5) for det andet.

    // Opgave 2: Rekursiv metode til summering
    public static int sumDeleligMedTreOgOtte(int N) {
        if (N <= 0) return 0;
        if (N % 3 == 0 || N % 8 == 0) {
            return N + sumDeleligMedTreOgOtte(N - 1);
        }
        return sumDeleligMedTreOgOtte(N - 1);
    }

    // Opgave 3: Gren i et binært træ
    class BinaryNode {
        int value;
        BinaryNode left;
        BinaryNode right;

        public BinaryNode(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        public static BinaryNode getOnlyChild(BinaryNode node) {
            if (node.left != null && node.right == null) return node.left;
            if (node.right != null && node.left == null) return node.right;
            return null;
        }

        public static int countBranches(BinaryNode root) {
            if (root == null || getOnlyChild(root) == null) return 0;
            BinaryNode child = getOnlyChild(root);
            BinaryNode grandChild = getOnlyChild(child);
            if (grandChild != null && grandChild.left == null && grandChild.right == null) {
                return 1 + countBranches(root.left) + countBranches(root.right);
            }
            return countBranches(root.left) + countBranches(root.right);
        }
    }

// Supplerende opgave 1: Karakteristik af træet
// Træet er et binært søgetræ. Optimal højde: O(log(n)), hvor n er antallet af noder.

// Supplerende opgave 2: Transformation til prioritetskø
// 1. Traversér træet i in-order og indsaml alle værdier.
// 2. Indsæt værdierne i en heap.
// Tidskompleksitet: O(n log(n)).

// Opgave 4: Hopscotch Hash Tabel
// Implementer hopscotch-hashing med opdateringer til hoppe-tabellen.
// Problemet med hashning til indeks 50 kan føre til kollisioner og behov for rehashing.

    // Opgave 5: Dijkstra og MST
// Dijkstra-algoritme implementeret fra start vertex A:
    public static void dijkstra(int[][] graph, int startVertex) {
        int numVertices = graph.length;
        boolean[] known = new boolean[numVertices];
        int[] distances = new int[numVertices];
        int[] previous = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            distances[i] = Integer.MAX_VALUE;
            previous[i] = -1;
        }
        distances[startVertex] = 0;

        for (int i = 0; i < numVertices; i++) {
            int u = findMinVertex(distances, known);
            known[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!known[v] && graph[u][v] != 0 && distances[u] + graph[u][v] < distances[v]) {
                    distances[v] = distances[u] + graph[u][v];
                    previous[v] = u;
                }
            }
        }
        printSolution(distances, previous);
    }

    private static int findMinVertex(int[] distances, boolean[] known) {
        int minDistance = Integer.MAX_VALUE;
        int minVertex = -1;
        for (int i = 0; i < distances.length; i++) {
            if (!known[i] && distances[i] < minDistance) {
                minDistance = distances[i];
                minVertex = i;
            }
        }
        return minVertex;
    }

    private static void printSolution(int[] distances, int[] previous) {
        System.out.println("Vertex\tDistance\tPrevious");
        for (int i = 0; i < distances.length; i++) {
            System.out.println(i + "\t" + distances[i] + "\t" + previous[i]);
        }
    }

// MST ved brug af Kruskal eller Prim algoritme: Tilføj kanter i vægt-ordnet rækkefølge og undgå cyklusser.
// Tidskompleksitet: O(E log(V)), hvor E er antallet af kanter og V er antallet af noder.



}
