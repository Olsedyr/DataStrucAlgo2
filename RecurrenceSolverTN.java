public class RecurrenceSolverTN {

    public static void main(String[] args) {
        // Example recurrence relation
        String recurrence = "T(N) = 2*T(N/2) + N"; // Example recurrence relation
        int N = 10; // Example value of N

        // Solve the recurrence and determine Big-O notation
        int result = solveRecurrence(recurrence, N);
        String bigO = determineBigONotation(recurrence);

        // Output the results
        System.out.println("Recurrence: " + recurrence);
        System.out.println("T(N) for N = " + N + " is: " + result);
        System.out.println("Big-O notation: " + bigO);
    }

    // Method to solve the recurrence relation
    public static int solveRecurrence(String recurrence, int N) {
        if (N == 1) {
            return 0; // Base case: T(1) = 0
        }

        // Handle different recurrence relations
        if (recurrence.equals("T(N) = T(N-1) + (N-1)")) {
            return solveRecurrence(recurrence, N - 1) + (N - 1);
        } else if (recurrence.equals("T(N) = T(N/2) + 1")) {
            return solveRecurrence(recurrence, N / 2) + 1;
        } else if (recurrence.equals("T(N) = 2*T(N/2) + N")) {
            return 2 * solveRecurrence(recurrence, N / 2) + N;
        } else if (recurrence.equals("T(N) = T(N/2) + N")) {
            return solveRecurrence(recurrence, N / 2) + N;
        } else {
            throw new IllegalArgumentException("Unsupported recurrence relation: " + recurrence);
        }
    }

    // Method to determine the Big-O notation
    public static String determineBigONotation(String recurrence) {
        // Determine Big-O notation based on the recurrence relation
        switch (recurrence) {
            case "T(N) = T(N-1) + (N-1)":
                return "O(N^2)";
            case "T(N) = T(N/2) + 1":
                return "O(log N)";
            case "T(N) = 2*T(N/2) + N":
                return "O(N log N)";
            case "T(N) = T(N/2) + N":
                return "O(N)";
            case "T(N) = T(N/3) + N":
                return "O(N)";
            case "T(N) = T(N/4) + N":
                return "O(N)";
            case "T(N) = 2*T(N/2) + 1":
                return "O(N)";
            case "T(N) = 2*T(N/2) + N^2":
                return "O(N^2)";
            case "T(N) = T(N/2) + N^2":
                return "O(N^2)";
            case "T(N) = 3*T(N/3) + N":
                return "O(N log N)";
            case "T(N) = 3*T(N/3) + N^2":
                return "O(N^2)";
            case "T(N) = 4*T(N/2) + N":
                return "O(N log N)";
            case "T(N) = T(N/2) + N log N":
                return "O(N log N)";
            case "T(N) = T(N/2) + N^2 log N":
                return "O(N^2 log N)";
            case "T(N) = T(N-2) + N":
                return "O(N^2)";
            case "T(N) = 2*T(N/2) + log N":
                return "O(N)";
            case "T(N) = 3*T(N/2) + N^3":
                return "O(N^3)";
            case "T(N) = T(N-1) + 1":
                return "O(N)";
            case "T(N) = T(N/2) + log N":
                return "O(log N)";
            case "T(N) = T(N/3) + N log N":
                return "O(N log N)";
            case "T(N) = 2*T(N/3) + N":
                return "O(N)";
            case "T(N) = T(N/4) + N^2":
                return "O(N^2)";
            case "T(N) = T(N/5) + N^3":
                return "O(N^3)";
            case "T(N) = T(N-1) + 2*N":
                return "O(N^2)";
            case "T(N) = 4*T(N/2) + N^2":
                return "O(N^2)";
            case "T(N) = 2*T(N/2) + log(N)":
                return "O(N)";
            case "T(N) = T(N/2) + log(N^2)":
                return "O(log N)";
            case "T(N) = T(N-2) + N log N":
                return "O(N log N)";
            case "T(N) = 5*T(N/2) + N":
                return "O(N log N)";
            case "T(N) = T(N/2) + 2^N":
                return "O(2^N)";
            case "T(N) = T(N-1) + N^3":
                return "O(N^3)";
            case "T(N) = 2*T(N/2) + N^2 log N":
                return "O(N^2 log N)";
            case "T(N) = T(N/2) + N^3":
                return "O(N^3)";
            case "T(N) = T(N-1) + N^2 log N":
                return "O(N^2 log N)";
            case "T(N) = 3*T(N/3) + N log N":
                return "O(N log N)";
            case "T(N) = 2*T(N/2) + N log^2 N":
                return "O(N log^2 N)";
            case "T(N) = 2*T(N/3) + N^2 log N":
                return "O(N^2 log N)";
            case "T(N) = T(N/3) + N log^2 N":
                return "O(N log^2 N)";
            case "T(N) = 2*T(N/2) + N^2 log^2 N":
                return "O(N^2 log^2 N)";
            case "T(N) = T(N-1) + N log N":
                return "O(N log N)";
            case "T(N) = T(N/4) + N log N":
                return "O(N log N)";
            case "T(N) = T(N-1) + log N":
                return "O(N log N)";
            case "T(N) = 2*T(N/2) + N^3":
                return "O(N^3)";
            case "T(N) = T(N-1) + 2^N":
                return "O(2^N)";
            case "T(N) = 2*T(N/2) + N^4":
                return "O(N^4)";
            default:
                throw new IllegalArgumentException("Unsupported recurrence relation: " + recurrence);
        }
    }
}
