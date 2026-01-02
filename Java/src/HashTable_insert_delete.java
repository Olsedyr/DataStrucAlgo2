import java.util.*;

public class HashTable_insert_delete {

    public enum Method {
        LINEAR, QUADRATIC, DOUBLE, CHAINING
    }

    static class HashTable {
        private int tableSize;
        private Method method;
        private Integer[] table; // For linear, quadratic, double hashing
        private List<Integer>[] chains; // For chaining
        private int hashIndex; // Fixed start index (if any), else use hash function
        private int secondaryHash; // For double hashing; if 0, compute default
        private List<Character> insertionOrder;

        @SuppressWarnings("unchecked")
        public HashTable(int size, Method method, Integer hashIndex, Integer secondaryHash) {
            this.tableSize = size;
            this.method = method;
            this.hashIndex = (hashIndex == null) ? -1 : hashIndex;
            this.secondaryHash = (secondaryHash == null) ? 0 : secondaryHash;
            this.insertionOrder = new ArrayList<>();

            if (method == Method.CHAINING) {
                chains = new List[tableSize];
                for (int i = 0; i < tableSize; i++) {
                    chains[i] = new ArrayList<>();
                }
            } else {
                table = new Integer[tableSize];
                Arrays.fill(table, null);
            }
        }

        // Convert char to key (A=1, B=2, ...)
        private int charToKey(char c) {
            return Character.toUpperCase(c) - 'A' + 1;
        }

        // Convert key to char (1=A, 2=B, ...)
        private char keyToChar(int k) {
            return (char) ('A' + k - 1);
        }

        // Default hash function
        private int hashFunction(int key) {
            return key % tableSize;
        }

        // Secondary hash for double hashing
        private int getSecondaryHash(int key) {
            if (secondaryHash != 0) return secondaryHash;
            return 1 + (key % (tableSize - 1));
        }

        // Manual insert at specific index (for given table)
        public void manualInsert(int index, char c) {
            int key = charToKey(c);
            insertionOrder.add(c);
            if (method == Method.CHAINING) {
                chains[index].add(key);
                System.out.println("Manually inserted '" + c + "' at index " + index + " in chaining.");
            } else {
                table[index] = key;
                System.out.println("Manually inserted '" + c + "' at index " + index + ".");
            }
        }

        // Automatic insert based on method and hashing
        public void insert(char c) {
            int key = charToKey(c);
            insertionOrder.add(c);
            int startIdx = (hashIndex >= 0) ? hashIndex : hashFunction(key);
            int idx = -1;

            switch (method) {
                case LINEAR:
                    idx = insertLinear(key, startIdx);
                    break;
                case QUADRATIC:
                    idx = insertQuadratic(key, startIdx);
                    break;
                case DOUBLE:
                    idx = insertDoubleHashing(key, startIdx);
                    break;
                case CHAINING:
                    idx = insertChaining(key, startIdx);
                    break;
            }
            System.out.println("Inserted '" + c + "' (key=" + key + ") at index " + idx + " using " + method + " method.");
        }

        private int insertLinear(int key, int startIdx) {
            int idx = startIdx;
            while (table[idx] != null) {
                idx = (idx + 1) % tableSize;
            }
            table[idx] = key;
            return idx;
        }

        private int insertQuadratic(int key, int startIdx) {
            int i = 0;
            while (i <= tableSize) {
                int idx = (startIdx + i * i) % tableSize;
                if (table[idx] == null) {
                    table[idx] = key;
                    return idx;
                }
                i++;
            }
            return -1; // Table full
        }

        private int insertDoubleHashing(int key, int startIdx) {
            int h2 = getSecondaryHash(key);
            int i = 0;
            while (i <= tableSize) {
                int idx = (startIdx + i * h2) % tableSize;
                if (table[idx] == null) {
                    table[idx] = key;
                    return idx;
                }
                i++;
            }
            return -1; // Table full
        }

        private int insertChaining(int key, int startIdx) {
            chains[startIdx].add(key);
            return startIdx;
        }

        // Display current hash table
        public void display() {
            System.out.println("\nCurrent Hash Table:");
            if (method == Method.CHAINING) {
                for (int i = 0; i < tableSize; i++) {
                    if (chains[i].isEmpty()) {
                        System.out.printf("%2d : #%n", i);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (int k : chains[i]) {
                            sb.append(keyToChar(k));
                            sb.append(",");
                        }
                        sb.deleteCharAt(sb.length()-1); // Remove last comma
                        System.out.printf("%2d : %s%n", i, sb.toString());
                    }
                }
            } else {
                for (int i = 0; i < tableSize; i++) {
                    if (table[i] == null) {
                        System.out.printf("%2d : #%n", i);
                    } else {
                        System.out.printf("%2d : %c%n", i, keyToChar(table[i]));
                    }
                }
            }
        }

