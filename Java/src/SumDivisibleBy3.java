public class SumDivisibleBy3 {

    // Rekursiv metode til at beregne summen af tal delelige med 3
    public static int sumDivisibleBy3(int N) {
        // Basis tilfælde: Hvis N er mindre end 3, returner 0
        if (N < 3) {
            return 0;
        }
        // Hvis N er delelig med 3, inkluder N i summen og kald rekursionen med N-3
        if (N % 3 == 0) {
            return N + sumDivisibleBy3(N - 3);
        }
        // Hvis N ikke er delelig med 3, kald rekursionen med N-1
        return sumDivisibleBy3(N - 1);
    }

    public static void main(String[] args) {
        int N1 = 17;
        int N2 = 14;

        int result1 = sumDivisibleBy3(N1);
        int result2 = sumDivisibleBy3(N2);

        System.out.println("Summen af tal delelige med 3 op til " + N1 + " er: " + result1); // Skal udskrive 30
        System.out.println("Summen af tal delelige med 3 op til " + N2 + " er: " + result2); // Skal udskrive 30
    }
}