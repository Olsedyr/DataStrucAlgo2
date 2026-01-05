package Basics;

/**
 * Heap Sort - En sammenligningsbaseret sorteringsalgoritme baseret på binær heap
 * Tidskompleksitet: O(n log n) for alle tilfælde
 * Rumkompleksitet: O(1)
 */
public class HeapSort {

    public static void heapSort(int[] arr) {
        int n = arr.length;

        // Byg max heap (omarranger array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // Ekstraher elementer et ad gangen fra heap
        for (int i = n - 1; i > 0; i--) {
            // Flyt nuværende rod til slutningen
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // Kald heapify på den reducerede heap
            heapify(arr, i, 0);
        }
    }

    private static void heapify(int[] arr, int n, int i) {
        int largest = i;        // Initialiser largest som rod
        int left = 2 * i + 1;   // Venstre barn
        int right = 2 * i + 2;  // Højre barn

        // Hvis venstre barn er større end rod
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        // Hvis højre barn er større end largest indtil nu
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        // Hvis largest ikke er rod
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Rekursivt heapify på den påvirkede sub-heap
            heapify(arr, n, largest);
        }
    }

    // Hjælpemetode til at printe array
    private static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }

    // Hjælpemetode til at printe heap som træ (simpel version)
    private static void printHeap(int[] arr, int n) {
        int levels = (int) (Math.log(n) / Math.log(2)) + 1;
        System.out.println("Heap struktur:");

        for (int i = 0; i < n; i++) {
            int parent = (i - 1) / 2;
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            System.out.print("Index " + i + ": " + arr[i]);
            if (i > 0) System.out.print(" (Parent: " + arr[parent] + ")");
            if (left < n) System.out.print(" [Left: " + arr[left] + "]");
            if (right < n) System.out.print(" [Right: " + arr[right] + "]");
            System.out.println();
        }
    }

    // Main metode til at teste Heap Sort
    public static void main(String[] args) {
        System.out.println("=== HEAP SORT TEST ===\n");

        // Test 1: Normalt array
        int[] numbers1 = {12, 11, 13, 5, 6, 7};
        System.out.println("Test 1 - Usorteret array:");
        printArray(numbers1);

        heapSort(numbers1);
        System.out.println("Sorteret array:");
        printArray(numbers1);

        // Test 2: Større array
        int[] numbers2 = {4, 10, 3, 5, 1, 8, 15, 2, 7, 6};
        System.out.println("\nTest 2 - Større usorteret array:");
        printArray(numbers2);

        heapSort(numbers2);
        System.out.println("Sorteret array:");
        printArray(numbers2);

        // Test 3: Heap bygning demonstration
        int[] numbers3 = {3, 9, 2, 1, 4, 5};
        System.out.println("\nTest 3 - Heap bygning:");
        System.out.println("Original array: ");
        printArray(numbers3);

        // Byg heap først
        int n = numbers3.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(numbers3, n, i);
        }
        System.out.println("Efter heapify (max-heap): ");
        printArray(numbers3);
        printHeap(numbers3, n);

        // Vis Big O information
        System.out.println("\n=== BIG O NOTATION ===");
        System.out.println("• Tidskompleksitet: O(n log n) for alle tilfælde");
        System.out.println("• Bygge heap: O(n)");
        System.out.println("• Ekstrahere hvert element: O(log n)");
        System.out.println("• Total: O(n + n log n) = O(n log n)");
        System.out.println("• Rumkompleksitet: O(1) - in-place sortering");
        System.out.println("• Stabilitet: Ikke stabil - bevarer ikke rækkefølgen af ens elementer");
        System.out.println("\n=== FORDELE ===");
        System.out.println("• O(n log n) i alle tilfælde");
        System.out.println("• In-place sortering (ingen ekstra plads)");
        System.out.println("• God til store arrays");
    }
}