import java.util.PriorityQueue;

public class PriorityQueueExample {
    public static void main(String[] args) {
        // Create a priority queue
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        // Insert elements into the priority queue
        pq.add(10); // Add element 10
        pq.add(20); // Add element 20
        pq.add(15); // Add element 15

        // Print and remove elements from the priority queue
        while (!pq.isEmpty()) {
            // Poll removes the element with the highest priority (smallest element in a min-heap)
            System.out.println(pq.poll());
        }
    }
}