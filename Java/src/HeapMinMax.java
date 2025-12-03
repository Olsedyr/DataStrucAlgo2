public class HeapMinMax {

    // -------------------------------
    // 1. Rekursiv tjek for min-heap
    // -------------------------------
    public static boolean isMinHeap(int[] arr, int i) {
        if (i >= arr.length) return true;

        int left = 2 * i;
        int right = 2 * i + 1;

        if (left < arr.length && arr[i] > arr[left]) return false;
        if (right < arr.length && arr[i] > arr[right]) return false;

        return isMinHeap(arr, i + 1);
    }

    // Wrapper
    public static boolean isMinHeap(int[] arr) {
        return isMinHeap(arr, 1);
    }

    // -------------------------------
    // 2. Rekursiv tjek for max-heap
    // -------------------------------
    public static boolean isMaxHeap(int[] arr, int i) {
        if (i >= arr.length) return true;

        int left = 2 * i;
        int right = 2 * i + 1;

        if (left < arr.length && arr[i] < arr[left]) return false;
        if (right < arr.length && arr[i] < arr[right]) return false;

        return isMaxHeap(arr, i + 1);
    }

    // Wrapper
    public static boolean isMaxHeap(int[] arr) {
        return isMaxHeap(arr, 1);
    }

    // -----------------------------------------------
    // 3. Find første index der bryder min-heap-reglen
    // -----------------------------------------------
    public static int findMinHeapError(int[] arr, int i) {
        if (i >= arr.length) return -1;

        int left = 2 * i;
        int right = 2 * i + 1;

        if (left < arr.length && arr[i] > arr[left]) return i;
        if (right < arr.length && arr[i] > arr[right]) return i;

        return findMinHeapError(arr, i + 1);
    }

    public static int findMinHeapError(int[] arr) {
        return findMinHeapError(arr, 1);
    }

    // -----------------------------------------------
    // 4. Find første index der bryder max-heap-reglen
    // -----------------------------------------------
    public static int findMaxHeapError(int[] arr, int i) {
        if (i >= arr.length) return -1;

        int left = 2 * i;
        int right = 2 * i + 1;

        if (left < arr.length && arr[i] < arr[left]) return i;
        if (right < arr.length && arr[i] < arr[right]) return i;

        return findMaxHeapError(arr, i + 1);
    }

    public static int findMaxHeapError(int[] arr) {
        return findMaxHeapError(arr, 1);
    }

    // -------------------------------
    // 5. Rekursiv heapify (min-heap)
    // -------------------------------
    public static void heapifyMin(int[] arr, int i, int size) {
        int smallest = i;
        int left = 2 * i;
        int right = 2 * i + 1;

        if (left <= size && arr[left] < arr[smallest]) smallest = left;
        if (right <= size && arr[right] < arr[smallest]) smallest = right;

        if (smallest != i) {
            int temp = arr[i];
            arr[i] = arr[smallest];
            arr[smallest] = temp;

            heapifyMin(arr, smallest, size); // rekursiv nedad
        }
    }

    // -------------------------------
    // 6. Rekursiv heapify (max-heap)
    // -------------------------------
    public static void heapifyMax(int[] arr, int i, int size) {
        int largest = i;
        int left = 2 * i;
        int right = 2 * i + 1;

        if (left <= size && arr[left] > arr[largest]) largest = left;
        if (right <= size && arr[right] > arr[largest]) largest = right;

        if (largest != i) {
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;

            heapifyMax(arr, largest, size); // rekursiv
        }
    }

    // -------------------------------
    // 7. Build min-heap
    // -------------------------------
    public static int[] buildMinHeap(int[] arr) {
        int[] copy = arr.clone();
        int size = copy.length - 1;

        for (int i = size / 2; i >= 1; i--) {
            heapifyMin(copy, i, size);
        }
        return copy;
    }

    // -------------------------------
    // 8. Build max-heap
    // -------------------------------
    public static int[] buildMaxHeap(int[] arr) {
        int[] copy = arr.clone();
        int size = copy.length - 1;

        for (int i = size / 2; i >= 1; i--) {
            heapifyMax(copy, i, size);
        }
        return copy;
    }

    // -------------------------------
    // TEST
    // -------------------------------
    public static void main(String[] args) {
        int[] arr = {
                0, // index 0 bruges ikke
                14,17,16,28,22,65,29,31,30,26,
                23,89,64,35,32,48,47,46,45
        };

        System.out.println("Er min-heap: " + isMinHeap(arr));
        System.out.println("Er max-heap: " + isMaxHeap(arr));

        System.out.println("Min-heap fejl ved index: " + findMinHeapError(arr));
        System.out.println("Max-heap fejl ved index: " + findMaxHeapError(arr));

        int[] minH = buildMinHeap(arr);
        int[] maxH = buildMaxHeap(arr);

        System.out.println("\nKorrekt min-heap:");
        print(minH);

        System.out.println("\nKorrekt max-heap:");
        print(maxH);
    }

    // Udskriv array
    public static void print(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
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
