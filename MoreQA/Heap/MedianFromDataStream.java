package MoreQA.Heap;

import java.util.*;

// Class to find median from data stream using two heaps
public class MedianFromDataStream {

    // Max-heap to store the smaller half
    private PriorityQueue<Integer> maxHeap;
    // Min-heap to store the larger half
    private PriorityQueue<Integer> minHeap;

    // Constructor to initialize the two heaps
    public MedianFromDataStream() {
        maxHeap = new PriorityQueue<>((a, b) -> b - a); // Max-heap (reverse order)
        minHeap = new PriorityQueue<>(); // Min-heap (natural order)
    }

    // Function to add a number to the data stream
    public void addNum(int num) {
        // Add to maxHeap first (smaller half)
        maxHeap.offer(num);

        // Ensure that every element in maxHeap is <= every element in minHeap
        if (!minHeap.isEmpty() && maxHeap.peek() > minHeap.peek()) {
            minHeap.offer(maxHeap.poll());
        }

        // Balance the sizes of the heaps. The maxHeap can only have at most one extra element.
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    // Function to find the median from the data stream
    public double findMedian() {
        // If the size of maxHeap is greater than minHeap, the median is the root of maxHeap
        if (maxHeap.size() > minHeap.size()) {
            return maxHeap.peek();
        }
        // If the sizes are equal, the median is the average of the roots of maxHeap and minHeap
        return (maxHeap.peek() + minHeap.peek()) / 2.0;
    }

    public static void main(String[] args) {
        MedianFromDataStream medianFinder = new MedianFromDataStream();

        // Adding numbers to the data stream
        medianFinder.addNum(1);
        System.out.println("Median: " + medianFinder.findMedian()); // Output: 1

        medianFinder.addNum(2);
        System.out.println("Median: " + medianFinder.findMedian()); // Output: 1.5

        medianFinder.addNum(3);
        System.out.println("Median: " + medianFinder.findMedian()); // Output: 2

        medianFinder.addNum(4);
        System.out.println("Median: " + medianFinder.findMedian()); // Output: 2.5

        medianFinder.addNum(5);
        System.out.println("Median: " + medianFinder.findMedian()); // Output: 3
    }
}
