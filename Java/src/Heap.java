import java.util.Arrays;

public class Heap {

    // ----------------------------
    // Min-heapify rekursiv
    // ----------------------------
    public static void minHeapify(int[] arr, int n, int i) {
        int smallest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr[l] < arr[smallest])
            smallest = l;
        if (r < n && arr[r] < arr[smallest])
            smallest = r;

        if (smallest != i) {
            swap(arr, i, smallest);
            minHeapify(arr, n, smallest);
        }
    }

    // ----------------------------
    // Max-heapify rekursiv
    // ----------------------------
    public static void maxHeapify(int[] arr, int n, int i) {
        int largest = i;
        int l = 2 * i + 1;
        int r = 2 * i + 2;

        if (l < n && arr[l] > arr[largest])
            largest = l;
        if (r < n && arr[r] > arr[largest])
            largest = r;

        if (largest != i) {
            swap(arr, i, largest);
            maxHeapify(arr, n, largest);
        }
    }

    // ----------------------------
    // Byg min-heap
    // ----------------------------
    public static void buildMinHeap(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            minHeapify(arr, n, i);
        }
    }

    // ----------------------------
    // Byg max-heap
    // ----------------------------
    public static void buildMaxHeap(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            maxHeapify(arr, n, i);
        }
    }

    // ----------------------------
    // Heapsort (max-heap)
    // ----------------------------
    public static void heapSort(int[] arr) {
        int n = arr.length;

        // Byg max heap
        buildMaxHeap(arr);

        // Træk det største element ud og sæt det bagerst
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);

            // Heapify roden med reduceret heap størrelse
            maxHeapify(arr, i, 0);
        }
    }

    // ----------------------------
    // Indsæt i min-heap (bubble-up)
    // ----------------------------
    public static void minHeapInsert(int[] heap, int val, int size) {
        // size = antal elementer i heap før indsættelse (skal ikke være fuld)
        heap[size] = val;
        int i = size;

        // Bubble op
        while (i != 0 && heap[(i - 1) / 2] > heap[i]) {
            swap(heap, i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    // ----------------------------
    // Indsæt i max-heap (bubble-up)
    // ----------------------------
    public static void maxHeapInsert(int[] heap, int val, int size) {
        heap[size] = val;
        int i = size;

        // Bubble op
        while (i != 0 && heap[(i - 1) / 2] < heap[i]) {
            swap(heap, i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    // ----------------------------
    // Ekstraher min fra min-heap
    // ----------------------------
    public static int minHeapExtract(int[] heap, int size) {
        if (size <= 0) return Integer.MIN_VALUE;
        int root = heap[0];
        heap[0] = heap[size - 1];
        minHeapify(heap, size - 1, 0);
        return root;
    }

    // ----------------------------
    // Ekstraher max fra max-heap
    // ----------------------------
    public static int maxHeapExtract(int[] heap, int size) {
        if (size <= 0) return Integer.MIN_VALUE;
        int root = heap[0];
        heap[0] = heap[size - 1];
        maxHeapify(heap, size - 1, 0);
        return root;
    }

    // ----------------------------
    // Hjælpefunktion swap
    // ----------------------------
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // ----------------------------
    // Udskriv array til konsol
    // ----------------------------
    public static void printArray(String msg, int[] arr, int size) {
        System.out.print(msg);
        for (int i = 0; i < size; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    // ----------------------------
    // Main - test af funktionerne
    // ----------------------------
    public static void main(String[] args) {
        int[] arr = {17, 14, 66, 28, 22, 65, 23, 31, 30, 26, 23};
        System.out.println("Original array:");
        System.out.println(Arrays.toString(arr));

        // Min heap
        int[] minHeap = Arrays.copyOf(arr, arr.length);
        buildMinHeap(minHeap);
        printArray("Min Heap: ", minHeap, minHeap.length);

        // Eksempel indsæt i min-heap
        int minHeapSize = minHeap.length;
        minHeap = Arrays.copyOf(minHeap, minHeapSize + 1);
        minHeapInsert(minHeap, 0, minHeapSize);
        minHeapSize++;
        printArray("Min Heap after insert 0: ", minHeap, minHeapSize);

        // Ekstraher min
        int extractedMin = minHeapExtract(minHeap, minHeapSize);
        minHeapSize--;
        printArray("Min Heap after extract min (" + extractedMin + "): ", minHeap, minHeapSize);

        // Max heap
        int[] maxHeap = Arrays.copyOf(arr, arr.length);
        buildMaxHeap(maxHeap);
        printArray("Max Heap: ", maxHeap, maxHeap.length);

        // Eksempel indsæt i max-heap
        int maxHeapSize = maxHeap.length;
        maxHeap = Arrays.copyOf(maxHeap, maxHeapSize + 1);
        maxHeapInsert(maxHeap, 10, maxHeapSize);
        maxHeapSize++;
        printArray("Max Heap after insert 10: ", maxHeap, maxHeapSize);

        // Ekstraher max
        int extractedMax = maxHeapExtract(maxHeap, maxHeapSize);
        maxHeapSize--;
        printArray("Max Heap after extract max (" + extractedMax + "): ", maxHeap, maxHeapSize);

        // Heapsort
        int[] heapSortArr = Arrays.copyOf(arr, arr.length);
        heapSort(heapSortArr);
        printArray("Sorted array (heapsort): ", heapSortArr, heapSortArr.length);
    }
}
