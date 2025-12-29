public class BinarySearchOnSortedArray {

    // ITERATIVE binary search
    public static int binarySearchIterative(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    // RECURSIVE binary search (public)
    public static int binarySearchRecursive(int[] arr, int target) {
        return binarySearchRecursive(arr, target, 0, arr.length - 1);
    }

    // RECURSIVE binary search (actual recursion)
    private static int binarySearchRecursive(int[] arr, int target, int left, int right) {
        // Base case: search space exhausted
        if (left > right) {
            return -1;
        }

        int mid = left + (right - left) / 2;

        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            return binarySearchRecursive(arr, target, mid + 1, right);
        } else {
            return binarySearchRecursive(arr, target, left, mid - 1);
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int target = 5;

        int iterativeResult = binarySearchIterative(arr, target);
        int recursiveResult = binarySearchRecursive(arr, target);

        if (iterativeResult != -1) {
            System.out.println("Iterative: Element found at index: " + iterativeResult);
        } else {
            System.out.println("Iterative: Element not found");
        }

        if (recursiveResult != -1) {
            System.out.println("Recursive: Element found at index: " + recursiveResult);
        } else {
            System.out.println("Recursive: Element not found");
        }
    }
}
