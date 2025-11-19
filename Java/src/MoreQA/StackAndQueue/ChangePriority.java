package MoreQA.StackAndQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class ChangePriority {
    static class MaxHeap {
        private List<Integer> heap;
        private int size; // Fixed size of the heap

        // Constructor to initialize the heap with a fixed size and default values
        public MaxHeap(int size) {
            this.size = size;
            heap = new ArrayList<>(Collections.nCopies(size, -1)); // Initialize with -1
        }

        // Insert a value into the heap at a specific index
        public void insert(int value, int index) {
            if (index < 0 || index >= size) {
                throw new IllegalArgumentException("Index out of bounds");
            }
            if (heap.get(index) != -1) {
                throw new IllegalStateException("Index " + index + " is already occupied");
            }
            heap.set(index, value);
            bubbleUp(index);
        }

        // Extract the maximum value from the heap
        public int extractMax() {
            if (heap.isEmpty() || heap.get(0) == -1) {
                throw new NoSuchElementException("Heap is empty");
            }
            int max = heap.get(0);
            int last = heap.get(heap.size() - 1);
            heap.set(0, last); // Move the last element to the root
            heap.set(heap.size() - 1, -1); // Mark the last position as empty
            if (heap.get(0) != -1) {
                bubbleDown(0);
            }
            return max;
        }

        // Change the priority of an element in the heap
        public void changePriority(int fromP, int toP) {
            int index = heap.indexOf(fromP);
            if (index == -1) {
                throw new NoSuchElementException("Element with priority " + fromP + " not found");
            }
            heap.set(index, toP);
            if (toP > fromP) {
                bubbleUp(index);
            } else {
                bubbleDown(index);
            }
        }

        // Bubble up to maintain the heap property
        private void bubbleUp(int index) {
            while (index > 0) {
                int parentIndex = (index - 1) / 2;
                if (heap.get(index) <= heap.get(parentIndex)) {
                    break;
                }
                Collections.swap(heap, index, parentIndex);
                index = parentIndex;
            }
        }

        // Bubble down to maintain the heap property
        private void bubbleDown(int index) {
            int size = heap.size();
            while (index < size) {
                int leftChildIndex = 2 * index + 1;
                int rightChildIndex = 2 * index + 2;
                int largest = index;

                if (leftChildIndex < size && heap.get(leftChildIndex) > heap.get(largest)) {
                    largest = leftChildIndex;
                }
                if (rightChildIndex < size && heap.get(rightChildIndex) > heap.get(largest)) {
                    largest = rightChildIndex;
                }

                if (largest == index) {
                    break;
                }
                Collections.swap(heap, index, largest);
                index = largest;
            }
        }

        // Display the heap
        public void display() {
            System.out.println(heap);
        }
    }

    public static void main(String[] args) {
        // Create a heap with a fixed size of 10
        MaxHeap maxHeap = new MaxHeap(10);

        // Insert some elements at specific indices
        maxHeap.insert(5, 0); // Insert 5 at index 0
        maxHeap.insert(3, 1); // Insert 3 at index 1
        maxHeap.insert(8, 2); // Insert 8 at index 2
        maxHeap.insert(1, 3); // Insert 1 at index 3
        maxHeap.insert(6, 4); // Insert 6 at index 4

        System.out.println("Initial heap:");
        maxHeap.display();

        // Change priority of 3 to 10
        maxHeap.changePriority(3, 10);
        System.out.println("Heap after changing priority of 3 to 10:");
        maxHeap.display();

        // Change priority of 8 to 2
        maxHeap.changePriority(8, 2);
        System.out.println("Heap after changing priority of 8 to 2:");
        maxHeap.display();

        // Extract the maximum value
        System.out.println("Extracted max: " + maxHeap.extractMax());
        System.out.println("Heap after extracting max:");
        maxHeap.display();
    }
}