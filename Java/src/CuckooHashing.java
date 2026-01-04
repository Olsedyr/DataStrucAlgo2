public class CuckooHashing<K, V> {
    // Entry klasse til at gemme nøgle-værdi par
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Tabeller til Cuckoo Hashing
    private Entry<K, V>[] table1;
    private Entry<K, V>[] table2;
    private int capacity;
    private int size;
    private final double MAX_LOAD_FACTOR = 0.5;
    private final int MAX_DISPLACEMENT = 20;

    // Hash funktioner
    private final HashFunction<K> hash1;
    private final HashFunction<K> hash2;

    // Interface for hash funktioner
    public interface HashFunction<K> {
        int hash(K key, int capacity);
    }

    // Default hash funktioner - MEGET FORSKELLIGE!
    public static class DefaultHash1<K> implements HashFunction<K> {
        public int hash(K key, int capacity) {
            int h = key.hashCode();
            // Simpel hash baseret på hashCode
            return Math.abs(h) % capacity;
        }
    }

    public static class DefaultHash2<K> implements HashFunction<K> {
        // Bruger et helt andet primtal for at sikre forskellig fordeling
        private static final int PRIME = 31;

        public int hash(K key, int capacity) {
            int h = key.hashCode();
            // Multiplicer med primtal og rotér bits for helt anden fordeling
            h = h * PRIME;
            h = ((h >>> 16) ^ h) * 0x45d9f3b;
            h = ((h >>> 16) ^ h) * 0x45d9f3b;
            h = (h >>> 16) ^ h;
            return Math.abs(h) % capacity;
        }
    }

    // Konstruktør med default hash funktioner
    @SuppressWarnings("unchecked")
    public CuckooHashing(int initialCapacity) {
        this(initialCapacity, new DefaultHash1<>(), new DefaultHash2<>());
    }

    // Konstruktør med custom hash funktioner
    @SuppressWarnings("unchecked")
    public CuckooHashing(int initialCapacity, HashFunction<K> hash1, HashFunction<K> hash2) {
        this.capacity = Math.max(initialCapacity, 4);
        this.table1 = (Entry<K, V>[]) new Entry[capacity];
        this.table2 = (Entry<K, V>[]) new Entry[capacity];
        this.size = 0;
        this.hash1 = hash1;
        this.hash2 = hash2;
    }

    // Indsæt nøgle-værdi par
    public boolean put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        // Tjek om nøglen allerede findes
        if (containsKey(key)) {
            return updateExistingKey(key, value);
        }

        // Tjek load factor
        if (getLoadFactor() >= MAX_LOAD_FACTOR) {
            rehash();
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        return insertEntry(newEntry, true);
    }

    // Hent værdi for nøgle
    public V get(K key) {
        int index1 = hash1.hash(key, capacity);
        int index2 = hash2.hash(key, capacity);

        if (table1[index1] != null && table1[index1].key.equals(key)) {
            return table1[index1].value;
        }
        if (table2[index2] != null && table2[index2].key.equals(key)) {
            return table2[index2].value;
        }
        return null;
    }

    // Fjern nøgle
    public boolean remove(K key) {
        int index1 = hash1.hash(key, capacity);
        int index2 = hash2.hash(key, capacity);

        if (table1[index1] != null && table1[index1].key.equals(key)) {
            table1[index1] = null;
            size--;
            return true;
        }
        if (table2[index2] != null && table2[index2].key.equals(key)) {
            table2[index2] = null;
            size--;
            return true;
        }
        return false;
    }

    // Tjek om nøgle findes
    public boolean containsKey(K key) {
        int index1 = hash1.hash(key, capacity);
        int index2 = hash2.hash(key, capacity);

        return (table1[index1] != null && table1[index1].key.equals(key)) ||
                (table2[index2] != null && table2[index2].key.equals(key));
    }

    // Hent størrelse
    public int size() {
        return size;
    }

    // Hent kapacitet
    public int capacity() {
        return capacity;
    }

    // Hent load factor
    public double getLoadFactor() {
        return (double) size / (2 * capacity);
    }

    // Ryd tabellen
    public void clear() {
        java.util.Arrays.fill(table1, null);
        java.util.Arrays.fill(table2, null);
        size = 0;
    }

    // Rehash tabellen - dobel kapacitet
    private void rehash() {
        int oldCapacity = capacity;
        capacity = capacity * 2;

        @SuppressWarnings("unchecked")
        Entry<K, V>[] newTable1 = (Entry<K, V>[]) new Entry[capacity];
        @SuppressWarnings("unchecked")
        Entry<K, V>[] newTable2 = (Entry<K, V>[]) new Entry[capacity];

        Entry<K, V>[] oldTable1 = table1;
        Entry<K, V>[] oldTable2 = table2;

        table1 = newTable1;
        table2 = newTable2;
        size = 0;

        // Genindsæt alle elementer
        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable1[i] != null) {
                put(oldTable1[i].key, oldTable1[i].value);
            }
            if (oldTable2[i] != null) {
                put(oldTable2[i].key, oldTable2[i].value);
            }
        }
    }

    // Indsæt entry med Cuckoo Hashing algoritme
    private boolean insertEntry(Entry<K, V> entry, boolean allowRehash) {
        Entry<K, V> current = entry;
        int currentTable = 1;
        int attempts = 0;

        while (attempts < MAX_DISPLACEMENT) {
            int hash = (currentTable == 1) ?
                    hash1.hash(current.key, capacity) :
                    hash2.hash(current.key, capacity);

            Entry<K, V>[] table = (currentTable == 1) ? table1 : table2;

            if (table[hash] == null) {
                table[hash] = current;
                size++;
                return true;
            }

            // Swap
            Entry<K, V> temp = table[hash];
            table[hash] = current;
            current = temp;
            currentTable = (currentTable == 1) ? 2 : 1;
            attempts++;
        }

        // For mange forsøg - rehash
        if (allowRehash) {
            rehash();
            return insertEntry(current, false);
        }

        throw new IllegalStateException("Cuckoo hashing failed - too many displacements");
    }

    // Opdater eksisterende nøgle
    private boolean updateExistingKey(K key, V value) {
        int index1 = hash1.hash(key, capacity);
        int index2 = hash2.hash(key, capacity);

        if (table1[index1] != null && table1[index1].key.equals(key)) {
            table1[index1].value = value;
            return true;
        }
        if (table2[index2] != null && table2[index2].key.equals(key)) {
            table2[index2].value = value;
            return true;
        }
        return false;
    }

    // Print tabel status
    public void printStatus() {
        System.out.println("\n=== CUCKOO HASH TABLE STATUS ===");
        System.out.println("Size: " + size);
        System.out.println("Capacity per table: " + capacity);
        System.out.println("Total slots: " + (2 * capacity));
        System.out.printf("Load factor: %.2f (Max: %.2f)%n", getLoadFactor(), MAX_LOAD_FACTOR);

        int occupied1 = countOccupied(table1);
        int occupied2 = countOccupied(table2);
        System.out.println("Table occupancy:");
        System.out.println("  Table 1: " + occupied1 + "/" + capacity +
                String.format(" (%.0f%%)", (occupied1 * 100.0 / capacity)));
        System.out.println("  Table 2: " + occupied2 + "/" + capacity +
                String.format(" (%.0f%%)", (occupied2 * 100.0 / capacity)));
        System.out.println("  Balance ratio: " +
                String.format("%.2f", Math.max(occupied1, occupied2) * 1.0 /
                        Math.max(1, Math.min(occupied1, occupied2))));

        if (getLoadFactor() > MAX_LOAD_FACTOR) {
            System.out.println("WARNING: Load factor above threshold!");
        }
    }

    // Print tabellerne visuelt
    public void printTables() {
        System.out.println("\n=== TABEL VISUALISERING ===");
        System.out.println("Table 1:");
        printTable(table1);
        System.out.println("\nTable 2:");
        printTable(table2);
    }

    private int countOccupied(Entry<K, V>[] table) {
        int count = 0;
        for (Entry<K, V> entry : table) {
            if (entry != null) count++;
        }
        return count;
    }

    private void printTable(Entry<K, V>[] table) {
        int limit = Math.min(table.length, 20);
        for (int i = 0; i < limit; i++) {
            String content;
            if (table[i] == null) {
                content = "[EMPTY]";
            } else {
                content = String.format("[%s: %s]",
                        table[i].key.toString().substring(0, Math.min(5, table[i].key.toString().length())),
                        table[i].value);
            }
            System.out.printf("Index %3d: %-15s", i, content);
            if ((i + 1) % 4 == 0) System.out.println();
        }
        if (table.length > limit) {
            System.out.println("... (truncated, total: " + table.length + " entries)");
        }
        if (table.length % 4 != 0) System.out.println();
    }

    // Test og demonstration
    public static void main(String[] args) {
        System.out.println("=== CUCKOO HASHING DEMONSTRATION ===");

        // Test 1: Basic operations
        System.out.println("\n1. BASIC OPERATIONS TEST");
        testBasicOperations();

        // Test 2: Load factor test
        System.out.println("\n\n2. LOAD FACTOR TEST (target: 50% load)");
        testLoadFactor();

        // Test 3: Cuckoo evakuerings demonstration
        System.out.println("\n\n3. CUCKOO EVACUATION DEMO");
        testCuckooEvacuation();
    }

    private static void testBasicOperations() {
        CuckooHashing<String, Integer> table = new CuckooHashing<>(10);

        String[] names = {"Alice", "Bob", "Charlie", "Diana", "Eve",
                "Frank", "Grace", "Henry", "Ivy", "Jack"};

        for (int i = 0; i < names.length; i++) {
            table.put(names[i], 20 + i);
        }

        table.printStatus();
        table.printTables();

        System.out.println("\nTest operations:");
        for (String name : names) {
            System.out.println(name + ": " + table.get(name) +
                    " (contains: " + table.containsKey(name) + ")");
        }

        System.out.println("\nRemoving Bob and Eve:");
        table.remove("Bob");
        table.remove("Eve");
        table.printStatus();
    }

    private static void testLoadFactor() {
        System.out.println("Simulating load factor growth (starting capacity: 8):");
        CuckooHashing<String, Integer> table = new CuckooHashing<>(8);

        // Brug strings for bedre hash distribution
        String[] keys = {"alpha", "beta", "gamma", "delta", "epsilon", "zeta",
                "eta", "theta", "iota", "kappa", "lambda", "mu",
                "nu", "xi", "omicron", "pi", "rho", "sigma",
                "tau", "upsilon", "phi", "chi", "psi", "omega", "extra"};

        for (int i = 0; i < Math.min(keys.length, 25); i++) {
            table.put(keys[i], i + 1);

            if (i < 10 || (i + 1) % 5 == 0) {
                int t1 = table.countOccupied(table.table1);
                int t2 = table.countOccupied(table.table2);
                System.out.printf("Insert %2d: Size=%2d, Load=%.2f, Capacity=%d, T1=%d, T2=%d%n",
                        i + 1, table.size(), table.getLoadFactor(), table.capacity(), t1, t2);
            }
        }
        table.printStatus();
    }

    private static void testCuckooEvacuation() {
        System.out.println("Demonstrating Cuckoo displacement:");

        CuckooHashing<Integer, String> table = new CuckooHashing<>(4,
                (key, capacity) -> key % capacity,
                (key, capacity) -> (key * 2) % capacity
        );

        System.out.println("Inserting 1, 5, 9 (these will collide):");
        table.put(1, "A");
        table.put(5, "B");
        table.put(9, "C");
        table.printTables();

        System.out.println("\nCurrent get operations:");
        System.out.println("get(1): " + table.get(1));
        System.out.println("get(5): " + table.get(5));
        System.out.println("get(9): " + table.get(9));

        System.out.println("\nInserting 13 (causes rehash):");
        table.put(13, "D");
        table.printStatus();
    }
}