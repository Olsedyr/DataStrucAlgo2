package MoreQA.Graph;

import java.util.*;

public class LongestConsecutiveSubsequence {

    // Function to find the length of the longest consecutive subsequence
    public static int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // Step 1: Insert all elements in a HashSet for O(1) look-up time
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }

        int longestStreak = 0;

        // Step 2: Iterate through the array
        for (int num : nums) {
            // If it's the start of a possible sequence (num-1 is not in the set)
            if (!numSet.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;

                // Step 3: Count the length of the consecutive subsequence
                while (numSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }

                // Step 4: Update the longest streak found
                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }

        return longestStreak;
    }

    public static void main(String[] args) {
        // Example 1: Test case where longest consecutive subsequence is 4
        int[] nums1 = {100, 4, 200, 1, 3, 2};
        System.out.println("Longest Consecutive Subsequence Length (Example 1): " + longestConsecutive(nums1)); // 4

        // Example 2: Test case with already sorted consecutive subsequence
        int[] nums2 = {1, 2, 3, 4, 5};
        System.out.println("Longest Consecutive Subsequence Length (Example 2): " + longestConsecutive(nums2)); // 5

        // Example 3: Test case with only one element
        int[] nums3 = {9};
        System.out.println("Longest Consecutive Subsequence Length (Example 3): " + longestConsecutive(nums3)); // 1

        // Example 4: Test case with no consecutive numbers
        int[] nums4 = {10, 5, 3, 100, 200};
        System.out.println("Longest Consecutive Subsequence Length (Example 4): " + longestConsecutive(nums4)); // 1
    }
}
