import java.util.*;

public class HashCollisionHandlingAndRehashing_DifferentIndex {

    // Interface for alle hash tabel implementeringer
    interface IHashTable {
        void insert(int key, String value);
        boolean search(int key);
        boolean delete(int key);
        void printTable(String title);
        int getRehashCount();
    }

    // ========== PROBING METODER (Quadratic, Linear, Double) ==========
    static class ProbingHashTable implements IHashTable {
        private String[] table;
        private int[] keys;
        private boolean[] active;
        private int capacity;
        private int size;
        private double loadFactorThreshold;
        private String collisionMethod;
        private int rehashCount;

        public ProbingHashTable(int capacity, double loadFactorThreshold, String collisionMethod) {
            this.capacity = capacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.collisionMethod = collisionMethod;
            this.table = new String[capacity];
            this.keys = new int[capacity];
            this.active = new boolean[capacity];
            this.size = 0;
            this.rehashCount = 0;
        }

        private int hash1(int key) {
            return Math.abs(key) % capacity;
        }

        private int hash2(int key) {
            return 7 - (Math.abs(key) % 7);
        }

        public void insert(int key, String value) {
            // Check load factor MED det nye element
            if ((double)(size + 1) / capacity > loadFactorThreshold) {
                rehash();
            }

            for (int i = 0; i < capacity; i++) {
                int index = getIndex(key, i);

                if (table[index] == null || !active[index]) {
                    table[index] = value;
                    keys[index] = key;
                    active[index] = true;
                    size++;
                    return;
                }
            }
        }

        private int getIndex(int key, int i) {
            if (collisionMethod.equals("quadratic")) {
                return (hash1(key) + i * i) % capacity;
            } else if (collisionMethod.equals("linear")) {
                return (hash1(key) + i) % capacity;
            } else if (collisionMethod.equals("double")) {
                return (hash1(key) + i * hash2(key)) % capacity;
            } else {
                return (hash1(key) + i) % capacity;
            }
        }

        public boolean search(int key) {
            for (int i = 0; i < capacity; i++) {
                int index = getIndex(key, i);

                if (table[index] == null) {
                    return false;
                }
                if (keys[index] == key && active[index]) {
                    return true;
                }
            }
            return false;
        }

        public boolean delete(int key) {
            for (int i = 0; i < capacity; i++) {
                int index = getIndex(key, i);

                if (table[index] == null) {
                    return false;
                }
                if (keys[index] == key && active[index]) {
                    active[index] = false;
                    size--;
                    return true;
                }
            }
            return false;
        }

        private void rehash() {
            rehashCount++;
            System.out.println("\n⚠️ Rehashing #" + rehashCount  + " triggered! " +
                    (size+1) + " elementer i " + capacity + " slots  " +
                    String.format("> %.2f ", (double)size/capacity) + " load factor");

            int oldCapacity = capacity;
            String[] oldTable = table;
            int[] oldKeys = keys;
            boolean[] oldActive = active;

            // Dobbel kapaciteten
            capacity = capacity * 2;
            table = new String[capacity];
            keys = new int[capacity];
            active = new boolean[capacity];
            size = 0;

            // Indsæt alle gamle elementer i den nye tabel (uden load factor check)
            for (int i = 0; i < oldCapacity; i++) {
                if (oldActive[i]) {
                    // Direkte indsættelse uden at tjekke load factor
                    for (int j = 0; j < capacity; j++) {
                        int index = getIndex(oldKeys[i], j);
                        if (table[index] == null) {
                            table[index] = oldTable[i];
                            keys[index] = oldKeys[i];
                            active[index] = true;
                            size++;
                            break;
                        }
                    }
                }
            }

            System.out.println("Rehashed fra " + oldCapacity + " til " + capacity + " slots");
            System.out.println("Elementer: " + size + ", Load: " +
                    String.format("%.2f", (double)size / capacity));
        }

        public void printTable(String title) {
            System.out.println("\n" + title);
            System.out.print("Index:  ");
            int displaySize = Math.min(capacity, 20);
            for (int i = 0; i < displaySize; i++) {
                System.out.printf("%2d ", i);
            }
            if (capacity > 20) {
                System.out.print("...");
            }

            System.out.print("\nValue:  ");
            for (int i = 0; i < displaySize; i++) {
                if (table[i] == null || !active[i]) {
                    System.out.print(" - ");
                } else {
                    System.out.printf("%2s ", table[i]);
                }
            }
            if (capacity > 20) {
                System.out.print("...");
            }
            System.out.println("\nKapacitet: " + capacity + " | Elementer: " + size +
                    " | Load: " + String.format("%.2f", (double)size / capacity) +
                    " | Rehashes: " + rehashCount);
        }

        public int getRehashCount() {
            return rehashCount;
        }
    }

    // ========== SEPARATE CHAINING ==========
    static class ChainingHashTable implements IHashTable {
        private static class KeyValuePair {
            int key;
            String value;

