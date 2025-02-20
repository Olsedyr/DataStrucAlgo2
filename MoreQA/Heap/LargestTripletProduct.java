package MoreQA.Heap;

import java.util.PriorityQueue;

public class LargestTripletProduct {

    // Variables to store the largest and smallest numbers
    private int firstLargest, secondLargest, thirdLargest;
    private int firstSmallest, secondSmallest;

    public LargestTripletProduct() {
        // Initialize the largest and smallest variables
        firstLargest = secondLargest = thirdLargest = Integer.MIN_VALUE;
        firstSmallest = secondSmallest = Integer.MAX_VALUE;
    }

    // Function to add a number to the stream
    public void addNum(int num) {
        // Update the largest numbers
        if (num > firstLargest) {
            thirdLargest = secondLargest;
            secondLargest = firstLargest;
            firstLargest = num;
        } else if (num > secondLargest) {
            thirdLargest = secondLargest;
            secondLargest = num;
        } else if (num > thirdLargest) {
            thirdLargest = num;
        }

        // Update the smallest numbers
        if (num < firstSmallest) {
            secondSmallest = firstSmallest;
            firstSmallest = num;
        } else if (num < secondSmallest) {
            secondSmallest = num;
        }
    }

    // Function to find the largest triplet product
    public int getLargestTripletProduct() {
        // Calculate the largest product from either:
        // 1. The product of the three largest numbers
        // 2. The product of the two smallest (possibly negative) numbers and the largest number
        return Math.max(firstLargest * secondLargest * thirdLargest,
                firstSmallest * secondSmallest * firstLargest);
    }

    public static void main(String[] args) {
        LargestTripletProduct tripletFinder = new LargestTripletProduct();

        // Adding numbers to the data stream
        tripletFinder.addNum(1);
        tripletFinder.addNum(2);
        tripletFinder.addNum(3);
        System.out.println("Largest Triplet Product: " + tripletFinder.getLargestTripletProduct()); // Output: 6

        tripletFinder.addNum(4);
        System.out.println("Largest Triplet Product: " + tripletFinder.getLargestTripletProduct()); // Output: 24

        tripletFinder.addNum(-1);
        System.out.println("Largest Triplet Product: " + tripletFinder.getLargestTripletProduct()); // Output: 24

        tripletFinder.addNum(-10);
        System.out.println("Largest Triplet Product: " + tripletFinder.getLargestTripletProduct()); // Output: 400

        tripletFinder.addNum(0);
        System.out.println("Largest Triplet Product: " + tripletFinder.getLargestTripletProduct()); // Output: 400
    }
}
