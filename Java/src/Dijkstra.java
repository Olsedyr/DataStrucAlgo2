import java.util.*;
import java.io.*;

class Edge {
    String from, to;
    int weight;

    Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
}

class Graph {
    Map<String, Map<String, Integer>> adjList = new HashMap<>();

    void addDirectedEdge(String from, String to, int weight) {
        adjList.computeIfAbsent(from, k -> new HashMap<>()).put(to, weight);
    }

    void addUndirectedEdge(String from, String to, int weight) {
        addDirectedEdge(from, to, weight);
        addDirectedEdge(to, from, weight);
    }

    Set<String> nodes() {
        Set<String> allNodes = new HashSet<>(adjList.keySet());
        for (Map<String, Integer> neighbors : adjList.values()) {
            allNodes.addAll(neighbors.keySet());
        }
        return allNodes;
    }

    Map<String, Integer> neighbors(String node) {
        return adjList.getOrDefault(node, new HashMap<>());
    }
}

class DijkstraResult {
    Map<String, Integer> dist;
    Map<String, String> prev;
    Set<String> visited;
    List<Map<String, Object>> steps; // Tilføjet for at gemme hvert trin

    DijkstraResult(Map<String, Integer> dist, Map<String, String> prev, Set<String> visited, List<Map<String, Object>> steps) {
        this.dist = dist;
        this.prev = prev;
        this.visited = visited;
        this.steps = steps;
    }
}

public class Dijkstra {

    private static final String START_NODE = "F";

    // DIN GRAF - INDKORPORERET KORREKT
    private static final List<Edge> EDGES = Arrays.asList(
            new Edge("F", "E", 4),
            new Edge("E", "D", 1),
            new Edge("D", "B", 9),
            new Edge("B", "C", 7),
            new Edge("B", "A", 1),
            new Edge("C", "H", 5),
            new Edge("H", "I", 7),
            new Edge("I", "J", 5),
            new Edge("I", "D", 2),
            new Edge("J", "G", 10),
            new Edge("A", "C", 1)
    );

    public static DijkstraResult dijkstra(Graph graph, String source, boolean writeSteps) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        List<Map<String, Object>> steps = new ArrayList<>();

        // Initialiser alle noder
        for (String node : graph.nodes()) {
            dist.put(node, Integer.MAX_VALUE);
            prev.put(node, null);
        }
        dist.put(source, 0);

        // Gem starttilstand (Step 0)
        if (writeSteps) {
            Map<String, Object> step0 = new HashMap<>();
            step0.put("step", 0);
            step0.put("current", "-");
            step0.put("dist", new HashMap<>(dist));
            step0.put("prev", new HashMap<>(prev));
            step0.put("visited", new HashSet<>(visited));
            steps.add(step0);
        }

        PriorityQueue<Object[]> queue = new PriorityQueue<>((a, b) -> Integer.compare((Integer)a[0], (Integer)b[0]));
        queue.offer(new Object[]{0, source});

        int stepCounter = 1;

        while (!queue.isEmpty()) {
            Object[] entry = queue.poll();
            int currentDist = (Integer) entry[0];
            String u = (String) entry[1];

            if (visited.contains(u)) continue;
            visited.add(u);

            // Gem tilstand før vi behandler naboer (dette er trinnet hvor u bliver "known")
            if (writeSteps) {
                Map<String, Object> step = new HashMap<>();
                step.put("step", stepCounter++);
                step.put("current", u);
                step.put("dist", new HashMap<>(dist));
                step.put("prev", new HashMap<>(prev));
                step.put("visited", new HashSet<>(visited));
                steps.add(step);
            }

            for (Map.Entry<String, Integer> neighbor : graph.neighbors(u).entrySet()) {
                String v = neighbor.getKey();
                int weight = neighbor.getValue();

                int newDist = dist.get(u) + weight;
                if (newDist < dist.get(v)) {
                    dist.put(v, newDist);
                    prev.put(v, u);
                    queue.offer(new Object[]{newDist, v});
                }
            }
        }

