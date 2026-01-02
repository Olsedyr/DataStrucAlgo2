public class SumOfDigitsStrings {

    public static void main(String[] args) {
        String[] testValues = {
                "1024",
                "-98765",
                "123456789012345678901234567890"
        };

        for (String value : testValues) {
            System.out.println("Tal: " + value);
            System.out.println("Iterativ sum: " + sumOfDigitsIterative(value));
            System.out.println("Rekursiv sum: " + sumOfDigitsRecursive(value));
            System.out.println();
        }
    }

    /**
     * Iterativ metode der summerer cifrene i et tal repræsenteret som String
     */
    public static int sumOfDigitsIterative(String n) {
        int sum = 0;

        for (int i = 0; i < n.length(); i++) {
            char c = n.charAt(i);

            if (Character.isDigit(c)) {
                sum += c - '0';
            }
        }

        return sum;
    }

    /**
     * Rekursiv metode der summerer cifrene i et tal repræsenteret som String
     */
    public static int sumOfDigitsRecursive(String n) {
        // Fjern minus-tegn hvis det findes
        if (n.startsWith("-")) {
            return sumOfDigitsRecursive(n.substring(1));
        }

        // Basistilfælde
        if (n.length() == 0) {
            return 0;
        }

        char firstChar = n.charAt(0);
        int digit = firstChar - '0';

        return digit + sumOfDigitsRecursive(n.substring(1));
    }
}
