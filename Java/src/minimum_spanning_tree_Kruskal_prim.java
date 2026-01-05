import java.util.*;

public class minimum_spanning_tree_Kruskal_prim {

    // Edge klasse til at repræsentere en kant
    static class Edge implements Comparable<Edge> {
        String u, v;
        int weight;

        Edge(String u, String v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        // Sorter efter vægt (til Kruskal)
        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }

        // For at undgå dubletter i grafen
        Edge normalized() {
            return u.compareTo(v) < 0 ? this : new Edge(v, u, weight);
        }

        @Override
        public String toString() {
            return u + "-" + v;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Edge edge = (Edge) obj;
            Edge norm1 = this.normalized();
            Edge norm2 = edge.normalized();
            return norm1.u.equals(norm2.u) && norm1.v.equals(norm2.v);
        }

        @Override
        public int hashCode() {
            Edge norm = normalized();
            return Objects.hash(norm.u, norm.v);
        }
    }

    // Graf klasse
    static class Graph {
        Map<String, List<Edge>> adjacencyList;
        Set<Edge> edges;

        Graph() {
            adjacencyList = new HashMap<>();
            edges = new HashSet<>();
        }

        void addEdge(String u, String v, int weight) {
            Edge edge = new Edge(u, v, weight);
            edges.add(edge);

            adjacencyList.computeIfAbsent(u, k -> new ArrayList<>()).add(edge);
            adjacencyList.computeIfAbsent(v, k -> new ArrayList<>()).add(edge);
        }

        int getNodeCount() {
            return adjacencyList.size();
        }

        List<String> getNodes() {
            return new ArrayList<>(adjacencyList.keySet());
        }
    }

    // Resultat klasse for MST
    static class MSTResult {
        List<Edge> edges;
        List<Edge> steps; // Rækkefølge af tilføjelser
        Map<String, Integer> nodeOrder; // Node -> step nummer
        int totalWeight;

        MSTResult(List<Edge> edges, List<Edge> steps, Map<String, Integer> nodeOrder) {
            this.edges = edges;
            this.steps = steps;
            this.nodeOrder = nodeOrder;
            this.totalWeight = edges.stream().mapToInt(e -> e.weight).sum();
        }
    }

    // Prim's algoritme
    static MSTResult prim(Graph graph, String start) {
        List<Edge> mstEdges = new ArrayList<>();
        List<Edge> steps = new ArrayList<>();
        Map<String, Integer> nodeOrder = new HashMap<>();

        Set<String> visited = new HashSet<>();
        visited.add(start);
        nodeOrder.put(start, 1);

        // Priority queue til at holde kanter (min-heap baseret på vægt)
        PriorityQueue<Edge> pq = new PriorityQueue<>();

        // Tilføj alle kanter fra startnoden til priority queue
        for (Edge edge : graph.adjacencyList.getOrDefault(start, new ArrayList<>())) {
            pq.add(edge);
        }

        int currentStep = 2;

        while (!pq.isEmpty() && visited.size() < graph.getNodeCount()) {
            Edge edge = pq.poll();

            // Find den node der ikke er besøgt endnu
            String nextNode = null;
            if (!visited.contains(edge.u)) nextNode = edge.u;
            if (!visited.contains(edge.v)) nextNode = edge.v;

            if (nextNode != null) {
                visited.add(nextNode);
                mstEdges.add(edge);
                steps.add(edge);
                nodeOrder.put(nextNode, currentStep);
                currentStep++;

                // Tilføj alle kanter fra den nye node
                for (Edge nextEdge : graph.adjacencyList.get(nextNode)) {
                    if (!visited.contains(nextEdge.u) || !visited.contains(nextEdge.v)) {
                        pq.add(nextEdge);
                    }
                }
            }
        }

        return new MSTResult(mstEdges, steps, nodeOrder);
    }

    // Union-Find til Kruskal
    static class UnionFind {
        Map<String, String> parent;
        Map<String, Integer> rank;

        UnionFind(List<String> nodes) {
            parent = new HashMap<>();
            rank = new HashMap<>();
            for (String node : nodes) {
                parent.put(node, node);
                rank.put(node, 0);
            }
        }

        String find(String node) {
            if (!parent.get(node).equals(node)) {
                parent.put(node, find(parent.get(node))); // Path compression
            }
            return parent.get(node);
        }

        boolean union(String u, String v) {
            String rootU = find(u);
            String rootV = find(v);

            if (rootU.equals(rootV)) {
                return false; // De er allerede i samme komponent
            }

            // Union by rank
            if (rank.get(rootU) < rank.get(rootV)) {
                parent.put(rootU, rootV);
            } else if (rank.get(rootU) > rank.get(rootV)) {
                parent.put(rootV, rootU);
            } else {
                parent.put(rootV, rootU);
                rank.put(rootU, rank.get(rootU) + 1);
            }

            return true;
        }
    }

