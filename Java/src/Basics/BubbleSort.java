package Basics;

/**
 * Bubble Sort - En simpel sammenligningsbaseret sorteringsalgoritme
 * Tidskompleksitet: O(n²) i værste og gennemsnitlige tilfælde, O(n) i bedste tilfælde
 * Rumkompleksitet: O(1)
 */
public class BubbleSort {

    public static void bubbleSort(int[] arr) {
        int n = arr.length;
        boolean swapped;

        for (int i = 0; i < n - 1; i++) {
            swapped = false;

            for (int j = 0; j < n - 1 - i; j++) {
                // Sammenlign nabolag og byt hvis nødvendigt
                if (arr[j] > arr[j + 1]) {
                    // Byt elementer
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }

            // Hvis ingen byt, er arrayet allerede sorteret
            if (!swapped) {
                break;
            }
        }
    }

    // Hjælpemetode til at printe array
    private static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    // Main metode til at teste Bubble Sort
    public static void main(String[] args) {
        System.out.println("=== BUBBLE SORT TEST ===\n");

        // Test 1: Normalt array
        int[] numbers1 = {64, 34, 25, 12, 22, 11, 90};
        System.out.println("Test 1 - Usorteret array:");
        printArray(numbers1);

        bubbleSort(numbers1);
        System.out.println("Sorteret array:");
        printArray(numbers1);

        // Test 2: Allerede sorteret array
        int[] numbers2 = {1, 2, 3, 4, 5, 6};
        System.out.println("\nTest 2 - Allerede sorteret array:");
        printArray(numbers2);

        bubbleSort(numbers2);
        System.out.println("Efter Bubble Sort (ingen ændringer):");
        printArray(numbers2);

        // Test 3: Omvendt sorteret array
        int[] numbers3 = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        System.out.println("\nTest 3 - Omvendt sorteret array:");
        printArray(numbers3);

        bubbleSort(numbers3);
        System.out.println("Efter Bubble Sort:");
        printArray(numbers3);

        // Test 4: Array med duplikater
        int[] numbers4 = {5, 2, 8, 2, 5, 1, 8, 1};
        System.out.println("\nTest 4 - Array med duplikater:");
        printArray(numbers4);

        bubbleSort(numbers4);
        System.out.println("Efter Bubble Sort:");
        printArray(numbers4);

        // Vis Big O information
        System.out.println("\n=== BIG O NOTATION ===");
        System.out.println("• Værste tilfælde: O(n²) - når arrayet er omvendt sorteret");
        System.out.println("• Bedste tilfælde: O(n) - når arrayet allerede er sorteret");
        System.out.println("• Gennemsnit: O(n²)");
        System.out.println("• Rumkompleksitet: O(1) - in-place sortering");
        System.out.println("• Stabilitet: Stabil - bevarer rækkefølgen af ens elementer");
    }
}