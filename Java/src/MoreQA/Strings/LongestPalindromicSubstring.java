package MoreQA.Strings;

public class LongestPalindromicSubstring {

    public static void main(String[] args) {
        // Test cases
        String str1 = "Min hat, den har tre buler. Tre buler har min hat!”";    // "bab" or "aba"
        String str2 = "Anna betalte sine regninger”";     // "bb"
        String str3 = "olfert+ roede en tur i sin kajak”";        // "a"

        // Testing all approaches
        System.out.println("Approach 1 (Brute Force) - Longest palindromic substring for str1: " + longestPalindromeBruteForce(str1));

        System.out.println("Approach 1 (Brute Force) - Longest palindromic substring for str2: " + longestPalindromeBruteForce(str2));

        System.out.println("Approach 1 (Brute Force) - Longest palindromic substring for str3: " + longestPalindromeBruteForce(str3));


    }

    // Approach 1: Brute Force (Check all substrings)
    // Time Complexity: O(n^3) - We check all substrings and validate each one in O(n) time.
    public static String longestPalindromeBruteForce(String str) {
        int n = str.length();

        String result = "";
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                String subStr = str.substring(i, j);
                if (isPalindrome(subStr) && subStr.length() > result.length()) {
                    if (subStr.length()>3) {
                        result = subStr;


                    }
                }
            }
        }
        return result;
    }

    // Helper function to check if a string is palindrome
    private static boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
}
