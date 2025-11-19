package MoreQA.KahnAlgorithm;

import java.util.*;

class KahnGraph {
    int V; // Number of vertices in the graph
    LinkedList<Integer> adjList[]; // Adjacency list representation of the graph

    // Constructor
    KahnGraph(int V) {
        this.V = V;
        adjList = new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    // Add a directed edge u -> v
    void addEdge(int u, int v) {
        adjList[u].add(v);
    }

    // Kahn's Algorithm for Topological Sorting
    void kahnTopologicalSort() {
        int inDegree[] = new int[V]; // Array to store in-degrees of all vertices
        Queue<Integer> q = new LinkedList<>(); // Queue to process vertices with in-degree 0

        // Step 1: Calculate in-degree of all vertices
        for (int i = 0; i < V; i++) {
            for (int node : adjList[i]) {
                inDegree[node]++;
            }
        }

        // Step 2: Add all vertices with in-degree 0 to the queue
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                q.add(i);
            }
        }

        // Step 3: Process the queue
        System.out.println("Topological Sorting:");
        while (!q.isEmpty()) {
            int u = q.poll();
            System.out.print(u + " "); // Print the topological order

            // Step 4: Decrease in-degree of adjacent vertices
            for (int v : adjList[u]) {
                if (--inDegree[v] == 0) {
                    q.add(v);
                }
            }
        }
        System.out.println();
    }
}

public class KahnGraphMain {

    // Main function to test Kahn's Algorithm
    public static void main(String[] args) {
        // Create a graph with 6 vertices
        KahnGraph g = new KahnGraph(6);

        // Add directed edges
        g.addEdge(5, 2);
        g.addEdge(5, 0);
        g.addEdge(4, 0);
        g.addEdge(4, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 1);

        // Run Kahn's Algorithm for Topological Sorting
        System.out.println("Running Kahn's Algorithm (Topological Sort):");
        g.kahnTopologicalSort();
    }
}