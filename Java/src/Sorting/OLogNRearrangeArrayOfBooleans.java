package Sorting;
//Suppose you have an array of N elements containing only two distinct keys, true
//and false. Give an O(N) algorithm to rearrange the list so that all false elements
//precede the true elements. You may use only constant extra space.

public class OLogNRearrangeArrayOfBooleans {

    public static void rearrange(boolean[] arr) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            // Move the left pointer until we find a 'true'
            while (left < right && !arr[left]) {
                left++;
            }
            // Move the right pointer until we find a 'false'
            while (left < right && arr[right]) {
                right--;
            }
            // Swap 'true' at left and 'false' at right
            if (left < right) {
                boolean temp = arr[left];
                arr[left] = arr[right];
                arr[right] = temp;
                left++;
                right--;
            }
        }
    }

    public static void main(String[] args) {
        boolean[] arr = {true, false, true, false, false, true, true, false};

        rearrange(arr);

        // Print the rearranged array
        for (boolean b : arr) {
            System.out.print(b + " ");
        }
    }

}
