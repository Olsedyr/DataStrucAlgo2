import java.util.*;

public class HashCollisionHandlingAndRehashing {

    // ==================== KONFIGURATIONSKLASSE ====================
    static class HashConfig {
        int method;           // 1=Quadratic, 2=Linear, 3=Chaining, 4=Double
        int capacity;         // Tabelstørrelse
        double loadFactor;    // Load factor threshold
        List<Element> elements; // Elementer og deres hash-værdier
        boolean showSteps;    // Vis trin-for-trin output

        static class Element {
            int value;        // Elementværdi
            int hashIndex;    // Hash-værdi (index det hasher til)

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
        protected String[] table;
        protected int capacity;
        protected int size;
        protected double loadFactorThreshold;
        protected int nextY = 1;
        protected boolean showSteps;
        protected List<Element> failedElements = new ArrayList<>();

        static class Element {
            int value;
            int hashIndex;

            Element(int value, int hashIndex) {
                this.value = value;
                this.hashIndex = hashIndex;
            }
        }

        HashTable(int capacity, double loadFactorThreshold, boolean showSteps) {
            this.capacity = capacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.showSteps = showSteps;
            this.size = 0;
            this.table = new String[capacity];
        }

        abstract boolean insert(int element, int hashIndex);
        abstract HashTable rehash();

        protected void checkAndRehashIfNeeded() {
            if (getCurrentLoad() >= loadFactorThreshold) {
                if (showSteps) {
                    System.out.println("\n⚠️  Load factor overskredet: " +
                            String.format("%.3f", getCurrentLoad()) +
                            " >= " + loadFactorThreshold);
                }
            }
        }

        public boolean insertWithRehash(int element, int hashIndex) {
            boolean success = insert(element, hashIndex);

            if (!success) {
                failedElements.add(new Element(element, hashIndex));
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
                    System.out.printf("%3s", table[i]);
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
                    System.out.println("  Index " + i + ": " + table[i]);
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
                for (Element e : failedElements) {
                    System.out.println("  - " + e.value + " (hash index: " + e.hashIndex + ")");
                }
            }
        }

        public int getCapacity() { return capacity; }
        public int getSize() { return size; }
        public double getCurrentLoad() { return (double) size / capacity; }
        public List<Element> getFailedElements() { return failedElements; }
        public void clearFailedElements() { failedElements.clear(); }
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
                        " (hash index: " + hashIndex + ")");
            }

            checkAndRehashIfNeeded();

            int probe = 0;
            while (probe < capacity) {
                int index = (hashIndex + probe * probe) % capacity;

                if (showSteps && probe < 5) { // Vis kun første 5 probes for at undgå for meget output
                    System.out.println("  Probe " + probe + ": (" + hashIndex + " + " +
                            probe + "²) % " + capacity + " = " + index);
                }

                if (table[index] == null) {
                    table[index] = "Y" + nextY++;
                    size++;

                    if (showSteps) {
                        System.out.println("  ✓ Placeret som " + table[index] + " på index " + index);
                    }

                    return true;
                }

                probe++;
            }

