package MoreQA.Strings;

public class StringContainsCharacter {

    // Iterative function
    public static boolean containsCharIterative(String str, char c) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }

    // Pure recursive function (no helper)
    public static boolean containsCharRecursive(String str, char c) {
        // Base case: empty string
        if (str.length() == 0) {
            return false;
        }

        // If first character matches
        if (str.charAt(0) == c) {
            return true;
        }

        // Recursive call on the rest of the string
        return containsCharRecursive(str.substring(1), c);
    }

    public static void main(String[] args) {
        String str = "Hello, world!";
        char c = 'w';

        boolean iterativeResult = containsCharIterative(str, c);
        boolean recursiveResult = containsCharRecursive(str, c);

        System.out.println("Iterative: Does the string contain the character '" + c + "'? " + iterativeResult);
        System.out.println("Recursive: Does the string contain the character '" + c + "'? " + recursiveResult);
    }
}
