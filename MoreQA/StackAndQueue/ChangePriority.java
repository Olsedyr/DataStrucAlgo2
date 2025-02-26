package MoreQA.StackAndQueue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class ChangePriority {
    static class MaxHeap {
        private List<Integer> heap;

        public MaxHeap() {
            heap = new ArrayList<>();
        }

        public void insert(int value) {
            heap.add(value);
            bubbleUp(heap.size() - 1);
        }

        public int extractMax() {
            if (heap.isEmpty()) {
                throw new NoSuchElementException("Heap is empty");
            }
            int max = heap.get(0);
            int last = heap.remove(heap.size() - 1);
            if (!heap.isEmpty()) {
                heap.set(0, last);
                bubbleDown(0);
            }
            return max;
        }

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

        public void display() {
            System.out.println(heap);
        }
    }

    public static void main(String[] args) {
        MaxHeap maxHeap = new MaxHeap();
        maxHeap.insert(5);
        maxHeap.insert(3);
        maxHeap.insert(8);
        maxHeap.insert(1);
        maxHeap.insert(6);

        System.out.println("Initial heap:");
        maxHeap.display();

        maxHeap.changePriority(3, 10);
        System.out.println("Heap after changing priority of 3 to 10:");
        maxHeap.display();

        maxHeap.changePriority(8, 2);
        System.out.println("Heap after changing priority of 8 to 2:");
        maxHeap.display();
    }
}

