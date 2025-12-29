public class WhoIsFasterIterativORRecursive {

    static long iterativeCount = 0;
    static long recursiveCount = 0;

    // Iterativ metode: tæller ned fra n til 0 med step 1
    public static void iterativeCountDown(long n) {
        for (long i = n; i >= 0; i--) {
            iterativeCount++;
        }
    }

    // Rekursiv metode: tæller ned fra n til 0 med step stepSize
    public static void recursiveCountDown(long n, long stepSize) {
        recursiveCount++;
        if (n <= 0) {
            return;
        }
        recursiveCountDown(n - stepSize, stepSize);
    }

    public static void main(String[] args) {
        // Test iterativt på 49
        long iterativeStart = System.currentTimeMillis();
        iterativeCountDown(2_000_000_000L);
        long iterativeEnd = System.currentTimeMillis();

        System.out.println("Iterativ metode:");
        System.out.println("Antal gennemløb: " + iterativeCount);
        System.out.println("Tid (ms): " + (iterativeEnd - iterativeStart));

        // Test rekursivt på stort tal med step 1 million
        long largeNumber = 2_075_316_483L;
        long stepSize = 1_000_000L;

        long recursiveStart = System.currentTimeMillis();
        recursiveCountDown(largeNumber, stepSize);
        long recursiveEnd = System.currentTimeMillis();

        System.out.println("\nRekursiv metode (step size = " + stepSize + "):");
        System.out.println("Antal gennemløb: " + recursiveCount);
        System.out.println("Tid (ms): " + (recursiveEnd - recursiveStart));
    }
}


//Hvis du sammenligner samme antal trin: fx både tæller til 2.000 loops, så vil den iterative
// næsten altid være hurtigere end den rekursive i Java (pga. funktionstack overhead).
// gælder generelt, at iteration i Java er mere effektivt end rekursion, især når rekursionen ikke er tail-call-optimeret (Java gør det ikke).