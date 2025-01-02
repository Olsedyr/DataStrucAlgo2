package Sorting;

//Suppose arrays A and B are both sorted and both contain N elements. Give an
//O(logN) algorithm to find the median of A ∪ B.

//binary search approach. The idea is to partition the two arrays in such a way that
//the left half of the combined array contains all the smaller elements and
// the right half contains all the larger elements.

public class OLogNMedianOfTwoSortedArrays {

    public static double findMedianSortedArrays(int[] A, int[] B) {
        // Ensure A is the smaller array
        if (A.length > B.length) {
            int[] temp = A;
            A = B;
            B = temp;
        }

        int N1 = A.length;
        int N2 = B.length;
        int total = N1 + N2;
        int half = (total + 1) / 2;

        int left = 0, right = N1;

        while (left <= right) {
            int partitionA = (left + right) / 2;
            int partitionB = half - partitionA;

            // Handle edge cases for left and right boundaries
            int maxLeftA = (partitionA == 0) ? Integer.MIN_VALUE : A[partitionA - 1];
            int minRightA = (partitionA == N1) ? Integer.MAX_VALUE : A[partitionA];

            int maxLeftB = (partitionB == 0) ? Integer.MIN_VALUE : B[partitionB - 1];
            int minRightB = (partitionB == N2) ? Integer.MAX_VALUE : B[partitionB];

            // Check if we have found the correct partitions
            if (maxLeftA <= minRightB && maxLeftB <= minRightA) {
                // We have partitioned correctly
                if (total % 2 == 0) {
                    return (Math.max(maxLeftA, maxLeftB) + Math.min(minRightA, minRightB)) / 2.0;
                } else {
                    return Math.max(maxLeftA, maxLeftB);
                }
            } else if (maxLeftA > minRightB) {
                // We are too far on right side for partition A. Go on left side.
                right = partitionA - 1;
            } else {
                // We are too far on left side for partition A. Go on right side.
                left = partitionA + 1;
            }
        }

        throw new IllegalArgumentException("Input arrays are not sorted.");
    }

    public static void main(String[] args) {
        int[] A = {1, 3};
        int[] B = {2};

        System.out.println("Median: " + findMedianSortedArrays(A, B));
    }
}
