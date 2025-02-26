package MoreQA.Strings;

public class StringContainsCharacter {
    // Function to check if a string contains a specific character
    public static boolean containsChar(String str, char c) {
        // Iterate through each character in the string
        for (int i = 0; i < str.length(); i++) {
            // If the character matches, return true
            if (str.charAt(i) == c) {
                return true;
            }
        }
        // If no match is found, return false
        return false;
    }

    public static void main(String[] args) {
        String str = "Hello, world!";
        char c = 'w';


        // Test the containsChar function
        boolean charResult = containsChar(str, c);
        System.out.println("Does the string contain the character '" + c + "'? " + charResult);


    }
}

