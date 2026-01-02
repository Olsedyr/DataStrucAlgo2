public class ArrayRotator {

    // --- Iterative metode til rotation (helt in-place og iterativ) ---
    public static void rotateIterative(int[] arr, int k) {
        int n = arr.length;
        if (n == 0) return;
        k = k % n;
        if (k == 0) return;

        reverseIterative(arr, 0, n - 1);
        reverseIterative(arr, 0, k - 1);
        reverseIterative(arr, k, n - 1);
    }

    // --- Rekursiv metode til rotation ---
    public static void rotateRecursive(int[] arr, int k) {
        int n = arr.length;
        if (n == 0) return;
        k = k % n;
        if (k == 0) return;

        // Basistilfælde
        if (k == 0) return;

        // Roter én plads til højre (flyt sidste element forrest)
        rotateOneRightRecursive(arr, n - 1);

        // Rekursivt kald med k-1
        rotateRecursive(arr, k - 1);
    }

    // Hjælpemetode: roterer arrayet 1 plads til højre rekursivt (ved at bytte elementer)
    private static void rotateOneRightRecursive(int[] arr, int end) {
        if (end == 0) return;
        // Swap element med forrige rekursivt
        swap(arr, end, end - 1);
        rotateOneRightRecursive(arr, end - 1);
    }

    // Iterativ reverse
    public static void reverseIterative(int[] arr, int start, int end) {
        while (start < end) {
            swap(arr, start, end);
            start++;
            end--;
        }
    }

    // Rekursiv reverse
    public static void reverseRecursive(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        swap(arr, start, end);
        reverseRecursive(arr, start + 1, end - 1);
    }

    // Hjælpefunktion til at bytte to elementer
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Udskriv array
    public static void printArray(int[] arr) {
        for (int v : arr) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        int[] original = {1, 2, 3, 4, 5, 6, 7};
        int k = 3;

        // Iterativ test - O(N)
        int[] arrIter = original.clone();
        System.out.println("Original array:");
        printArray(original);

        System.out.println("Efter rotation (iterativ):");
        rotateIterative(arrIter, k);
        printArray(arrIter);

        // Rekursiv test - O(N*K)
        int[] arrRec = original.clone();
        System.out.println("Efter rotation (rekursiv):");
        rotateRecursive(arrRec, k);
        printArray(arrRec);
    }
}
