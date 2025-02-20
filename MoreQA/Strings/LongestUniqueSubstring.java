package MoreQA.Strings;

import java.util.HashSet;

public class LongestUniqueSubstring {
    /**
     * Finds the length of the longest substring without repeating characters.
     * Uses the sliding window technique with a HashSet.
     * Time Complexity: O(n), where n is the length of the string.
     */
    public static int longestUniqueSubstring(String s) {
        int maxLength = 0;
        int left = 0;
        HashSet<Character> set = new HashSet<>();

        for (int right = 0; right < s.length(); right++) {
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left));
                left++;
            }
            set.add(s.charAt(right));
            maxLength = Math.max(maxLength, right - left + 1);
        }
        return maxLength;
    }

    public static void main(String[] args) {
        String input = "abcabcbb";
        System.out.println("Length of longest substring without repeating characters: " + longestUniqueSubstring(input));
    }
}
