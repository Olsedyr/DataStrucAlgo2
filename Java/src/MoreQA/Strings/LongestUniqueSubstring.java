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

    public static class SumAfToTal {

        public static boolean sumAfToTalLigParameterQuadratic(int[] arr, int l, int X) {
            for (int i = 0; i < l; i++) {
                for (int j = i + 1; j < l; j++) {
                    if (arr[i] + arr[j] == X) {
                        return true;
                    }
                }
            }
            return false;
        }

        // Version 2: Lineær tidskompleksitet
        public static boolean sumAfToTalLigParameterLinear(int[] arr, int l, int X) {
            int left = 0, right = l - 1;
            while (left < right) {
                int sum = arr[left] + arr[right];
                if (sum == X) return true;
                else if (sum < X) left++;
                else right--;
            }
            return false;
        }


        public static void main(String[] args) {
            int[] arr = {1, 2, 3, 5, 8};
            System.out.println(sumAfToTalLigParameterLinear(arr, arr.length, 10));
            System.out.println(sumAfToTalLigParameterQuadratic(arr, arr.length, 4));
        }
    }
}
