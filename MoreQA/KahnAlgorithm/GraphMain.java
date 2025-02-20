package MoreQA.KahnAlgorithm;

import java.util.*;

// Graph class for Kahn's Topological Sorting
class Graph {
    int V;
    LinkedList<Integer> adjList[];

    // Constructor
    Graph(int V) {
        this.V = V;
        adjList = new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    // Add directed edge u -> v
    void addEdge(int u, int v) {
        adjList[u].add(v);
    }

    // Kahn's Algorithm for Topological Sorting
    void kahnTopologicalSort() {
        int inDegree[] = new int[V];
        Queue<Integer> q = new LinkedList<>();

        // Calculate in-degree of all vertices
        for (int i = 0; i < V; i++) {
            for (int node : adjList[i]) {
                inDegree[node]++;
            }
        }

        // Push all vertices with no incoming edges (in-degree 0) into the queue
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                q.add(i);
            }
        }

        // Process each vertex in the queue
        while (!q.isEmpty()) {
            int u = q.poll();
            System.out.print(u + " "); // Print the topological sort

            // For each neighbor, decrease its in-degree
            for (int v : adjList[u]) {
                if (--inDegree[v] == 0) {
                    q.add(v);
                }
            }
        }
        System.out.println();
    }
}

public class GraphMain {
//The algorithm determines an order
// in which vertices can be visited such
// that for every directed edge u -> v,
// vertex u comes before v in the ordering.
// This is useful for problems like task
// scheduling, where certain tasks depend
// on others.

    //Key: You provide the vertices and edges
    // in the graph, and the algorithm
    // computes a valid topological order
    // (not a "path" like in Dijkstra’s
    // algorithm).

    //In Graph.java, you can modify
    // the directed edges using addEdge(u, v)
    // and run Kahn's topological sort by
    // calling kahnTopologicalSort().


    // Main function to test Kahn's Topological Sorting
    public static void main(String[] args) {
        System.out.println("Running Kahn's Algorithm (Topological Sort) on a Directed Acyclic Graph:");
        Graph g = new Graph(6);
        g.addEdge(5, 2);
        g.addEdge(5, 0);
        g.addEdge(4, 0);
        g.addEdge(4, 1);
        g.addEdge(2, 3);
        g.addEdge(3, 1);
        g.kahnTopologicalSort(); // Output should be a valid topological order
    }
}
