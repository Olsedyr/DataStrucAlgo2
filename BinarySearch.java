public class BinarySearch {
    // This method performs a binary search on a sorted array
    public static int binarySearch(int[] arr, int target) {
        int left = 0; // Initialize the left boundary of the search range
        int right = arr.length - 1; // Initialize the right boundary of the search range

        // Continue searching while the search range is valid
        while (left <= right) {
            // Calculate the middle index to avoid potential overflow
            int mid = left + (right - left) / 2;

            // Check if the middle element is the target
            if (arr[mid] == target) {
                return mid; // Target found, return its index
            } else if (arr[mid] < target) {
                left = mid + 1; // Target is in the right half, adjust the left boundary
            } else {
                right = mid - 1; // Target is in the left half, adjust the right boundary
            }
        }

        return -1; // Target not found, return -1
    }

    public static void main(String[] args) {
        // Example usage of the binary search method
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9}; // Sorted array to search in
        int target = 5; // Target value to search for
        int result = binarySearch(arr, target); // Perform the binary search

        // Print the result of the search
        if (result != -1) {
            System.out.println("Element found at index: " + result);
        } else {
            System.out.println("Element not found");
        }
    }
}