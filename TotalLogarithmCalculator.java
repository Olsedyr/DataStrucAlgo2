public class TotalLogarithmCalculator {
    // Rekursiv metode til at beregne totallogaritmen af N
    public static int logTo(int N) {
        // Basis tilfælde: Hvis N er 1, returner 0 (2^0 = 1)
        if (N == 1) {
            return 0;
        }
        // Rekursivt tilfælde: logTo(N) = 1 + logTo(N / 2)
        return 1 + logTo(N / 2);
    }

    public static void main(String[] args) {
        int N1 = 32;
        int N2 = 4096;

        int result1 = logTo(N1);
        int result2 = logTo(N2);

        System.out.println("Totallogaritmen af " + N1 + " er: " + result1); // Skal udskrive 5
        System.out.println("Totallogaritmen af " + N2 + " er: " + result2); // Skal udskrive 12
    }
}