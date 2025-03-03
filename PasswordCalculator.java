public class PasswordCalculator {

    // The password always contains six digits
    // (zero is not a valid digit).

    // Digits must appear in ascending order
    // (123567 is legal; 124563 is not).
    // The same digit may only occur once.

    // Function to calculate factorial of a number
    public static int factorial(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    // Function to calculate binomial coefficient (n choose k)
    public static int binomialCoefficient(int n, int k) {
        // Fix the case where k > n, return 0 because it's an invalid choice
        if (k > n) {
            return 0;
        }
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    public static void main(String[] args) {
        int n = 9; // Total digits available (1-9)
        int k = 6; // Digits to choose (6 digits for the password)

        int numberOfPasswords = binomialCoefficient(n, k);
        System.out.println("The number of possible passwords is: " + numberOfPasswords);
    }
}
