import java.util.HashMap;
import java.util.Map;

public class RecursiveFindTheNumberOccurrenceNoValueGiven {

    // O(n^2) time complexity - Brute force method
    public static int mostOccurringNumberBruteForce(int[] array) {
        int maxCount = 0;
        int mostOccurring = array[0];
        for (int i = 0; i < array.length; i++) {
            int count = 0;
            for (int j = 0; j < array.length; j++) {
                if (array[j] == array[i]) {
                    count++;
                }
            }
            if (count > maxCount) {
                maxCount = count;
                mostOccurring = array[i];
            }
        }
        return mostOccurring;
    }

    // O(n) time complexity - Using HashMap (Iterative)
    public static int mostOccurringNumberIterative(int[] array) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        for (int num : array) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        int mostOccurring = array[0];
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostOccurring = entry.getKey();
            }
        }
        return mostOccurring;
    }

    // O(n) time complexity - Using HashMap (Recursive)
    public static int mostOccurringNumberRecursive(int[] array) {
        Map<Integer, Integer> frequencyMap = new HashMap<>();
        return mostOccurringNumberRecursiveHelper(array, 0, frequencyMap);
    }

    private static int mostOccurringNumberRecursiveHelper(int[] array, int index, Map<Integer, Integer> frequencyMap) {
        if (index == array.length) {
            int mostOccurring = array[0];
            int maxCount = 0;
            for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
                if (entry.getValue() > maxCount) {
                    maxCount = entry.getValue();
                    mostOccurring = entry.getKey();
                }
            }
            return mostOccurring;
        }

        int num = array[index];
        frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        return mostOccurringNumberRecursiveHelper(array, index + 1, frequencyMap);
    }

    public static void main(String[] args) {
        int[] array = {1, 3, 2, 3, 4, 3, 5, 1, 2, 3};

        // Test brute force method
        int mostOccurringBruteForce = mostOccurringNumberBruteForce(array);
        System.out.println("Most occurring number (Brute Force): " + mostOccurringBruteForce);

        // Test iterative method
        int mostOccurringIterative = mostOccurringNumberIterative(array);
        System.out.println("Most occurring number (Iterative): " + mostOccurringIterative);

        // Test recursive method
        int mostOccurringRecursive = mostOccurringNumberRecursive(array);
        System.out.println("Most occurring number (Recursive): " + mostOccurringRecursive);
    }
}