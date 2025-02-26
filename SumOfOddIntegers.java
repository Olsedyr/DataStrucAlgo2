public class SumOfOddIntegers {

    // Recursive method to calculate the sum of odd integers from 1 to N
    public static int sumOddIntegers(int N) {
        // Base case: if N is less than 1, return 0 (no odd numbers to sum)
        if (N < 1) {
            return 0;
        }
        // If N is odd, include it in the sum
        if (N % 2 != 0) {
            return N + sumOddIntegers(N - 2);
        }
        // If N is even, move to the next odd number
        return sumOddIntegers(N - 1);
    }

    public static void main(String[] args) {
        //Summen det skal køre op til
        int N = 9; // Example value
        int result = sumOddIntegers(N);
        System.out.println("The sum of odd integers from 1 to " + N + " is: " + result);
    }
}