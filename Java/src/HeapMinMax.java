public class HeapMinMax {

    private int[] heap;
    private int size;

    public HeapMinMax(int capacity) {
        heap = new int[capacity + 1]; // index 0 ignoreres
        size = 0;
    }

    // -------------------------------
    // 1. Korrekt rekursiv tjek for min-heap (med size)
    // -------------------------------
    public static boolean isMinHeap(int[] arr, int i, int size) {
        if (i > size / 2) return true; // Alle leaf nodes er automatisk gyldige

        int left = 2 * i;
        int right = 2 * i + 1;

        // Tjek venstre barn
        boolean leftOk = (left > size) || (arr[i] <= arr[left] && isMinHeap(arr, left, size));

        // Tjek højre barn
        boolean rightOk = (right > size) || (arr[i] <= arr[right] && isMinHeap(arr, right, size));

        return leftOk && rightOk;
    }

    public static boolean isMinHeap(int[] arr, int size) {
        if (size < 1) return true; // tom heap
        return isMinHeap(arr, 1, size);
    }

    // -------------------------------
    // 2. Korrekt rekursiv tjek for max-heap (med size)
    // -------------------------------
    public static boolean isMaxHeap(int[] arr, int i, int size) {
        if (i > size / 2) return true; // Alle leaf nodes er automatisk gyldige

        int left = 2 * i;
        int right = 2 * i + 1;

        // Tjek venstre barn
        boolean leftOk = (left > size) || (arr[i] >= arr[left] && isMaxHeap(arr, left, size));

        // Tjek højre barn
        boolean rightOk = (right > size) || (arr[i] >= arr[right] && isMaxHeap(arr, right, size));

        return leftOk && rightOk;
    }

    public static boolean isMaxHeap(int[] arr, int size) {
        if (size < 1) return true; // tom heap
        return isMaxHeap(arr, 1, size);
    }

    // -----------------------------------------------
    // 3. Find første index der bryder min-heap-reglen (med size) - LEVEL ORDER
    // -----------------------------------------------
    public static int findMinHeapError(int[] arr, int size) {
        // Brug en rekursiv hjælpefunktion til level-order gennemgang
        return findMinHeapErrorLevelOrder(arr, 1, size, 1);
    }

    private static int findMinHeapErrorLevelOrder(int[] arr, int currentLevel, int size, int startIndex) {
        if (startIndex > size) return -1;

        // Beregn start og slut for dette niveau
        int levelStart = (int)Math.pow(2, currentLevel - 1);
        int levelEnd = Math.min((int)Math.pow(2, currentLevel) - 1, size);

        // Tjek alle noder på dette niveau
        for (int i = levelStart; i <= levelEnd; i++) {
            if (i > size / 2) continue; // Leaf nodes har ingen børn

            int left = 2 * i;
            int right = 2 * i + 1;

            if (left <= size && arr[i] > arr[left]) return i;
            if (right <= size && arr[i] > arr[right]) return i;
        }

        // Gå til næste niveau
        return findMinHeapErrorLevelOrder(arr, currentLevel + 1, size, levelEnd + 1);
    }

    // -----------------------------------------------
    // 4. Find første index der bryder max-heap-reglen (med size) - LEVEL ORDER
    // -----------------------------------------------
    public static int findMaxHeapError(int[] arr, int size) {
        // Brug en rekursiv hjælpefunktion til level-order gennemgang
        return findMaxHeapErrorLevelOrder(arr, 1, size, 1);
    }

    private static int findMaxHeapErrorLevelOrder(int[] arr, int currentLevel, int size, int startIndex) {
        if (startIndex > size) return -1;

        // Beregn start og slut for dette niveau
        int levelStart = (int)Math.pow(2, currentLevel - 1);
        int levelEnd = Math.min((int)Math.pow(2, currentLevel) - 1, size);

        // Tjek alle noder på dette niveau
        for (int i = levelStart; i <= levelEnd; i++) {
            if (i > size / 2) continue; // Leaf nodes har ingen børn

            int left = 2 * i;
            int right = 2 * i + 1;

            if (left <= size && arr[i] < arr[left]) return i;
            if (right <= size && arr[i] < arr[right]) return i;
        }

        // Gå til næste niveau
        return findMaxHeapErrorLevelOrder(arr, currentLevel + 1, size, levelEnd + 1);
    }

    // -------------------------------
    // 5. Find ALLE fejl i min-heap (rekursiv - uden duplikater)
    // -------------------------------
    public static java.util.Set<Integer> findAllMinHeapErrors(int[] arr, int i, int size, java.util.Set<Integer> errors) {
        if (i > size / 2) return errors; // leaf nodes har ingen børn at tjekke

        int left = 2 * i;
        int right = 2 * i + 1;

        if (left <= size && arr[i] > arr[left]) errors.add(i);
        if (right <= size && arr[i] > arr[right]) errors.add(i);

        // Tjek rekursivt i undertræerne
        findAllMinHeapErrors(arr, left, size, errors);
        findAllMinHeapErrors(arr, right, size, errors);

        return errors;
    }

    // -------------------------------
    // 6. Find ALLE fejl i max-heap (rekursiv - uden duplikater)
    // -------------------------------
    public static java.util.Set<Integer> findAllMaxHeapErrors(int[] arr, int i, int size, java.util.Set<Integer> errors) {
        if (i > size / 2) return errors; // leaf nodes har ingen børn at tjekke

        int left = 2 * i;
        int right = 2 * i + 1;

        if (left <= size && arr[i] < arr[left]) errors.add(i);
        if (right <= size && arr[i] < arr[right]) errors.add(i);

        // Tjek rekursivt i undertræerne
        findAllMaxHeapErrors(arr, left, size, errors);
        findAllMaxHeapErrors(arr, right, size, errors);

        return errors;
    }

    // -------------------------------
    // 7. Rekursiv heapify nedad (min-heap)
    // -------------------------------
    public void heapifyDownMin(int i) {
        int smallest = i;
        int left = 2 * i;
        int right = 2 * i + 1;

        if (left <= size && heap[left] < heap[smallest]) smallest = left;
        if (right <= size && heap[right] < heap[smallest]) smallest = right;

        if (smallest != i) {
            int temp = heap[i];
            heap[i] = heap[smallest];
            heap[smallest] = temp;

            heapifyDownMin(smallest); // rekursiv kald nedad
        }
    }

    // -------------------------------
    // 8. Rekursiv heapify opad (min-heap)
    // -------------------------------
    public void heapifyUpMin(int i) {
        if (i <= 1) return; // rod

        int parent = i / 2;

        if (heap[parent] > heap[i]) {
            int temp = heap[parent];
            heap[parent] = heap[i];
            heap[i] = temp;

            heapifyUpMin(parent); // rekursiv opad
        }
    }

    // -------------------------------
    // 9. Rekursiv heapify nedad (max-heap)
    // -------------------------------
    public void heapifyDownMax(int i) {
        int largest = i;
        int left = 2 * i;
        int right = 2 * i + 1;

        if (left <= size && heap[left] > heap[largest]) largest = left;
        if (right <= size && heap[right] > heap[largest]) largest = right;

        if (largest != i) {
            int temp = heap[i];
            heap[i] = heap[largest];
            heap[largest] = temp;

            heapifyDownMax(largest); // rekursiv kald nedad
        }
    }

    // -------------------------------
    // 10. Rekursiv heapify opad (max-heap)
    // -------------------------------
    public void heapifyUpMax(int i) {
        if (i <= 1) return; // rod

        int parent = i / 2;

        if (heap[parent] < heap[i]) {
            int temp = heap[parent];
            heap[parent] = heap[i];
            heap[i] = temp;

            heapifyUpMax(parent); // rekursiv opad
        }
    }

    // -------------------------------
    // 11. Indsæt nyt element (rekursiv heapify opad - min-heap)
    // -------------------------------
    public void insertMin(int value) {
        if (size >= heap.length - 1) {
            System.out.println("Heap er fuld!");
            return;
        }
        heap[++size] = value;
        heapifyUpMin(size);
    }

    // -------------------------------
    // 12. Indsæt nyt element (rekursiv heapify opad - max-heap)
    // -------------------------------
    public void insertMax(int value) {
        if (size >= heap.length - 1) {
            System.out.println("Heap er fuld!");
            return;
        }
        heap[++size] = value;
        heapifyUpMax(size);
    }

    // -------------------------------
    // 13. Build min-heap fra et array (rekursiv)
    // -------------------------------
    public void buildMinHeap(int[] arr, int start, int end) {
        if (start > end) return;
        insertMin(arr[start]);
        buildMinHeap(arr, start + 1, end);
    }

    // -------------------------------
    // 14. Build max-heap fra et array (rekursiv)
    // -------------------------------
    public void buildMaxHeap(int[] arr, int start, int end) {
        if (start > end) return;
        insertMax(arr[start]);
        buildMaxHeap(arr, start + 1, end);
    }

    // -------------------------------
    // 15. Udskriv heap-array
    // -------------------------------
    public String getHeapString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 1; i <= size; i++) {
            sb.append(heap[i]);
            if (i < size) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // -------------------------------
    // 16. Forklar min-heap fejl
    // -------------------------------
    public static String explainMinHeapError(int[] arr, int i, int size) {
        if (i <= 0 || i > size) return "Ugyldigt indeks.";

        int left = 2 * i;
        int right = 2 * i + 1;

        StringBuilder explanation = new StringBuilder();
        boolean hasError = false;

        if (left <= size && arr[i] > arr[left]) {
            explanation.append(String.format("  - Index %d (%d) > venstre barn %d (index %d)\n",
                    i, arr[i], arr[left], left));
            hasError = true;
        }
        if (right <= size && arr[i] > arr[right]) {
            explanation.append(String.format("  - Index %d (%d) > højre barn %d (index %d)\n",
                    i, arr[i], arr[right], right));
            hasError = true;
        }

        if (hasError) {
            return explanation.toString();
        }
        return "  Ingen fejl ved dette indeks.";
    }

    // -------------------------------
    // 17. Forklar max-heap fejl
    // -------------------------------
    public static String explainMaxHeapError(int[] arr, int i, int size) {
        if (i <= 0 || i > size) return "Ugyldigt indeks.";

        int left = 2 * i;
        int right = 2 * i + 1;

        StringBuilder explanation = new StringBuilder();
        boolean hasError = false;

        if (left <= size && arr[i] < arr[left]) {
            explanation.append(String.format("  - Index %d (%d) < venstre barn %d (index %d)\n",
                    i, arr[i], arr[left], left));
            hasError = true;
        }
        if (right <= size && arr[i] < arr[right]) {
            explanation.append(String.format("  - Index %d (%d) < højre barn %d (index %d)\n",
                    i, arr[i], arr[right], right));
            hasError = true;
        }

        if (hasError) {
            return explanation.toString();
        }
        return "  Ingen fejl ved dette indeks.";
    }

    // -------------------------------
    // MAIN - Med fast array du kan redigere direkte i koden
    // -------------------------------
    public static void main(String[] args) {
        System.out.println("=== HEAP ANALYSE ===\n");

        // -------------------------------------------------------------------
        // REDIGÉR KUN HER: Indtast dit array
        // Format: {0, værdi1, værdi2, ...} hvis der står 0 på index 0
        // Format: {værdi1, værdi2, ...} hvis arrayet starter direkte med værdier
        int[] ditArray = {7, 4, 28, 3, 55, 2, 51, 60, 1, 48, 58, 69, 40, 57, 36};
        // -------------------------------------------------------------------

        // Find størrelsen (ignorer index 0 hvis det er 0)
        int heapSize;
        if (ditArray.length > 0 && ditArray[0] == 0) {
            heapSize = ditArray.length - 1;
        } else {
            heapSize = ditArray.length;
        }

        // Opret et 1-baseret array til analysen
        int[] userArray = new int[heapSize + 1];
        userArray[0] = 0; // Index 0 ignoreres altid

        if (ditArray.length > 0 && ditArray[0] == 0) {
            // Kopier fra index 1 i ditArray
            System.arraycopy(ditArray, 1, userArray, 1, heapSize);
        } else {
            // Kopier hele ditArray (0-baseret konverteres til 1-baseret)
            for (int i = 0; i < heapSize; i++) {
                userArray[i + 1] = ditArray[i];
            }
        }

        // 1. Vis originalt array
        System.out.println("ORIGINALT ARRAY:");
        System.out.print("[");
        for (int i = 1; i <= heapSize; i++) {
            System.out.print(userArray[i]);
            if (i < heapSize) System.out.print(", ");
        }
        System.out.println("]\n");

        // 2. Analyse af min-heap
        System.out.println("ANALYSE AF MIN-HEAP:");
        boolean isMinHeap = isMinHeap(userArray, heapSize);
        System.out.println("Er min-heap? " + isMinHeap);

        if (!isMinHeap) {
            // Find første fejl
            int firstMinError = findMinHeapError(userArray, heapSize);
            System.out.println("\nFørste min-heap fejl ved index " + firstMinError + ":");
            System.out.print(explainMinHeapError(userArray, firstMinError, heapSize));

            // Find alle fejl
            java.util.Set<Integer> minErrorsSet = new java.util.HashSet<>();
            findAllMinHeapErrors(userArray, 1, heapSize, minErrorsSet);
            java.util.List<Integer> minErrors = new java.util.ArrayList<>(minErrorsSet);
            java.util.Collections.sort(minErrors);

            if (minErrors.size() > 1) {
                System.out.println("\nAlle min-heap fejl:");
                for (int err : minErrors) {
                    if (err != firstMinError) { // Vi har allerede vist den første
                        System.out.println("Index " + err + ":");
                        System.out.print(explainMinHeapError(userArray, err, heapSize));
                    }
                }
            }
        }
        System.out.println();

        // 3. Analyse af max-heap
        System.out.println("ANALYSE AF MAX-HEAP:");
        boolean isMaxHeap = isMaxHeap(userArray, heapSize);
        System.out.println("Er max-heap? " + isMaxHeap);

        if (!isMaxHeap) {
            // Find første fejl
            int firstMaxError = findMaxHeapError(userArray, heapSize);
            System.out.println("\nFørste max-heap fejl ved index " + firstMaxError + ":");
            System.out.print(explainMaxHeapError(userArray, firstMaxError, heapSize));

            // Find alle fejl
            java.util.Set<Integer> maxErrorsSet = new java.util.HashSet<>();
            findAllMaxHeapErrors(userArray, 1, heapSize, maxErrorsSet);
            java.util.List<Integer> maxErrors = new java.util.ArrayList<>(maxErrorsSet);
            java.util.Collections.sort(maxErrors);

            if (maxErrors.size() > 1) {
                System.out.println("\nAlle max-heap fejl:");
                for (int err : maxErrors) {
                    if (err != firstMaxError) { // Vi har allerede vist den første
                        System.out.println("Index " + err + ":");
                        System.out.print(explainMaxHeapError(userArray, err, heapSize));
                    }
                }
            }
        }
        System.out.println();

        // 4. Byg og vis korrekt min-heap
        System.out.println("KORREKT MIN-HEAP FRA DIT ARRAY:");
        HeapMinMax minHeap = new HeapMinMax(heapSize);
        minHeap.buildMinHeap(userArray, 1, heapSize);
        System.out.println(minHeap.getHeapString());

        // 5. Byg og vis korrekt max-heap
        System.out.println("\nKORREKT MAX-HEAP FRA DIT ARRAY:");
        HeapMinMax maxHeap = new HeapMinMax(heapSize);
        maxHeap.buildMaxHeap(userArray, 1, heapSize);
        System.out.println(maxHeap.getHeapString());

        System.out.println("\n=== ANALYSE FÆRDIG ===");
    }
}
//For en node med indeks i i et heap-array:
//
//Venstre barns indeks:
//2 * i
//
//Højre barns indeks:
//2 * i + 1
//
//Tjek for min-heap:
//For hver node i skal værdien være mindre end eller lig med sine børn:
//arr[i] ≤ arr[2*i] og
// arr[i] ≤ arr[2*i + 1] (hvis børnene findes)
//
//Tjek for max-heap:
//For hver node i skal værdien være større end eller lig med sine børn:
//arr[i] ≥ arr[2*i] og
//arr[i] ≥ arr[2*i + 1] (hvis børnene findes)
//
//Sådan gør du manuelt:
//
//Kig på hver node i (fra 1 til n/2) — altså alle noder, der har børn ikke leafs.
//
//Brug formlerne til at finde børnene.
//
//Tjek om node i opfylder betingelsen for min-heap eller max-heap.
