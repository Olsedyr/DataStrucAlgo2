package MoreQA.Heap;

import java.util.PriorityQueue;

public class ConnectRopesMinimumEffort {

    // Function to find the minimum cost to connect all ropes
    public static int minCost(int[] ropes) {
        // Create a min-heap (priority queue)
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();

        // Push all ropes into the min-heap
        for (int rope : ropes) {
            minHeap.add(rope);
        }

        // Variable to store the total cost
        int totalCost = 0;

        // While there is more than one rope, keep connecting the two smallest
        while (minHeap.size() > 1) {
            // Pop the two smallest ropes
            int first = minHeap.poll();
            int second = minHeap.poll();

            // The cost of connecting these two ropes
            int cost = first + second;
            totalCost += cost;

            // Push the new rope back into the heap
            minHeap.add(cost);
        }

        // Return the total cost to connect all ropes
        return totalCost;
    }

    public static void main(String[] args) {
        // Example 1
        int[] ropes1 = {4, 3, 2, 6};
        System.out.println("Minimum cost to connect ropes: " + minCost(ropes1)); // Output: 29

        // Example 2
        int[] ropes2 = {1, 2, 3, 4, 5};
        System.out.println("Minimum cost to connect ropes: " + minCost(ropes2)); // Output: 33
    }
}
