import java.util.*;

public class HashCollisionHandlingAndRehashing_SameHashIndex {

    // ==================== KONFIGURATIONSKLASSE ====================
    static class HashConfig {
        int method;           // 1=Quadratic, 2=Linear
        int capacity;         // Tabelstørrelse
        double loadFactor;    // Load factor threshold
        List<Element> elements;
        boolean showSteps;

        static class Element {
            int value;
            int hashIndex;

            Element(int value, int hashIndex) {
                this.value = value;
                this.hashIndex = hashIndex;
            }
        }

        HashConfig(int method, int capacity, double loadFactor, boolean showSteps) {
            this.method = method;
            this.capacity = capacity;
            this.loadFactor = loadFactor;
            this.showSteps = showSteps;
            this.elements = new ArrayList<>();
        }

        void addElement(int value, int hashIndex) {
            elements.add(new Element(value, hashIndex));
        }

        void addElementsSameHash(int[] values, int hashIndex) {
            for (int value : values) {
                addElement(value, hashIndex);
            }
        }
    }

    // ==================== ABSTRAKT HASH-KLASSE ====================
    abstract static class HashTable {
        // Skiftet fra Integer[] til Entry[] for at kunne gemme både value og hashIndex
        protected Entry[] table;
        protected int capacity;
        protected int size;
        protected double loadFactorThreshold;
        protected boolean showSteps;
        protected List<HashConfig.Element> failedElements = new ArrayList<>();

        static class Entry {
            int value;
            int hashIndex;

            Entry(int value, int hashIndex) {
                this.value = value;
                this.hashIndex = hashIndex;
            }
        }

        HashTable(int capacity, double loadFactorThreshold, boolean showSteps) {
            this.capacity = capacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.showSteps = showSteps;
            this.size = 0;
            this.table = new Entry[capacity];
        }

        abstract boolean insert(int element, int hashIndex);
        abstract HashTable rehash();

        protected void checkAndRehashIfNeeded() {
            if (getCurrentLoad() >= loadFactorThreshold && showSteps) {
                System.out.println("\n⚠️  Load factor overskredet: " +
                        String.format("%.3f", getCurrentLoad()) +
                        " >= " + loadFactorThreshold);
            }
        }

        public boolean insertWithRehash(int element, int hashIndex) {
            boolean success = insert(element, hashIndex);

            if (!success) {
                failedElements.add(new HashConfig.Element(element, hashIndex));
                if (showSteps) {
                    System.out.println("  ❌ Kunne ikke indsætte element " + element);
                }
            }
            return success;
        }

        public void display(String title) {
            System.out.println("\n" + title);
            System.out.println("Kapacitet: " + capacity + ", Elementer: " + size +
                    ", Load: " + String.format("%.3f", getCurrentLoad()));

            System.out.print("Indeks:  ");
            for (int i = 0; i < capacity; i++) {
                System.out.printf("%3d", i);
            }

            System.out.print("\nTabel:   ");
            for (int i = 0; i < capacity; i++) {
                if (table[i] == null) {
                    System.out.print("  -");
                } else {
                    System.out.printf("%3c", (char)table[i].value);
                }
            }
            System.out.println("\n");
        }

        public void displayDetailed() {
            System.out.println("\n=== DETALJERET OVERSIGT ===");
            display("Hash Tabel:");

            System.out.println("Brugte pladser:");
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    System.out.println("  Index " + i + ": " + table[i].value +
                            " (" + (char)table[i].value + "), hashIndex: " + table[i].hashIndex);
                }
            }

            System.out.print("\nTomme pladser: ");
            boolean first = true;
            for (int i = 0; i < capacity; i++) {
                if (table[i] == null) {
                    if (!first) System.out.print(", ");
                    System.out.print(i);
                    first = false;
                }
            }
            System.out.println();

