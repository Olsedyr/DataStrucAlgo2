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
    Map<String, List<String>> adjListDirected = new HashMap<>(); // For topological sort

    void addDirectedEdge(String from, String to, int weight) {
        adjList.computeIfAbsent(from, k -> new HashMap<>()).put(to, weight);
        adjListDirected.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
        // Ensure all nodes are in the adjacency list
        adjListDirected.putIfAbsent(to, new ArrayList<>());
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

    // For topological sort
    List<String> getNeighborsDirected(String node) {
        return adjListDirected.getOrDefault(node, new ArrayList<>());
    }

    Set<String> getAllNodesDirected() {
        return adjListDirected.keySet();
    }

    Map<String, Integer> calculateInDegrees() {
        Map<String, Integer> inDegree = new HashMap<>();

        // Initialize all nodes with 0
        for (String node : getAllNodesDirected()) {
            inDegree.put(node, 0);
        }

        // Calculate in-degrees
        for (String node : getAllNodesDirected()) {
            for (String neighbor : getNeighborsDirected(node)) {
                inDegree.put(neighbor, inDegree.getOrDefault(neighbor, 0) + 1);
            }
        }

        return inDegree;
    }
}

class DijkstraResult {
    Map<String, Integer> dist;
    Map<String, String> prev;
    Set<String> visited;
    List<Map<String, Object>> steps;

    DijkstraResult(Map<String, Integer> dist, Map<String, String> prev, Set<String> visited, List<Map<String, Object>> steps) {
        this.dist = dist;
        this.prev = prev;
        this.visited = visited;
        this.steps = steps;
    }
}

public class Dijkstra {

    private static final String START_NODE = "C"; // Ændret til A for bedre demonstration

    // OPDATERET GRAF - DAG (Directed Acyclic Graph) for topological sort
    private static final List<Edge> EDGES = Arrays.asList(
            new Edge("A", "B", 1),
            new Edge("B", "C", 2),
            new Edge("C", "D", 3),
            new Edge("D", "E", 4),
            new Edge("E", "F", 5),
            new Edge("F", "G", 6),
            new Edge("G", "H", 7)
    );

