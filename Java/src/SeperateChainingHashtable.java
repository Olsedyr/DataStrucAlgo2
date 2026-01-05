public class SeperateChainingHashtable {

    static class Node {
        int key;
        String value;
        Node next;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    static class ChainingHashTable {
        private Node[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;

        public ChainingHashTable(int initialCapacity, double loadFactorThreshold) {
            this.capacity = initialCapacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.table = new Node[capacity];
            this.size = 0;
        }

        // Hash-funktion
        private int hash(int key) {
            return key % capacity;
        }

        // Indsæt med separate chaining
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
            System.out.print("  Hash: " + key + " % " + capacity + " = " + index);

            // Tjek om nøglen allerede findes i linked listen
            Node current = table[index];
            while (current != null) {
                if (current.key == key) {
                    current.value = value;
                    System.out.println(", opdateret eksisterende i kæde");
                    return true;
                }
                current = current.next;
            }

            // Indsæt nyt element i starten af listen
            Node newNode = new Node(key, value);
            newNode.next = table[index];
            table[index] = newNode;
            size++;

            System.out.println(", tilføjet til kæde på index " + index);
            return true;
        }

        // Søg med separate chaining
        public String search(int key) {
            int index = hash(key);
            Node current = table[index];

            while (current != null) {
                if (current.key == key) {
                    return current.value;
                }
                current = current.next;
            }

            return null;
        }

        // Slet element
        public boolean delete(int key) {
            System.out.println("\nForsøger at slette nøgle " + key);

            int index = hash(key);
            Node current = table[index];
            Node prev = null;

            while (current != null) {
                if (current.key == key) {
                    if (prev == null) {
                        // Fjern første node
                        table[index] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size--;
                    System.out.println("  ✓ Slettet nøgle " + key + " fra index " + index);
                    return true;
                }
                prev = current;
                current = current.next;
            }

            System.out.println("  ❌ Nøgle " + key + " ikke fundet");
            return false;
        }

        // Rehashing for separate chaining
        private void performRehash() {
            System.out.println("\n=== UDFØRER REHASHING (SEPARATE CHAINING) ===");

            Node[] oldTable = table;
            int oldCapacity = capacity;

            // Dobbel kapaciteten og find næste primtal
            capacity = findNextPrime(capacity * 2);
            table = new Node[capacity];
            size = 0;

            System.out.println("Ny kapacitet: " + capacity);

            // Genindskriv alle elementer
            for (int i = 0; i < oldCapacity; i++) {
                Node current = oldTable[i];
                while (current != null) {
                    // Indsæt current node i den nye tabel
                    int newIndex = hash(current.key);

                    // Opret en kopi af noden (uden next pointer)
                    Node newNode = new Node(current.key, current.value);
                    newNode.next = table[newIndex];
                    table[newIndex] = newNode;
                    size++;

                    current = current.next;
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
            System.out.println("\nSEPARATE CHAINING TABEL");
            System.out.println("Kapacitet: " + capacity +
                    ", Elementer: " + size +
                    ", Load: " + String.format("%.3f", (double)size/capacity));
            System.out.println("Load factor threshold: " + loadFactorThreshold);

            for (int i = 0; i < capacity; i++) {
                System.out.print("Index " + i + ": ");
                Node current = table[i];
                if (current == null) {
                    System.out.println("EMPTY");
                } else {
                    while (current != null) {
                        System.out.print("[" + current.key + ":" + current.value + "]");
                        if (current.next != null) {
                            System.out.print(" -> ");
                        }
                        current = current.next;
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }

        public int getCapacity() { return capacity; }
        public int getSize() { return size; }
        public double getCurrentLoadFactor() { return (double) size / capacity; }
    }

    public static void main(String[] args) {
        System.out.println("SEPARATE CHAINING HASH-TABEL");
        System.out.println("=".repeat(50));

        ChainingHashTable table = new ChainingHashTable(11, 0.5);

        System.out.println("Indsætter elementer (nogle med samme hash):");
        table.insert(1, "A");      // 1 % 11 = 1
        table.insert(12, "L");     // 12 % 11 = 1 (kollision - går i samme kæde)
        table.insert(23, "W");     // 23 % 11 = 1 (kollision - går i samme kæde)
        table.insert(3, "C");      // 3 % 11 = 3
        table.insert(15, "O");     // 15 % 11 = 4
        table.insert(5, "E");      // 5 % 11 = 5
        table.insert(19, "S");     // 19 % 11 = 8

        System.out.println("\n" + "=".repeat(50));
        System.out.println("FINAL RESULTAT");
        System.out.println("=".repeat(50));
        table.displayTable();

        // Test søgning og sletning
        System.out.println("Søger efter nøgle 12: " + table.search(12));
        System.out.println("Søger efter nøgle 23: " + table.search(23));

        table.delete(12);
        System.out.println("\nEfter sletning af nøgle 12:");
        table.displayTable();
    }
}