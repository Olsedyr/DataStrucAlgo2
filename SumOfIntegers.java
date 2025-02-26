public class SumOfIntegers {

    // Recursive method to calculate the sum of integers from 1 to N
    public static int sum(int N) {
        // Base case: if N is 1, return 1
        if (N == 1) {
            return 1;
        }
        // Recursive case: sum of N and the sum of integers from 1 to N-1
        return N + sum(N - 1);
    }

    public static void main(String[] args) {
        //Summen op til N
        int N = 5; // Example value
        int result = sum(N);
        System.out.println("The sum of integers from 1 to " + N + " is: " + result);
    }
}