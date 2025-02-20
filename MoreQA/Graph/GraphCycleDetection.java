package MoreQA.Graph;

import java.util.*;

public class GraphCycleDetection {

    // Function to detect cycle in a directed graph using DFS
    public static boolean hasCycleDirected(int V, List<List<Integer>> adj) {
        // Visited array to track visited nodes and the recursion stack
        int[] visited = new int[V]; // 0 - Not visited, 1 - Visiting, 2 - Visited

        // Perform DFS on each unvisited node
        for (int i = 0; i < V; i++) {
            if (visited[i] == 0) {
                if (dfsDirected(i, adj, visited)) {
                    return true; // Cycle detected
                }
            }
        }

        return false; // No cycle
    }

    // Helper function to perform DFS and detect cycle in directed graph
    private static boolean dfsDirected(int node, List<List<Integer>> adj, int[] visited) {
        // Mark the node as being visited (part of the current recursion stack)
        visited[node] = 1;

        // Recur for all the vertices adjacent to this node
        for (Integer neighbor : adj.get(node)) {
            if (visited[neighbor] == 1) {
                return true; // Cycle detected
            }
            if (visited[neighbor] == 0) {
                if (dfsDirected(neighbor, adj, visited)) {
                    return true;
                }
            }
        }

        // Mark the node as completely visited (recursion is done)
        visited[node] = 2;
        return false;
    }

    // Function to detect cycle in an undirected graph using DFS
    public static boolean hasCycleUndirected(int V, List<List<Integer>> adj) {
        boolean[] visited = new boolean[V];

        // Perform DFS from each unvisited node
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                if (dfsUndirected(i, adj, visited, -1)) {
                    return true; // Cycle detected
                }
            }
        }

        return false; // No cycle
    }

    // Helper function to perform DFS and detect cycle in undirected graph
    private static boolean dfsUndirected(int node, List<List<Integer>> adj, boolean[] visited, int parent) {
        visited[node] = true;

        // Recur for all the vertices adjacent to this node
        for (Integer neighbor : adj.get(node)) {
            // If the neighbor is not visited, visit it
            if (!visited[neighbor]) {
                if (dfsUndirected(neighbor, adj, visited, node)) {
                    return true; // Cycle detected
                }
            }
            // If the neighbor is visited and not the parent, cycle is detected
            else if (neighbor != parent) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int V = 4;
        List<List<Integer>> adj = new ArrayList<>();

        // Create the adjacency list for a directed graph
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        adj.get(0).add(1);
        adj.get(1).add(2);
        adj.get(2).add(0);
        adj.get(3).add(2);

        // Check for cycle in directed graph
        System.out.println("Cycle in directed graph: " + hasCycleDirected(V, adj));

        // For an undirected graph, we would build a different adjacency list:
        List<List<Integer>> adjUndirected = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            adjUndirected.add(new ArrayList<>());
        }

        adjUndirected.get(0).add(1);
        adjUndirected.get(1).add(0);
        adjUndirected.get(1).add(2);
        adjUndirected.get(2).add(1);
        adjUndirected.get(2).add(3);
        adjUndirected.get(3).add(2);

        // Check for cycle in undirected graph
        System.out.println("Cycle in undirected graph: " + hasCycleUndirected(V, adjUndirected));
    }
}
