public class NthFibbonacciNumber {
    // Recursive method to calculate the nth Fibonacci number
    public static int fibonacci(int n) {
        // Base cases: if n is 0 or 1, return n
        if (n == 0 || n == 1) {
            return n;
        }
        // Recursive case: sum of the two preceding numbers
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void main(String[] args) {
        int n = 10; // Example value
        int result = fibonacci(n);
        System.out.println("The " + n + "th Fibonacci number is: " + result);
    }
}

