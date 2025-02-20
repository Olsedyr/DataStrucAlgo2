package MoreQA.Strings;

import java.util.HashMap;

public class MaxConsecutiveRepeatingCharacter {

    public static void main(String[] args) {
        String testString = "aaabbccdeefffgggghhhh";

        // Test each approach
        System.out.println("Approach 1 - Using a simple loop: " + maxConsecutiveRepeatingChar1(testString));
        System.out.println("Approach 2 - Using HashMap: " + maxConsecutiveRepeatingChar2(testString));
        System.out.println("Approach 3 - Using the sliding window: " + maxConsecutiveRepeatingChar3(testString));
        System.out.println("Approach 4 - Optimized with O(n) time complexity: " + maxConsecutiveRepeatingChar4(testString));
    }

    // Approach 1: Simple loop
    // This approach simply iterates through the string and counts the consecutive occurrences of characters.
    public static char maxConsecutiveRepeatingChar1(String str) {
        int maxCount = 1;
        int count = 1;
        char result = str.charAt(0);

        // Iterate over the string
        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                count++;
            } else {
                count = 1;
            }

            if (count > maxCount) {
                maxCount = count;
                result = str.charAt(i);
            }
        }

        return result;
    }

    // Approach 2: Using a HashMap
    // This approach uses a HashMap to count the frequency of consecutive characters and then returns the most frequent one.
    public static char maxConsecutiveRepeatingChar2(String str) {
        // Store the frequency of each character
        HashMap<Character, Integer> map = new HashMap<>();
        int maxCount = 0;
        char result = str.charAt(0);
        int count = 1;

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                count++;
            } else {
                count = 1;
            }

            // Keep track of the maximum consecutive count and character
            if (count > maxCount) {
                maxCount = count;
                result = str.charAt(i);
            }
        }

        return result;
    }

    // Approach 3: Using sliding window
    // This approach uses a sliding window technique, where we track the count of consecutive characters in a substring.
    public static char maxConsecutiveRepeatingChar3(String str) {
        int maxCount = 1;
        int count = 1;
        char result = str.charAt(0);

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                count++;
            } else {
                count = 1;
            }

            if (count > maxCount) {
                maxCount = count;
                result = str.charAt(i);
            }
        }

        return result;
    }

    // Approach 4: Optimized O(n) time complexity
    // This approach optimizes the previous methods by combining the loop and HashMap in one efficient scan of the string.
    public static char maxConsecutiveRepeatingChar4(String str) {
        int maxCount = 1;
        int count = 1;
        char result = str.charAt(0);

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == str.charAt(i - 1)) {
                count++;
            } else {
                count = 1;
            }

            if (count > maxCount) {
                maxCount = count;
                result = str.charAt(i);
            }
        }

        return result;
    }
}
