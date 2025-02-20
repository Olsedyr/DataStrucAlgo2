package MoreQA.arrays;

import java.util.ArrayList;
import java.util.List;

public class ArrayAlgorithms {

    /**
     * Negative Marking Approach: O(N) time, O(1) space
     * This method modifies the array by marking visited indices as negative.
     * If an index is visited twice (i.e., the value is already negative), the element is a duplicate.
     */
    public static List<Integer> findDuplicatesNegativeMarking(int[] nums) {
        List<Integer> duplicates = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int index = Math.abs(nums[i]) - 1; // Convert value to index
            if (nums[index] < 0) { // If already negative, it's a duplicate
                duplicates.add(index + 1);
            } else {
                nums[index] = -nums[index]; // Mark as visited
            }
        }
        return duplicates;
    }

    /**
     * Cyclic Sort Approach: O(N) time, O(1) space
     * This method sorts numbers in place to their correct positions.
     * If an element is not in its correct position after sorting, it is a duplicate.
     */
    public static List<Integer> findDuplicatesCyclicSort(int[] nums) {
        List<Integer> duplicates = new ArrayList<>();
        int i = 0;
        while (i < nums.length) {
            if (nums[i] != nums[nums[i] - 1]) { // If not in the correct position, swap
                int temp = nums[i];
                nums[i] = nums[temp - 1];
                nums[temp - 1] = temp;
            } else {
                i++;
            }
        }
        for (i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1) { // If not in the correct position, it's a duplicate
                duplicates.add(nums[i]);
            }
        }
        return duplicates;
    }

    /**
     * Kadane's Algorithm: O(N) time, O(1) space
     * This method finds the largest sum contiguous subarray using dynamic programming.
     * It keeps track of the maximum sum ending at each index and updates the global maximum.
     */
    public static int findMaxSubarraySum(int[] nums) {
        int maxSoFar = nums[0];
        int maxEndingHere = nums[0];

        for (int i = 1; i < nums.length; i++) {
            maxEndingHere = Math.max(nums[i], maxEndingHere + nums[i]);
            maxSoFar = Math.max(maxSoFar, maxEndingHere);
        }

        return maxSoFar;
    }

    public static void main(String[] args) {
        int[] nums = {4, 3, 2, 7, 8, 2, 3, 1};
        int[] subarrayNums = {-2, 1, -3, 4, -1, 2, 1, -5, 4};

        System.out.println("Negative Marking Approach: " + findDuplicatesNegativeMarking(nums.clone()));
        System.out.println("Cyclic Sort Approach: " + findDuplicatesCyclicSort(nums.clone()));
        System.out.println("Largest Sum Contiguous Subarray (Kadane's Algorithm): " + findMaxSubarraySum(subarrayNums));
    }
}
