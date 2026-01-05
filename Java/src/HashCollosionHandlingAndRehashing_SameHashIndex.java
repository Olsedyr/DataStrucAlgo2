import java.util.*;

public class HashCollosionHandlingAndRehashing_SameHashIndex {

    public static void main(String[] args) {
        // INPUT - Ændr kun her
        int capacity = 13;
        int commonHash = 3;
        double loadFactorThreshold = 0.5; // Standard load factor

        // Kør analysen
        analyzeUntilRehash(capacity, commonHash, loadFactorThreshold);
    }

    public static void analyzeUntilRehash(int capacity, int commonHash, double loadFactorThreshold) {
        System.out.println("QUADRATIC PROBING - IND TIL REHASHING");
        System.out.println("Kapacitet: " + capacity + ", Fælles hash: " + commonHash);
        System.out.println("Load factor threshold: " + loadFactorThreshold);
        System.out.println();

        // Arrays til at gemme data
        String[] table = new String[capacity];
        int[] probeValues = new int[capacity];
        Arrays.fill(table, "");
        Arrays.fill(probeValues, -1);

        int size = 0;
        int elementCount = 0;
        boolean canContinue = true;

        System.out.println("Indsættelsesproces og beregninger:");
        System.out.println("===================================");

        while (canContinue) {
            elementCount++;
            String elementLabel = "Y" + elementCount;

            // Quadratic probing
            int probe = 0;
            int position = -1;
            String calculation = "";

            while (probe < capacity) {
                int candidatePos = (commonHash + probe * probe) % capacity;

                if (table[candidatePos].isEmpty()) {
                    position = candidatePos;
                    // Lav beregnings-strengen
                    calculation = String.format("(%d + %d²) mod %d = %d",
                            commonHash, probe, capacity, candidatePos);
                    break;
                }
                probe++;
            }

            if (position == -1) {
                System.out.println("\n" + elementLabel + " kan ikke placeres - ingen ledige pladser!");
                canContinue = false;
            } else {
                // Tjek load factor
                double projectedLoadFactor = (double) (size + 1) / capacity;

                if (projectedLoadFactor > loadFactorThreshold) {
                    System.out.printf("\n%s: %s%n", elementLabel, calculation);
                    System.out.printf("Load factor vil blive %.3f > %.3f%n",
                            projectedLoadFactor, loadFactorThreshold);
                    System.out.println("REHASHING ER NU UUNDGÅELIGT!");
                    canContinue = false;
                } else {
                    // Indsæt elementet
                    table[position] = elementLabel;
                    probeValues[position] = probe;
                    size++;

                    System.out.printf("%s: %s → placeret på index %d%n",
                            elementLabel, calculation, position);
                }
            }
        }

        // Vis den endelige tabel
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TABEL NÅR REHASHING ER UUNDGÅELIGT");
        System.out.println("=".repeat(60));

        displayTableWithProbeInfo(table, probeValues, capacity);

        // Vis alle beregninger
        System.out.println("\nALLE BEREGNINGER:");
        System.out.println("=================");
        for (int i = 0; i < capacity; i++) {
            if (!table[i].isEmpty()) {
                int probe = probeValues[i];
                System.out.printf("%s: i=%d, (%d + %d²) mod %d = %d%n",
                        table[i], probe, commonHash, probe, capacity, i);
            }
        }

        // Vis statistik
        System.out.println("\nSTATISTIK:");
        System.out.println("Antal indsatte elementer: " + size);
        System.out.printf("Aktuel load factor: %.3f%n", (double)size/capacity);
        System.out.println("Load factor threshold: " + loadFactorThreshold);

        // Vis hvilke i-værdier der er brugt
        System.out.println("\nBrugte i-værdier:");
        for (int i = 0; i < capacity; i++) {
            if (!table[i].isEmpty()) {
                System.out.printf("i=%d: %s på index %d%n",
                        probeValues[i], table[i], i);
            }
        }
    }

    public static void displayTableWithProbeInfo(String[] table, int[] probeValues, int capacity) {
        System.out.print("Indeks:    ");
        for (int i = 0; i < capacity; i++) {
            System.out.printf("%4d", i);
        }

        System.out.print("\nElement:   ");
        for (int i = 0; i < capacity; i++) {
            if (table[i].isEmpty()) {
                System.out.print("    ");
            } else {
                System.out.printf("%4s", table[i]);
            }
        }

        System.out.print("\ni-værdi:   ");
        for (int i = 0; i < capacity; i++) {
            if (table[i].isEmpty()) {
                System.out.print("    ");
            } else {
                System.out.printf("%4d", probeValues[i]);
            }
        }
        System.out.println();
    }
}