public class CountPalindromeSubstrings {

    public static void main(String[] args) {
        String str1 = "aab"; // Output should be 4 ("a", "a", "b", "aa")
        String str2 = "abc"; // Output should be 3 ("a", "b", "c")
        String str3 = "aaaa"; // Output should be 10 (all substrings are palindromes)

        // Testing all approaches
        System.out.println("Approach 1 (Brute Force) - Count of palindromic substrings for str1: " + countPalindromesBruteForce(str1));
        System.out.println("Approach 2 (Expand Around Center) - Count of palindromic substrings for str1: " + countPalindromesExpandAroundCenter(str1));
        System.out.println("Approach 3 (Dynamic Programming) - Count of palindromic substrings for str1: " + countPalindromesDP(str1));

        System.out.println("Approach 1 (Brute Force) - Count of palindromic substrings for str2: " + countPalindromesBruteForce(str2));
        System.out.println("Approach 2 (Expand Around Center) - Count of palindromic substrings for str2: " + countPalindromesExpandAroundCenter(str2));
        System.out.println("Approach 3 (Dynamic Programming) - Count of palindromic substrings for str2: " + countPalindromesDP(str2));

        System.out.println("Approach 1 (Brute Force) - Count of palindromic substrings for str3: " + countPalindromesBruteForce(str3));
        System.out.println("Approach 2 (Expand Around Center) - Count of palindromic substrings for str3: " + countPalindromesExpandAroundCenter(str3));
        System.out.println("Approach 3 (Dynamic Programming) - Count of palindromic substrings for str3: " + countPalindromesDP(str3));
    }

    // Approach 1: Brute Force (Check all substrings)
    // Time Complexity: O(n^3) - We check all substrings and validate each one in O(n) time.
    public static int countPalindromesBruteForce(String str) {
        int n = str.length();
        int count = 0;

        // Generate all substrings
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                String subStr = str.substring(i, j);
                if (isPalindrome(subStr)) {
                    count++;
                }
            }
        }
        return count;
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
    public static int countPalindromesExpandAroundCenter(String str) {
        int n = str.length();
        int count = 0;

        for (int i = 0; i < n; i++) {
            // Odd-length palindromes (expand around single character)
            count += expandAroundCenter(str, i, i);
            // Even-length palindromes (expand around pair of characters)
            count += expandAroundCenter(str, i, i + 1);
        }

        return count;
    }

    // Helper function to expand around the center and count palindromes
    private static int expandAroundCenter(String str, int left, int right) {
        int count = 0;
        while (left >= 0 && right < str.length() && str.charAt(left) == str.charAt(right)) {
            count++;
            left--;
            right++;
        }
        return count;
    }

    // Approach 3: Dynamic Programming (DP approach to store results)
    // Time Complexity: O(n^2) - We fill an n x n DP table.
    public static int countPalindromesDP(String str) {
        int n = str.length();
        if (n == 0) return 0;

        boolean[][] dp = new boolean[n][n];
        int count = 0;

        // Every single character is a palindrome
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
            count++;
        }

        // Check for 2-length substrings
        for (int i = 0; i < n - 1; i++) {
            if (str.charAt(i) == str.charAt(i + 1)) {
                dp[i][i + 1] = true;
                count++;
            }
        }

        // Check for lengths greater than 2
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i < n - len + 1; i++) {
                int j = i + len - 1;
                if (str.charAt(i) == str.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    count++;
                }
            }
        }

        return count;
    }
}
