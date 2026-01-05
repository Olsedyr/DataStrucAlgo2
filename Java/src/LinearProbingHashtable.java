import java.util.*;

public class LinearProbingHashtable {

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

    static class LinearHashTable {
        private HashEntry[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;

        public LinearHashTable(int initialCapacity, double loadFactorThreshold) {
            this.capacity = initialCapacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.table = new HashEntry[capacity];
            this.size = 0;
        }

        // Beregn hash (standard)
        private int hash(int key) {
            return key % capacity;
        }

        // Alternativ hash: (key + 1) % capacity
        private int hashPlusOne(int key) {
            return (key + 1) % capacity;
        }

        // Indsæt element
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

            int index = hash(key);
            int startIndex = index;

            System.out.print("  Lineær probing starter på index " + index);

            // Lineær probing: (hash + i) % capacity
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

                // Lineær probing: gå til næste indeks
                index = (index + 1) % capacity;
                probe++;

                // Hvis vi er kommet tilbage til start, er tabellen fuld
                if (index == startIndex) {
                    break;
                }
            }

            System.out.println(", ingen ledig plads fundet!");
            return false;
        }

        // Søg efter element
        public String search(int key) {
            int index = hash(key);
            int startIndex = index;

            do {
                if (table[index] == null) {
                    return null;
                }
                if (!table[index].isDeleted && table[index].key == key) {
                    return table[index].value;
                }
                index = (index + 1) % capacity;
            } while (index != startIndex && table[index] != null);

            return null;
        }

        // Slet element
        public boolean delete(int key) {
            System.out.println("\nForsøger at slette nøgle " + key);

            int index = hash(key);
            int startIndex = index;

            do {
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
                index = (index + 1) % capacity;
            } while (index != startIndex);

            System.out.println("  ❌ Ikke fundet");
            return false;
        }

        // Rehashing
        private void performRehash() {
            System.out.println("\n=== UDFØRER REHASHING (LINEÆR) ===");

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

            // Indsæt alle aktive elementer igen med lineær probing
            for (HashEntry entry : activeEntries) {
                int index = hash(entry.key);
                while (table[index] != null) {
                    index = (index + 1) % capacity;
                }
                table[index] = entry;
                size++;
            }
        }

        // Find næste primtal
        private int findNextPrime(int n) {
            if (n <= 1) return 2;
            while (!isPrime(n)) {
                n++;
            }
            return n;
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

        // Vis tabel
        public void displayTable() {
            System.out.println("\nLINEÆR PROBING TABEL");
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
        }

        public int getCapacity() { return capacity; }
        public int getSize() { return size; }
        public double getCurrentLoadFactor() { return (double) size / capacity; }
    }

    // Test
    public static void main(String[] args) {
        System.out.println("LINEÆR PROBING HASH-TABEL");
        System.out.println("=".repeat(50));

        LinearHashTable table = new LinearHashTable(11, 0.5);

        // Test med samme data som tidligere
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