        // Get current table keys (for error checking)
        public Map<Character, Integer> getPositions() {
            Map<Character, Integer> map = new LinkedHashMap<>();
            if (method == Method.CHAINING) {
                for (int i = 0; i < tableSize; i++) {
                    for (int k : chains[i]) {
                        map.put(keyToChar(k), i);
                    }
                }
            } else {
                for (int i = 0; i < tableSize; i++) {
                    if (table[i] != null) {
                        map.put(keyToChar(table[i]), i);
                    }
                }
            }
            return map;
        }

        public List<Character> getInsertionOrder() {
            return insertionOrder;
        }

        // Find error for linear probing
        public void findErrorLinear(int hashIndex) {
            System.out.println("\n--- ANALYZING TABLE with LINEAR PROBING ---");
            Map<Character, Integer> pos = getPositions();
            List<Character> order = getInsertionOrder();

            System.out.println("Current positions:");
            for (char c : order) {
                if (pos.containsKey(c)) {
                    System.out.printf("  '%c' at index %d%n", c, pos.get(c));
                }
            }

            int tableSize = this.tableSize;

            for (char suspect : order) {
                System.out.println("\nTrying suspect: '" + suspect + "'");
                Set<Integer> occupied = new HashSet<>();
                boolean allCorrect = true;

                int nextIdx = hashIndex;
                for (char c : order) {
                    if (c == suspect) continue;

                    while (occupied.contains(nextIdx)) {
                        nextIdx = (nextIdx + 1) % tableSize;
                    }
                    if (!pos.containsKey(c) || pos.get(c) != nextIdx) {
                        System.out.printf("  '%c' at %d, expected %d - mismatch%n", c, pos.getOrDefault(c, -1), nextIdx);
                        allCorrect = false;
                        break;
                    }
                    occupied.add(nextIdx);
                    nextIdx = (nextIdx + 1) % tableSize;
                }
                if (allCorrect) {
                    int suspectIdx = pos.get(suspect);
                    int correctIdx = hashIndex;
                    while (occupied.contains(correctIdx)) {
                        correctIdx = (correctIdx + 1) % tableSize;
                    }
                    System.out.println("ERROR FOUND:");
                    System.out.printf("  Element: '%c'%n", suspect);
                    System.out.printf("  Currently at: %d%n", suspectIdx);
                    System.out.printf("  Should be at: %d%n", correctIdx);
                    return;
                }
            }
            System.out.println("No single error found.");
        }

        // Find error for quadratic probing
        public void findErrorQuadratic(int hashIndex) {
            System.out.println("\n--- ANALYZING TABLE with QUADRATIC PROBING ---");
            Map<Character, Integer> pos = getPositions();
            List<Character> order = getInsertionOrder();

            System.out.println("Current positions:");
            for (char c : order) {
                if (pos.containsKey(c)) {
                    System.out.printf("  '%c' at index %d%n", c, pos.get(c));
                }
            }

            int tableSize = this.tableSize;

            for (char suspect : order) {
                System.out.println("\nTrying suspect: '" + suspect + "'");
                Set<Integer> occupied = new HashSet<>();
                boolean allCorrect = true;

                for (char c : order) {
                    if (c == suspect) continue;
                    int currentIdx = pos.get(c);

                    int i = 0;
                    int expectedIdx = -1;
                    while (i <= tableSize) {
                        int probeIdx = (hashIndex + i * i) % tableSize;
                        if (!occupied.contains(probeIdx)) {
                            expectedIdx = probeIdx;
                            occupied.add(probeIdx);
                            break;
                        }
                        i++;
                    }

                    if (expectedIdx != currentIdx) {
                        System.out.printf("  '%c' at %d, expected %d - mismatch%n", c, currentIdx, expectedIdx);
                        allCorrect = false;
                        break;
                    }
                }

                if (allCorrect) {
                    int suspectIdx = pos.get(suspect);
                    int i = 0;
                    int suspectCorrect = -1;
                    while (i <= tableSize) {
                        int probeIdx = (hashIndex + i * i) % tableSize;
                        if (!occupied.contains(probeIdx)) {
                            suspectCorrect = probeIdx;
                            break;
                        }
                        i++;
                    }
                    System.out.println("ERROR FOUND:");
                    System.out.printf("  Element: '%c'%n", suspect);
                    System.out.printf("  Currently at: %d%n", suspectIdx);
                    System.out.printf("  Should be at: %d%n", suspectCorrect);
                    System.out.printf("  Calculation: (%d + %d^2) mod %d = %d%n", hashIndex, i, tableSize, suspectCorrect);
                    return;
                }
            }
            System.out.println("No single error found.");
        }

