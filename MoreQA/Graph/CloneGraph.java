package MoreQA.Graph;

import java.util.*;

class Node {
    int val;
    List<Node> neighbors;

    Node(int val) {
        this.val = val;
        this.neighbors = new ArrayList<>();
    }
}

public class CloneGraph {

    // Function to clone the graph using BFS
    public static Node cloneGraph(Node node) {
        if (node == null) {
            return null;
        }

        // Map to store the mapping of original node to new node
        Map<Node, Node> map = new HashMap<>();

        // Initialize a queue for BFS
        Queue<Node> queue = (Queue<Node>) new LinkedList();
        queue.offer(node);

        // Create the root node for the cloned graph
        map.put(node, new Node(node.val));

        // BFS to clone the graph
        while (!queue.isEmpty()) {
            Node curr = queue.poll();

            // Iterate over all neighbors of the current node
            for (Node neighbor : curr.neighbors) {
                if (!map.containsKey(neighbor)) {
                    // If the neighbor hasn't been cloned, clone it
                    map.put(neighbor, new Node(neighbor.val));
                    queue.offer(neighbor);
                }
                // Link the current node's clone to its neighbor's clone
                map.get(curr).neighbors.add(map.get(neighbor));
            }
        }

        // Return the cloned root node
        return map.get(node);
    }

    public static void main(String[] args) {
        // Creating a graph for testing
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);

        // Setting up the graph's edges
        node1.neighbors.add(node2);
        node1.neighbors.add(node4);
        node2.neighbors.add(node1);
        node2.neighbors.add(node3);
        node3.neighbors.add(node2);
        node3.neighbors.add(node4);
        node4.neighbors.add(node1);
        node4.neighbors.add(node3);

        // Cloning the graph
        Node clonedGraph = cloneGraph(node1);

        // Print the original and cloned graphs' structure for verification
        System.out.println("Original Graph:");
        printGraph(node1);
        System.out.println("\nCloned Graph:");
        printGraph(clonedGraph);
    }

    // Utility function to print the graph's structure
    public static void printGraph(Node node) {
        if (node == null) return;

        Set<Node> visited = new HashSet<>();
        printHelper(node, visited);
    }

    public static void printHelper(Node node, Set<Node> visited) {
        if (visited.contains(node)) return;

        visited.add(node);
        System.out.print(node.val + " -> ");
        for (Node neighbor : node.neighbors) {
            System.out.print(neighbor.val + " ");
        }
        System.out.println();

        for (Node neighbor : node.neighbors) {
            printHelper(neighbor, visited);
        }
    }
}
