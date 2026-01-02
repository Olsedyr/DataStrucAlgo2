public class SumOfDigitsIntegers {

    public static void main(String[] args) {
        int[] testValues = { 1024, -98765, 0, 7 };

        for (int value : testValues) {
            System.out.println("Tal: " + value);
            System.out.println("Iterativ sum: " + sumOfDigitsIterative(value));
            System.out.println("Rekursiv sum: " + sumOfDigitsRecursive(value));
            System.out.println();
        }
    }

    /**
     * Iterativ metode
     */
    public static int sumOfDigitsIterative(int n) {
        n = Math.abs(n); // håndter negative tal
        int sum = 0;

        while (n > 0) {
            sum += n % 10;
            n /= 10;
        }

        return sum;
    }

    /**
     * Rekursiv metode
     */
    public static int sumOfDigitsRecursive(int n) {
        n = Math.abs(n); // håndter negative tal

        // basistilfælde
        if (n == 0) {
            return 0;
        }

        return n % 10 + sumOfDigitsRecursive(n / 10);
    }
}