            if (!failedElements.isEmpty()) {
                System.out.println("\nElementer der ikke kunne indsættes: " + failedElements.size());
                for (HashConfig.Element e : failedElements) {
                    System.out.println("  - " + e.value + " (" + (char)e.value + "), hash index: " + e.hashIndex);
                }
            }
        }

        public int getCapacity() { return capacity; }
        public int getSize() { return size; }
        public double getCurrentLoad() { return (double) size / capacity; }
        public List<HashConfig.Element> getFailedElements() { return failedElements; }
    }

    // ==================== QUADRATIC PROBING ====================
    static class QuadraticProbing extends HashTable {

        public QuadraticProbing(int capacity, double loadFactorThreshold, boolean showSteps) {
            super(capacity, loadFactorThreshold, showSteps);
        }

        @Override
        public boolean insert(int element, int hashIndex) {
            if (showSteps) {
                System.out.println("\nForsøger at indsætte element " + element +
                        " (" + (char)element + "), hash index: " + hashIndex);
            }

            checkAndRehashIfNeeded();

            int probe = 0;
            while (probe < capacity) {
                int index = (hashIndex + probe * probe) % capacity;

                if (showSteps && probe < capacity) {
                    System.out.println("  Probe " + probe +
                            ": (" + hashIndex + " + " + probe + "²) % " +
                            capacity + " = " + index);
                }

                if (table[index] == null) {
                    table[index] = new Entry(element, hashIndex); // GEM hashIndex her!
                    size++;

                    if (showSteps) {
                        System.out.println("  ✓ Placeret på index " + index);
                    }
                    return true;
                }
                probe++;
            }

            if (showSteps) {
                System.out.println("  ❌ Ingen ledig plads fundet!");
            }
            return false;
        }

        @Override
        public QuadraticProbing rehash() {
            if (showSteps) {
                System.out.println("\n=== UDFØRER REHASHING ===");
                System.out.println("Gammel kapacitet: " + capacity);
            }

            int newCapacity = capacity * 2;
            QuadraticProbing newTable =
                    new QuadraticProbing(newCapacity, loadFactorThreshold, showSteps);

            // GENBRUG hashIndex ved indsættelse!
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    Entry entry = table[i];
                    newTable.insert(entry.value, entry.hashIndex);
                }
            }

            if (showSteps) {
                System.out.println("Ny kapacitet: " + newCapacity);
            }
            return newTable;
        }
    }

    // ==================== LINEAR PROBING ====================
    static class LinearProbing extends HashTable {

        public LinearProbing(int capacity, double loadFactorThreshold, boolean showSteps) {
            super(capacity, loadFactorThreshold, showSteps);
        }

        @Override
        public boolean insert(int element, int hashIndex) {
            checkAndRehashIfNeeded();

            int index = hashIndex;
            int probe = 0;

            while (probe < capacity) {
                if (table[index] == null) {
                    table[index] = new Entry(element, hashIndex); // GEM hashIndex her!
                    size++;
                    return true;
                }
                index = (index + 1) % capacity;
                probe++;
            }
            return false;
        }

        @Override
        public LinearProbing rehash() {
            int newCapacity = capacity * 2;
            LinearProbing newTable =
                    new LinearProbing(newCapacity, loadFactorThreshold, showSteps);

            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    Entry entry = table[i];
                    newTable.insert(entry.value, entry.hashIndex);
                }
            }
            return newTable;
        }
    }

    // ==================== FABRIK ====================
    static HashTable createHashTable(HashConfig config) {
        switch (config.method) {
            case 1: return new QuadraticProbing(config.capacity, config.loadFactor, config.showSteps);
            case 2: return new LinearProbing(config.capacity, config.loadFactor, config.showSteps);
            default: return new QuadraticProbing(config.capacity, config.loadFactor, config.showSteps);
        }
    }

    // ==================== ANALYSE ====================
    static void runCompleteAnalysis(HashConfig config) {
        HashTable table = createHashTable(config);

        for (HashConfig.Element e : config.elements) {
            if (!table.insertWithRehash(e.value, e.hashIndex)) break;
        }

        table.displayDetailed();
    }

    // ==================== MAIN ====================
    public static void main(String[] args) {
        HashConfig config = new HashConfig(1, 13, 1 , true);

        int[] asciiValues = {'C', 'L', 'P', 'U', 'Y', 'M', 'G', 'F','A','B','C','D','E',};
        for (int v : asciiValues) {
            config.addElement(v, 3);  // Alle har samme hash index 5
        }

        runCompleteAnalysis(config);
    }
}
