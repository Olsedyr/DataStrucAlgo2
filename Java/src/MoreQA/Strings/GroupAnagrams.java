package MoreQA.Strings;

import java.util.*;

public class GroupAnagrams {

    public static void main(String[] args) {
        // Sample input list of words
        List<String> words = Arrays.asList("cat", "dog", "tac", "god", "act", "odg");

        // Test the function to group anagrams
        System.out.println("Grouped Anagrams: ");
        groupAnagrams(words);
    }

    // Function to group anagrams together
    public static void groupAnagrams(List<String> words) {
        // A map to store groups of anagrams
        Map<String, List<String>> map = new HashMap<>();

        // Iterate through each word in the list
        for (String word : words) {
            // Convert the word to a character array, sort it, and convert it back to a string
            char[] charArray = word.toCharArray();
            Arrays.sort(charArray);
            String sortedWord = new String(charArray);

            // If the sorted word is already in the map, add the current word to its list
            // If not, create a new list and add the word
            if (!map.containsKey(sortedWord)) {
                map.put(sortedWord, new ArrayList<>());
            }
            map.get(sortedWord).add(word);
        }

        // Print all anagram groups
        for (List<String> anagramGroup : map.values()) {
            System.out.println(anagramGroup);
        }
    }
}