    // Topologisk sortering - Kahn's algoritme
    public static List<String> topologicalSortKahn(Graph graph) {
        Map<String, Integer> inDegree = graph.calculateInDegrees();
        Queue<String> queue = new LinkedList<>();
        List<String> result = new ArrayList<>();
        Map<Integer, List<String>> steps = new TreeMap<>();
        int step = 0;

        // Find nodes with in-degree 0
        for (String node : inDegree.keySet()) {
            if (inDegree.get(node) == 0) {
                queue.add(node);
            }
        }

        while (!queue.isEmpty()) {
            // Gem trin
            steps.put(step, new ArrayList<>(result));
            step++;

            String current = queue.poll();
            result.add(current);

            // Reduce in-degree of neighbors
            for (String neighbor : graph.getNeighborsDirected(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // Gem det sidste trin
        steps.put(step, new ArrayList<>(result));

        // Check for cycle
        if (result.size() != graph.getAllNodesDirected().size()) {
            System.out.println("Warning: Graph contains a cycle! Topological sort not possible.");
            return new ArrayList<>();
        }

        writeTopologicalStepsToFile(steps, "topological_sort.txt");
        return result;
    }

    // Topologisk sortering - DFS metode
    public static List<String> topologicalSortDFS(Graph graph) {
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();
        Stack<String> stack = new Stack<>();
        List<Map<String, Object>> dfsSteps = new ArrayList<>();
        int step = 0;

        for (String node : graph.getAllNodesDirected()) {
            if (!visited.contains(node)) {
                if (!dfsTopological(node, graph, visited, recursionStack, stack, dfsSteps, step)) {
                    System.out.println("Cycle detected! Graph is not a DAG.");
                    return new ArrayList<>();
                }
            }
        }

        List<String> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        writeDFSTopologicalStepsToFile(dfsSteps, "topological_dfs_steps.txt");
        return result;
    }

    private static boolean dfsTopological(String node, Graph graph, Set<String> visited,
                                          Set<String> recursionStack, Stack<String> stack,
                                          List<Map<String, Object>> steps, int step) {

        Map<String, Object> stepInfo = new HashMap<>();
        stepInfo.put("step", step);
        stepInfo.put("node", node);
        stepInfo.put("visited", new HashSet<>(visited));
        stepInfo.put("recursionStack", new HashSet<>(recursionStack));
        stepInfo.put("stack", new ArrayList<>(stack));
        steps.add(stepInfo);

        visited.add(node);
        recursionStack.add(node);

        for (String neighbor : graph.getNeighborsDirected(node)) {
            if (!visited.contains(neighbor)) {
                if (!dfsTopological(neighbor, graph, visited, recursionStack, stack, steps, step + 1)) {
                    return false;
                }
            } else if (recursionStack.contains(neighbor)) {
                return false; // Cycle detected
            }
        }

        recursionStack.remove(node);
        stack.push(node);
        return true;
    }

    public static void writeTopologicalStepsToFile(Map<Integer, List<String>> steps, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== TOPOLOGISK SORTERING (Kahn's Algoritme) ===");
            writer.println("Fjerner løbende noder med in-degree 0");
            writer.println();

            for (Map.Entry<Integer, List<String>> entry : steps.entrySet()) {
                writer.println("Trin " + entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("Topologisk sortering trin skrevet til fil: " + filename);
        } catch (IOException e) {
            System.err.println("Fejl ved skrivning til fil: " + e.getMessage());
        }
    }

    public static void writeDFSTopologicalStepsToFile(List<Map<String, Object>> steps, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== TOPOLOGISK SORTERING (DFS Metode) ===");
            writer.println("Dybd først gennemgang med backtracking");
            writer.println();

            for (Map<String, Object> step : steps) {
                writer.println("--- Trin " + step.get("step") + " ---");
                writer.println("Nuværende node: " + step.get("node"));
                writer.println("Besøgte noder: " + step.get("visited"));
                writer.println("Rekursionsstack: " + step.get("recursionStack"));
                writer.println("Resultatstack: " + step.get("stack"));
                writer.println();
            }

            System.out.println("DFS Topologisk sortering trin skrevet til fil: " + filename);
        } catch (IOException e) {
            System.err.println("Fejl ved skrivning til fil: " + e.getMessage());
        }
    }

    // Resten af dine eksisterende metoder forbliver uændret...
    public static DijkstraResult dijkstra(Graph graph, String source, boolean writeSteps) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        List<Map<String, Object>> steps = new ArrayList<>();

        for (String node : graph.nodes()) {
            dist.put(node, Integer.MAX_VALUE);
            prev.put(node, null);
        }
        dist.put(source, 0);

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

        // Kør Dijkstra på rettet graf
        DijkstraResult dirResult = dijkstra(directed, START_NODE, true);
        writeDijkstraStepsToFile(dirResult, "DIJKSTRA PÅ RETTET GRAF", "dijkstra.txt");
        printDijkstraTable(dirResult, "DIJKSTRA PÅ RETTET GRAF");

        // Kør Dijkstra på urettet graf
        DijkstraResult undirResult = dijkstra(undirected, START_NODE, false);
        printDijkstraTable(undirResult, "DIJKSTRA PÅ URETTET GRAF");

        // Kør Kruskal MST
        List<Edge> mst = kruskalMST(undirected);
        printMST(mst);

        // Kør Topologisk sortering (Kahn's algoritme)
        System.out.println("\n=== TOPOLOGISK SORTERING (KAHN) ===");
        List<String> topologicalOrderKahn = topologicalSortKahn(directed);
        System.out.println("Topologisk rækkefølge (Kahn): " + topologicalOrderKahn);

        // Kør Topologisk sortering (DFS metode)
        System.out.println("\n=== TOPOLOGISK SORTERING (DFS) ===");
        List<String> topologicalOrderDFS = topologicalSortDFS(directed);
        System.out.println("Topologisk rækkefølge (DFS): " + topologicalOrderDFS);

        System.out.println("""
------------------------------------------------------------
📌 FORKLARING AF ALGORITMER

1. Dijkstra's algoritme:
   - Finder korteste vej fra en startnode til alle andre
   - Kræver ikke-negative vægte
   - Bruger prioritetskø

2. Kruskal's algoritme:
   - Finder Minimum Spanning Tree (MST)
   - Kun for urettede grafer
   - Bruger Union-Find data struktur

3. Topologisk sortering:
   - Kun for Directed Acyclic Graphs (DAGs)
   - Kahn's algoritme: Fjerner noder med in-degree 0
   - DFS metode: Dybd først gennemgang
   - Anvendelse: Projektplanlægning, kursusafhængigheder

Filer genereret:
   - dijkstra.txt: Dijkstra trin-for-trin
   - topological_sort.txt: Kahn's algoritme trin
   - topological_dfs_steps.txt: DFS metode trin
------------------------------------------------------------
        """);
    }
}