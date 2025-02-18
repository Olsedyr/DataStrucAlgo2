public class SumDeleligMedTreOgOtte {

    public static int sumDeleligMedTreOgOtte(int N) {
        return sumHelper(N, 0);
    }

    private static int sumHelper(int n, int sum) {
        if (n <= 0) return sum;
        if (n % 3 == 0 || n % 8 == 0) {
            sum += n;
        }
        return sumHelper(n - 1, sum);
    }

    public static void main(String[] args) {
        System.out.println(sumDeleligMedTreOgOtte(26)); // Output: 132
    }
}
