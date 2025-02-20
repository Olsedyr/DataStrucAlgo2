package MoreQA.Graph;

import java.util.*;

public class GraphBridges {

    static int time = 0;  // Global variable for discovery time

    // Function to perform DFS and find bridges
    public static void dfs(int u, List<List<Integer>> adj, boolean[] visited, int[] disc, int[] low, int[] parent, List<String> bridges) {
        visited[u] = true;
        disc[u] = low[u] = ++time; // Set discovery and low values

        // Explore all neighbors of the current vertex
        for (Integer v : adj.get(u)) {
            // If v is not visited, then do DFS
            if (!visited[v]) {
                parent[v] = u;
                dfs(v, adj, visited, disc, low, parent, bridges);

                // Check if the subtree rooted at v has a connection back to one of the ancestors of u
                low[u] = Math.min(low[u], low[v]);

                // If the lowest vertex reachable from v is below u in DFS tree, then u-v is a bridge
                if (low[v] > disc[u]) {
                    bridges.add(u + "-" + v);
                }
            }
            // Update low[u] for back edge
            else if (v != parent[u]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
    }

    // Function to find all bridges in the graph
    public static List<String> findBridges(int V, List<List<Integer>> adj) {
        boolean[] visited = new boolean[V];
        int[] disc = new int[V]; // Discovery time of visited vertices
        int[] low = new int[V];  // Lowest discovery time reachable
        int[] parent = new int[V]; // Parent of vertices in DFS tree
        List<String> bridges = new ArrayList<>();

        // Initialize parent and visited arrays
        Arrays.fill(parent, -1);

        // Perform DFS for each unvisited vertex
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfs(i, adj, visited, disc, low, parent, bridges);
            }
        }

        return bridges;  // Return list of bridges
    }

    public static void main(String[] args) {
        int V = 5;
        List<List<Integer>> adj = new ArrayList<>();

        // Create the adjacency list for the graph
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        adj.get(0).add(1);
        adj.get(1).add(0);
        adj.get(0).add(2);
        adj.get(2).add(0);
        adj.get(1).add(2);
        adj.get(2).add(1);
        adj.get(1).add(3);
        adj.get(3).add(1);
        adj.get(3).add(4);
        adj.get(4).add(3);

        // Find all bridges in the graph
        List<String> bridges = findBridges(V, adj);

        System.out.println("Bridges in the graph: " + bridges);
    }
}
