package MoreQA.Strings;

import java.util.Arrays;

public class LongestCommonPrefix {

    public static void main(String[] args) {
        String[] arr1 = {"flower", "flow", "flight"}; // Output should be "fl"
        String[] arr2 = {"dog", "racecar", "car"}; // Output should be ""

        // Testing all approaches
        System.out.println("Approach 1 (Using Sorting) - Longest Common Prefix for arr1: " + longestCommonPrefixSorting(arr1));
        System.out.println("Approach 1 (Using Sorting) - Longest Common Prefix for arr2: " + longestCommonPrefixSorting(arr2));
    }

    // Approach 1: Using Sorting (Sort the array and find LCP between the first and last elements)
    // Time Complexity: O(n * log n + n * m) - O(n * log n) for sorting, O(n * m) for finding the LCP
    public static String longestCommonPrefixSorting(String[] strs) {
        if (strs == null || strs.length == 0) return "";

        // Sort the array of strings
        Arrays.sort(strs);

        // After sorting, the longest common prefix must be the common prefix between the first and the last string
        String first = strs[0];
        String last = strs[strs.length - 1];

        // Find the longest common prefix between the first and last strings
        int i = 0;
        while (i < first.length() && i < last.length() && first.charAt(i) == last.charAt(i)) {
            i++;
        }

        // Return the common prefix
        return first.substring(0, i);
    }
}
