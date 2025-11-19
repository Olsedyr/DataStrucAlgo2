package MoreQA.Strings;

public class StringContainsWord {
    // Function to check if a string contains a specific word
    public static boolean containsWord(String str, String word) {
        // Use the contains method of the String class
        return str.contains(word);
    }
    public static void main(String[] args) {
        String str = "Hello, world!";

        String word = "world";

        // Test the containsWord function
        boolean wordResult = containsWord(str, word);
        System.out.println("Does the string contain the word \"" + word + "\"? " + wordResult);
    }
}

