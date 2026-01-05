import java.util.*;

public class HashtableWithRehash {

    static class HashEntry {
        int key;
        String value;
        boolean isDeleted;

        HashEntry(int key, String value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }
    }

    static class HashTable {
        private HashEntry[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;

        public HashTable(int initialCapacity, double loadFactorThreshold) {
            this.capacity = initialCapacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.table = new HashEntry[capacity];
            this.size = 0;
        }

        // Ændre load factor
        public void setLoadFactorThreshold(double newThreshold) {
            this.loadFactorThreshold = newThreshold;
        }

        // Hjælpemetode til at konvertere bogstav til nøgle (A=1, B=2, ...)
        private int charToKey(char c) {
            return Character.toUpperCase(c) - 'A' + 1;
        }

        // Beregn hash-værdi
        private int calculateHash(int key) {
            return key % capacity;
        }

        // INDSÆT METODER - FLEKSIBEL OG SIMPEL

        // Metode 1: Indsæt med integer nøgle og String værdi
        public void insert(int key, String value) {
            insertInternal(key, value);
        }

        // Metode 2: Indsæt med char (automatisk konvertering til nøgle)
        public void insert(char value) {
            int key = charToKey(value);
            insertInternal(key, String.valueOf(value));
        }

        // Metode 3: Indsæt flere chars på én gang
        public void insertAll(char[] values) {
            for (char c : values) {
                insert(c);
            }
        }

        // Metode 4: Indsæt flere Strings med keys på én gang
        public void insertAll(int[] keys, String[] values) {
            for (int i = 0; i < keys.length; i++) {
                insert(keys[i], values[i]);
            }
        }

        // Fælles insert logik
        private void insertInternal(int key, String value) {
            System.out.println("\nIndsætter " + value + " (nøgle=" + key + ")");

            // Tjek om denne indsættelse vil overskride load factor
            double projectedLoadFactor = (double) (size + 1) / capacity;
            if (projectedLoadFactor > loadFactorThreshold) {
                System.out.println("\n⚠️  Load factor vil blive " +
                        String.format("%.3f", projectedLoadFactor) +
                        " > " + loadFactorThreshold);
                System.out.println("=== TABEL FØR REHASHING ===");
                displayTable();
                performRehash();
                System.out.println("\n=== TABEL EFTER REHASHING ===");
                displayTable();
            }

            int hash = calculateHash(key);
            int probe = 0;

            System.out.print("  Hash: " + key + " % " + capacity + " = " + hash);

            while (probe < capacity) {
                int index = (hash + probe * probe) % capacity;

                if (table[index] == null || table[index].isDeleted) {
                    table[index] = new HashEntry(key, value);
                    size++;
                    System.out.println(", placeret på index " + index);
                    return;
                }
                // Hvis samme nøgle, opdater
                else if (table[index].key == key) {
                    table[index].value = value;
                    table[index].isDeleted = false;
                    System.out.println(", opdateret på index " + index);
                    return;
                }

                probe++;
            }

            System.out.println(", ingen ledig plads!");
        }

        // SØG OG SLET METODER

        public String search(int key) {
            int hash = calculateHash(key);
            int probe = 0;

            while (probe < capacity) {
                int index = (hash + probe * probe) % capacity;

                if (table[index] == null) {
                    return null;
                }
                else if (!table[index].isDeleted && table[index].key == key) {
                    return table[index].value;
                }

                probe++;
            }

            return null;
        }

        public boolean delete(int key) {
            System.out.println("\nForsøger at slette nøgle " + key);

            int hash = calculateHash(key);
            int probe = 0;

            while (probe < capacity) {
                int index = (hash + probe * probe) % capacity;

                if (table[index] == null) {
                    System.out.println("  ❌ Ikke fundet");
                    return false;
                }
                else if (!table[index].isDeleted && table[index].key == key) {
                    table[index].isDeleted = true;
                    size--;
                    System.out.println("  ✓ Slettet fra index " + index);
                    return true;
                }

                probe++;
            }

            System.out.println("  ❌ Ikke fundet");
            return false;
        }

        // AUTOMATISK REHASHING

        private void performRehash() {
            System.out.println("\n=== UDFØRER REHASHING ===");

            // Gem aktive elementer
            List<HashEntry> activeEntries = new ArrayList<>();
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null && !table[i].isDeleted) {
                    activeEntries.add(table[i]);
                }
            }

            // Dobbel kapaciteten og find næste primtal
            int newCapacity = findNextPrime(capacity * 2);

            // Nulstil
            table = new HashEntry[newCapacity];
            capacity = newCapacity;
            size = 0;

            System.out.println("Ny kapacitet: " + newCapacity);

            // Indsæt alle aktive elementer igen
            for (HashEntry entry : activeEntries) {
                int hash = calculateHash(entry.key);
                int probe = 0;

                while (probe < capacity) {
                    int index = (hash + probe * probe) % capacity;

                    if (table[index] == null) {
                        table[index] = entry;
                        size++;
                        break;
                    }
                    probe++;
                }
            }
        }

        // HJÆLPEMETODER

