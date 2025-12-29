public class HashCollisionHandlingDemo {

    // ==================== SEPARATE CHAINING ====================
    static class SeparateChaining {
        static class Node {
            int key;
            Node next;
            Node(int key) {
                this.key = key;
                this.next = null;
            }
        }

        private Node[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;

        public SeparateChaining(int capacity, double loadFactorThreshold) {
            this.capacity = capacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.size = 0;
            table = new Node[capacity];
        }

        private int hash(int key) {
            return key % capacity;
        }

        // DIREKTE INDSÆTTELSE uden hashing (til setup)
        public void setAt(int index, int key) {
            if (index >= 0 && index < capacity) {
                System.out.println("  Set index [" + index + "] = " + key);
                Node newNode = new Node(key);

                if (table[index] == null) {
                    table[index] = newNode;
                } else {
                    // Tilføj til enden af kæden
                    Node current = table[index];
                    while (current.next != null) {
                        current = current.next;
                    }
                    current.next = newNode;
                }
                size++;
            }
        }

        public boolean insert(int key) {
            if (loadFactorThreshold > 0 && (double) size / capacity >= loadFactorThreshold) {
                rehash();
            }

            int index = hash(key);
            System.out.println("\n  Insert " + key + ": hash(" + key + ") = " + key + " % " + capacity + " = " + index);

            // Check om key allerede findes i kæden
            Node current = table[index];
            int chainPos = 0;
            while (current != null) {
                System.out.println("    Chain position " + chainPos + ": " + current.key);
                if (current.key == key) {
                    System.out.println("    ✗ Key " + key + " already exists in chain");
                    return false;
                }
                current = current.next;
                chainPos++;
            }

            // Tilføj nyt element først i kæden (prepend)
            Node newNode = new Node(key);
            newNode.next = table[index];
            table[index] = newNode;
            size++;
            System.out.println("    ✓ Added " + key + " to chain at index [" + index + "] (chain length now: " + (chainPos + 1) + ")");
            return true;
        }

        public boolean search(int key) {
            int index = hash(key);
            System.out.println("\n  Search " + key + ": hash(" + key + ") = " + index);

            Node current = table[index];
            int chainPos = 0;
            while (current != null) {
                System.out.println("    Chain position " + chainPos + ": " + current.key);
                if (current.key == key) {
                    System.out.println("    ✓ Found at index [" + index + "], position " + chainPos + " in chain");
                    return true;
                }
                current = current.next;
                chainPos++;
            }
            System.out.println("    ✗ Not found");
            return false;
        }

        public boolean delete(int key) {
            int index = hash(key);
            System.out.println("\n  Delete " + key + ": hash(" + key + ") = " + index);

            if (table[index] == null) {
                System.out.println("    ✗ Not found (empty chain)");
                return false;
            }

            // Special case: første element i kæden
            if (table[index].key == key) {
                table[index] = table[index].next;
                size--;
                System.out.println("    ✓ Deleted from index [" + index + "] (was first in chain)");
                return true;
            }

            // Søg i resten af kæden
            Node current = table[index];
            int chainPos = 1;
            while (current.next != null) {
                System.out.println("    Chain position " + chainPos + ": " + current.next.key);
                if (current.next.key == key) {
                    current.next = current.next.next;
                    size--;
                    System.out.println("    ✓ Deleted from index [" + index + "], position " + chainPos + " in chain");
                    return true;
                }
                current = current.next;
                chainPos++;
            }
            System.out.println("    ✗ Not found");
            return false;
        }

        private void rehash() {
            System.out.println("\n  ⚠️  REHASHING (load=" + String.format("%.2f", (double)size/capacity) + " >= " + loadFactorThreshold + ")");
            int oldCapacity = capacity;
            Node[] oldTable = table;

            capacity = capacity * 2;
            table = new Node[capacity];
            size = 0;

            System.out.println("  Old capacity: " + oldCapacity + " -> New capacity: " + capacity);
            for (int i = 0; i < oldCapacity; i++) {
                Node current = oldTable[i];
                while (current != null) {
                    insert(current.key);
                    current = current.next;
                }
            }
            System.out.println("  ✓ Rehash complete\n");
        }

        public void display() {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║  SEPARATE CHAINING TABLE (capacity=" + capacity + ", size=" + size + ", load=" + String.format("%.2f", (double)size/capacity) + ")  ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            for (int i = 0; i < capacity; i++) {
                System.out.print("  [" + String.format("%2d", i) + "]: ");
                if (table[i] == null) {
                    System.out.println("empty");
                } else {
                    Node current = table[i];
                    while (current != null) {
                        System.out.print(current.key);
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
    }

    // ==================== LINEAR PROBING ====================
    static class LinearProbing {
        private Integer[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;
        private static final Integer DELETED = Integer.MIN_VALUE;

        public LinearProbing(int capacity, double loadFactorThreshold) {
            this.capacity = capacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.size = 0;
            table = new Integer[capacity];
        }

        private int hash(int key) {
            return key % capacity;
        }

        // DIREKTE INDSÆTTELSE uden hashing (til setup)
        public void setAt(int index, int key) {
            if (index >= 0 && index < capacity) {
                if (table[index] == null || table[index] == DELETED) {
                    table[index] = key;
                    size++;
                    System.out.println("  Set index [" + index + "] = " + key);
                } else {
                    System.out.println("  ⚠️  Index [" + index + "] already occupied by " + table[index]);
                }
            }
        }

        public boolean insert(int key) {
            if (loadFactorThreshold > 0 && (double) size / capacity >= loadFactorThreshold) {
                rehash();
            }

            int index = hash(key);
            int probes = 0;

            System.out.println("\n  Insert " + key + ": hash(" + key + ") = " + key + " % " + capacity + " = " + index);

            while (probes < capacity) {
                int currentIndex = (index + probes) % capacity;
                System.out.println("    Probe " + probes + ": check index [" + currentIndex + "] = " +
                        (table[currentIndex] == null ? "empty" :
                                table[currentIndex] == DELETED ? "DELETED" :
                                        table[currentIndex].toString()));

                if (table[currentIndex] == null || table[currentIndex] == DELETED) {
                    table[currentIndex] = key;
                    size++;
                    System.out.println("    ✓ Placed " + key + " at index [" + currentIndex + "] after " + probes + " probes");
                    return true;
                }
                if (table[currentIndex] == key) {
                    System.out.println("    ✗ Key " + key + " already exists at index [" + currentIndex + "]");
                    return false;
                }
                probes++;
            }
            System.out.println("    ❌ Table full! Could not insert " + key);
            return false;
        }

        public boolean search(int key) {
            int index = hash(key);
            int probes = 0;

            System.out.println("\n  Search " + key + ": hash(" + key + ") = " + index);

            while (probes < capacity) {
                int currentIndex = (index + probes) % capacity;
                System.out.println("    Probe " + probes + ": check index [" + currentIndex + "] = " +
                        (table[currentIndex] == null ? "empty" :
                                table[currentIndex] == DELETED ? "DELETED" :
                                        table[currentIndex].toString()));

                if (table[currentIndex] == null) {
                    System.out.println("    ✗ Not found (reached empty slot)");
                    return false;
                }
                if (table[currentIndex] == key) {
                    System.out.println("    ✓ Found at index [" + currentIndex + "]");
                    return true;
                }
                probes++;
            }
            System.out.println("    ✗ Not found (searched entire table)");
            return false;
        }

        public boolean delete(int key) {
            int index = hash(key);
            int probes = 0;

            System.out.println("\n  Delete " + key + ": hash(" + key + ") = " + index);

            while (probes < capacity) {
                int currentIndex = (index + probes) % capacity;
                System.out.println("    Probe " + probes + ": check index [" + currentIndex + "]");

                if (table[currentIndex] == null) {
                    System.out.println("    ✗ Not found");
                    return false;
                }
                if (table[currentIndex] == key) {
                    table[currentIndex] = DELETED;
                    size--;
                    System.out.println("    ✓ Deleted from index [" + currentIndex + "], marked as DELETED");
                    return true;
                }
                probes++;
            }
            return false;
        }

        private void rehash() {
            System.out.println("\n  ⚠️  REHASHING (load=" + String.format("%.2f", (double)size/capacity) + " >= " + loadFactorThreshold + ")");
            int oldCapacity = capacity;
            Integer[] oldTable = table;

            capacity = capacity * 2;
            table = new Integer[capacity];
            size = 0;

            System.out.println("  Old capacity: " + oldCapacity + " -> New capacity: " + capacity);
            for (Integer key : oldTable) {
                if (key != null && key != DELETED) {
                    insert(key);
                }
            }
            System.out.println("  ✓ Rehash complete\n");
        }

        public void display() {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║  LINEAR PROBING TABLE (capacity=" + capacity + ", size=" + size + ", load=" + String.format("%.2f", (double)size/capacity) + ")  ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            for (int i = 0; i < capacity; i++) {
                System.out.print("  [" + String.format("%2d", i) + "]: ");
                if (table[i] == null) {
                    System.out.println("empty");
                } else if (table[i] == DELETED) {
                    System.out.println("DELETED");
                } else {
                    System.out.println(table[i]);
                }
            }
            System.out.println();
        }
    }

    // ==================== QUADRATIC PROBING ====================
    static class QuadraticProbing {
        private Integer[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;
        private static final Integer DELETED = Integer.MIN_VALUE;

        public QuadraticProbing(int capacity, double loadFactorThreshold) {
            this.capacity = capacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.size = 0;
            table = new Integer[capacity];
        }

        private int hash(int key) {
            return key % capacity;
        }

        public void setAt(int index, int key) {
            if (index >= 0 && index < capacity) {
                if (table[index] == null || table[index] == DELETED) {
                    table[index] = key;
                    size++;
                    System.out.println("  Set index [" + index + "] = " + key);
                } else {
                    System.out.println("  ⚠️  Index [" + index + "] already occupied by " + table[index]);
                }
            }
        }

        public boolean insert(int key) {
            if (loadFactorThreshold > 0 && (double) size / capacity >= loadFactorThreshold) {
                rehash();
            }

            int index = hash(key);

            System.out.println("\n  Insert " + key + ": hash(" + key + ") = " + key + " % " + capacity + " = " + index);

            for (int i = 0; i < capacity; i++) {
                int offset = i * i;
                int currentIndex = (index + offset) % capacity;
                System.out.println("    Probe i=" + i + ": (" + index + " + " + i + "²) % " + capacity + " = " + currentIndex +
                        ", value = " + (table[currentIndex] == null ? "empty" :
                        table[currentIndex] == DELETED ? "DELETED" :
                                table[currentIndex].toString()));

                if (table[currentIndex] == null || table[currentIndex] == DELETED) {
                    table[currentIndex] = key;
                    size++;
                    System.out.println("    ✓ Placed " + key + " at index [" + currentIndex + "]");
                    return true;
                }
                if (table[currentIndex] == key) {
                    System.out.println("    ✗ Key " + key + " already exists");
                    return false;
                }
            }
            System.out.println("    ❌ Could not find empty slot!");
            return false;
        }

        public boolean search(int key) {
            int index = hash(key);

            System.out.println("\n  Search " + key + ": hash(" + key + ") = " + index);

            for (int i = 0; i < capacity; i++) {
                int currentIndex = (index + i * i) % capacity;
                System.out.println("    Probe i=" + i + ": check index [" + currentIndex + "]");

                if (table[currentIndex] == null) {
                    System.out.println("    ✗ Not found");
                    return false;
                }
                if (table[currentIndex] == key) {
                    System.out.println("    ✓ Found at index [" + currentIndex + "]");
                    return true;
                }
            }
            System.out.println("    ✗ Not found");
            return false;
        }

        public boolean delete(int key) {
            int index = hash(key);

            System.out.println("\n  Delete " + key + ": hash(" + key + ") = " + index);

            for (int i = 0; i < capacity; i++) {
                int currentIndex = (index + i * i) % capacity;

                if (table[currentIndex] == null) {
                    System.out.println("    ✗ Not found");
                    return false;
                }
                if (table[currentIndex] == key) {
                    table[currentIndex] = DELETED;
                    size--;
                    System.out.println("    ✓ Deleted from index [" + currentIndex + "]");
                    return true;
                }
            }
            return false;
        }

        private void rehash() {
            System.out.println("\n  ⚠️  REHASHING (load=" + String.format("%.2f", (double)size/capacity) + " >= " + loadFactorThreshold + ")");
            int oldCapacity = capacity;
            Integer[] oldTable = table;

            capacity = capacity * 2;
            table = new Integer[capacity];
            size = 0;

            for (Integer key : oldTable) {
                if (key != null && key != DELETED) {
                    insert(key);
                }
            }
            System.out.println("  ✓ Rehash complete\n");
        }

        public void display() {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║  QUADRATIC PROBING TABLE (capacity=" + capacity + ", size=" + size + ", load=" + String.format("%.2f", (double)size/capacity) + ")  ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            for (int i = 0; i < capacity; i++) {
                System.out.print("  [" + String.format("%2d", i) + "]: ");
                if (table[i] == null) {
                    System.out.println("empty");
                } else if (table[i] == DELETED) {
                    System.out.println("DELETED");
                } else {
                    System.out.println(table[i]);
                }
            }
            System.out.println();
        }
    }

    // ==================== DOUBLE HASHING ====================
    static class DoubleHashing {
        private Integer[] table;
        private int capacity;
        private int size;
        private double loadFactorThreshold;
        private static final Integer DELETED = Integer.MIN_VALUE;

        public DoubleHashing(int capacity, double loadFactorThreshold) {
            this.capacity = capacity;
            this.loadFactorThreshold = loadFactorThreshold;
            this.size = 0;
            table = new Integer[capacity];
        }

        private int hash1(int key) {
            return key % capacity;
        }

        private int hash2(int key) {
            return 1 + (key % (capacity - 1));
        }

        public void setAt(int index, int key) {
            if (index >= 0 && index < capacity) {
                if (table[index] == null || table[index] == DELETED) {
                    table[index] = key;
                    size++;
                    System.out.println("  Set index [" + index + "] = " + key);
                } else {
                    System.out.println("  ⚠️  Index [" + index + "] already occupied by " + table[index]);
                }
            }
        }

        public boolean insert(int key) {
            if (loadFactorThreshold > 0 && (double) size / capacity >= loadFactorThreshold) {
                rehash();
            }

            int index1 = hash1(key);
            int index2 = hash2(key);

            System.out.println("\n  Insert " + key + ": hash1(" + key + ") = " + index1 + ", hash2(" + key + ") = " + index2);

            for (int i = 0; i < capacity; i++) {
                int currentIndex = (index1 + i * index2) % capacity;

                System.out.println("    Probe i=" + i + ": (" + index1 + " + " + i + " * " + index2 + ") % " + capacity + " = " + currentIndex +
                        ", value = " + (table[currentIndex] == null ? "empty" :
                        table[currentIndex] == DELETED ? "DELETED" :
                                table[currentIndex].toString()));

                if (table[currentIndex] == null || table[currentIndex] == DELETED) {
                    table[currentIndex] = key;
                    size++;
                    System.out.println("    ✓ Placed " + key + " at index [" + currentIndex + "]");
                    return true;
                }
                if (table[currentIndex] == key) {
                    System.out.println("    ✗ Key " + key + " already exists");
                    return false;
                }
            }
            System.out.println("    ❌ Could not find empty slot!");
            return false;
        }

        public boolean search(int key) {
            int index1 = hash1(key);
            int index2 = hash2(key);

            System.out.println("\n  Search " + key + ": hash1(" + key + ") = " + index1 + ", hash2(" + key + ") = " + index2);

            for (int i = 0; i < capacity; i++) {
                int currentIndex = (index1 + i * index2) % capacity;
                System.out.println("    Probe i=" + i + ": check index [" + currentIndex + "]");

                if (table[currentIndex] == null) {
                    System.out.println("    ✗ Not found");
                    return false;
                }
                if (table[currentIndex] == key) {
                    System.out.println("    ✓ Found at index [" + currentIndex + "]");
                    return true;
                }
            }
            System.out.println("    ✗ Not found");
            return false;
        }

        public boolean delete(int key) {
            int index1 = hash1(key);
            int index2 = hash2(key);

            System.out.println("\n  Delete " + key + ": hash1(" + key + ") = " + index1 + ", hash2(" + key + ") = " + index2);

            for (int i = 0; i < capacity; i++) {
                int currentIndex = (index1 + i * index2) % capacity;

                if (table[currentIndex] == null) {
                    System.out.println("    ✗ Not found");
                    return false;
                }
                if (table[currentIndex] == key) {
                    table[currentIndex] = DELETED;
                    size--;
                    System.out.println("    ✓ Deleted from index [" + currentIndex + "]");
                    return true;
                }
            }
            return false;
        }

        private void rehash() {
            System.out.println("\n  ⚠️  REHASHING (load=" + String.format("%.2f", (double)size/capacity) + " >= " + loadFactorThreshold + ")");
            int oldCapacity = capacity;
            Integer[] oldTable = table;

            capacity = capacity * 2;
            table = new Integer[capacity];
            size = 0;

            for (Integer key : oldTable) {
                if (key != null && key != DELETED) {
                    insert(key);
                }
            }
            System.out.println("  ✓ Rehash complete\n");
        }

        public void display() {
            System.out.println("\n╔═══════════════════════════════════════════════════════╗");
            System.out.println("║  DOUBLE HASHING TABLE (capacity=" + capacity + ", size=" + size + ", load=" + String.format("%.2f", (double)size/capacity) + ")  ║");
            System.out.println("╚═══════════════════════════════════════════════════════╝");
            for (int i = 0; i < capacity; i++) {
                System.out.print("  [" + String.format("%2d", i) + "]: ");
                if (table[i] == null) {
                    System.out.println("empty");
                } else if (table[i] == DELETED) {
                    System.out.println("DELETED");
                } else {
                    System.out.println(table[i]);
                }
            }
            System.out.println();
        }
    }

    // ==================== TEST ====================
    public static void main(String[] args) {
        System.out.println("=== SEPARATE CHAINING ===");
        SeparateChaining sc = new SeparateChaining(11, 0.75);
        // Setup: læg 11 i index 0, og 0, 2, 4 i index 2 kæden:
        sc.setAt(0, 11);
        sc.setAt(2, 0);
        sc.setAt(2, 2);
        sc.setAt(2, 4);
        sc.display();

        // 8 hasher til index 8 (8 % 11 = 8), så ingen collision her
        sc.insert(8);
        // 13 hasher til index 2 (13 % 11 = 2), collision med 0,2,4, så ny node sættes i starten af kæden
        sc.insert(13);
        sc.display();

        System.out.println("=== LINEAR PROBING ===");
        LinearProbing lp = new LinearProbing(11, 0.75);
        // Setup manuelt:
        lp.setAt(2, 0);
        lp.setAt(3, 2);
        lp.setAt(4, 4);
        lp.display();

        // 8 hasher til 8, index 8 er tom, indsættes direkte
        lp.insert(8);
        // 13 hasher til 2, men index 2 er optaget (collision), prøver index 3 (optaget), index 4 (optaget), index 5 (ledig), indsætter der
        lp.insert(13);
        lp.display();

        System.out.println("=== QUADRATIC PROBING ===");
        QuadraticProbing qp = new QuadraticProbing(11, 0.75);
        qp.setAt(2, 0);
        qp.setAt(3, 2);
        qp.setAt(4, 4);
        qp.display();

        // 8 hasher til 8, indsættes direkte
        qp.insert(8);
        // 13 hasher til 2, prøver index 2 (optaget), 3^2=1, index (2+1)%11=3 (optaget), 2^2=4, index (2+4)%11=6 (ledig), indsættes på 6
        qp.insert(13);
        qp.display();

        System.out.println("=== DOUBLE HASHING ===");
        DoubleHashing dh = new DoubleHashing(11, 0.75);
        dh.setAt(2, 0);
        dh.setAt(3, 2);
        dh.setAt(4, 4);
        dh.display();

        // 8 hasher til index1=8, index2=1+ (8 % 10) = 1 + 8 = 9, index 8 er ledig
        dh.insert(8);
        // 13: index1=13 % 11=2, index2=1 + (13 % 10)=1+3=4
        // Prøver index2: (2 + 0*4)%11=2 (optaget), (2 + 1*4)%11=6 (ledig), indsætter der
        dh.insert(13);
        dh.display();
    }
}
