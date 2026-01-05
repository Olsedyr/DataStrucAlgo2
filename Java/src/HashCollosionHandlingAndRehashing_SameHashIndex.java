import java.util.ArrayList;
import java.util.List;

public class HashCollosionHandlingAndRehashing_SameHashIndex {

    static class HashEntry {
        int key;    // Bogstavets position i alfabetet (A=1, B=2, ...)
        char value; // Selve bogstavet

        HashEntry(int key, char value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return value + "(k=" + key + ")";
        }
    }

    static class QuadraticHashTable {
        private HashEntry[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;
        private List<HashEntry> allElements; // Gemmer alle elementer i indsættelsesrækkefølge

        public QuadraticHashTable(int initialCapacity, double loadFactorThreshold) {
            this.capacity = initialCapacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.table = new HashEntry[capacity];
            this.size = 0;
            this.allElements = new ArrayList<>();
            System.out.println("Oprettet hash-tabel med kapacitet " + capacity +
                    " og load factor threshold " + loadFactorThreshold);
        }

        // Ændre load factor threshold
        public void setLoadFactorThreshold(double newThreshold) {
            this.loadFactorThreshold = newThreshold;
            System.out.println("Load factor threshold ændret til: " + newThreshold);
        }

        // Indsæt et element med given nøgle og værdi
        public boolean insert(int key, char value) {
            System.out.println("\nIndsætter " + value + " (nøgle=" + key + ")");

            // Check om vi skal rehashe FØR indsættelse
            if ((double) size / capacity > loadFactorThreshold) {
                System.out.println("Load factor overskredet (" +
                        String.format("%.3f", (double)size/capacity) +
                        " > " + loadFactorThreshold + "), udfører rehashing...");
                performRehash();
            }

            int originalHash = key % capacity;
            int probe = 0;

            System.out.print("  Beregner hash: " + key + " % " + capacity + " = " + originalHash);

            // Quadratic probing: h(k, i) = (hash(k) + i²) % capacity
            while (probe < capacity) {
                int index = (originalHash + probe * probe) % capacity;

                if (probe == 0) {
                    System.out.println(", forsøger index " + index);
                } else {
                    System.out.println("  Probe " + probe + ": (" + originalHash + " + " +
                            probe + "²) % " + capacity + " = " + index);
                }

                if (table[index] == null) {
                    table[index] = new HashEntry(key, value);
                    size++;
                    allElements.add(new HashEntry(key, value)); // Gem i listen
                    System.out.println("  ✓ " + value + " placeret på index " + index);
                    return true;
                }
                // Hvis den allerede findes med samme nøgle, erstatter vi (valgfrit)
                else if (table[index].key == key) {
                    table[index].value = value;
                    System.out.println("  ↺ " + value + " erstattede eksisterende på index " + index);
                    return true;
                }

                probe++;
            }

            System.out.println("  ❌ Kunne ikke finde ledig plads for " + value);
            return false;
        }

        // Indsæt flere elementer på én gang
        public void insertAll(char[] elements) {
            System.out.println("\n--- Indsætter " + elements.length + " elementer ---");
            for (char c : elements) {
                int key = charToKey(c);
                insert(key, c);
            }
        }

        // Indsæt flere elementer på én gang med eksplicitte nøgler
        public void insertAll(int[] keys, char[] values) {
            if (keys.length != values.length) {
                System.out.println("Fejl: Antal nøgler og værdier er ikke ens!");
                return;
            }

            System.out.println("\n--- Indsætter " + keys.length + " elementer ---");
            for (int i = 0; i < keys.length; i++) {
                insert(keys[i], values[i]);
            }
        }

        // Udfør rehashing manuelt
        public void performRehash() {
            System.out.println("\n=== UDFØRER REHASHING ===");

            // Beregn ny kapacitet: næste primtal efter 2*gammel kapacitet
            int newCapacity = findNextPrime(capacity * 2);

            // Opret ny tabel
            HashEntry[] newTable = new HashEntry[newCapacity];

            System.out.println("Gammel kapacitet: " + capacity);
            System.out.println("Ny kapacitet: " + newCapacity);
            System.out.println("Flytter " + allElements.size() + " elementer til ny tabel...");

            // Indsæt alle elementer fra listen i den nye tabel
            for (HashEntry entry : allElements) {
                int newHash = entry.key % newCapacity;
                int probe = 0;

                // Quadratic probing i den nye tabel
                while (probe < newCapacity) {
                    int index = (newHash + probe * probe) % newCapacity;

                    if (newTable[index] == null) {
                        newTable[index] = entry;
                        break;
                    }
                    probe++;
                }
            }

            // Opdater tabel og kapacitet
            table = newTable;
            capacity = newCapacity;

            System.out.println("Rehashing fuldført!");
        }

        // Find næste primtal efter et givet tal
        private int findNextPrime(int n) {
            if (n <= 1) return 2;

            while (true) {
                if (isPrime(n)) {
                    return n;
                }
                n++;
            }
        }

        // Tjek om et tal er primtal
        private boolean isPrime(int n) {
            if (n <= 1) return false;
            if (n <= 3) return true;
            if (n % 2 == 0 || n % 3 == 0) return false;

            for (int i = 5; i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) return false;
            }
            return true;
        }

