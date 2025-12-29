import java.util.*;

class Edge2 {
    String from, to;
    int weight;

    Edge2(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "(" + from + " → " + to + ", " + weight + ")";
    }
}

public class bellman_ford_negativeEdge {

    private final List<Edge> edges;
    private final List<String> nodes;

    public bellman_ford_negativeEdge(List<Edge> edges) {
        this.edges = edges;
        Set<String> nodeSet = new HashSet<>();
        for (Edge e : edges) {
            nodeSet.add(e.from);
            nodeSet.add(e.to);
        }
        this.nodes = new ArrayList<>(nodeSet);
        Collections.sort(this.nodes);
    }

    private record Result(
            Map<String, Integer> distances,
            Map<String, String> predecessors,
            boolean negativeCycle,
            List<Edge> cycleEdges
    ) {}

    public Result bellmanFord(String startNode, boolean directed) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> predecessors = new HashMap<>();

        for (String node : nodes) {
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
        }
        distances.put(startNode, 0);

        List<Edge> edgeList = new ArrayList<>(edges);
        if (!directed) {
            for (Edge e : edges) {
                edgeList.add(new Edge(e.to, e.from, e.weight));
            }
        }

        // Relaxering n-1 gange
        for (int i = 0; i < nodes.size() - 1; i++) {
            boolean updated = false;
            for (Edge e : edgeList) {
                if (distances.get(e.from) != Integer.MAX_VALUE &&
                        distances.get(e.from) + e.weight < distances.get(e.to)) {
                    distances.put(e.to, distances.get(e.from) + e.weight);
                    predecessors.put(e.to, e.from);
                    updated = true;
                }
            }
            if (!updated) break;
        }

        // Negative cycle detection
        List<Edge> cycleEdges = new ArrayList<>();
        for (Edge e : edgeList) {
            if (distances.get(e.from) != Integer.MAX_VALUE &&
                    distances.get(e.from) + e.weight < distances.get(e.to)) {
                cycleEdges.add(e);
            }
        }

        return new Result(distances, predecessors, !cycleEdges.isEmpty(), cycleEdges);
    }

    public void runAnalysis() {
        if (nodes.isEmpty()) {
            System.out.println("Grafen er tom!");
            return;
        }

        String startNode = nodes.get(0);

        System.out.println("═".repeat(70));
        System.out.println("              BELLMAN-FORD ALGORITME");
        System.out.println("═".repeat(70));
        System.out.println("Kanter: " + edges);  // Nu pænt!
        System.out.println("Noder:  " + nodes);
        System.out.println("Startnode: " + startNode);
        System.out.println();

        String[] types = {"RETTET GRAF (Directed)", "URETTET GRAF (Undirected)"};
        boolean[] dirOptions = {true, false};

        for (int i = 0; i < 2; i++) {
            System.out.println(types[i]);
            System.out.println("─".repeat(50));

            Result result = bellmanFord(startNode, dirOptions[i]);

            System.out.printf("%-8s %-12s %-15s%n", "Node", "Distance", "Forgænger");
            System.out.println("─".repeat(40));
            for (String node : nodes) {
                String dist = result.distances.get(node) == Integer.MAX_VALUE
                        ? "∞" : result.distances.get(node).toString();
                String pred = result.predecessors.get(node) == null ? "-" : result.predecessors.get(node);
                System.out.printf("%-8s %-12s %-15s%n", node, dist, pred);
            }

            if (result.negativeCycle) {
                System.out.println("\n⚠️  NEGATIV CYKLUS DETEKTERET!");
                System.out.println("Kanter der stadig kan relaxeres:");
                for (Edge e : result.cycleEdges) {
                    int fromDist = result.distances.get(e.from);
                    int newDist = fromDist + e.weight;
                    System.out.printf("   %s → %s (vægt %d): %d + %d = %d < %d%n",
                            e.from, e.to, e.weight, fromDist, e.weight, newDist, result.distances.get(e.to));
                }
                System.out.println("→ Distancerne er ugyldige pga. negativ cyklus.");
            } else {
                System.out.println("\n✓ Ingen negativ cyklus fundet.");
                System.out.println("→ Korteste stier er korrekt beregnet.");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        List<Edge> myGraph = Arrays.asList(
                new Edge("A", "B", 4),
                new Edge("B", "C", -2),
                new Edge("A", "C", 3),
                new Edge("C", "D", 1),
                new Edge("D", "B", -1)
        );

        new bellman_ford_negativeEdge(myGraph).runAnalysis();
    }
}