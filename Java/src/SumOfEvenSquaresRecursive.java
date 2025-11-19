public class SumOfEvenSquaresRecursive {

    // Recursive method to calculate the sum of squares of even numbers from 1 to N
    public static int sumEvenSquares(int N) {
        // Base case: if N is less than 2, return 0 (no even numbers to square)
        if (N < 2) {
            return 0;
        }
        // If N is even, include its square in the sum
        if (N % 2 == 0) {
            return N * N + sumEvenSquares(N - 2);
        }
        // If N is odd, move to the next even number
        return sumEvenSquares(N - 1);
    }

    public static void main(String[] args) {

        //Summen det skal køre op til
        int N = 9; // Example value
        int result = sumEvenSquares(N);
        System.out.println("The sum of the squares of even numbers from 1 to " + N + " is: " + result);
    }
}