            KeyValuePair(int key, String value) {
                this.key = key;
                this.value = value;
            }
        }

        private ArrayList<KeyValuePair>[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;
        private int rehashCount;

        @SuppressWarnings("unchecked")
        public ChainingHashTable(int capacity, double loadFactorThreshold) {
            this.capacity = capacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.table = new ArrayList[capacity];
            for (int i = 0; i < capacity; i++) {
                table[i] = new ArrayList<>();
            }
            this.size = 0;
            this.rehashCount = 0;
        }

        private int hash(int key) {
            return Math.abs(key) % capacity;
        }

        public void insert(int key, String value) {
            if ((double)(size + 1) / capacity > loadFactorThreshold) {
                rehash();
            }

            int index = hash(key);
            table[index].add(new KeyValuePair(key, value));
            size++;
        }

        public boolean search(int key) {
            int index = hash(key);
            for (KeyValuePair pair : table[index]) {
                if (pair.key == key) {
                    return true;
                }
            }
            return false;
        }

        public boolean delete(int key) {
            int index = hash(key);
            for (int i = 0; i < table[index].size(); i++) {
                if (table[index].get(i).key == key) {
                    table[index].remove(i);
                    size--;
                    return true;
                }
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        private void rehash() {
            rehashCount++;
            System.out.println("\n⚠️ Rehashing #" + rehashCount + " triggered! " +
                    size + " elementer i " + capacity + " buckets = " +
                    String.format("%.2f", (double)size/capacity) + " load factor");

            int oldCapacity = capacity;
            ArrayList<KeyValuePair>[] oldTable = table;

            capacity = capacity * 2;
            table = new ArrayList[capacity];
            for (int i = 0; i < capacity; i++) {
                table[i] = new ArrayList<>();
            }
            size = 0;

            // Indsæt alle gamle elementer i den nye tabel
            for (int i = 0; i < oldCapacity; i++) {
                for (KeyValuePair pair : oldTable[i]) {
                    int newIndex = hash(pair.key);
                    table[newIndex].add(pair);
                    size++;
                }
            }

            System.out.println("Rehashed fra " + oldCapacity + " til " + capacity + " buckets");
            System.out.println("Elementer: " + size + ", Load: " +
                    String.format("%.2f", (double)size / capacity));
        }

        public void printTable(String title) {
            System.out.println("\n" + title);
            int bucketsUsed = 0;
            for (int i = 0; i < capacity && i < 20; i++) {
                if (table[i].size() > 0) {
                    System.out.print("Bucket " + i + ": ");
                    for (KeyValuePair pair : table[i]) {
                        System.out.print("[" + pair.value + "] ");
                    }
                    System.out.println();
                    bucketsUsed++;
                }
            }
            if (capacity > 20) {
                System.out.println("... (resten af de " + (capacity - 20) + " buckets ikke vist)");
            }
            System.out.println("Kapacitet: " + capacity + " buckets | Elementer: " + size +
                    " | Load: " + String.format("%.2f", (double)size / capacity) +
                    " | Rehashes: " + rehashCount);
        }

        public int getRehashCount() {
            return rehashCount;
        }
    }

    // ========== MAIN ==========
    public static void main(String[] args) {
        double threshold = 0.5; // Opgave siger maks 0.5 load factor
        System.out.println("=== ALLE 4 KOLLISIONSMETODER ===");
        System.out.println("Load Factor Threshold: " + threshold + "\n");

        // Test alle metoder med load factor 0.5
        testMethod("QUADRATIC PROBING", new ProbingHashTable(10, threshold, "quadratic"));
        testMethod("LINEAR PROBING", new ProbingHashTable(10, threshold, "linear"));
        testMethod("DOUBLE HASHING", new ProbingHashTable(10, threshold, "double"));
        testMethod("SEPARATE CHAINING", new ChainingHashTable(10, threshold));

        // Forklaring
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📚 FORKLARING AF REHASHING LOGIK:");
        System.out.println("=".repeat(50));
    }

    static void testMethod(String title, IHashTable table) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(title);
        System.out.println("=".repeat(50));

        // Indsæt samme elementer i alle tabeller
        int[] keys = {1, 23, 3, 15, 5, 19, 33, 2, 3};
        String[] values = {"A", "W", "C", "O", "E", "S", "X", "B", "D"};

        System.out.print("Indsætter: ");
        for (int i = 0; i < keys.length; i++) {
            System.out.print(values[i] + "(" + keys[i] + ") ");
            table.insert(keys[i], values[i]);
        }

        table.printTable(title);

        // Test søgning
        System.out.println("\nTest søgning:");
        System.out.println("Findes 15? " + table.search(15));
        System.out.println("Findes 99? " + table.search(99));

        // Test sletning
        System.out.println("\nTest sletning:");
        System.out.println("Sletter 3: " + table.delete(3));
        table.printTable("Efter sletning af 3");

        System.out.println("\n" + "-".repeat(50));
    }
}