        return new DijkstraResult(dist, prev, visited, steps);
    }

    public static void writeDijkstraStepsToFile(DijkstraResult result, String title, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== " + title + " ===");
            writer.println("Dijkstra algoritme - Trin for trin");
            writer.println("Startnode: " + START_NODE);
            writer.println();

            List<String> allNodes = new ArrayList<>(result.dist.keySet());
            Collections.sort(allNodes);

            for (Map<String, Object> step : result.steps) {
                int stepNum = (int) step.get("step");
                String current = (String) step.get("current");
                Map<String, Integer> dist = (Map<String, Integer>) step.get("dist");
                Map<String, String> prev = (Map<String, String>) step.get("prev");
                Set<String> visited = (Set<String>) step.get("visited");

                writer.println("--- Trin " + stepNum + " ---");
                if (stepNum == 0) {
                    writer.println("Startkonfiguration:");
                } else {
                    writer.println("Markerer node " + current + " som 'Known'");
                }

                writer.println("+------+-------+-------+-------+");
                writer.println("| Node | Known |   dv  |   pv  |");
                writer.println("+------+-------+-------+-------+");

                for (String node : allNodes) {
                    String known = visited.contains(node) ? "Ja" : "Nej";
                    String dv;
                    if (dist.get(node) == Integer.MAX_VALUE) {
                        dv = "∞";
                    } else {
                        dv = String.format("%3d", dist.get(node));
                    }
                    String pv = prev.get(node) == null ? " -" : String.format("%3s", prev.get(node));

                    writer.printf("|  %-3s |  %-4s |  %-4s |  %-4s |\n", node, known, dv, pv);
                }
                writer.println("+------+-------+-------+-------+");
                writer.println();
            }

            writer.println("=== SLUTRESULTAT ===");
            writer.println("+------+-------+-------+-------+");
            writer.println("| Node | Known |   dv  |   pv  |");
            writer.println("+------+-------+-------+-------+");

            for (String node : allNodes) {
                String known = result.visited.contains(node) ? "Ja" : "Nej";
                String dv;
                if (result.dist.get(node) == Integer.MAX_VALUE) {
                    dv = "∞";
                } else {
                    dv = String.format("%3d", result.dist.get(node));
                }
                String pv = result.prev.get(node) == null ? " -" : String.format("%3s", result.prev.get(node));

                writer.printf("|  %-3s |  %-4s |  %-4s |  %-4s |\n", node, known, dv, pv);
            }
            writer.println("+------+-------+-------+-------+");

            writer.println("\nKorteste veje fra " + START_NODE + ":");
            for (String node : allNodes) {
                if (node.equals(START_NODE)) continue;
                if (result.dist.get(node) < Integer.MAX_VALUE) {
                    List<String> path = new ArrayList<>();
                    String current = node;
                    while (current != null) {
                        path.add(current);
                        current = result.prev.get(current);
                    }
                    Collections.reverse(path);
                    writer.printf("%s -> %s: %s (vægt: %d)\n",
                            START_NODE, node, String.join(" -> ", path), result.dist.get(node));
                }
            }

            System.out.println("Dijkstra trin skrevet til fil: " + filename);

        } catch (IOException e) {
            System.err.println("Fejl ved skrivning til fil: " + e.getMessage());
        }
    }

    public static List<Edge> kruskalMST(Graph undirected) {
        List<Edge> allEdges = new ArrayList<>();
        for (String u : undirected.nodes()) {
            for (Map.Entry<String, Integer> e : undirected.neighbors(u).entrySet()) {
                String v = e.getKey();
                if (u.compareTo(v) < 0) {
                    allEdges.add(new Edge(u, v, e.getValue()));
                }
            }
        }

        allEdges.sort(Comparator.comparingInt(e -> e.weight));

        Map<String, String> parent = new HashMap<>();
        for (String node : undirected.nodes()) parent.put(node, node);

        List<Edge> mst = new ArrayList<>();
        int totalWeight = 0;

        for (Edge edge : allEdges) {
            String rootU = find(parent, edge.from);
            String rootV = find(parent, edge.to);
            if (!rootU.equals(rootV)) {
                mst.add(edge);
                totalWeight += edge.weight;
                union(parent, rootU, rootV);
            }
        }

        System.out.println("\nTotal MST weight: " + totalWeight);
        return mst;
    }

    private static String find(Map<String, String> parent, String node) {
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent, parent.get(node)));
        }
        return parent.get(node);
    }

    private static void union(Map<String, String> parent, String x, String y) {
        parent.put(x, y);
    }

    public static void printDijkstraTable(DijkstraResult result, String title) {
        System.out.println("\n=== " + title + " ===");
        System.out.println("+------+-------+----+----+");
        System.out.println("| Node | Known | dv | pv |");
        System.out.println("+------+-------+----+----+");

        List<String> nodes = new ArrayList<>(result.dist.keySet());
        Collections.sort(nodes);

        for (String node : nodes) {
            String known = result.visited.contains(node) ? "Ja" : "Nej";
            String dv = result.dist.get(node) == Integer.MAX_VALUE ? "∞" : String.valueOf(result.dist.get(node));
            String pv = result.prev.get(node) == null ? "-" : result.prev.get(node);
            System.out.printf("|  %-3s |  %-4s | %-2s | %-2s |\n", node, known, dv, pv);
        }
        System.out.println("+------+-------+----+----+");
    }

    public static void printMST(List<Edge> mst) {
        System.out.println("\n=== MST (URETTET GRAF) ===\n");
        System.out.println("MST edges i den rækkefølge de tilføjes:");
        for (Edge e : mst) {
            System.out.printf("%s - %s  (weight %d)\n", e.from, e.to, e.weight);
        }
    }

    public static void main(String[] args) {
        Graph directed = new Graph();
        Graph undirected = new Graph();

        for (Edge e : EDGES) {
            directed.addDirectedEdge(e.from, e.to, e.weight);
            undirected.addUndirectedEdge(e.from, e.to, e.weight);
        }

        // Kør Dijkstra på rettet graf og skriv trin til fil
        DijkstraResult dirResult = dijkstra(directed, START_NODE, true);
        writeDijkstraStepsToFile(dirResult, "DIJKSTRA PÅ RETTET GRAF (Med pile)", "dijkstra.txt");
        printDijkstraTable(dirResult, "DIJKSTRA PÅ RETTET GRAF (Med pile)");

        // Kør Dijkstra på urettet graf (uden at gemme trin)
        DijkstraResult undirResult = dijkstra(undirected, START_NODE, false);
        printDijkstraTable(undirResult, "DIJKSTRA PÅ URETTET GRAF (Uden pile)");

        List<Edge> mst = kruskalMST(undirected);
        printMST(mst);

        System.out.println("""
------------------------------------------------------------
📌 FORKLARING
Directed graf:
   - Bruges til navigation, trafikrouting, algoritmer med retning.
   - Dijkstra tager højde for retning (f.eks. envejsgader).

Undirected graf:
   - Bruges til netværk, kabling, sociale forbindelser.
   - Dijkstra kan køre, men kanter virker begge veje.
   - MST giver kun mening på undirected graf.

Startkonfiguration fra tabel:
   - Startnode: F
   - Kør Dijkstra fra node F

Trin-for-trin output er skrevet til 'dijkstra.txt'
------------------------------------------------------------
        """);
    }
}