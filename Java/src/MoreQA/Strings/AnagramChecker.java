package MoreQA.Strings;

import java.util.Arrays;
import java.util.HashMap;

public class AnagramChecker {

    //BEst approach is approach 3 or 4 both
    // have a time complexity of O(n),
    // where n is the length of the strings.

    public static void main(String[] args) {
        String str1 = "stale";
        String str2 = "least";

        // Test each approach
        System.out.println("Approach 1 - Using sorting: " + areAnagrams1(str1, str2));
        System.out.println("Approach 2 - Using HashMap: " + areAnagrams2(str1, str2));
        System.out.println("Approach 3 - Using character count array: " + areAnagrams3(str1, str2));
        System.out.println("Approach 4 - Using frequency count: " + areAnagrams4(str1, str2));
    }

    // Approach 1: Using Sorting
    // This approach sorts both strings and compares the sorted strings.
    public static boolean areAnagrams1(String str1, String str2) {
        // If lengths are not the same, they can't be anagrams
        if (str1.length() != str2.length()) {
            return false;
        }

        // Convert the strings to char arrays, sort them, and compare
        char[] arr1 = str1.toCharArray();
        char[] arr2 = str2.toCharArray();
        Arrays.sort(arr1);
        Arrays.sort(arr2);

        return Arrays.equals(arr1, arr2);
    }

    // Approach 2: Using HashMap
    // This approach uses a HashMap to count the frequency of characters in both strings.
    public static boolean areAnagrams2(String str1, String str2) {
        // If lengths are not the same, they can't be anagrams
        if (str1.length() != str2.length()) {
            return false;
        }

        HashMap<Character, Integer> map = new HashMap<>();

        // Count characters for str1
        for (char c : str1.toCharArray()) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }

        // Subtract counts for str2
        for (char c : str2.toCharArray()) {
            if (!map.containsKey(c)) {
                return false;
            }
            map.put(c, map.get(c) - 1);
            if (map.get(c) == 0) {
                map.remove(c);
            }
        }

        // If map is empty, the strings are anagrams
        return map.isEmpty();
    }

    // Approach 3: Using character count array
    // This approach uses an integer array of size 256 (assuming ASCII characters) to count the frequency of characters.
    public static boolean areAnagrams3(String str1, String str2) {
        // If lengths are not the same, they can't be anagrams
        if (str1.length() != str2.length()) {
            return false;
        }

        int[] count = new int[256]; // Assuming ASCII characters

        // Count characters for str1
        for (char c : str1.toCharArray()) {
            count[c]++;
        }

        // Subtract counts for str2
        for (char c : str2.toCharArray()) {
            count[c]--;
            if (count[c] < 0) {
                return false; // If count goes negative, they are not anagrams
            }
        }

        return true;
    }

    // Approach 4: Using frequency count with a single loop
    // This approach counts the frequency of characters in both strings using a single loop and compares them.
    public static boolean areAnagrams4(String str1, String str2) {
        // If lengths are not the same, they can't be anagrams
        if (str1.length() != str2.length()) {
            return false;
        }

        int[] count = new int[256]; // Assuming ASCII characters

        // Compare characters of both strings and update the count array
        for (int i = 0; i < str1.length(); i++) {
            count[str1.charAt(i)]++;
            count[str2.charAt(i)]--;
        }

        // If all counts are zero, the strings are anagrams
        for (int i : count) {
            if (i != 0) {
                return false;
            }
        }

        return true;
    }
}
