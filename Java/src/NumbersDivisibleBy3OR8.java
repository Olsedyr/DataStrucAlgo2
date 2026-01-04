public class NumbersDivisibleBy3OR8 {
    public static int sumDeleligMedTreOgOtte(int N) {
        if (N <= 0) return 0;

        if (N % 3 == 0 || N % 8 == 0) {
            return N + sumDeleligMedTreOgOtte(N - 1);
        } else {
            return sumDeleligMedTreOgOtte(N - 1);
        }
    }

    public static void main(String[] args) {
        System.out.println(sumDeleligMedTreOgOtte(12));
    }
}
