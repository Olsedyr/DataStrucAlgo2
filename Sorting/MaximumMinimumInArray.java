package Sorting;


//The following divide-and-conquer algorithm is proposed for finding the simultane-
//ous maximum and minimum: If there is one item, it is the maximum and minimum,
//and if there are two items, then compare them and in one comparison you can find
//the maximum and minimum. Otherwise, split the input into two halves, divided
//as evenly as possibly (if N is odd, one of the two halves will have one more element
//than the other). Recursively find the maximum and minimum of each half, and
//then in two additional comparisons produce the maximum and minimum for the
//entire problem.
//a. Suppose N is a power of 2. What is the exact number of comparisons used by
//this algorithm?
//b. Suppose N is of the form 3 · 2^k. What is the exact number of comparisons used
//by this algorithm?
//c. Modify the algorithm as follows: When N is even, but not divisible by four, split
//the input into sizes of N/2 − 1 and N/2 + 1. What is the exact number of
//comparisons used by this algorithm?





public class MaximumMinimumInArray {
    // Function to find maximum and minimum
    public static int[] findMaxMin(int[] arr, int left, int right) {
        // Base cases
        if (left == right) {
            // Only one element
            return new int[] { arr[left], arr[left] }; // {min, max}
        }
        if (right - left == 1) {
            // Two elements
            if (arr[left] < arr[right]) {
                return new int[] { arr[left], arr[right] }; // {min, max}
            } else {
                return new int[] { arr[right], arr[left] }; // {min, max}
            }
        }

        // Calculate mid point
        int mid = (left + right) / 2;

        // Recursively find max and min in both halves
        int[] leftMinMax = findMaxMin(arr, left, mid);
        int[] rightMinMax = findMaxMin(arr, mid + 1, right);

        // Combine results
        int min = Math.min(leftMinMax[0], rightMinMax[0]);
        int max = Math.max(leftMinMax[1], rightMinMax[1]);

        return new int[] { min, max }; // {min, max}
    }

    public static void main(String[] args) {
        int[] arr = { 3, 5, 1, 8, 4, 2 };

        // Finding min and max using the divide-and-conquer algorithm
        int[] result = findMaxMin(arr, 0, arr.length - 1);
        System.out.println("Minimum: " + result[0]);
        System.out.println("Maximum: " + result[1]);
    }
}
