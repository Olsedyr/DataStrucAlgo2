public class SumOfOddSquaresRecursive {

    // Recursive method to calculate the sum of squares of odd numbers from 1 to N
    public static int sumOddSquares(int N) {
        // Base case: if N is less than 1, return 0 (no odd numbers to square)
        if (N < 1) {
            return 0;
        }
        // If N is odd, include its square in the sum
        if (N % 2 != 0) {
            return N * N + sumOddSquares(N - 2);
        }
        // If N is even, move to the next odd number
        return sumOddSquares(N - 1);
    }

    public static void main(String[] args) {
        int N = 9; // Example value
        int result = sumOddSquares(N);
        System.out.println("The sum of the squares of odd numbers from 1 to " + N + " is: " + result);
    }
}
