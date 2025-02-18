import java.util.HashMap;
import java.util.Map;

public class MostFrequentLetter {

    public static void main(String[] args) {
        String word = "banana";  // Ændr strengen efter behov
        Map<Character, Integer> frequency = new HashMap<>();

        // Kald den rekursive metode for at tælle forekomster
        countLettersRecursively(word, 0, frequency);

        // Find det mest forekommende bogstav
        char mostFrequent = '?';
        int maxCount = 0;
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostFrequent = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        System.out.println("Mest forekommende bogstav: " + mostFrequent + " med " + maxCount + " forekomster.");
    }

    /**
     * Rekursiv metode, der tæller forekomster af hvert bogstav i strengen.
     * @param word Strengen der skal gennemløbes.
     * @param index Den nuværende position i strengen.
     * @param freq Map der akkumulerer antallet af forekomster for hvert bogstav.
     */
    private static void countLettersRecursively(String word, int index, Map<Character, Integer> freq) {
        if (index >= word.length()) {
            return;
        }
        char ch = word.charAt(index);
        freq.put(ch, freq.getOrDefault(ch, 0) + 1);
        countLettersRecursively(word, index + 1, freq);
    }
}
