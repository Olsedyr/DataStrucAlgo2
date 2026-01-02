public class SumOfDigitsArray {

    public static void main(String[] args) {
        int[] digits = {1, 0, 2, 4};

        System.out.println("Iterativ sum: " + sumIterative(digits));
        System.out.println("Rekursiv sum: " + sumRecursive(digits, 0));
    }

    /**
     * Iterativ metode der summerer cifrene i et array
     */
    public static int sumIterative(int[] digits) {
        int sum = 0;

        for (int digit : digits) {
            sum += digit;
        }

        return sum;
    }

    /**
     * Rekursiv metode
     * index bruges til at gå igennem arrayet
     */
    public static int sumRecursive(int[] digits, int index) {
        // basistilfælde
        if (index == digits.length) {
            return 0;
        }

        return digits[index] + sumRecursive(digits, index + 1);
    }
}
