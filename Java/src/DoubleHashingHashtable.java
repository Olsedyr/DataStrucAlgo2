import java.util.*;

public class DoubleHashingHashtable {

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

    static class DoubleHashTable {
        private HashEntry[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;

        public DoubleHashTable(int initialCapacity, double loadFactorThreshold) {
            this.capacity = initialCapacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.table = new HashEntry[capacity];
            this.size = 0;
        }

        // Første hash-funktion
        private int hash1(int key) {
            return key % capacity;
        }

        // Anden hash-funktion (skal returnere et tal mellem 1 og capacity-1)
        private int hash2(int key) {
            // Brug et primtal mindre end capacity for bedre spredning
            int prime = 7; // Et lille primtal
            return prime - (key % prime);
        }

        // Indsæt med dobbelt hashing
        public boolean insert(int key, String value) {
            System.out.println("\nIndsætter " + value + " (nøgle=" + key + ")");

            // Check om vi skal rehashe
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

            int h1 = hash1(key);
            int h2 = hash2(key);
            int index = h1;

            System.out.print("  Hash1: " + key + " % " + capacity + " = " + h1);
            System.out.print(", Hash2: " + h2);

            int probe = 0;
            while (probe < capacity) {
                if (table[index] == null || table[index].isDeleted) {
                    table[index] = new HashEntry(key, value);
                    size++;
                    System.out.println(", placeret på index " + index + " (probe=" + probe + ")");
                    return true;
                }
                // Hvis samme nøgle, opdater
                else if (table[index].key == key) {
                    table[index].value = value;
                    table[index].isDeleted = false;
                    System.out.println(", opdateret på index " + index + " (probe=" + probe + ")");
                    return true;
                }

                // Dobbelt hashing: (hash1 + probe * hash2) % capacity
                index = (h1 + probe * h2) % capacity;
                probe++;

                // Sikr at index er positiv
                if (index < 0) index += capacity;
            }

            System.out.println(", ingen ledig plads fundet!");
            return false;
        }

        // Søg med dobbelt hashing
        public String search(int key) {
            int h1 = hash1(key);
            int h2 = hash2(key);
            int index = h1;

            int probe = 0;
            while (probe < capacity) {
                if (table[index] == null) {
                    return null;
                }
                if (!table[index].isDeleted && table[index].key == key) {
                    return table[index].value;
                }

                index = (h1 + probe * h2) % capacity;
                if (index < 0) index += capacity;
                probe++;
            }

            return null;
        }

        // Slet element
        public boolean delete(int key) {
            System.out.println("\nForsøger at slette nøgle " + key);

            int h1 = hash1(key);
            int h2 = hash2(key);
            int index = h1;

            int probe = 0;
            while (probe < capacity) {
                if (table[index] == null) {
                    System.out.println("  ❌ Ikke fundet");
                    return false;
                }
                if (!table[index].isDeleted && table[index].key == key) {
                    table[index].isDeleted = true;
                    size--;
                    System.out.println("  ✓ Slettet fra index " + index);
                    return true;
                }

                index = (h1 + probe * h2) % capacity;
                if (index < 0) index += capacity;
                probe++;
            }

            System.out.println("  ❌ Ikke fundet");
            return false;
        }

        // Rehashing for dobbelt hashing
        private void performRehash() {
            System.out.println("\n=== UDFØRER REHASHING (DOBBELT HASHING) ===");

            List<HashEntry> activeEntries = new ArrayList<>();
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null && !table[i].isDeleted) {
                    activeEntries.add(table[i]);
                }
            }

            int newCapacity = findNextPrime(capacity * 2);
            table = new HashEntry[newCapacity];
            capacity = newCapacity;
            size = 0;

            System.out.println("Ny kapacitet: " + newCapacity);

            // Indsæt med dobbelt hashing i ny tabel
            for (HashEntry entry : activeEntries) {
                int h1 = hash1(entry.key);
                int h2 = hash2(entry.key);
                int index = h1;

                int probe = 0;
                while (probe < capacity) {
                    if (table[index] == null) {
                        table[index] = entry;
                        size++;
                        break;
                    }
                    index = (h1 + probe * h2) % capacity;
                    if (index < 0) index += capacity;
                    probe++;
                }
            }
        }

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

        public void displayTable() {
            System.out.println("\nDOBBELT HASHING TABEL");
            System.out.println("Kapacitet: " + capacity +
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
                    System.out.printf("%3s", table[i].value.length() > 0 ? table[i].value.substring(0, 1) : "-");
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

            // Vis hash2 værdier for hvert element
            System.out.println("Hash2 værdier for elementer:");
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null && !table[i].isDeleted) {
                    System.out.println("  " + table[i].value + " (nøgle=" + table[i].key + "): Hash2 = " + hash2(table[i].key));
                }
            }
            System.out.println();
        }

        public int getCapacity() { return capacity; }
        public int getSize() { return size; }
        public double getCurrentLoadFactor() { return (double) size / capacity; }
    }

    public static void main(String[] args) {
        System.out.println("DOBBELT HASHING HASH-TABEL");
        System.out.println("=".repeat(50));

        DoubleHashTable table = new DoubleHashTable(11, 0.5);

        System.out.println("Indsætter elementer:");
        table.insert(1, "A");
        table.insert(23, "W");
        table.insert(3, "C");
        table.insert(15, "O");
        table.insert(5, "E");
        table.insert(19, "S"); // Dette burde udløse rehashing

        System.out.println("\n" + "=".repeat(50));
        System.out.println("FINAL RESULTAT");
        System.out.println("=".repeat(50));
        table.displayTable();

        // Test søgning og sletning
        System.out.println("Søger efter nøgle 23: " + table.search(23));
        table.delete(3);
        System.out.println("Søger efter nøgle 3 (efter sletning): " + table.search(3));

        table.displayTable();
    }
}