        // Find error for double hashing
        public void findErrorDoubleHashing(int hashIndex) {
            System.out.println("\n--- ANALYZING TABLE with DOUBLE HASHING ---");
            Map<Character, Integer> pos = getPositions();
            List<Character> order = getInsertionOrder();

            System.out.println("Current positions:");
            for (char c : order) {
                if (pos.containsKey(c)) {
                    System.out.printf("  '%c' at index %d%n", c, pos.get(c));
                }
            }

            int tableSize = this.tableSize;

            for (char suspect : order) {
                System.out.println("\nTrying suspect: '" + suspect + "'");
                Set<Integer> occupied = new HashSet<>();
                boolean allCorrect = true;

                for (char c : order) {
                    if (c == suspect) continue;
                    int currentIdx = pos.get(c);
                    int key = charToKey(c);

                    int h2 = getSecondaryHash(key);

                    int i = 0;
                    int expectedIdx = -1;
                    while (i <= tableSize) {
                        int probeIdx = (hashIndex + i * h2) % tableSize;
                        if (!occupied.contains(probeIdx)) {
                            expectedIdx = probeIdx;
                            occupied.add(probeIdx);
                            break;
                        }
                        i++;
                    }

                    if (expectedIdx != currentIdx) {
                        System.out.printf("  '%c' at %d, expected %d - mismatch%n", c, currentIdx, expectedIdx);
                        allCorrect = false;
                        break;
                    }
                }

                if (allCorrect) {
                    int suspectIdx = pos.get(suspect);
                    int key = charToKey(suspect);
                    int h2 = getSecondaryHash(key);
                    int i = 0;
                    int suspectCorrect = -1;
                    while (i <= tableSize) {
                        int probeIdx = (hashIndex + i * h2) % tableSize;
                        if (!occupied.contains(probeIdx)) {
                            suspectCorrect = probeIdx;
                            break;
                        }
                        i++;
                    }
                    System.out.println("ERROR FOUND:");
                    System.out.printf("  Element: '%c'%n", suspect);
                    System.out.printf("  Currently at: %d%n", suspectIdx);
                    System.out.printf("  Should be at: %d%n", suspectCorrect);
                    System.out.printf("  h2(%d) = %d%n", key, h2);
                    System.out.printf("  Calculation: (%d + %d * %d) mod %d = %d%n", hashIndex, i, h2, tableSize, suspectCorrect);
                    return;
                }
            }
            System.out.println("No single error found.");
        }

        // Find error for chaining (checks if all elements that hash to hashIndex are in the chain at hashIndex)
        public void findErrorChaining(int hashIndex) {
            System.out.println("\n--- ANALYZING TABLE with CHAINING ---");
            List<Integer> chainAtIndex = chains[hashIndex];
            List<Character> order = getInsertionOrder();

            Set<Character> atHash = new HashSet<>();
            Set<Character> elsewhere = new HashSet<>();

            for (int i = 0; i < tableSize; i++) {
                if (i == hashIndex) {
                    for (int k : chains[i]) {
                        atHash.add(keyToChar(k));
                    }
                } else {
                    for (int k : chains[i]) {
                        elsewhere.add(keyToChar(k));
                    }
                }
            }

            System.out.println("Elements at index " + hashIndex + ": " + atHash);
            System.out.println("Elements elsewhere: " + elsewhere);

            // Check if elements that hash to hashIndex are only in chain at hashIndex
            // We assume insertionOrder are all keys that hash to hashIndex (as per your setup)
            Set<Character> expectedSet = new HashSet<>(order);

            boolean errorFound = false;
            for (Character c : elsewhere) {
                if (expectedSet.contains(c)) {
                    System.out.println("ERROR FOUND: '" + c + "' is at wrong index");
                    errorFound = true;
                }
            }
            if (!errorFound) {
                System.out.println("All elements correctly placed!");
            }
        }
    }

    // ====== MAIN ======
    public static void main(String[] args) {
        int tableSize = 17;
        Method method = Method.QUADRATIC; // Change to LINEAR, DOUBLE, CHAINING as needed
        int hashIndex = 5; // Set to fixed hash index (if needed), else -1 for normal hash function
        Integer secondaryHash = null; // For double hashing: null to use default, else specify int

        System.out.println("=== Example 1: Manual insertion ===");
        HashTable ht = new HashTable(tableSize, method, hashIndex, secondaryHash);

        // Manual insertion at indices (from your example)
        ht.manualInsert(3, 'G');
        ht.manualInsert(4, 'F');
        ht.manualInsert(5, 'C');
        ht.manualInsert(6, 'L');
        ht.manualInsert(8, 'P');
        ht.manualInsert(9, 'U');
        ht.manualInsert(13, 'Y');
        ht.manualInsert(14, 'M');

        ht.display();

        switch (method) {
            case LINEAR -> ht.findErrorLinear(hashIndex);
            case QUADRATIC -> ht.findErrorQuadratic(hashIndex);
            case DOUBLE -> ht.findErrorDoubleHashing(hashIndex);
            case CHAINING -> ht.findErrorChaining(hashIndex);
        }

        System.out.println("\n=== Example 2: Auto insertion ===");
        HashTable ht2 = new HashTable(tableSize, method, hashIndex, secondaryHash);

        char[] elements = {'G', 'F', 'C', 'L', 'P', 'U', 'Y', 'M'};
        for (char c : elements) {
            ht2.insert(c);
        }

        ht2.display();
    }
}
