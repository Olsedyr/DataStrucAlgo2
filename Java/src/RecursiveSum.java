public class RecursiveSum    {

    // Summerer fra 0 op til n (kun positive n)
    public static int sumPositiveUpTo(int n) {
        if (n <= 0) {
            return 0; // Base case: sum op til 0 er 0
        }
        return n + sumPositiveUpTo(n - 1);
    }

    // Summerer fra 0 ned til n (kun negative n)
    public static int sumNegativeDownTo(int n) {
        if (n >= 0) {
            return 0; // Base case: sum ned til 0 er 0
        }
        return n + sumNegativeDownTo(n + 1);
    }

    // Summerer fra 0 til n, både positive og negative n
    public static int sumUpTo(int n) {
        if (n == 0) {
            return 0; // Base case
        } else if (n > 0) {
            return n + sumUpTo(n - 1);
        } else { // n < 0
            return n + sumUpTo(n + 1);
        }
    }

    public static void main(String[] args) {
        int pos = 5;
        int neg = -5;

        System.out.println("Sum positive op til " + pos + ": " + sumPositiveUpTo(pos));
        System.out.println("Sum negative ned til " + neg + ": " + sumNegativeDownTo(neg));
        System.out.println("Sum op til " + pos + " (både positive og negative): " + sumUpTo(pos));
        System.out.println("Sum op til " + neg + " (både positive og negative): " + sumUpTo(neg));
    }
}
