import java.util.*;

public class DAG_ShortestPath {

    private static final int INF = Integer.MAX_VALUE;

    // Immutable Edge record representing a directed edge: (to vertex, weight)
    record Edge(int to, int weight) {}

    static class Graph {
        private final int V; // Number of vertices
        private final List<List<Edge>> adj; // Adjacency list

        public Graph(int V) {
            this.V = V;
            this.adj = new ArrayList<>(V);
            for (int i = 0; i < V; i++)
                adj.add(new ArrayList<>());
        }

        public void addEdge(int from, int to, int weight) {
            adj.get(from).add(new Edge(to, weight));
        }

        // ------------------------------------------------------------
        // DAG SHORTEST PATH (with dynamic steps)
        // ------------------------------------------------------------

        private void topoDFS(int v, boolean[] visited, Deque<Integer> order) {
            visited[v] = true;
            System.out.println("[DAG] DFS visits vertex " + v);

            for (Edge e : adj.get(v)) {
                if (!visited[e.to()]) {
                    System.out.println("[DAG] DFS goes deeper to vertex " + e.to());
                    topoDFS(e.to(), visited, order);
                }
            }
            System.out.println("[DAG] Pushing vertex " + v + " onto topological stack");
            order.push(v);
        }

        public int[] shortestPathDAG(int source) {

            System.out.println("\n=== START DAG SHORTEST PATH ===");

            boolean[] visited = new boolean[V];
            Deque<Integer> topoOrder = new ArrayDeque<>();

            // Step 1: Topological sorting
            System.out.println("[DAG] Starting topological sort...");
            for (int i = 0; i < V; i++)
                if (!visited[i])
                    topoDFS(i, visited, topoOrder);

            System.out.println("[DAG] Topological order: " + topoOrder + "\n");

            // Step 2: Initialize distances
            int[] dist = new int[V];
            Arrays.fill(dist, INF);
            dist[source] = 0;

            System.out.println("[DAG] Initial distances:");
            System.out.println(Arrays.toString(dist) + "\n");

            // Step 3: Relax edges in topological order
            while (!topoOrder.isEmpty()) {
                int u = topoOrder.pop();

                if (dist[u] == INF) {
                    System.out.println("[DAG] Skipping vertex " + u + " (distance is ∞)");
                    continue;
                }

                System.out.println("[DAG] Processing vertex " + u + " with dist = " + dist[u]);

                for (Edge e : adj.get(u)) {
                    int v = e.to();
                    int w = e.weight();
                    int newDist = dist[u] + w;

                    System.out.println("  [DAG] Considering edge " + u + " → " + v + " (weight " + w + ")");

                    if (newDist < dist[v]) {
                        System.out.println("    [DAG] Relax: dist[" + v + "] updated from " + dist[v] + " to " + newDist);
                        dist[v] = newDist;
                    } else {
                        System.out.println("    [DAG] No update");
                    }
                }
                System.out.println("  [DAG] Current distances: " + Arrays.toString(dist) + "\n");
            }

            System.out.println("=== END DAG SHORTEST PATH ===\n");
            return dist;
        }

        // ------------------------------------------------------------
        // DIJKSTRA (with dynamic steps)
        // ------------------------------------------------------------

        public int[] dijkstra(int source) {

            System.out.println("\n=== START DIJKSTRA ===");

            int[] dist = new int[V];
            Arrays.fill(dist, INF);
            dist[source] = 0;

            PriorityQueue<int[]> pq =
                    new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

            pq.add(new int[]{source, 0});

            System.out.println("[Dijkstra] Initial distances: " + Arrays.toString(dist) + "\n");

            while (!pq.isEmpty()) {
                int[] top = pq.poll();
                int u = top[0];
                int d = top[1];

                System.out.println("[Dijkstra] Extracting vertex " + u + " (distance = " + d + ")");

                if (d > dist[u]) {
                    System.out.println("  [Dijkstra] Skipping vertex (outdated distance)\n");
                    continue;
                }

                for (Edge e : adj.get(u)) {
                    int v = e.to();
                    int w = e.weight();
                    int newDist = d + w;

                    System.out.println("  [Dijkstra] Considering edge " + u + " → " + v + " (weight " + w + ")");

                    if (newDist < dist[v]) {
                        System.out.println("    [Dijkstra] Relax: dist[" + v + "] = " + newDist);
                        dist[v] = newDist;
                        pq.add(new int[]{v, newDist});
                    } else {
                        System.out.println("    [Dijkstra] No update");
                    }
                }

                System.out.println("  [Dijkstra] Current distances: " + Arrays.toString(dist) + "\n");
            }

            System.out.println("=== END DIJKSTRA ===\n");
            return dist;
        }
    }

    // Utility: print final distances nicely
    public static void printDistances(String title, int[] dist, int source) {
        System.out.println("=== " + title + " ===");
        for (int i = 0; i < dist.length; i++) {
            System.out.printf("To vertex %d: %s%n", i, dist[i] == INF ? "∞" : dist[i]);
        }
        System.out.println();
    }

    // Main method: runs both algorithms on the same graph
    public static void main(String[] args) {

        Graph g = new Graph(6);

        // Same graph for both algorithms
        g.addEdge(0, 1, 5);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 3, 6);
        g.addEdge(1, 2, 2);
        g.addEdge(2, 4, 4);
        g.addEdge(2, 5, 2);
        g.addEdge(2, 3, 7);
        g.addEdge(3, 4, -1); // Note: Negative edge allowed for DAG, NOT for Dijkstra
        g.addEdge(4, 5, -2);

        int source = 0;

        // Run DAG shortest path with step output
        int[] dagDist = g.shortestPathDAG(source);

        // Run Dijkstra with step output
        int[] dijDist = g.dijkstra(source);

        // Print final results neatly
        printDistances("RESULTS – DAG Shortest Path", dagDist, source);
        printDistances("RESULTS – Dijkstra", dijDist, source);
    }
}
