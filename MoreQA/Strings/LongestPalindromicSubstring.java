package MoreQA.Strings;

public class LongestPalindromicSubstring {

    public static void main(String[] args) {
        // Test cases
        String str1 = "babad";    // "bab" or "aba"
        String str2 = "cbbd";     // "bb"
        String str3 = "a";        // "a"
        String str4 = "ac";       // "a"

        // Testing all approaches
        System.out.println("Approach 1 (Brute Force) - Longest palindromic substring for str1: " + longestPalindromeBruteForce(str1));
        System.out.println("Approach 2 (Expand Around Center) - Longest palindromic substring for str1: " + longestPalindromeExpandAroundCenter(str1));
        System.out.println("Approach 3 (Dynamic Programming) - Longest palindromic substring for str1: " + longestPalindromeDP(str1));

        System.out.println("Approach 1 (Brute Force) - Longest palindromic substring for str2: " + longestPalindromeBruteForce(str2));
        System.out.println("Approach 2 (Expand Around Center) - Longest palindromic substring for str2: " + longestPalindromeExpandAroundCenter(str2));
        System.out.println("Approach 3 (Dynamic Programming) - Longest palindromic substring for str2: " + longestPalindromeDP(str2));

        System.out.println("Approach 1 (Brute Force) - Longest palindromic substring for str3: " + longestPalindromeBruteForce(str3));
        System.out.println("Approach 2 (Expand Around Center) - Longest palindromic substring for str3: " + longestPalindromeExpandAroundCenter(str3));
        System.out.println("Approach 3 (Dynamic Programming) - Longest palindromic substring for str3: " + longestPalindromeDP(str3));

        System.out.println("Approach 1 (Brute Force) - Longest palindromic substring for str4: " + longestPalindromeBruteForce(str4));
        System.out.println("Approach 2 (Expand Around Center) - Longest palindromic substring for str4: " + longestPalindromeExpandAroundCenter(str4));
        System.out.println("Approach 3 (Dynamic Programming) - Longest palindromic substring for str4: " + longestPalindromeDP(str4));
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
                    result = subStr;
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

    // Approach 2: Expand Around Center (Efficient approach)
    // Time Complexity: O(n^2) - We expand around each character and pair of characters.
    public static String longestPalindromeExpandAroundCenter(String str) {
        int n = str.length();
        if (n == 0) return "";

        int start = 0, end = 0;
        for (int i = 0; i < n; i++) {
            int len1 = expandAroundCenter(str, i, i); // Odd-length palindrome
            int len2 = expandAroundCenter(str, i, i + 1); // Even-length palindrome
            int len = Math.max(len1, len2);

            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }

        return str.substring(start, end + 1);
    }

    // Helper function to expand around the center and return the length of palindrome
    private static int expandAroundCenter(String str, int left, int right) {
        while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
            left--;
            right++;
        }
        return right - left - 1;
    }

    // Approach 3: Dynamic Programming (DP approach to store results)
    // Time Complexity: O(n^2) - We fill an n x n DP table.
    public static String longestPalindromeDP(String str) {
        int n = str.length();
        if (n == 0) return "";

        boolean[][] dp = new boolean[n][n];
        int start = 0, maxLength = 1;

        // Every single character is a palindrome
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        // Check for 2-length substrings
        for (int i = 0; i < n - 1; i++) {
            if (str.charAt(i) == str.charAt(i + 1)) {
                dp[i][i + 1] = true;
                start = i;
                maxLength = 2;
            }
        }

        // Check for lengths greater than 2
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i < n - len + 1; i++) {
                int j = i + len - 1;
                if (str.charAt(i) == str.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    if (len > maxLength) {
                        start = i;
                        maxLength = len;
                    }
                }
            }
        }

        return str.substring(start, start + maxLength);
    }
}
