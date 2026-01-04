import java.util.*;

public class TwoHashTables_CustomHashing {
    private static final int TABLE_SIZE = 16;

    public enum ProbingType {
        LINEAR, QUADRATIC
    }

    // Default hash function from the exercise
    public static int defaultHash(char key) {
        int k = getAlphabetPosition(key);
        return (11 * k) % TABLE_SIZE;
    }

    // Custom hash function - change this as needed
    public static int customHash(char key) {
        int k = getAlphabetPosition(key);
        // Example custom hash - change this formula
        return (13 * k + 5) % TABLE_SIZE;
    }

    private static int getAlphabetPosition(char c) {
        return Character.toUpperCase(c) - 'A' + 1;
    }

    public static String[] createHashTable(List<Character> keys, ProbingType probingType, boolean verbose) {
        return createHashTable(keys, probingType, verbose, "default");
    }

    public static String[] createHashTable(List<Character> keys, ProbingType probingType, boolean verbose, String hashType) {
        String[] table = new String[TABLE_SIZE];

        // Choose hash function
        java.util.function.Function<Character, Integer> hashFunc;
        String hashFormula;
        if (hashType.equalsIgnoreCase("custom")) {
            hashFunc = TwoHashTables_CustomHashing::customHash;
            hashFormula = "(13*k + 5) % 16";
            if (verbose) System.out.println("\n=== HASHTABEL MED CUSTOM HASHFUNKTION ===");
        } else {
            hashFunc = TwoHashTables_CustomHashing::defaultHash;
            hashFormula = "(11*k) % 16";
            if (verbose) System.out.println("\n=== HASHTABEL MED DEFAULT HASHFUNKTION ===");
        }

        if (verbose) {
            System.out.println("Nøgler: " + keys);
            System.out.println("Kollisionsstrategi: " + (probingType == ProbingType.LINEAR ? "Linear probing" : "Quadratic probing"));
            System.out.println("Hashformel: " + hashFormula);
            System.out.println();
        }

        for (char key : keys) {
            int alphabetPos = getAlphabetPosition(key);
            int hashIndex = hashFunc.apply(key);
            int originalIndex = hashIndex;
            int probeCount = 0;

            if (verbose) {
                System.out.print("Indsætter '" + key + "' (k=" + alphabetPos + "): ");
                if (hashType.equalsIgnoreCase("custom")) {
                    System.out.print("hash(" + key + ") = (13*" + alphabetPos + "+5)%16 = " + (13*alphabetPos+5) + "%16 = " + hashIndex);
                } else {
                    System.out.print("hash(" + key + ") = (11*" + alphabetPos + ")%16 = " + (11*alphabetPos) + "%16 = " + hashIndex);
                }
            }

            // Handle collision
            if (table[hashIndex] != null) {
                if (verbose) System.out.print(" → KOLLISION med '" + table[hashIndex] + "'!");

                if (probingType == ProbingType.LINEAR) {
                    int i = 1;
                    while (table[hashIndex] != null) {
                        hashIndex = (originalIndex + i) % TABLE_SIZE;
                        if (verbose) System.out.print(" → prøver position " + hashIndex);
                        i++;
                        probeCount++;
                        if (i > TABLE_SIZE) break;
                    }
                } else { // Quadratic probing
                    int i = 1;
                    while (table[hashIndex] != null) {
                        hashIndex = (originalIndex + i * i) % TABLE_SIZE;
                        if (verbose) System.out.print(" → prøver (h+" + i + "²)=" + ((originalIndex + i*i) % TABLE_SIZE) + " (beregnet: " + originalIndex + "+" + (i*i) + "=" + (originalIndex+i*i) + ", modulo: " + ((originalIndex+i*i) % TABLE_SIZE) + ")");
                        i++;
                        probeCount++;
                        if (i > TABLE_SIZE) break;
                    }
                }
            }

            table[hashIndex] = String.valueOf(key);

            if (verbose) {
                if (probeCount > 0) {
                    System.out.println(" → indsat på position " + hashIndex + " (efter " + probeCount + " forsøg)");
                } else {
                    System.out.println(" → indsat på position " + hashIndex);
                }
            }
        }

        if (verbose) {
            printTable(table);
            System.out.println();
        }

        return table;
    }

    public static void printTable(String[] table) {
        System.out.println("\nSluttabel:");
        System.out.println("╔════════════════════════════════════════╗");

        for (int row = 0; row < 4; row++) {
            System.out.print("║ ");
            for (int col = 0; col < 4; col++) {
                int index = row * 4 + col;
                String value = table[index] == null ? "___" : " " + table[index] + " ";
                System.out.print(String.format("Index %2d: %-3s  ", index, value));
            }
            System.out.println("║");
        }

        System.out.println("╚════════════════════════════════════════╝");

        // Show statistics
        int filled = 0;
        for (String s : table) {
            if (s != null) filled++;
        }
        System.out.println("Udfyldning: " + filled + "/" + TABLE_SIZE + " (" + (filled*100/TABLE_SIZE) + "%)");
    }

    public static void main(String[] args) {
        // Original exercise - Table 1
        List<Character> keys1 = Arrays.asList('D', 'E', 'M', 'O', 'C', 'R', 'A', 'T');
        System.out.println("=== FØRSTE TABEL (LINEAR PROBING) ===");
        String[] table1 = createHashTable(keys1, ProbingType.LINEAR, true, "default");

        // Original exercise - Table 2
        List<Character> keys2 = Arrays.asList('R', 'E', 'P', 'U', 'B', 'L', 'I', 'C', 'A', 'N');
        System.out.println("=== ANDEN TABEL (QUADRATIC PROBING) ===");
        String[] table2 = createHashTable(keys2, ProbingType.QUADRATIC, true, "default");

        // Example with custom hash
        System.out.println("=== DEMO MED CUSTOM HASH ===");
        List<Character> demoKeys = Arrays.asList('A', 'B', 'C', 'D', 'E');
        String[] customTable = createHashTable(demoKeys, ProbingType.LINEAR, true, "custom");
    }
}