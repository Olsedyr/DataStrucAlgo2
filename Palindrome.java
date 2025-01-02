//A palindrome is a word, phrase, number, or other sequence of characters
// that reads the same forward and backward
// (ignoring spaces, punctuation, and capitalization)


 class Palindrome {
    // Method to check if a given string is a palindrome
    public static boolean isPalindrome(String str) {
        // Remove all non-alphanumeric characters and convert to lowercase
        String cleanedStr = str.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        // Initialize pointers for the start and end of the string
        int left = 0;
        int right = cleanedStr.length() - 1;

        // Check characters from both ends towards the center
        while (left < right) {
            // If characters at the current pointers do not match, it's not a palindrome
            if (cleanedStr.charAt(left) != cleanedStr.charAt(right)) {
                return false;
            }
            // Move the pointers towards the center
            left++;
            right--;
        }

        // If all characters matched, it's a palindrome
        return true;
    }

    public static void main(String[] args) {
        // Test cases
        String[] testStrings = {
                "A man, a plan, a canal, Panama",
                "racecar",
                "hello",
                "Was it a car or a cat I saw?",
                "No 'x' in Nixon"
        };

        // Check each test string and print the result
        for (String test : testStrings) {
            System.out.println("\"" + test + "\" is " + (isPalindrome(test) ? "a palindrome." : "not a palindrome."));
        }
    }
}