        private int findNextPrime(int n) {
            if (n <= 1) return 2;
            while (!isPrime(n)) {
                n++;
            }
            return n;
        }

        private boolean isPrime(int n) {
            if (n <= 1) return false;
            if (n <= 3) return true;
            if (n % 2 == 0 || n % 3 == 0) return false;

            for (int i = 5; i * i <= n; i += 6) {
                if (n % i == 0 || n % (i + 2) == 0) return false;
            }
            return true;
        }

        // VISNINGSMETODER

        public void displayTable() {
            System.out.println("\nKapacitet: " + capacity +
                    ", Elementer: " + size +
                    ", Load: " + String.format("%.3f", (double)size/capacity));
            System.out.println("Load factor threshold: " + loadFactorThreshold);

            System.out.print("Indeks:  ");
            for (int i = 0; i < capacity; i++) {
                System.out.printf("%3d", i);
            }

            System.out.print("\nVærdi:   ");
            for (int i = 0; i < capacity; i++) {
                if (table[i] == null) {
                    System.out.print("  -");
                } else if (table[i].isDeleted) {
                    System.out.print("  X");
                } else {
                    System.out.printf("%3s", table[i].value);
                }
            }

            System.out.print("\nNøgle:   ");
            for (int i = 0; i < capacity; i++) {
                if (table[i] == null) {
                    System.out.print("  -");
                } else if (table[i].isDeleted) {
                    System.out.print("  X");
                } else {
                    System.out.printf("%3d", table[i].key);
                }
            }
            System.out.println("\n");
        }

        // Get-metoder
        public int getCapacity() { return capacity; }
        public int getSize() { return size; }
        public double getCurrentLoadFactor() { return (double) size / capacity; }
    }

    // TEST AF OPGAVE 6
    public static void testExercise6() {
        System.out.println("=".repeat(50));
        System.out.println("OPGAVE 6 - AUTOMATISK REHASHING");
        System.out.println("=".repeat(50));

        // Opret tabel med kapacitet 11 og load factor 0.5
        HashTable table = new HashTable(11, 0.5);

        // Indsæt bogstaver direkte - ingen keys nødvendige!
        System.out.println("\n--- Indsætter bogstaver ---");
        table.insert('A');
        table.insert('W');
        table.insert('C');
        table.insert('O');
        table.insert('E');
        table.insert('S'); // Dette vil udløse automatisk rehashing

        System.out.println("\n" + "=".repeat(50));
        System.out.println("FINAL RESULTAT");
        System.out.println("=".repeat(50));
        table.displayTable();
    }

    // TEST MED STRINGS OG KEYS
    public static void testWithStrings() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST MED STRINGS OG KEYS");
        System.out.println("=".repeat(50));

        HashTable table = new HashTable(7, 0.75);

        // Brug den generelle insert med keys og strings
        table.insert(100, "Apple");
        table.insert(200, "Banana");
        table.insert(300, "Cherry");

        System.out.println("\nTabel efter 3 strings:");
        table.displayTable();

        // Slet et element
        table.delete(200);

        System.out.println("Tabel efter sletning:");
        table.displayTable();

        // Søg efter et element
        String result = table.search(300);
        System.out.println("Søgning efter nøgle 300: " + (result != null ? result : "Ikke fundet"));
    }

    // TEST MED FLERE BOGSTAVER PÅ ÉN GANG
    public static void testMultipleChars() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST MED FLERE BOGSTAVER");
        System.out.println("=".repeat(50));

        HashTable table = new HashTable(10, 0.6);

        // Indsæt flere bogstaver på én gang
        char[] letters = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
        table.insertAll(letters);

        System.out.println("\nTabel efter indsættelse af 7 bogstaver:");
        table.displayTable();
    }

    // HVIS DU VIL BRUGE DIREKTE KEYS (som i opgave 6 med forskellige keys)
    public static void testWithExplicitKeys() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST MED EKSPLICITTE KEYS");
        System.out.println("=".repeat(50));

        HashTable table = new HashTable(11, 0.5);

        // Eksplicitte keys som i opgave 6
        table.insert(1, "A");   // A = nøgle 1
        table.insert(23, "W");  // W = nøgle 23
        table.insert(3, "C");   // C = nøgle 3
        table.insert(15, "O");  // O = nøgle 15
        table.insert(5, "E");   // E = nøgle 5
        table.insert(19, "S");  // S = nøgle 19

        System.out.println("\nTabel med eksplicitte keys:");
        table.displayTable();
    }

    public static void main(String[] args) {
        System.out.println("UNIVERSAL HASH-TABEL - SIMPEL OG FLEKSIBEL");
        System.out.println("=".repeat(50));

        // Vælg hvilke tests du vil køre:
        testExercise6();        // Simpel test med bogstaver
        // testWithStrings();      // Test med strings og keys
        // testMultipleChars();    // Test med flere bogstaver
        // testWithExplicitKeys(); // Test med eksplicitte keys

        System.out.println("\n" + "=".repeat(50));
        System.out.println("TEST FÆRDIG");
        System.out.println("=".repeat(50));

    }
}