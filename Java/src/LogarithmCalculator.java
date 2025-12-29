public class LogarithmCalculator {

    public static void main(String[] args) {
        // Test the logarithm methods with example values
        //O(LogN)
        System.out.println("log2(32): " + logBase2(4096));   // Output: 5
        System.out.println("log3(27): " + logBase3(27));   // Output: 3
        System.out.println("log4(64): " + logBase4(64));   // Output: 3
        System.out.println("log5(125): " + logBase5(125)); // Output: 3
        System.out.println("log6(216): " + logBase6(216)); // Output: 3
        System.out.println("log7(343): " + logBase7(343)); // Output: 3
        System.out.println("log8(512): " + logBase8(512)); // Output: 3
        System.out.println("log9(729): " + logBase9(729)); // Output: 3
        System.out.println("log10(1000): " + logBase10(1000)); // Output: 3
    }

    // Method to calculate the base 2 logarithm of N
    public static int logBase2(int N) {
        // Base case: if N is 1, the log base 2 of 1 is 0
        if (N == 1) {
            return 0;
        }
        else if (N <= 0 || (N & (N - 1)) != 0) {
            throw new IllegalArgumentException("N must be a positive power of 2");
        }
        // Recursive case: divide N by 2 and add 1 to the result
        return 1 + logBase2(N / 2);
    }

    // Method to calculate the base 3 logarithm of N
    public static int logBase3(int N) {
        // Base case: if N is 1, the log base 3 of 1 is 0
        if (N == 1) {
            return 0;
        }
        // Recursive case: divide N by 3 and add 1 to the result
        return 1 + logBase3(N / 3);
    }

    // Method to calculate the base 4 logarithm of N
    public static int logBase4(int N) {
        // Base case: if N is 1, the log base 4 of 1 is 0
        if (N == 1) {
            return 0;
        }
        // Recursive case: divide N by 4 and add 1 to the result
        return 1 + logBase4(N / 4);
    }

    // Method to calculate the base 5 logarithm of N
    public static int logBase5(int N) {
        // Base case: if N is 1, the log base 5 of 1 is 0
        if (N == 1) {
            return 0;
        }
        // Recursive case: divide N by 5 and add 1 to the result
        return 1 + logBase5(N / 5);
    }

    // Method to calculate the base 6 logarithm of N
    public static int logBase6(int N) {
        // Base case: if N is 1, the log base 6 of 1 is 0
        if (N == 1) {
            return 0;
        }
        // Recursive case: divide N by 6 and add 1 to the result
        return 1 + logBase6(N / 6);
    }

    // Method to calculate the base 7 logarithm of N
    public static int logBase7(int N) {
        // Base case: if N is 1, the log base 7 of 1 is 0
        if (N == 1) {
            return 0;
        }
        // Recursive case: divide N by 7 and add 1 to the result
        return 1 + logBase7(N / 7);
    }

    // Method to calculate the base 8 logarithm of N
    public static int logBase8(int N) {
        // Base case: if N is 1, the log base 8 of 1 is 0
        if (N == 1) {
            return 0;
        }
        // Recursive case: divide N by 8 and add 1 to the result
        return 1 + logBase8(N / 8);
    }

    // Method to calculate the base 9 logarithm of N
    public static int logBase9(int N) {
        // Base case: if N is 1, the log base 9 of 1 is 0
        if (N == 1) {
            return 0;
        }
        // Recursive case: divide N by 9 and add 1 to the result
        return 1 + logBase9(N / 9);
    }

    // Method to calculate the base 10 logarithm of N
    public static int logBase10(int N) {
        // Base case: if N is 1, the log base 10 of 1 is 0
        if (N == 1) {
            return 0;
        }
        // Recursive case: divide N by 10 and add 1 to the result
        return 1 + logBase10(N / 10);
    }
}