public class SequenceAnalyzer {

    static final int LIMIT = 10_000;

    public static void main(String[] args) {
        System.out.println("=== ITERATIV ===");
        findLongestIterative();

        System.out.println("\n=== REKURSIV ===");
        findLongestRecursive();
    }

    // -------------------------------------------------
    // SEKVENSSREGEL (KUN DENNE SKAL ÆNDRES TIL EKSAMEN)
    // -------------------------------------------------
    static long next(long n) {
        if (n % 2 == 0)  //Kan ændre regler her fx n -> n/3 ville være if (n % 3 ==0)
            return n / 2;      // even-regel
        else
            return 3 * n + 1;  // odd-regel
    }                          //Kan ændre regler her fx n -> 5n +1 ville være return 5*n+1

    // -------------------------------------------------
    // ITERATIV LØSNING
    // -------------------------------------------------
    static void findLongestIterative() {
        int maxLength = 0;
        int bestStart = 0;

        for (int i = 1; i < LIMIT; i++) {
            int length = sequenceLengthIterative(i);

            if (length > maxLength) {
                maxLength = length;
                bestStart = i;
            }
        }

        System.out.println("Starttal: " + bestStart);
        System.out.println("Længde: " + maxLength);
    }

    static int sequenceLengthIterative(long n) {
        int count = 1;

        while (n != 1) {
            n = next(n);
            count++;
        }

        return count;
    }

    // -------------------------------------------------
    // REKURSIV LØSNING
    // -------------------------------------------------
    static void findLongestRecursive() {
        int maxLength = 0;
        int bestStart = 0;

        for (int i = 1; i < LIMIT; i++) {
            int length = sequenceLengthRecursive(i);

            if (length > maxLength) {
                maxLength = length;
                bestStart = i;
            }
        }

        System.out.println("Starttal: " + bestStart);
        System.out.println("Længde: " + maxLength);
    }

    static int sequenceLengthRecursive(long n) {
        if (n == 1)
            return 1;

        return 1 + sequenceLengthRecursive(next(n));
    }
}


//Tidskompleksitet
//
//For hvert tal op til 10.000 beregnes en sekvens
//
//Længden af hver sekvens er ikke kendt på forhånd
//
//Worst case:
//
//O(n · k)



//KAN DEN FORBEDRES?
//✅ JA – med memoization (cache)
//
//Idé:
//
//Hvis du allerede har beregnet længden for et tal, så genbrug den
//
//Eksempel (teoretisk):
//
//Map<Long, Integer> cache
//
//
//Resultat:
//
//Meget hurtigere
//
//Tidskompleksitet nærmer sig O(n)
//