        // Vis hele hash-tabellen
        public void displayTable() {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("HASH TABEL - KAPACITET: " + capacity +
                    ", ELEMENTER: " + size +
                    ", LOAD FACTOR: " + String.format("%.3f", (double)size/capacity));
            System.out.println("Load factor threshold: " + loadFactorThreshold);
            System.out.println("=".repeat(60));

            // Vis indekslinje
            System.out.print("Indeks:  ");
            for (int i = 0; i < capacity; i++) {
                System.out.printf("%3d", i);
            }

            // Vis værdilinje
            System.out.print("\nVærdi:   ");
            for (int i = 0; i < capacity; i++) {
                if (table[i] == null) {
                    System.out.print("  -");
                } else {
                    System.out.printf("%3c", table[i].value);
                }
            }

            // Vis nøglelinje
            System.out.print("\nNøgle:   ");
            for (int i = 0; i < capacity; i++) {
                if (table[i] == null) {
                    System.out.print("  -");
                } else {
                    System.out.printf("%3d", table[i].key);
                }
            }

            System.out.println("\n");
        }

        // Vis detaljeret information
        public void displayDetailedInfo() {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("DETALJERET INFORMATION");
            System.out.println("=".repeat(60));

            System.out.println("Elementer i indsættelsesrækkefølge:");
            for (int i = 0; i < allElements.size(); i++) {
                HashEntry entry = allElements.get(i);
                System.out.printf("  %d. %c (nøgle=%2d)%n", i+1, entry.value, entry.key);
            }

            System.out.println("\nPlacering i tabellen:");
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    HashEntry entry = table[i];
                    int calculatedHash = entry.key % capacity;
                    System.out.printf("  Index %2d: %c (nøgle=%2d, hash=%2d %% %2d = %2d)%n",
                            i, entry.value, entry.key,
                            entry.key, capacity, calculatedHash);
                }
            }

            // Vis tomme pladser
            System.out.print("\nTomme indeks: ");
            boolean first = true;
            for (int i = 0; i < capacity; i++) {
                if (table[i] == null) {
                    if (!first) System.out.print(", ");
                    System.out.print(i);
                    first = false;
                }
            }
            System.out.println();