    // Kruskal's algoritme
    static MSTResult kruskal(Graph graph) {
        List<Edge> mstEdges = new ArrayList<>();
        List<Edge> steps = new ArrayList<>();

        // Sortér alle kanter efter vægt
        List<Edge> sortedEdges = new ArrayList<>(graph.edges);
        Collections.sort(sortedEdges);

        UnionFind uf = new UnionFind(graph.getNodes());

        for (Edge edge : sortedEdges) {
            if (uf.union(edge.u, edge.v)) {
                mstEdges.add(edge);
                steps.add(edge);
            }
        }

        // Kruskal har ikke nodeOrder som Prim
        return new MSTResult(mstEdges, steps, new HashMap<>());
    }

    // Hjælpefunktion til at udskrive resultater
    static void printMSTResult(String algorithmName, MSTResult result) {
        System.out.println("\n" + algorithmName + ":");
        System.out.println("Tilføjelsesrækkefølge:");

        for (int i = 0; i < result.steps.size(); i++) {
            Edge edge = result.steps.get(i);
            System.out.printf("  Step %d: %s (vægt %d)\n", i + 1, edge, edge.weight);
        }

        System.out.println("\nMST kanter:");
        for (Edge edge : result.edges) {
            System.out.printf("  %s (vægt %d)\n", edge, edge.weight);
        }

        System.out.printf("=> Total vægt: %d\n", result.totalWeight);

        if (!result.nodeOrder.isEmpty()) {
            System.out.println("\nNode tilføjelsesrækkefølge:");
            List<Map.Entry<String, Integer>> sortedOrder = new ArrayList<>(result.nodeOrder.entrySet());
            sortedOrder.sort(Map.Entry.comparingByValue());

            for (Map.Entry<String, Integer> entry : sortedOrder) {
                System.out.printf("  Node %s: Step %d\n", entry.getKey(), entry.getValue());
            }
        }
    }

    // Main metode
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("MINIMUM SPANNING TREE - PRIM VS KRUSKAL");
        System.out.println("=".repeat(60));

        // Opret grafen med de samme data som Python-versionen
        Graph graph = new Graph();

        // Tilføj kanter (samme som i Python)
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 2);
        graph.addEdge("C", "D", 3);
        graph.addEdge("D", "E", 4);
        graph.addEdge("E", "F", 5);
        graph.addEdge("F", "G", 6);
        graph.addEdge("G", "H", 7);

        System.out.println("\nGraf oprettet med " + graph.getNodeCount() + " noder og " +
                graph.edges.size() + " kanter");

        // Kør Prim's algoritme fra forskellige startnoder
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PRIM'S ALGORITME");
        System.out.println("=".repeat(60));

        MSTResult primA = prim(graph, "C");
        printMSTResult("Prim (startnode 'C')", primA);

        MSTResult primF = prim(graph, "F");
        printMSTResult("\nPrim (startnode 'F')", primF);

        // Kør Kruskal's algoritme
        System.out.println("\n" + "=".repeat(60));
        System.out.println("KRUSKAL'S ALGORITME");
        System.out.println("=".repeat(60));

        MSTResult kruskalResult = kruskal(graph);
        printMSTResult("Kruskal", kruskalResult);

        // Sammenlign resultater
        System.out.println("\n" + "=".repeat(60));
        System.out.println("SAMMENLIGNING");
        System.out.println("=".repeat(60));

        System.out.println("Totalvægte:");
        System.out.printf("  Prim (start A): %d\n", primA.totalWeight);
        System.out.printf("  Prim (start F): %d\n", primF.totalWeight);
        System.out.printf("  Kruskal:       %d\n", kruskalResult.totalWeight);

        if (primA.totalWeight == primF.totalWeight && primF.totalWeight == kruskalResult.totalWeight) {
            System.out.println("\n✅ Alle algoritmer giver samme totalvægt!");
        } else {
            System.out.println("\n⚠️  Forskelle i totalvægt - tjek implementeringen!");
        }

        // Tjek om MST'erne indeholder de samme kanter (uafhængigt af rækkefølge)
        Set<Edge> primAEdges = new HashSet<>(primA.edges);
        Set<Edge> primFEdges = new HashSet<>(primF.edges);
        Set<Edge> kruskalEdges = new HashSet<>(kruskalResult.edges);

        System.out.println("\nSammenligning af kanter:");
        System.out.println("  Prim(A) og Prim(F) har samme kanter: " +
                primAEdges.equals(primFEdges));
        System.out.println("  Prim(A) og Kruskal har samme kanter: " +
                primAEdges.equals(kruskalEdges));

        // Forklaring
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FORKLARING");
        System.out.println("=".repeat(60));
        System.out.println("""
            Prim's algoritme:
              • Vokser MST fra startnoden ud
              • Tilføjer altid den billigste kant der forbinder træet med en ny node
              • Rækkefølgen afhænger af startnoden
            
            Kruskal's algoritme:
              • Sorterer alle kanter efter vægt
              • Tilføjer kanter i stigende rækkefølge (hvis de ikke skaber cykler)
              • Rækkefølgen er altid den samme (stigende vægt)
            
            I denne graf har alle kanter unikke vægte, så både Prim og Kruskal
            finder den samme MST (samme kanter), men i forskellig rækkefølge.
            
            Hvis grafen havde kanter med samme vægt, kunne algoritmerne
            potentielt finde forskellige MST'er med samme totale vægt.
            """);
    }
}