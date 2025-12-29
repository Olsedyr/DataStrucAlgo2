package Sorting;

import java.util.Arrays;

// An anagram is a word or phrase formed by rearranging the letters
// of another word or phrase, typically using all the original
// letters exactly once.
public class StringsAnagram {

    // Sort + sammenligning
    public static boolean areAnagrams(String s1, String s2) {
        // Sort both strings
        char[] charArray1 = s1.toCharArray();
        char[] charArray2 = s2.toCharArray();
        Arrays.sort(charArray1);
        Arrays.sort(charArray2);

        // Compare sorted strings
        return Arrays.equals(charArray1, charArray2);
    }

    // Iterativ metode - tæller forekomster af bogstaver (antager små bogstaver a-z)
    public static boolean areAnagramsIterative(String s1, String s2) {
        if (s1.length() != s2.length()) return false;

        int[] counts = new int[26];
        for (int i = 0; i < s1.length(); i++) {
            counts[s1.charAt(i) - 'a']++;
            counts[s2.charAt(i) - 'a']--;
        }

        for (int count : counts) {
            if (count != 0) return false;
        }
        return true;
    }

    // Rekursiv metode - fjerner bogstaver fra s2 når de findes i s1
    public static boolean areAnagramsRecursive(String s1, String s2) {
        if (s1.length() != s2.length()) return false;
        if (s1.length() == 0) return true;

        char firstChar = s1.charAt(0);
        int indexInS2 = s2.indexOf(firstChar);

        if (indexInS2 == -1) return false;

        // Fjern førsteChar fra s2
        String s2New = s2.substring(0, indexInS2) + s2.substring(indexInS2 + 1);

        // Kald rekursivt med resten af s1 (uden første tegn) og s2New
        return areAnagramsRecursive(s1.substring(1), s2New);
    }

    public static void main(String[] args) {
        // Test cases
        String str1 = "listen";
        String str2 = "silent";

        System.out.println("Sort + Compare: " + areAnagrams(str1, str2));
        System.out.println("Iterative count: " + areAnagramsIterative(str1, str2));
        System.out.println("Recursive check: " + areAnagramsRecursive(str1, str2));

        System.out.println();

        str1 = "gram";
        str2 = "armg";

        System.out.println("Sort + Compare: " + areAnagrams(str1, str2));
        System.out.println("Iterative count: " + areAnagramsIterative(str1, str2));
        System.out.println("Recursive check: " + areAnagramsRecursive(str1, str2));
    }
}
