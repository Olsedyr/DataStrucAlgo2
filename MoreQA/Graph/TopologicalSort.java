package MoreQA.Graph;

import java.util.*;

public class TopologicalSort {

    // Kahn's Algorithm (BFS-based approach)
    public static List<Integer> topologicalSortKahn(int V, List<List<Integer>> adj) {
        int[] inDegree = new int[V];  // Store the in-degree of each node
        List<Integer> result = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        // Step 1: Compute the in-degree for each vertex
        for (int i = 0; i < V; i++) {
            for (int neighbor : adj.get(i)) {
                inDegree[neighbor]++;
            }
        }

        // Step 2: Add all vertices with 0 in-degree to the queue
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        // Step 3: Process the nodes in topological order
        while (!queue.isEmpty()) {
            int node = queue.poll();
            result.add(node);

            // Reduce in-degree of neighboring vertices
            for (int neighbor : adj.get(node)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.add(neighbor);
                }
            }
        }

        // If the result size is less than the number of vertices, the graph has a cycle
        if (result.size() != V) {
            System.out.println("Graph contains a cycle. Topological sort not possible.");
            return new ArrayList<>();
        }

        return result;
    }

    // DFS-based Approach
    private static void dfs(int node, List<List<Integer>> adj, boolean[] visited, Stack<Integer> stack) {
        visited[node] = true;

        // Visit all neighbors
        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor, adj, visited, stack);
            }
        }

        // Push the node to stack after visiting all neighbors (post-order)
        stack.push(node);
    }

    public static List<Integer> topologicalSortDFS(int V, List<List<Integer>> adj) {
        boolean[] visited = new boolean[V];
        Stack<Integer> stack = new Stack<>();

        // Perform DFS for each node
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfs(i, adj, visited, stack);
            }
        }

        // Reverse the stack to get the topological order
        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }

        return result;
    }

    // Main function to test both approaches
    public static void main(String[] args) {
        // Example Graph (Adjacency List Representation)
        int V = 6;
        List<List<Integer>> adj = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        // Add edges to the graph (Directed edges)
        adj.get(5).add(2);
        adj.get(5).add(0);
        adj.get(4).add(0);
        adj.get(4).add(1);
        adj.get(2).add(3);
        adj.get(3).add(1);

        // Test Kahn's Algorithm (BFS)
        List<Integer> resultKahn = topologicalSortKahn(V, adj);
        System.out.println("Topological Sort (Kahn's Algorithm): " + resultKahn);

        // Test DFS-based Approach
        List<Integer> resultDFS = topologicalSortDFS(V, adj);
        System.out.println("Topological Sort (DFS-based): " + resultDFS);
    }
}
