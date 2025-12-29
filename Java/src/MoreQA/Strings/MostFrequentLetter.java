package MoreQA.Strings;

import java.util.HashMap;
import java.util.Map;

public class MostFrequentLetter {

    public static void main(String[] args) {
        String word = "banana";

        Map<Character, Integer> freqIterative = countLettersIteratively(word);
        char mostFrequentIter = findMostFrequentLetter(freqIterative);
        System.out.println("Iterativt - Mest forekommende bogstav: " + mostFrequentIter + " med " + freqIterative.get(mostFrequentIter) + " forekomster.");

        Map<Character, Integer> freqRecursive = new HashMap<>();
        countLettersRecursively(word, 0, freqRecursive);
        char mostFrequentRec = findMostFrequentLetter(freqRecursive);
        System.out.println("Rekursivt - Mest forekommende bogstav: " + mostFrequentRec + " med " + freqRecursive.get(mostFrequentRec) + " forekomster.");
    }

    private static Map<Character, Integer> countLettersIteratively(String word) {
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            freq.put(ch, freq.getOrDefault(ch, 0) + 1);
        }
        return freq;
    }

    private static void countLettersRecursively(String word, int index, Map<Character, Integer> freq) {
        if (index >= word.length()) {
            return;
        }
        char ch = word.charAt(index);
        freq.put(ch, freq.getOrDefault(ch, 0) + 1);
        countLettersRecursively(word, index + 1, freq);
    }

    private static char findMostFrequentLetter(Map<Character, Integer> frequency) {
        char mostFrequent = '?';
        int maxCount = 0;
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequent = entry.getKey();
                maxCount = entry.getValue();
            }
        }
        return mostFrequent;
    }
}
