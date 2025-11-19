public class SumDivisibleByAnyOrAll {

    public static int sumDivisibleByAnyOrAll(int N, int[] divisors, boolean allDivisible) {
        if (N < 1) {
            return 0;
        }

        boolean conditionMet;
        if (allDivisible) {
            // Tjek om N er deleligt med ALLE divisorer
            conditionMet = true;
            for (int d : divisors) {
                if (d == 0 || N % d != 0) {
                    conditionMet = false;
                    break;
                }
            }
        } else {
            // Tjek om N er deleligt med MINDEST ÉT divisor
            conditionMet = false;
            for (int d : divisors) {
                if (d != 0 && N % d == 0) {
                    conditionMet = true;
                    break;
                }
            }
        }

        if (conditionMet) {
            System.out.println("Tallet " + N + " opfylder betingelsen");
            return N + sumDivisibleByAnyOrAll(N - 1, divisors, allDivisible);
        } else {
            return sumDivisibleByAnyOrAll(N - 1, divisors, allDivisible);
        }
    }

    public static void main(String[] args) {
        int N = 26;
        int[] divisors = {3, 8};

        System.out.println("Summen af tal op til " + N + " delelige med mindst én divisor:");
        int sumAny = sumDivisibleByAnyOrAll(N, divisors, false);
        System.out.println("Resultat: " + sumAny);

        System.out.println("\nSummen af tal op til " + N + " delelige med alle divisorer:");
        int sumAll = sumDivisibleByAnyOrAll(N, divisors, true);
        System.out.println("Resultat: " + sumAll);
    }
}
