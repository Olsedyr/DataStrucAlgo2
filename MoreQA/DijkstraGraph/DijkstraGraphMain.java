package MoreQA.DijkstraGraph;

import java.util.*;

// Dijkstra's Algorithm for Shortest Path
class DijkstraGraph {
    int V;
    LinkedList<Edge> adjList[];

    // Constructor
    DijkstraGraph(int V) {
        this.V = V;
        adjList = new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    // Add an edge u -> v with weight w
    void addEdge(int u, int v, int w) {
        adjList[u].add(new Edge(v, w));
    }

    // Dijkstra's Algorithm for Shortest Path
    void dijkstra(int source) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<Node> pq = new PriorityQueue<>(V, new Comparator<Node>() {
            public int compare(Node n1, Node n2) {
                return Integer.compare(n1.dist, n2.dist);
            }
        });

        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node u = pq.poll();

            // Process each neighbor of u
            for (Edge edge : adjList[u.vertex]) {
                int v = edge.dest;
                int weight = edge.weight;
                if (dist[u.vertex] + weight < dist[v]) {
                    dist[v] = dist[u.vertex] + weight;
                    pq.add(new Node(v, dist[v]));
                }
            }
        }

        // Print the shortest path
        System.out.println("Shortest distances from source " + source + ":");
        for (int i = 0; i < V; i++) {
            System.out.println("Vertex " + i + ": " + (dist[i] == Integer.MAX_VALUE ? "INF" : dist[i]));
        }
    }

    // Inner class for edges (used in Dijkstra)
    class Edge {
        int dest;
        int weight;

        Edge(int dest, int weight) {
            this.dest = dest;
            this.weight = weight;
        }
    }

    // Inner class for nodes in priority queue (used in Dijkstra)
    class Node {
        int vertex;
        int dist;

        Node(int vertex, int dist) {
            this.vertex = vertex;
            this.dist = dist;
        }
    }
}

public class DijkstraGraphMain {

    //The algorithm uses the edges and their
    // associated weights to find the shortest
    // path from the source vertex to each of
    // the other vertices in the graph.


    //You provide the starting vertex
    // and the graph’s edge weights,
    // and the algorithm calculates
    // the shortest distance from the source
    // to each vertex in the graph.






//In DijkstraGraph.java, you can modify
// the graph’s edges using
// addEdge(u, v, weight) and run
// Dijkstra’s algorithm by calling
// dijkstra(source), where source is
// the starting vertex.


    // Main function to test Dijkstra's Algorithm
    public static void main(String[] args) {
        System.out.println("Running Dijkstra's Algorithm (Shortest Path):");
        DijkstraGraph dg = new DijkstraGraph(9);
        dg.addEdge(0, 1, 4);
        dg.addEdge(0, 7, 8);
        dg.addEdge(1, 2, 8);
        dg.addEdge(1, 7, 11);
        dg.addEdge(2, 3, 7);
        dg.addEdge(2, 8, 2);
        dg.addEdge(2, 5, 4);
        dg.addEdge(3, 4, 9);
        dg.addEdge(3, 5, 14);
        dg.addEdge(4, 5, 10);
        dg.addEdge(5, 6, 2);
        dg.addEdge(6, 7, 1);
        dg.addEdge(6, 8, 6);
        dg.addEdge(7, 8, 7);
        dg.dijkstra(0); // Output shortest paths from source vertex 0
    }
}
