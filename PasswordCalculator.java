public class PasswordCalculator {

    //The password always contains six digits
    // (zero is not a valid digit).

    //Digits must appear in ascending order
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
        return factorial(n) / (factorial(k) * factorial(n - k));
    }

    public static void main(String[] args) {
        int n = 9; // Total digits available
        int k = 6; // Digits to choose

        int numberOfPasswords = binomialCoefficient(n, k);
        System.out.println("The number of possible passwords is: " + numberOfPasswords);
    }
}