            // Vis statistik
            System.out.println("\nStatistik:");
            System.out.println("  Kapacitet: " + capacity);
            System.out.println("  Antal elementer: " + size);
            System.out.println("  Load factor: " + String.format("%.3f", (double)size/capacity));
            System.out.println("  Load factor threshold: " + loadFactorThreshold);
            System.out.println("  Elementer i listen: " + allElements.size());
        }

        // Hjælpemetode til at konvertere bogstav til nøgle
        private int charToKey(char c) {
            return Character.toUpperCase(c) - 'A' + 1;
        }

        // Gettere
        public int getCapacity() { return capacity; }
        public int getSize() { return size; }
        public double getCurrentLoadFactor() { return (double) size / capacity; }
        public List<HashEntry> getAllElements() { return allElements; }
    }

    // Hjælpemetode til at konvertere bogstav til nøgle (ekstern brug)
    public static int charToKey(char c) {
        return Character.toUpperCase(c) - 'A' + 1;
    }

    // Test 1: Kør opgave 6 med standard parametre
    public static void testExercise6() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 1: OPGAVE 6 (Standard: kapacitet=11, loadFactor=0.5)");
        System.out.println("=".repeat(60));

        QuadraticHashTable table = new QuadraticHashTable(11, 0.5);

        // Indsæt elementerne fra opgaven
        char[] elements = {'A', 'W', 'C', 'O', 'E', 'S'};
        table.insertAll(elements);

        // Vis den oprindelige tabel
        table.displayTable();
        table.displayDetailedInfo();

        // Check om rehashing er nødvendig
        if (table.getCurrentLoadFactor() > 0.5) {
            System.out.println("\n--- Load factor > 0.5, udfører rehashing ---");
            table.performRehash();

            // Vis den nye tabel
            table.displayTable();
            table.displayDetailedInfo();
        }
    }

    // Test 2: Ændre load factoren til 0.75
    public static void testWithHigherLoadFactor() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 2: Højere load factor (kapacitet=11, loadFactor=0.75)");
        System.out.println("=".repeat(60));

        QuadraticHashTable table = new QuadraticHashTable(11, 0.75);

        // Indsæt de samme elementer
        char[] elements = {'A', 'W', 'C', 'O', 'E', 'S'};
        table.insertAll(elements);

        // Vis tabel - ingen rehashing forventes da 6/11=0.545 < 0.75
        table.displayTable();
        table.displayDetailedInfo();

        // Tilføj flere elementer for at se om rehashing udløses
        System.out.println("\n--- Tilføjer flere elementer ---");
        char[] moreElements = {'X', 'Y', 'Z', 'B', 'D', 'F'};

        for (char c : moreElements) {
            int key = charToKey(c);
            table.insert(key, c);

            if (table.getCurrentLoadFactor() > 0.75) {
                // Rehashing sker automatisk i insert() metoden
                System.out.println("  Rehashing blev udløst for " + c);
            }
        }

        table.displayTable();
        table.displayDetailedInfo();
    }

    // Test 3: Meget lav load factor threshold
    public static void testWithLowLoadFactor() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 3: Lav load factor (kapacitet=11, loadFactor=0.3)");
        System.out.println("=".repeat(60));

        QuadraticHashTable table = new QuadraticHashTable(11, 0.3);

        // Indsæt kun 4 elementer (4/11=0.363 > 0.3)
        char[] elements = {'A', 'W', 'C', 'O'};

        for (char c : elements) {
            int key = charToKey(c);
            table.insert(key, c);
        }

        table.displayTable();
        table.displayDetailedInfo();
    }

    // Test 4: Manuel kontrol - indsæt, ændre load factor, indsæt mere
    public static void testManualControl() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("TEST 4: Manuel kontrol");
        System.out.println("=".repeat(60));

        // Start med høj load factor
        QuadraticHashTable table = new QuadraticHashTable(7, 0.8);

        // Indsæt nogle elementer
        char[] firstBatch = {'A', 'B', 'C', 'D'};
        table.insertAll(firstBatch);
        table.displayTable();

        // Ændre load factor til noget lavere
        System.out.println("\n--- Ændrer load factor threshold fra 0.8 til 0.4 ---");
        table.setLoadFactorThreshold(0.4);

        // Tilføj et element mere - dette burde udløse rehashing
        System.out.println("\n--- Tilføjer et element mere ---");
        table.insert(charToKey('E'), 'E');

        table.displayTable();
        table.displayDetailedInfo();
    }

    public static void main(String[] args) {
        System.out.println("HASH-TABEL MED REHASHING - EKSEMPELKØRSLER");
        System.out.println("=".repeat(60));

        // Kør forskellige tests
        testExercise6();          // Standard opgave 6

        testWithHigherLoadFactor(); // Test med højere load factor

        testWithLowLoadFactor();    // Test med lav load factor

        testManualControl();        // Manuel kontrol test

        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALLE TESTS FÆRDIGE");
        System.out.println("=".repeat(60));
    }
}