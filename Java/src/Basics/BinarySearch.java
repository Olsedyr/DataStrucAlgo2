package Basics;

/**
 * Binary Search - Søgealgoritme der kræver et sorteret array
 * Tidskompleksitet: O(log n)
 * Rumkompleksitet: O(1) for iterativ, O(log n) for rekursiv
 */
public class BinarySearch {

    // Iterativ implementation
    public static int binarySearchIterative(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2; // Undgår overflow

            // Tjek om target er midt i arrayet
            if (arr[mid] == target) {
                return mid;
            }

            // Hvis target er større, ignorer venstre halvdel
            if (arr[mid] < target) {
                left = mid + 1;
            }
            // Hvis target er mindre, ignorer højre halvdel
            else {
                right = mid - 1;
            }
        }

        // Element ikke fundet
        return -1;
    }

    // Rekursiv implementation
    public static int binarySearchRecursive(int[] arr, int target, int left, int right) {
        if (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            }

            if (arr[mid] > target) {
                return binarySearchRecursive(arr, target, left, mid - 1);
            }

            return binarySearchRecursive(arr, target, mid + 1, right);
        }

        return -1;
    }

    // Hjælpemetode til at printe søgeproces
    public static void printSearchProcess(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        int step = 1;

        System.out.println("Søgeproces for " + target + ":");

        while (left <= right) {
            int mid = left + (right - left) / 2;

            System.out.println("Step " + step + ": left=" + left + ", right=" + right +
                    ", mid=" + mid + " (arr[" + mid + "]=" + arr[mid] + ")");

            if (arr[mid] == target) {
                System.out.println("✓ Fundet på index " + mid);
                return;
            }

            if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
            step++;
        }
        System.out.println("✗ Ikke fundet");
    }

    // Main metode til at teste Binary Search
    public static void main(String[] args) {
        System.out.println("=== BINARY SEARCH TEST ===\n");

        // Sorteret array (KRAVE for binary search!)
        int[] numbers = {2, 3, 4, 10, 15, 20, 25, 30, 35, 40, 45, 50};

        System.out.println("Sorteret array:");
        for (int i = 0; i < numbers.length; i++) {
            System.out.println("Index " + i + ": " + numbers[i]);
        }
        System.out.println();

        // Test 1: Element der findes midt i
        int target1 = 20;
        System.out.println("Test 1 - Søgning efter " + target1);
        printSearchProcess(numbers, target1);

        int result1 = binarySearchIterative(numbers, target1);
        System.out.println("Iterativ resultat: Index " + result1);

        // Test 2: Element der findes i starten
        int target2 = 2;
        System.out.println("\nTest 2 - Søgning efter " + target2);
        printSearchProcess(numbers, target2);

        // Test 3: Element der findes i slutningen
        int target3 = 50;
        System.out.println("\nTest 3 - Søgning efter " + target3);
        printSearchProcess(numbers, target3);

        // Test 4: Element der IKKE findes
        int target4 = 12;
        System.out.println("\nTest 4 - Søgning efter " + target4 + " (findes ikke)");
        printSearchProcess(numbers, target4);

        // Test 5: Rekursiv søgning
        int target5 = 35;
        System.out.println("\nTest 5 - Rekursiv søgning efter " + target5);
        int resultRecursive = binarySearchRecursive(numbers, target5, 0, numbers.length - 1);
        System.out.println("Rekursiv resultat: Index " + resultRecursive);

        // Vis Big O information
        System.out.println("\n=== BIG O NOTATION ===");
        System.out.println("• Tidskompleksitet: O(log n)");
        System.out.println("• Hver step halverer søgerummet");
        System.out.println("• For array med 1000 elementer: max ~10 steps (2¹⁰ = 1024)");
        System.out.println("• For array med 1.000.000 elementer: max ~20 steps (2²⁰ ≈ 1.000.000)");
        System.out.println("• Rumkompleksitet:");
        System.out.println("  - Iterativ: O(1)");
        System.out.println("  - Rekursiv: O(log n) pga. rekursionsstack");
        System.out.println("\n=== KRITISK BETINGELSE ===");
        System.out.println("• Arrayet skal være SORTERET først!");
        System.out.println("\n=== SAMMENLIGNING ===");
        System.out.println("• Binary Search: O(log n) - meget effektiv for store arrays");
        System.out.println("• Linear Search: O(n) - bedre for små eller usorterede arrays");
    }
}