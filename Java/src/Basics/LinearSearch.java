package Basics;

/**
 * Linear Search - Simpel søgealgoritme der gennemgår alle elementer
 * Tidskompleksitet: O(n)
 * Rumkompleksitet: O(1)
 */
public class LinearSearch {

    public static int linearSearch(int[] arr, int target) {
        // Gennemgå alle elementer
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i; // Returner index hvis fundet
            }
        }
        return -1; // Returner -1 hvis ikke fundet
    }

    // Udvidet version der returnerer alle forekomster
    public static void linearSearchAll(int[] arr, int target) {
        boolean found = false;
        System.out.print("Element fundet på index(es): ");

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                System.out.print(i + " ");
                found = true;
            }
        }

        if (!found) {
            System.out.print("ingen");
        }
        System.out.println();
    }

    // Version der tæller antal sammenligninger
    public static int linearSearchWithCount(int[] arr, int target) {
        int comparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                System.out.println("Sammenligninger brugt: " + comparisons);
                return i;
            }
        }

        System.out.println("Sammenligninger brugt: " + comparisons);
        return -1;
    }

    // Hjælpemetode til at printe søgeproces
    public static void printSearchProcess(int[] arr, int target) {
        System.out.println("Søgeproces for " + target + ":");

        for (int i = 0; i < arr.length; i++) {
            System.out.print("Step " + (i+1) + ": arr[" + i + "]=" + arr[i]);

            if (arr[i] == target) {
                System.out.println(" ✓ FUNDET!");
                return;
            }
            System.out.println(" ✗ ikke match");
        }
        System.out.println("✗ IKKE FUNDET");
    }

    // Main metode til at teste Linear Search
    public static void main(String[] args) {
        System.out.println("=== LINEAR SEARCH TEST ===\n");

        // Test array (BEHØVER IKKE VÆRE SORTERET!)
        int[] numbers = {10, 23, 45, 70, 11, 15, 23, 70, 5, 8};

        System.out.println("Array til søgning:");
        for (int i = 0; i < numbers.length; i++) {
            System.out.println("Index " + i + ": " + numbers[i]);
        }
        System.out.println();

        // Test 1: Element der findes først i arrayet
        int target1 = 10;
        System.out.println("Test 1 - Søgning efter " + target1 + " (første element)");
        printSearchProcess(numbers, target1);

        int result1 = linearSearchWithCount(numbers, target1);
        System.out.println("Resultat: Index " + result1);

        // Test 2: Element der findes midt i arrayet
        int target2 = 11;
        System.out.println("\nTest 2 - Søgning efter " + target2 + " (midt i arrayet)");
        printSearchProcess(numbers, target2);

        int result2 = linearSearchWithCount(numbers, target2);
        System.out.println("Resultat: Index " + result2);

        // Test 3: Element der findes sidst i arrayet
        int target3 = 8;
        System.out.println("\nTest 3 - Søgning efter " + target3 + " (sidste element)");
        printSearchProcess(numbers, target3);

        int result3 = linearSearchWithCount(numbers, target3);
        System.out.println("Resultat: Index " + result3);

        // Test 4: Element der findes flere steder
        int target4 = 23;
        System.out.println("\nTest 4 - Søgning efter " + target4 + " (flere forekomster)");
        linearSearchAll(numbers, target4);

        // Test 5: Element der IKKE findes
        int target5 = 99;
        System.out.println("\nTest 5 - Søgning efter " + target5 + " (findes ikke)");
        printSearchProcess(numbers, target5);

        int result5 = linearSearchWithCount(numbers, target5);
        System.out.println("Resultat: " + result5);

        // Test 6: Tomt array
        System.out.println("\nTest 6 - Tomt array");
        int[] emptyArray = {};
        int result6 = linearSearch(emptyArray, 5);
        System.out.println("Resultat: " + result6);

        // Vis Big O information
        System.out.println("\n=== BIG O NOTATION ===");
        System.out.println("• Bedste tilfælde: O(1) - når elementet er FØRST i arrayet");
        System.out.println("• Værste tilfælde: O(n) - når elementet er SIDST eller IKKE findes");
        System.out.println("• Gennemsnit: O(n) - skal gennemgå halvdelen af elementerne i gennemsnit");
        System.out.println("• Rumkompleksitet: O(1)");
        System.out.println("\n=== FORDELE ===");
        System.out.println("• Virker på både sorterede og usorterede arrays");
        System.out.println("• Simpel at implementere og forstå");
        System.out.println("• God til små arrays");
        System.out.println("\n=== ULEMPER ===");
        System.out.println("• Ineffektiv til store arrays (brug Binary Search i stedet)");
        System.out.println("• Skal gennemgå alle elementer i værste tilfælde");
        System.out.println("\n=== SAMMENLIGNING ===");
        System.out.println("• Linear Search: O(n) - god til små/usorterede arrays");
        System.out.println("• Binary Search: O(log n) - kræver sorteret array, bedre til store arrays");
    }
}