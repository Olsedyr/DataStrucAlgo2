import java.util.HashMap;
import java.util.Map;

public class ExerciseOne {

    public static String findMostFrequentWord(String input) {
        // Clean and split input into words
        String[] words = input.toLowerCase().replaceAll("[.,]", "").split(" ");

        // Count word occurrences
        Map<String, Integer> wordCounts = new HashMap<>();
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

        return mostFrequentWord;


    }

    public static void main(String[] args) {
        String input = "The cattle were running back and forth, but there was no wolf to be seen, heard, or smelled, so the shepherd decided " +
                "to take a little nap in a bed of grass and early summer flowers. Soon he was awakened by a sound he had never heard before.";

        System.out.println(findMostFrequentWord(input));
    }
}
