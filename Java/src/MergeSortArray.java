import java.util.Arrays;

public class MergeSortArray {

    public static void main(String[] args) {
        int[] arr = {5, 2, 9, 1, 3, 8};

        System.out.println("Original array: " + Arrays.toString(arr));

        mergeSort(arr, 0, arr.length - 1);

        System.out.println("Sorted array:   " + Arrays.toString(arr));
    }

    // ------------------------------- MERGE SORT ---------------------------------

    // Recursive merge sort
    public static void mergeSort(int[] arr, int left, int right) {

        if (left >= right) {
            return; // Base case: 1 element
        }

        int mid = left + (right - left) / 2;

        // Sort left half recursively
        mergeSort(arr, left, mid);

        // Sort right half recursively
        mergeSort(arr, mid + 1, right);

        // Merge the two sorted halves
        merge(arr, left, mid, right);
    }

    // ------------------------------- MERGE PART ---------------------------------

    private static void merge(int[] arr, int left, int mid, int right) {

        System.out.println("Merging: " + Arrays.toString(Arrays.copyOfRange(arr, left, right + 1)));

        // Sizes of the two subarrays
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Temporary arrays
        int[] L = new int[n1];
        int[] R = new int[n2];

        // Copy data to temp arrays
        for (int i = 0; i < n1; i++) L[i] = arr[left + i];
        for (int j = 0; j < n2; j++) R[j] = arr[mid + 1 + j];

        int i = 0, j = 0, k = left;

        // Merge sorted subarrays back into arr
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k++] = L[i++];
            } else {
                arr[k++] = R[j++];
            }
        }

        // Copy leftover elements (if any)
        while (i < n1) arr[k++] = L[i++];
        while (j < n2) arr[k++] = R[j++];

        System.out.println("After merge: " + Arrays.toString(Arrays.copyOfRange(arr, left, right + 1)));
    }
}
