package MoreQA.Trees;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;

public class InsertOperationAndDeleteMaxOperationAndDeleteMinoperation {

    public static void main(String[] args) {
        PriorityQueue<Integer> minHeap1 = new PriorityQueue<>();
        PriorityQueue<Integer> minHeap2 = new PriorityQueue<>();

        // Example elements
        int[] elements1 = {5, 9, 11, 14, 18, 19, 21, 33, 17, 27};
        int[] elements2 = {29, 10, 8, 1, 9, 3, 6};

        for (int elem : elements1) {
            minHeap1.add(elem);
        }

        for (int elem : elements2) {
            minHeap2.add(elem);
        }



        // insert operations on minHeap1
        minHeap1.add(7);
        minHeap1.add(15);

        System.out.println(" ");
        System.out.println("minHeap1 after insert(7) and insert(15):");
        printHeap(minHeap1);
        System.out.println(" ");

        System.out.println("Corresponding priority queue: " + minHeap1);
        System.out.println(" ");
        System.out.println(" ");




        // deleteMin operation on minHeap1
        minHeap1.poll();
        System.out.println("minHeap1 after deleteMin");
        printHeap(minHeap1);


        System.out.println("Corresponding priority queue: " + minHeap1);
        System.out.println(" ");
        System.out.println(" ");


        // deleteMin operation on minHeap2
        minHeap2.poll();

        System.out.println(" ");
        System.out.println(" ");


        System.out.println("minHeap2 after deleteMin:");
        printHeap(minHeap2);
        System.out.println("Corresponding priority queue: " + minHeap2);
    }

    public static void printHeap(PriorityQueue<Integer> heap) {
        List<Integer> list = new ArrayList<>(heap);
        list.sort(null); // Sort to maintain min-heap order

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