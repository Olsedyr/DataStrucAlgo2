package MoreQA.DijkstraGraph;

import java.util.*;

class DijkstraGraph {
    int V; // Number of vertices in the graph
    LinkedList<Edge> adjList[]; // Adjacency list representation of the graph

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
        adjList[u].add(new Edge(u, v, w));
        adjList[v].add(new Edge(v, u, w)); // For undirected graph
    }

    // Dijkstra's Algorithm for Shortest Path
    void dijkstra(int source) {
        int[] dist = new int[V]; // Array to store shortest distances from source
        Arrays.fill(dist, Integer.MAX_VALUE); // Initialize distances to infinity
        dist[source] = 0; // Distance to source is 0

        // Priority queue to process vertices based on shortest distance
        PriorityQueue<Node> pq = new PriorityQueue<>(V, Comparator.comparingInt(n -> n.dist));
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node u = pq.poll(); // Get vertex with shortest distance

            // Process each neighbor of u
            for (Edge edge : adjList[u.vertex]) {
                int v = edge.dest;
                int weight = edge.weight;
                // If a shorter path to v is found
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

    // Kruskal's Algorithm for Minimum Spanning Tree
    void kruskalMST() {
        // Priority queue to process edges based on weight
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        for (int i = 0; i < V; i++) {
            pq.addAll(adjList[i]);
        }

        int[] parent = new int[V]; // Array to store parent of each vertex
        for (int i = 0; i < V; i++) {
            parent[i] = i;
        }

        List<Edge> mst = new ArrayList<>(); // List to store edges in MST
        int mstWeight = 0; // Total weight of MST

        while (!pq.isEmpty() && mst.size() < V - 1) {
            Edge edge = pq.poll();
            int u = find(parent, edge.src);
            int v = find(parent, edge.dest);

            // If u and v are in different sets, add edge to MST
            if (u != v) {
                mst.add(edge);
                mstWeight += edge.weight;
                union(parent, u, v);
            }
        }

        // Print the MST
        System.out.println("Edges in the Minimum Spanning Tree:");
        for (Edge edge : mst) {
            System.out.println(edge.src + " - " + edge.dest + ": " + edge.weight);
        }
        System.out.println("Total weight of MST: " + mstWeight);
    }

    // Find the root of the set in which element k is present
    int find(int[] parent, int k) {
        if (parent[k] == k) {
            return k;
        }
        return parent[k] = find(parent, parent[k]);
    }

    // Perform union of two subsets
    void union(int[] parent, int u, int v) {
        int rootU = find(parent, u);
        int rootV = find(parent, v);
        parent[rootU] = rootV;
    }

    // Inner class for edges (used in Dijkstra and Kruskal)
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

    // Inner class for nodes in priority queue (used in Dijkstra)
    class Node {
        int vertex; // Vertex number
        int dist; // Distance from source

        Node(int vertex, int dist) {
            this.vertex = vertex;
            this.dist = dist;
        }
    }
}

public class DijkstraGraphMain {

    // Main function to test Dijkstra's and Kruskal's Algorithms
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Default graph data from Opgave 5
        int V = 9; // Number of vertices
        DijkstraGraph dg = new DijkstraGraph(V);

        // Add edges from Opgave 5
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

        System.out.print("Do you want to use the default graph from code? (yes/no): ");
        String useDefault = scanner.nextLine().trim().toLowerCase();

        if (!useDefault.equals("yes")) {
            System.out.print("Enter the number of vertices (count 1..2..3...): ");
            V = scanner.nextInt();
            dg = new DijkstraGraph(V);

            System.out.print("Enter the number of edges (count 1..2..3...): ");
            int E = scanner.nextInt();
            System.out.println("Enter edges in the format: source destination weight(Start from 0..Index0)");
            for (int i = 0; i < E; i++) {
                int src = scanner.nextInt();
                int dest = scanner.nextInt();
                int weight = scanner.nextInt();
                dg.addEdge(src, dest, weight);
            }
        }

        System.out.print("Enter the source vertex for Dijkstra's algorithm: ");
        int source = scanner.nextInt();

        System.out.println("Running Dijkstra's Algorithm (Shortest Path):");
        dg.dijkstra(source);

        System.out.println("\nRunning Kruskal's Algorithm (Minimum Spanning Tree):");
        dg.kruskalMST();

        scanner.close();
    }
}