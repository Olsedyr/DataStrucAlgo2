package MoreQA.Strings;

import java.util.HashMap;

public class SmallestWindowContainingAllCharacters {

    public static void main(String[] args) {
        String str1 = "ADOBECODEBANC";
        String str2 = "ABC";

        // Test each approach
        System.out.println("Approach 1 - Brute Force: " + smallestWindow1(str1, str2));
        System.out.println("Approach 2 - Using HashMap: " + smallestWindow2(str1, str2));
        System.out.println("Approach 3 - Sliding Window (Optimized): " + smallestWindow3(str1, str2));
    }

    // Approach 1: Brute Force
    // This approach checks all possible windows of the first string to see if they contain all characters of the second string.
    public static String smallestWindow1(String str1, String str2) {
        int n = str1.length();
        int m = str2.length();
        String result = "";

        for (int i = 0; i <= n - m; i++) {
            for (int j = i + m; j <= n; j++) {
                String subStr = str1.substring(i, j);
                if (containsAllChars(subStr, str2)) {
                    if (result.isEmpty() || subStr.length() < result.length()) {
                        result = subStr;
                    }
                }
            }
        }

        return result;
    }

    // Helper function to check if substring contains all characters of str2
    private static boolean containsAllChars(String str1, String str2) {
        HashMap<Character, Integer> map = new HashMap<>();
        for (char c : str2.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        for (char c : str1.toCharArray()) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) - 1);
            }
        }

        for (int val : map.values()) {
            if (val > 0) {
                return false;
            }
        }

        return true;
    }

    // Approach 2: Using HashMap
    // This approach uses a HashMap to track the characters in str2 and counts the occurrences of characters in str1 to find the smallest window.
    public static String smallestWindow2(String str1, String str2) {
        int n = str1.length();
        int m = str2.length();

        if (n < m) {
            return "";
        }

        HashMap<Character, Integer> str2Map = new HashMap<>();
        for (char c : str2.toCharArray()) {
            str2Map.put(c, str2Map.getOrDefault(c, 0) + 1);
        }

        int count = str2Map.size();
        HashMap<Character, Integer> str1Map = new HashMap<>();
        int left = 0, right = 0;
        int minLength = Integer.MAX_VALUE;
        String result = "";

        while (right < n) {
            char rightChar = str1.charAt(right);
            str1Map.put(rightChar, str1Map.getOrDefault(rightChar, 0) + 1);

            if (str2Map.containsKey(rightChar) && str1Map.get(rightChar).intValue() == str2Map.get(rightChar).intValue()) {
                count--;
            }

            while (count == 0) {
                char leftChar = str1.charAt(left);
                if (right - left + 1 < minLength) {
                    minLength = right - left + 1;
                    result = str1.substring(left, right + 1);
                }

                str1Map.put(leftChar, str1Map.get(leftChar) - 1);
                if (str2Map.containsKey(leftChar) && str1Map.get(leftChar).intValue() < str2Map.get(leftChar).intValue()) {
                    count++;
                }
                left++;
            }

            right++;
        }

        return result;
    }

    // Approach 3: Sliding Window (Optimized)
    // This optimized approach uses the sliding window technique to find the smallest window efficiently in O(n) time.
    public static String smallestWindow3(String str1, String str2) {
        int n = str1.length();
        int m = str2.length();

        if (n < m) {
            return "";
        }

        HashMap<Character, Integer> str2Map = new HashMap<>();
        for (char c : str2.toCharArray()) {
            str2Map.put(c, str2Map.getOrDefault(c, 0) + 1);
        }

        HashMap<Character, Integer> str1Map = new HashMap<>();
        int left = 0, right = 0;
        int minLength = Integer.MAX_VALUE;
        String result = "";
        int count = str2Map.size();

        while (right < n) {
            char rightChar = str1.charAt(right);
            str1Map.put(rightChar, str1Map.getOrDefault(rightChar, 0) + 1);

            if (str2Map.containsKey(rightChar) && str1Map.get(rightChar).intValue() == str2Map.get(rightChar).intValue()) {
                count--;
            }

            while (count == 0) {
                char leftChar = str1.charAt(left);
                if (right - left + 1 < minLength) {
                    minLength = right - left + 1;
                    result = str1.substring(left, right + 1);
                }

                str1Map.put(leftChar, str1Map.get(leftChar) - 1);
                if (str2Map.containsKey(leftChar) && str1Map.get(leftChar).intValue() < str2Map.get(leftChar).intValue()) {
                    count++;
                }
                left++;
            }

            right++;
        }

        return result;
    }
}
