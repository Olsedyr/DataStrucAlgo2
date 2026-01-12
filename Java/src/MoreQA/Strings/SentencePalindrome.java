package MoreQA.Strings;

import java.util.*;

public class SentencePalindrome {

    public static void main(String[] args) {
        // Test cases
        String sentence1 = "Min hat, den har tre buler. Tre buler har min hat!”"; // Palindrome
        String sentence2 = "Anna betalte sine regninger”";                // Not a palindrome
        String sentence3 = "rt+ roede en tur i sin kajak”";              // Palindrome

        // Testing all approaches
        System.out.println("Approach 1 (Reversed String) - Sentence 1 is palindrome: " + isPalindromeReversed(sentence1));
        //System.out.println("Approach 2 (Two-pointer technique) - Sentence 1 is palindrome: " + isPalindromeTwoPointer(sentence1));
        //System.out.println("Approach 3 (Stack-based) - Sentence 1 is palindrome: " + isPalindromeStack(sentence1));

        System.out.println("Approach 1 (Reversed String) - Sentence 2 is palindrome: " + isPalindromeReversed(sentence2));
        //System.out.println("Approach 2 (Two-pointer technique) - Sentence 2 is palindrome: " + isPalindromeTwoPointer(sentence2));
        //System.out.println("Approach 3 (Stack-based) - Sentence 2 is palindrome: " + isPalindromeStack(sentence2));

        System.out.println("Approach 1 (Reversed String) - Sentence 3 is palindrome: " + isPalindromeReversed(sentence3));
        //System.out.println("Approach 2 (Two-pointer technique) - Sentence 3 is palindrome: " + isPalindromeTwoPointer(sentence3));
        //System.out.println("Approach 3 (Stack-based) - Sentence 3 is palindrome: " + isPalindromeStack(sentence3));
    }

    // Approach 1: Reversed String (Simplest approach)
    // Time Complexity: O(n) - We clean up the string in O(n) time and reverse it in O(n) time.
    public static boolean isPalindromeReversed(String sentence) {
        String cleanedSentence = cleanString(sentence);
        String reversedSentence = new StringBuilder(cleanedSentence).reverse().toString();
        return cleanedSentence.equals(reversedSentence);
    }

    // Approach 2: Two-pointer technique (Efficient and optimal)
    // Time Complexity: O(n) - We only traverse the string once, comparing characters from both ends.
    public static boolean isPalindromeTwoPointer(String sentence) {
        String cleanedSentence = cleanString(sentence);
        int left = 0, right = cleanedSentence.length() - 1;

        while (left < right) {
            if (cleanedSentence.charAt(left) != cleanedSentence.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    // Approach 3: Stack-based approach (Useful when comparing one character at a time)
    // Time Complexity: O(n) - We traverse the string once and push/pop characters in O(1) time.
    public static boolean isPalindromeStack(String sentence) {
        String cleanedSentence = cleanString(sentence);
        Stack<Character> stack = new Stack<>();

        // Push all characters into the stack
        for (char ch : cleanedSentence.toCharArray()) {
            stack.push(ch);
        }

        // Pop characters and compare with original string
        for (char ch : cleanedSentence.toCharArray()) {
            if (ch != stack.pop()) {
                return false;
            }
        }

        return true;
    }

    // Helper function to clean up the string: remove non-alphanumeric characters and convert to lowercase
    // Time Complexity: O(n) - We process each character once.
    private static String cleanString(String sentence) {
        StringBuilder cleaned = new StringBuilder();
        for (char ch : sentence.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                cleaned.append(Character.toLowerCase(ch));
            }
        }
        return cleaned.toString();
    }
}
