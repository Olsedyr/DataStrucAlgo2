public class RecursiveFindTheNumberOccurrenceGivenAValue {
    // Recursive method to count occurrences of val in array x
    public static int getNumberEqual(int[] x, int n, int val) {
        // Base case: if n is 0, return 0
        if (n == 0) {
            return 0;
        }
        // Check if the last element is equal to val
        int count = (x[n - 1] == val) ? 1 : 0;
        // Recursive call for the rest of the array
        return count + getNumberEqual(x, n - 1, val);
    }

    public static void main(String[] args) {
        int[] x = {7, 4, 1, 4, 5, 6, 4, 8};
        int n = 8;
        int val = 4;
        System.out.println("Number of occurrences of " + val + ": " + getNumberEqual(x, n, val));
    }
}
