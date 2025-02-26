public class NaturalNumberWrittenAsXPowerToY {
    // Metode til at finde værdierne af X og Y, så Z = X^Y
    public static int[] findPowerRepresentation(int Z) {
        if (Z <= 0 || Z >= 100000) {
            return new int[]{-1, -1}; // Dummy værdi for ulovlig Z
        }

        int[] result = new int[]{-1, -1}; // Dummy værdi for ingen løsning
        for (int X = 3; X * X <= Z; X++) {
            for (int Y = 3; Math.pow(X, Y) <= Z; Y++) {
                if (Math.pow(X, Y) == Z) {
                    result[0] = X;
                    result[1] = Y;
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int Z1 = 6561;
        int Z2 = 3125;
        int Z3 = 100000; // Ulovlig værdi giver -1

        int[] result1 = findPowerRepresentation(Z1);
        int[] result2 = findPowerRepresentation(Z2);
        int[] result3 = findPowerRepresentation(Z3);

        System.out.println("Resultat for " + Z1 + ": X = " + result1[0] + ", Y = " + result1[1]);
        System.out.println("Resultat for " + Z2 + ": X = " + result2[0] + ", Y = " + result2[1]);
        System.out.println("Resultat for " + Z3 + ": X = " + result3[0] + ", Y = " + result3[1]);
    }
}