            if (showSteps) {
                System.out.println("  ❌ Ingen ledig plads fundet efter " + capacity + " probes!");
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
            QuadraticProbing newTable = new QuadraticProbing(newCapacity, loadFactorThreshold, showSteps);

            // Indsæt alle eksisterende elementer i den nye tabel
            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    // Beregn ny hash baseret på Y-nummeret
                    int yNumber = Integer.parseInt(table[i].substring(1));
                    int newHash = yNumber % newCapacity;
                    newTable.insert(yNumber, newHash); // Brug Y-nummeret som element
                }
            }

            if (showSteps) {
                System.out.println("Ny kapacitet: " + newCapacity);
                System.out.println("Alle elementer er blevet overført til den nye tabel");
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

            int probe = 0;
            int index = hashIndex;

            while (probe < capacity) {
                if (table[index] == null) {
                    table[index] = "Y" + nextY++;
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
            LinearProbing newTable = new LinearProbing(newCapacity, loadFactorThreshold, showSteps);

            for (int i = 0; i < capacity; i++) {
                if (table[i] != null) {
                    int yNumber = Integer.parseInt(table[i].substring(1));
                    int newHash = yNumber % newCapacity;
                    newTable.insert(yNumber, newHash);
                }
            }

            return newTable;
        }
    }

    // ==================== HASH TABEL FABRIK ====================
    static HashTable createHashTable(HashConfig config) {
        boolean showSteps = config.showSteps;
        double loadFactor = config.loadFactor;
        int capacity = config.capacity;

        switch (config.method) {
            case 1:
                return new QuadraticProbing(capacity, loadFactor, showSteps);
            case 2:
                return new LinearProbing(capacity, loadFactor, showSteps);
            default:
                System.out.println("Bruger Quadratic Probing som standard");
                return new QuadraticProbing(capacity, loadFactor, showSteps);
        }
    }

    // ==================== KØR ANALYSE ====================
    static void runCompleteAnalysis(HashConfig config) {
        System.out.println("=== KOMPLET HASH TABEL ANALYSE ===");
        System.out.println("Metode: " + (config.method == 1 ? "Quadratic Probing" : "Linear Probing"));
        System.out.println("Start kapacitet: " + config.capacity);
        System.out.println("Load factor threshold: " + config.loadFactor);
        System.out.println("Antal elementer: " + config.elements.size());

        // Trin 1: Opret initial tabel og indsæt elementer
        System.out.println("\n" + "=".repeat(50));
        System.out.println("TRIN 1: INDSÆTTELSE I ORIGINAL TABEL");
        System.out.println("=".repeat(50));

        HashTable table = createHashTable(config);
        List<HashConfig.Element> remainingElements = new ArrayList<>(config.elements);

        // Indsæt så mange elementer som muligt
        for (int i = 0; i < remainingElements.size(); i++) {
            HashConfig.Element element = remainingElements.get(i);
            boolean success = table.insertWithRehash(element.value, element.hashIndex);

            if (!success) {
                // Fjern dette element fra listen over elementer der skal indsættes
                remainingElements.remove(i);
                i--; // Juster index fordi vi fjernede et element

                if (config.showSteps) {
                    System.out.println("\n⚠️  KAN IKKE INDSÆTTE FLERE ELEMENTER!");
                    System.out.println("Rehashing er nu uundgåeligt.");
                }
                break;
            }
        }

        // Vis resultatet FØR rehashing
        System.out.println("\n" + "=".repeat(50));
        System.out.println("RESULTAT FØR REHASHING");
        System.out.println("=".repeat(50));
        table.displayDetailed();

        // Tjek om vi skal rehashe
        if (!table.getFailedElements().isEmpty() || table.getCurrentLoad() >= config.loadFactor) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("TRIN 2: UDFØRER REHASHING");
            System.out.println("=".repeat(50));

            // Gem de elementer der ikke blev indsat
            List<HashTable.Element> failedElements = new ArrayList<>(table.getFailedElements());

            // Opret ny tabel gennem rehashing
            HashTable newTable = table.rehash();

            System.out.println("\n✅ Rehashing fuldført!");
            System.out.println("Ny tabel kapacitet: " + newTable.getCapacity());

            // Indsæt de elementer der ikke kunne indsættes før
            if (!failedElements.isEmpty()) {
                System.out.println("\nForsøger at indsætte de " + failedElements.size() +
                        " elementer der ikke kunne indsættes før:");

                for (HashTable.Element element : failedElements) {
                    boolean success = newTable.insertWithRehash(element.value, element.hashIndex);
                    if (success) {
                        System.out.println("  ✓ Indsat element " + element.value);
                    } else {
                        System.out.println("  ❌ Kunne stadig ikke indsætte element " + element.value);
                    }
                }
            }

            // Prøv også at indsætte resterende elementer fra den originale liste
            if (!remainingElements.isEmpty()) {
                System.out.println("\nForsøger at indsætte resterende " + remainingElements.size() +
                        " elementer fra den originale liste:");

                for (HashConfig.Element element : remainingElements) {
                    boolean success = newTable.insertWithRehash(element.value, element.hashIndex);
                    if (success) {
                        System.out.println("  ✓ Indsat element " + element.value);
                    } else {
                        System.out.println("  ❌ Kunne ikke indsætte element " + element.value);
                    }
                }
            }

            // Vis resultatet EFTER rehashing
            System.out.println("\n" + "=".repeat(50));
            System.out.println("RESULTAT EFTER REHASHING");
            System.out.println("=".repeat(50));
            newTable.displayDetailed();

            // Sammenfattende statistik
            System.out.println("\n" + "=".repeat(50));
            System.out.println("SAMMENFATTENDE STATISTIK");
            System.out.println("=".repeat(50));
            System.out.println("Før rehashing:");
            System.out.println("  - Kapacitet: " + table.getCapacity());
            System.out.println("  - Elementer indsat: " + table.getSize());
            System.out.println("  - Load factor: " + String.format("%.3f", table.getCurrentLoad()));
            System.out.println("  - Elementer ikke indsat: " + failedElements.size());

            System.out.println("\nEfter rehashing:");
            System.out.println("  - Kapacitet: " + newTable.getCapacity());
            System.out.println("  - Elementer indsat: " + newTable.getSize());
            System.out.println("  - Load factor: " + String.format("%.3f", newTable.getCurrentLoad()));
            System.out.println("  - Elementer ikke indsat: " + newTable.getFailedElements().size());

            if (newTable.getFailedElements().isEmpty()) {
                System.out.println("\n✅ Alle elementer er nu indsat succesfuldt!");
            } else {
                System.out.println("\n⚠️  Nogle elementer kunne stadig ikke indsættes.");
            }

        } else {
            System.out.println("\n✅ Ingen rehashing nødvendig - alle elementer indsat!");
        }
    }

    // ==================== MAIN ====================
    public static void main(String[] args) {
        System.out.println("=== EKSEMPEL: QUADRATIC PROBING MED REHASHING ===");

        // Konfiguration for den specifikke opgave
        HashConfig config = new HashConfig(
                1,           // Metode: Quadratic Probing
                13,          // Kapacitet: 13
                1.0,         // Load factor: 1.0 (kun rehash hvis fuld)
                true         // Vis trin-for-trin
        );

        // Tilføj elementer (A-I) der alle hasher til index 3
        int[] asciiValues = {65, 66, 67, 68, 69, 70, 71, 72, 73}; // A-I
        for (int value : asciiValues) {
            config.addElement(value, 3);  // Alle hasher til index 3
        }

        // Kør den komplette analyse
        runCompleteAnalysis(config);

        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("FORKLARING AF RESULTATET");
        System.out.println("=".repeat(60));
        System.out.println("""
            
            MED QUADRATIC PROBING OG m=13:
            - Start hash index for alle elementer: 3
            - Quadratic probing følger sekvensen:
                1. (3 + 0²) % 13 = 3
                2. (3 + 1²) % 13 = 4
                3. (3 + 2²) % 13 = 7
                4. (3 + 3²) % 13 = 12
                5. (3 + 4²) % 13 = 6
                6. (3 + 5²) % 13 = 2
                7. (3 + 6²) % 13 = 0
            
            - Efter 7 elementer er alle disse pladser optaget
            - Næste probe: (3 + 7²) % 13 = (3 + 49) % 13 = 0 (allerede brugt)
            - Quadratic probing kan IKKE nå de resterende tomme pladser (1, 5, 8, 9, 10, 11)
            
            DERFOR:
            - Rehashing er uundgåeligt efter 7 elementer
            - Efter rehashing (ny kapacitet 26) kan alle elementer indsættes
            - Den nye tabel har bedre fordeling og lavere load factor
            """);

        // Ekstra eksempel med forskellige konfigurationer
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("EKSTRA EKSEMPEL: MED LAVERE LOAD FACTOR");
        System.out.println("=".repeat(60));

        HashConfig config2 = new HashConfig(
                1,           // Quadratic Probing
                13,          // Kapacitet: 13
                0.5,         // Load factor: 0.5 (rehash tidligere)
                false        // Ingen detaljeret output
        );

        config2.addElementsSameHash(new int[]{1, 2, 3, 4, 5, 6, 7}, 2);

        // Kort analyse
        System.out.println("\nMed load factor 0.5 vil rehashing ske efter 6 elementer:");
        System.out.println("(6/13 = 0.46, 7/13 = 0.54 > 0.5)");
        System.out.println("Dette viser hvordan load factor påvirker rehashing tidspunktet.");
    }
}