public class RecurrenceSolverTN {

    public static void main(String[] args) {
        int N = 10; // Example value for N
        System.out.println("T(N) for N = " + N + " is: " + solveRecurrence(N));
        System.out.println("Big-O notation: " + determineBigONotation(N));
    }

    // Method to solve the recurrence T(N) = T(N-1) + (N-1)
    public static int solveRecurrence(int N) {
        if (N == 1) {
            return 0; // Base case: T(1) = 0
        }

        //Write the equation here
        return solveRecurrence(N / 2) + (N);
    }

    // Method to determine the Big-O notation
    public static String determineBigONotation(int N) {
        // For T(N) = T(N-1) + (N-1), the solution is O(N^2)
        return "O(N^2)";
    }
}

