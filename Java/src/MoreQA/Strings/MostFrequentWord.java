package MoreQA.Strings;

import java.util.HashMap;
import java.util.Map;

public class MostFrequentWord {

    // Normal approach to find the most frequent word and its count
    public static Map.Entry<String, Integer> findMostFrequentWord(String input) {
        // Clean and split input into words
        String[] words = input.toLowerCase().replaceAll("[.,]", "").split(" ");

        // Initialize a HashMap to store word frequencies
        Map<String, Integer> wordCounts = new HashMap<>();

        // Iterate through words and update their counts
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        // Find the most frequent word
        String mostFrequentWord = null;
        int maxCount = 0;
        for (String word : wordCounts.keySet()) {
            if (wordCounts.get(word) > maxCount) {
                mostFrequentWord = word;
                maxCount = wordCounts.get(word);
            }
        }
        return Map.entry(mostFrequentWord, maxCount);
    }

    // Recursive approach to find the most frequent word and its count
    public static Map.Entry<String, Integer> findMostFrequentWordRecursive(String input) {
        // Clean and split input into words
        String[] words = input.toLowerCase().replaceAll("[.,]", "").split(" ");

        // Initialize a HashMap to store word frequencies
        Map<String, Integer> wordCounts = new HashMap<>();

        // Call the recursive method to count word occurrences
        countWordsRecursive(words, 0, wordCounts);

        // Find the most frequent word
        String mostFrequentWord = null;
        int maxCount = 0;
        for (String word : wordCounts.keySet()) {
            if (wordCounts.get(word) > maxCount) {
                mostFrequentWord = word;
                maxCount = wordCounts.get(word);
            }
        }

        return Map.entry(mostFrequentWord, maxCount);
    }

    // Recursive method to count words
    private static void countWordsRecursive(String[] words, int index, Map<String, Integer> wordCounts) {
        if (index == words.length) {
            return; // Base case: we've processed all words
        }

        // Get the current word and update its count in the map
        String word = words[index];
        wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);

        // Recursively process the next word
        countWordsRecursive(words, index + 1, wordCounts);
    }

    public static void main(String[] args) {
        String input = "The cattle were running back and forth, but there was no wolf to be seen, heard, or smelled, so the shepherd decided " +
                "to take a little nap in a bed of grass and early summer flowers. Soon he was awakened by a sound he had never heard before.";

        // Test the normal method
        Map.Entry<String, Integer> mostFrequentNormal = findMostFrequentWord(input);
        System.out.println("Most frequent word (normal method): " + mostFrequentNormal.getKey() + " (count: " + mostFrequentNormal.getValue() + ")");

        // Test the recursive method
        Map.Entry<String, Integer> mostFrequentRecursive = findMostFrequentWordRecursive(input);
        System.out.println("Most frequent word (recursive method): " + mostFrequentRecursive.getKey() + " (count: " + mostFrequentRecursive.getValue() + ")");
    }
}