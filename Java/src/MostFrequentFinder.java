import java.util.HashMap;
import java.util.Map;

public class MostFrequentFinder {

    // Iterative method for Integer array
    public static Integer mostFrequentIterative(Integer[] arr) {
        return mostFrequentIterativeGeneric(arr);
    }

    // Iterative method for Character array
    public static Character mostFrequentIterative(Character[] arr) {
        return mostFrequentIterativeGeneric(arr);
    }

    // Iterative method for String array
    public static String mostFrequentIterative(String[] arr) {
        return mostFrequentIterativeGeneric(arr);
    }

    // Generic iterative method to find the most frequent element
    private static <T> T mostFrequentIterativeGeneric(T[] arr) {
        Map<T, Integer> frequencyMap = new HashMap<>();
        T mostFrequent = null;
        int maxCount = 0;

        for (T elem : arr) {
            int count = frequencyMap.getOrDefault(elem, 0) + 1;
            frequencyMap.put(elem, count);

            if (count > maxCount) {
                maxCount = count;
                mostFrequent = elem;
            }
        }
        return mostFrequent;
    }

    // Recursive method for Integer array
    public static Integer mostFrequentRecursive(Integer[] arr) {
        return mostFrequentRecursiveGeneric(arr, 0, new HashMap<>(), null, 0);
    }

    // Recursive method for Character array
    public static Character mostFrequentRecursive(Character[] arr) {
        return mostFrequentRecursiveGeneric(arr, 0, new HashMap<>(), null, 0);
    }

    // Recursive method for String array
    public static String mostFrequentRecursive(String[] arr) {
        return mostFrequentRecursiveGeneric(arr, 0, new HashMap<>(), null, 0);
    }

    // Generic recursive method to find the most frequent element
    private static <T> T mostFrequentRecursiveGeneric(T[] arr, int index, Map<T, Integer> frequencyMap, T mostFrequent, int maxCount) {
        if (index == arr.length) {
            return mostFrequent;
        }

        T elem = arr[index];
        int count = frequencyMap.getOrDefault(elem, 0) + 1;
        frequencyMap.put(elem, count);

        if (count > maxCount) {
            mostFrequent = elem;
            maxCount = count;
        }

        return mostFrequentRecursiveGeneric(arr, index + 1, frequencyMap, mostFrequent, maxCount);
    }

    // Example usage
    public static void main(String[] args) {
        Integer[] intArray = {5, 28, 7, 25, 7, 9, 28, 11, 67, 5, 33, 28};
        Character[] charArray = {'a', 'b', 'a', 'c', 'b', 'a'};
        String[] stringArray = {"apple", "banana", "apple", "orange", "banana", "apple"};

        System.out.println("Iterative Integer: " + mostFrequentIterative(intArray));   // 28
        System.out.println("Recursive Integer: " + mostFrequentRecursive(intArray));   // 28

        System.out.println("Iterative Character: " + mostFrequentIterative(charArray)); // 'a'
        System.out.println("Recursive Character: " + mostFrequentRecursive(charArray)); // 'a'

        System.out.println("Iterative String: " + mostFrequentIterative(stringArray));  // "apple"
        System.out.println("Recursive String: " + mostFrequentRecursive(stringArray));  // "apple"
    }
}
