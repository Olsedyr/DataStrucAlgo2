package MoreQA.PrimsAlgorithm;
import java.util.*;

class PrimsGraph {
    int V; // Number of vertices in the graph
    LinkedList<Edge> adjList[]; // Adjacency list representation of the graph

    // Constructor
    PrimsGraph(int V) {
        this.V = V;
        adjList = new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    // Add an edge u -> v with weight w
    void addEdge(int u, int v, int w) {
        adjList[u].add(new Edge(u, v, w));
        adjList[v].add(new Edge(v, u, w)); // For undirected graph
    }

    // Prim's Algorithm for Minimum Spanning Tree
    void primMST() {
        boolean[] inMST = new boolean[V]; // To keep track of vertices included in MST
        Edge[] edgeTo = new Edge[V]; // To store the shortest edge to a vertex
        int[] distTo = new int[V]; // To store the shortest distance to a vertex
        Arrays.fill(distTo, Integer.MAX_VALUE); // Initialize distances to infinity
        distTo[0] = 0; // Start from vertex 0

        // Priority queue to process vertices based on shortest distance
        PriorityQueue<Node> pq = new PriorityQueue<>(V, Comparator.comparingInt(n -> n.dist));
        pq.add(new Node(0, 0));

        while (!pq.isEmpty()) {
            Node u = pq.poll(); // Get vertex with shortest distance
            inMST[u.vertex] = true; // Include vertex in MST

            // Process each neighbor of u
            for (Edge edge : adjList[u.vertex]) {
                int v = edge.dest;
                int weight = edge.weight;
                // If v is not in MST and weight of u-v is smaller than current distance to v
                if (!inMST[v] && weight < distTo[v]) {
                    distTo[v] = weight;
                    edgeTo[v] = edge;
                    pq.add(new Node(v, distTo[v]));
                }
            }
        }

        // Print the MST
        System.out.println("Edges in the Minimum Spanning Tree:");
        for (int i = 1; i < V; i++) {
            if (edgeTo[i] != null) {
                System.out.println(edgeTo[i].src + " - " + edgeTo[i].dest + ": " + edgeTo[i].weight);
            }
        }
    }

    // Inner class for edges
    class Edge {
        int src; // Source vertex
        int dest; // Destination vertex
        int weight; // Weight of the edge

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }

    // Inner class for nodes in priority queue
    class Node {
        int vertex; // Vertex number
        int dist; // Distance from source

        Node(int vertex, int dist) {
            this.vertex = vertex;
            this.dist = dist;
        }
    }
}

public class PrimsGraphMain {

    // Main function to test Prim's Algorithm
    public static void main(String[] args) {
        // Default graph
        int V = 7; // Number of vertices
        PrimsGraph pg = new PrimsGraph(V);

        //Source, destination, weight
        // Add edges
        pg.addEdge(0, 1, 4);
        pg.addEdge(1, 2, 1);
        pg.addEdge(2, 3, 9);
        pg.addEdge(3, 4, 1);
        pg.addEdge(3, 5, 7);
        pg.addEdge(4, 5, 1);
        pg.addEdge(0, 1, 4);
        pg.addEdge(1, 2, 1);
        pg.addEdge(2, 3, 9);
        pg.addEdge(3, 4, 1);
        pg.addEdge(3, 5, 7);
        pg.addEdge(4, 5, 1);


        System.out.println("Running Prim's Algorithm (Minimum Spanning Tree):");
        pg.primMST();
    }
}