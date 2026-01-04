public class HopScotch<K, V> {
    // Entry klasse til at gemme nøgle-værdi par
    private static class Entry<K, V> {
        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Tabellen med entries
    private Entry<K, V>[] table;
    // Hop information - bitmap for hvert bucket
    private int[] hop;
    private int capacity;
    private int size;
    private final int HOP_RANGE; // H = 4 (standard hopscotch neighborhood size)
    private final double MAX_LOAD_FACTOR = 0.75;

    // Hash funktion
    private final HashFunction<K> hashFunction;

    // Flag til verbose output
    private boolean verbose = false;

    public interface HashFunction<K> {
        int hash(K key, int capacity);
    }

    // Default hash funktion
    public static class DefaultHash<K> implements HashFunction<K> {
        public int hash(K key, int capacity) {
            int h = key.hashCode();
            h ^= (h >>> 16);
            return Math.abs(h) % capacity;
        }
    }

    // Konstruktør med default hash og HOP_RANGE
    @SuppressWarnings("unchecked")
    public HopScotch(int initialCapacity) {
        this(initialCapacity, 4, new DefaultHash<>());
    }

    // Konstruktør med custom HOP_RANGE
    @SuppressWarnings("unchecked")
    public HopScotch(int initialCapacity, int hopRange) {
        this(initialCapacity, hopRange, new DefaultHash<>());
    }

    // Konstruktør med custom hash og HOP_RANGE
    @SuppressWarnings("unchecked")
    public HopScotch(int initialCapacity, int hopRange, HashFunction<K> hashFunction) {
        this.capacity = Math.max(initialCapacity, 16);
        this.HOP_RANGE = hopRange;
        this.table = (Entry<K, V>[]) new Entry[capacity];
        this.hop = new int[capacity];
        this.size = 0;
        this.hashFunction = hashFunction;
    }

    // Sæt verbose mode
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    // Indsæt nøgle-værdi par MED DETALJERET OUTPUT
    public boolean put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        int homeIndex = hashFunction.hash(key, capacity);

        if (verbose) {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("INDSÆTTER: " + key + " (værdi: " + value + ")");
            System.out.println("Hasher til indeks: " + homeIndex);
            System.out.println("=".repeat(60));
        }

        // Tjek om nøglen allerede findes
        if (containsKey(key)) {
            if (verbose) System.out.println("→ Nøgle findes allerede, opdaterer værdi...");
            return updateExistingKey(key, value);
        }

        // Tjek load factor
        if (getLoadFactor() >= MAX_LOAD_FACTOR) {
            if (verbose) {
                System.out.println("⚠ Load factor " + String.format("%.2f", getLoadFactor()) +
                        " >= " + MAX_LOAD_FACTOR);
                System.out.println("→ Udfører REHASHING...");
            }
            rehash();
            homeIndex = hashFunction.hash(key, capacity);
        }

        // Forsøg at indsætte
        boolean success = insertEntry(key, value, homeIndex);

        if (success) {
            size++;
            if (verbose) {
                System.out.println("✓ INDSÆTTELSE LYKKEDES!");
                printNeighborhood(homeIndex);
            }
        } else {
            if (verbose) {
                System.out.println("✗ INDSÆTTELSE FEJLEDE!");
                System.out.println("→ Kræver REHASHING for at gøre plads");
            }
            rehash();
            homeIndex = hashFunction.hash(key, capacity);
            success = insertEntry(key, value, homeIndex);
            if (success) {
                size++;
                if (verbose) System.out.println("✓ Indsættelse lykkedes efter rehashing!");
            }
        }

        return success;
    }

    // Indsæt entry med Hopscotch algoritme OG DETALJERET FORKLARING
    private boolean insertEntry(K key, V value, int homeIndex) {
        if (verbose) {
            System.out.println("\n--- INDSÆTTELSESPROCES ---");
        }

        // Tjek om home position er tom
        if (table[homeIndex] == null) {
            if (verbose) {
                System.out.println("→ Home position [" + homeIndex + "] er TOM");
                System.out.println("→ Indsætter direkte på home position");
            }
            table[homeIndex] = new Entry<>(key, value);
            hop[homeIndex] |= 1; // Sæt bit 0 (element på home position)
            return true;
        }

        if (verbose) {
            System.out.println("→ Home position [" + homeIndex + "] er OPTAGET af: " +
                    table[homeIndex].key);
        }

        // Find første tomme plads
        int emptyIndex = findEmptySlot(homeIndex);

        if (emptyIndex == -1) {
            if (verbose) {
                System.out.println("✗ FEJL: Ingen tom plads fundet i hele tabellen!");
            }
            return false;
        }

        if (verbose) {
            System.out.println("→ Første tomme plads fundet ved indeks: " + emptyIndex);
            System.out.println("→ Afstand fra home: " + (emptyIndex - homeIndex));
        }

        // Hvis tom plads er inden for HOP_RANGE, indsæt direkte
        if (emptyIndex - homeIndex < HOP_RANGE) {
            if (verbose) {
                System.out.println("→ Tom plads er inden for HOP_RANGE (" + HOP_RANGE + ")");
                System.out.println("→ Kan indsætte direkte!");
            }
            table[emptyIndex] = new Entry<>(key, value);
            hop[homeIndex] |= (1 << (emptyIndex - homeIndex));
            return true;
        }

        // Ellers: flyt den tomme plads tættere på via displacement
        if (verbose) {
            System.out.println("→ Tom plads er UDEN FOR HOP_RANGE!");
            System.out.println("→ Starter DISPLACEMENT for at flytte tom plads tættere...\n");
        }

        int displacementCount = 0;
        while (emptyIndex - homeIndex >= HOP_RANGE) {
            displacementCount++;

            if (verbose) {
                System.out.println("  DISPLACEMENT #" + displacementCount + ":");
                System.out.println("  Tom plads ved: " + emptyIndex);
                System.out.println("  Skal flyttes til max indeks: " + (homeIndex + HOP_RANGE - 1));
            }

            int moveFromIndex = findMoveCandidate(emptyIndex);

            if (moveFromIndex == -1) {
                if (verbose) {
                    System.out.println("\n  ✗ KAN IKKE FINDE ELEMENT AT FLYTTE!");
                    System.out.println("  → Alle elementer i rækkevidde er allerede ved grænsen");
                    System.out.println("  → af deres ejers HOP_RANGE");
                    System.out.println("\n  KONKLUSION: Displacement fejlede - kræver REHASHING!");
                }
                return false;
            }

            if (verbose) {
                System.out.println("  → Fandt element at flytte ved indeks: " + moveFromIndex);
                System.out.println("  → Element: " + table[moveFromIndex].key);
            }

            // Find ejeren
            int owner = findOwner(moveFromIndex);

            if (verbose) {
                System.out.println("  → Ejer (home) af dette element: indeks " + owner);
                System.out.println("  → Flytter " + table[moveFromIndex].key +
                        " fra [" + moveFromIndex + "] til [" + emptyIndex + "]");
            }

            // Flyt elementet
            table[emptyIndex] = table[moveFromIndex];

            // Opdater hop bitmap
            hop[owner] &= ~(1 << (moveFromIndex - owner)); // Clear old bit
            hop[owner] |= (1 << (emptyIndex - owner));     // Set new bit

            // Den gamle position er nu tom
            table[moveFromIndex] = null;
            emptyIndex = moveFromIndex;

            if (verbose) {
                System.out.println("  ✓ Flytning komplet. Ny tom plads: " + emptyIndex);
                System.out.println("  Ny afstand fra home: " + (emptyIndex - homeIndex) + "\n");
            }
        }

        // Nu er emptyIndex inden for HOP_RANGE af homeIndex
        if (verbose) {
            System.out.println("✓ Tom plads er nu inden for HOP_RANGE!");
            System.out.println("→ Indsætter " + key + " ved indeks: " + emptyIndex);
            System.out.println("→ Total antal displacements: " + displacementCount);
        }

        table[emptyIndex] = new Entry<>(key, value);
        hop[homeIndex] |= (1 << (emptyIndex - homeIndex));
        return true;
    }

    // Find første tomme slot fra startIndex
    private int findEmptySlot(int startIndex) {
        for (int i = startIndex; i < capacity; i++) {
            if (table[i] == null) {
                return i;
            }
        }
        return -1;
    }

    // Find en kandidat der kan flyttes til emptyIndex
    private int findMoveCandidate(int emptyIndex) {
        // Kig HOP_RANGE-1 positioner tilbage
        int start = Math.max(0, emptyIndex - (HOP_RANGE - 1));

        for (int i = start; i < emptyIndex; i++) {
            if (table[i] == null) continue;

            // Find ejeren af dette element
            int owner = findOwner(i);

            // Tjek om vi kan flytte dette element til emptyIndex
            // Det kan vi hvis emptyIndex stadig er inden for ejerens HOP_RANGE
            if (owner != -1 && emptyIndex - owner < HOP_RANGE) {
                return i;
            }
        }
        return -1;
    }

    // Find ejeren (home index) af et element på position index
    private int findOwner(int index) {
        // Kig HOP_RANGE-1 positioner tilbage for at finde ejeren
        int start = Math.max(0, index - (HOP_RANGE - 1));

        for (int i = start; i <= index; i++) {
            int offset = index - i;
            if ((hop[i] & (1 << offset)) != 0) {
                return i;
            }
        }
        return -1;
    }

    // Hent værdi for nøgle
    public V get(K key) {
        int homeIndex = hashFunction.hash(key, capacity);

        // Tjek alle positioner i neighborhood (HOP_RANGE)
        for (int i = 0; i < HOP_RANGE; i++) {
            if ((hop[homeIndex] & (1 << i)) != 0) {
                int actualIndex = homeIndex + i;
                if (actualIndex < capacity && table[actualIndex] != null &&
                        table[actualIndex].key.equals(key)) {
                    return table[actualIndex].value;
                }
            }
        }
        return null;
    }

    // Tjek om nøgle findes
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    // Opdater eksisterende nøgle
    private boolean updateExistingKey(K key, V value) {
        int homeIndex = hashFunction.hash(key, capacity);

        for (int i = 0; i < HOP_RANGE; i++) {
            if ((hop[homeIndex] & (1 << i)) != 0) {
                int actualIndex = homeIndex + i;
                if (actualIndex < capacity && table[actualIndex] != null &&
                        table[actualIndex].key.equals(key)) {
                    table[actualIndex].value = value;
                    return true;
                }
            }
        }
        return false;
    }

    // Fjern nøgle
    public boolean remove(K key) {
        int homeIndex = hashFunction.hash(key, capacity);

        for (int i = 0; i < HOP_RANGE; i++) {
            if ((hop[homeIndex] & (1 << i)) != 0) {
                int actualIndex = homeIndex + i;
                if (actualIndex < capacity && table[actualIndex] != null &&
                        table[actualIndex].key.equals(key)) {
                    table[actualIndex] = null;
                    hop[homeIndex] &= ~(1 << i);
                    size--;
                    return true;
                }
            }
        }
        return false;
    }

    // Rehash
    private void rehash() {
        int oldCapacity = capacity;
        capacity = capacity * 2;

        @SuppressWarnings("unchecked")
        Entry<K, V>[] oldTable = table;

        table = (Entry<K, V>[]) new Entry[capacity];
        hop = new int[capacity];

        boolean oldVerbose = verbose;
        verbose = false; // Slå verbose fra under rehash

        size = 0;

        // Genindsæt alle elementer
        for (int i = 0; i < oldCapacity; i++) {
            if (oldTable[i] != null) {
                put(oldTable[i].key, oldTable[i].value);
            }
        }

        verbose = oldVerbose;

        if (verbose) {
            System.out.println("  → Rehashing komplet: " + oldCapacity + " → " + capacity);
        }
    }

    public int size() { return size; }
    public int capacity() { return capacity; }
    public double getLoadFactor() { return (double) size / capacity; }

    // Print neighborhood omkring et indeks
    private void printNeighborhood(int homeIndex) {
        System.out.println("\n--- NEIGHBORHOOD OMKRING INDEKS " + homeIndex + " ---");
        int start = Math.max(0, homeIndex - 2);
        int end = Math.min(capacity - 1, homeIndex + HOP_RANGE + 2);

        System.out.println("Indeks | Værdi        | Hop (binær)");
        System.out.println("-------|--------------|------------");

        for (int i = start; i <= end; i++) {
            String marker = (i == homeIndex) ? " ← HOME" : "";
            String value = (table[i] == null) ? "[EMPTY]" : "[" + table[i].key + "]";
            String hopBits = String.format("%" + HOP_RANGE + "s",
                    Integer.toBinaryString(hop[i])).replace(' ', '0');
            System.out.printf("%6d | %-12s | %s%s%n", i, value, hopBits, marker);
        }
        System.out.println();
    }

    // Visualisering af hele tabellen
    public void printTable() {
        printTable(0, Math.min(capacity, 30));
    }

    public void printTable(int start, int end) {
        System.out.println("\n=== HOPSCOTCH HASH TABLE ===");
        System.out.println("Size: " + size + ", Capacity: " + capacity +
                ", Load Factor: " + String.format("%.2f", getLoadFactor()));
        System.out.println("HOP_RANGE: " + HOP_RANGE);
        System.out.println("\nIndeks | Værdi        | Hop (binær) | Ejer");
        System.out.println("-------|--------------|-------------|------");

        end = Math.min(end, capacity);
        for (int i = start; i < end; i++) {
            String value = (table[i] == null) ? "[EMPTY]" :
                    "[" + table[i].key + ":" + table[i].value + "]";
            String hopBits = String.format("%" + HOP_RANGE + "s",
                    Integer.toBinaryString(hop[i])).replace(' ', '0');

            String owner = "";
            if (table[i] != null) {
                owner = String.valueOf(findOwner(i));
            }

            System.out.printf("%6d | %-12s | %s | %s%n", i, value, hopBits, owner);
        }
        if (end < capacity) {
            System.out.println("... (showing " + start + "-" + end + " of " + capacity + ")");
        }
        System.out.println();
    }


    //brug denne til opgave
    // LØSNING TIL EKSAMEN OPGAVE - DYNAMISK!
    public static void solveExamQuestion() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("EKSAMEN OPGAVE: HOPSCOTCH HASHING");
        System.out.println("=".repeat(70));

        // Opret tabel med custom hash til eksamen opgaven
        HopScotch<String, String> table = new HopScotch<>(60, 4,
                (key, capacity) -> {
                    switch(key) {
                        case "A": return 47;
                        case "B": return 51;
                        case "C": return 55;
                        case "D": return 51;
                        case "E": return 46;
                        case "F": return 51;
                        case "G": return 49;
                        case "H": return 53;
                        case "I": return 50;
                        case "J": return 51;
                        case "K": return 50; // Næste indsættelse til 50
                        default: return key.hashCode() % capacity;
                    }
                }
        );

        // SLÅ VERBOSE TIL for detaljeret output
        table.setVerbose(true);

        // Indsæt de 10 elementer
        String[] elements = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        System.out.println("\nINDSÆTTER FØRSTE 10 ELEMENTER:\n");

        for (String elem : elements) {
            table.put(elem, elem);
        }

        // Vis den resulterende tabel
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TABEL EFTER INDSÆTTELSE AF A-J:");
        System.out.println("=".repeat(70));
        table.printTable(45, 58);

        // Nu indsæt element der hasher til 50
        System.out.println("\n" + "=".repeat(70));
        System.out.println("FORSØGER AT INDSÆTTE K (HASHER TIL INDEKS 50)");
        System.out.println("=".repeat(70));

        table.put("K", "K");

        // Vis endelig tabel
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ENDELIG TABEL:");
        System.out.println("=".repeat(70));
        table.printTable(45, 60);
    }

    // Test program
    public static void main(String[] args) {
        // Kør eksamen opgave med fuld forklaring
        solveExamQuestion();

        System.out.println("\n\n" + "=".repeat(70));
        System.out.println("EKSTRA DEMO: DISPLACEMENT I AKTION");
        System.out.println("=".repeat(70));

        // Ekstra demo med simple tal
        HopScotch<Integer, String> demo = new HopScotch<>(16, 4,
                (key, capacity) -> key % capacity
        );
        demo.setVerbose(true);

        System.out.println("\nIndsætter tal der alle hasher til indeks 5:");
        demo.put(5, "A");   // Hasher til 5
        demo.put(21, "B");  // Hasher til 5
        demo.put(37, "C");  // Hasher til 5
        demo.put(53, "D");  // Hasher til 5

        demo.printTable(0, 16);
    }
}