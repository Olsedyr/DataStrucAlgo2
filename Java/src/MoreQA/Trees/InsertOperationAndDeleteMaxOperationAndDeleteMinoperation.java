package MoreQA.Trees;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class InsertOperationAndDeleteMaxOperationAndDeleteMinoperation {

    public static void main(String[] args) {
        // Min-heaps (default behavior of PriorityQueue)
        PriorityQueue<Integer> minHeap1 = new PriorityQueue<>();
        PriorityQueue<Integer> minHeap2 = new PriorityQueue<>();

        // Max-heap (using a custom comparator to reverse the natural order)
        PriorityQueue<Integer> maxHeap1 = new PriorityQueue<>(Collections.reverseOrder());
        PriorityQueue<Integer> maxHeap2 = new PriorityQueue<>(Collections.reverseOrder());

        // Example elements
        int[] elements1 = {42, 33, 37, 3, 15, 17, 4};
        // Another tree
        int[] elements2 = {42, 33, 37, 3, 15, 17, 4};

        // Insert elements into minHeap1 and maxHeap1
        for (int elem : elements1) {
            minHeap1.add(elem);
            maxHeap1.add(elem);
        }

        // Insert elements into minHeap2 and maxHeap2
        for (int elem : elements2) {
            minHeap2.add(elem);
            maxHeap2.add(elem);
        }

        // Insert operation on maxHeap2
        maxHeap2.add(35);

        System.out.println("Original maxHeap2");
        System.out.println(maxHeap2); // Shows the internal priority queue

        System.out.println("Original maxHeap1");
        System.out.println(maxHeap1); // Shows the internal priority queue

        System.out.println("\nmaxHeap2 after insert(35):");
        printHeap(maxHeap2);
        System.out.println("Corresponding priority queue: " + maxHeap2);

        System.out.println("\nmaxHeap1 after deleteMax:");
        maxHeap1.poll();  // Remove the largest element from maxHeap1 (deleteMax operation)
        printHeap(maxHeap1);
        System.out.println("Corresponding priority queue: " + maxHeap1);

        System.out.println("\nmaxHeap1 after deleteMax (removed element):");
        int maxElement = maxHeap1.poll();  // Remove the largest element again
        printHeap(maxHeap1);
        System.out.println("Corresponding priority queue: " + maxHeap1);
    }

    public static void printHeap(PriorityQueue<Integer> heap) {
        List<Integer> list = new ArrayList<>(heap);
        // No need to sort here since we are dealing with max-heap and PriorityQueue itself is sorted.
        int depth = (int) (Math.log(list.size()) / Math.log(2)) + 1;
        int index = 0;

        for (int i = 0; i < depth; i++) {
            int levelSize = (int) Math.pow(2, i);
            int spaces = (int) Math.pow(2, depth - i) - 1;
            printSpaces(spaces / 2);
            for (int j = 0; j < levelSize && index < list.size(); j++) {
                System.out.print(list.get(index++));
                printSpaces(spaces);
            }
            System.out.println();
        }
    }

    public static void printSpaces(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(" ");
        }
    }
}
