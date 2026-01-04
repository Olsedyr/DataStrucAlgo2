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
        private Integer secondaryHash; // For double hashing; use Integer instead of int
        private List<Character> insertionOrder;
        private boolean debugMode;

        @SuppressWarnings("unchecked")
        public HashTable(int size, Method method, Integer hashIndex, Integer secondaryHash, boolean debugMode) {
            this.tableSize = size;
            this.method = method;
            this.hashIndex = (hashIndex == null) ? -1 : hashIndex;
            this.secondaryHash = secondaryHash; // Keep as Integer
            this.insertionOrder = new ArrayList<>();
            this.debugMode = debugMode;

            if (method == Method.CHAINING) {
                chains = new List[tableSize];
                for (int i = 0; i < tableSize; i++) {
                    chains[i] = new ArrayList<>();
                }
            } else {
                table = new Integer[tableSize];
                Arrays.fill(table, null);
            }

            if (debugMode) {
                System.out.println("=== OPRETTET HASH-TABEL ===");
                System.out.println("Størrelse: " + size);
                System.out.println("Metode: " + method);
                if (hashIndex != -1) {
                    System.out.println("Fast hash-index: " + hashIndex);
                }
                if (method == Method.DOUBLE && secondaryHash != null) {
                    System.out.println("Sekundær hash: " + secondaryHash);
                }
                System.out.println();
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
            if (secondaryHash != null && secondaryHash != 0) {
                return secondaryHash;
            }
            return 1 + (key % (tableSize - 1));
        }

        // Manual insert at specific index (for given table)
        public void manualInsert(int index, char c) {
            int key = charToKey(c);
            insertionOrder.add(c);
            if (method == Method.CHAINING) {
                chains[index].add(key);
                if (debugMode) {
                    System.out.println("Manuelt indsat '" + c + "' (key=" + key + ") ved indeks " + index + " i chaining.");
                }
            } else {
                table[index] = key;
                if (debugMode) {
                    System.out.println("Manuelt indsat '" + c + "' (key=" + key + ") ved indeks " + index + ".");
                }
            }
        }

        // Automatic insert based on method and hashing
        public void insert(char c) {
            int key = charToKey(c);
            insertionOrder.add(c);
            int startIdx = (hashIndex >= 0) ? hashIndex : hashFunction(key);
            int idx = -1;

            if (debugMode) {
                System.out.println("\n--- Indsætter '" + c + "' ---");
                if (hashIndex >= 0) {
                    System.out.println("Startindeks (fast): " + startIdx);
                } else {
                    System.out.println("Hash(" + key + ") = " + key + " mod " + tableSize + " = " + startIdx);
                }
            }

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

            if (debugMode && idx != -1) {
                System.out.println("Resultat: '" + c + "' placeret ved indeks " + idx);
            }
        }

        // Insert with custom hash index (for specific problems)
        public void insertWithHash(char c, int customHashIndex) {
            int originalHashIndex = this.hashIndex;
            this.hashIndex = customHashIndex;
            insert(c);
            this.hashIndex = originalHashIndex;
        }

        private int insertLinear(int key, int startIdx) {
            int idx = startIdx;
            int probeCount = 0;

            if (debugMode) {
                System.out.println("Lineær probing:");
            }

            while (table[idx] != null) {
                if (debugMode) {
                    System.out.println("  Indeks " + idx + ": optaget af '" + keyToChar(table[idx]) + "'");
                }
                idx = (idx + 1) % tableSize;
                probeCount++;

                if (probeCount >= tableSize) {
                    if (debugMode) System.out.println("  Fejl: Tabel fuld!");
                    return -1;
                }
            }

            if (debugMode) {
                if (probeCount > 0) {
                    System.out.println("  Indeks " + idx + ": ledigt (efter " + (probeCount + 1) + " forsøg)");
                } else {
                    System.out.println("  Indeks " + idx + ": ledigt (ingen kollision)");
                }
            }

            table[idx] = key;
            return idx;
        }

        private int insertQuadratic(int key, int startIdx) {
            int i = 0;

            if (debugMode) {
                System.out.println("Kvadratisk probing:");
            }

            while (i <= tableSize) {
                int idx = (startIdx + i * i) % tableSize;

                if (table[idx] == null) {
                    if (debugMode) {
                        if (i > 0) {
                            System.out.println("  i=" + i + ": (" + startIdx + " + " + i + "²) mod " + tableSize + " = " + idx + " → ledigt (efter " + (i + 1) + " forsøg)");
                        } else {
                            System.out.println("  i=" + i + ": " + startIdx + " mod " + tableSize + " = " + idx + " → ledigt (ingen kollision)");
                        }
                    }
                    table[idx] = key;
                    return idx;
                } else {
                    if (debugMode) {
                        System.out.println("  i=" + i + ": (" + startIdx + " + " + i + "²) mod " + tableSize + " = " + idx + " → optaget af '" + keyToChar(table[idx]) + "'");
                    }
                }
                i++;
            }

            if (debugMode) System.out.println("  Fejl: Tabel fuld!");
            return -1;
        }

        private int insertDoubleHashing(int key, int startIdx) {
            int h2 = getSecondaryHash(key);
            int i = 0;

            if (debugMode) {
                System.out.println("Dobbelt hashing:");
                System.out.println("  h₁ = " + startIdx);
                System.out.println("  h₂(" + key + ") = " + h2);
            }

            while (i <= tableSize) {
                int idx = (startIdx + i * h2) % tableSize;

                if (table[idx] == null) {
                    if (debugMode) {
                        if (i > 0) {
                            System.out.println("  i=" + i + ": (" + startIdx + " + " + i + "×" + h2 + ") mod " + tableSize + " = " + idx + " → ledigt (efter " + (i + 1) + " forsøg)");
                        } else {
                            System.out.println("  i=" + i + ": " + startIdx + " mod " + tableSize + " = " + idx + " → ledigt (ingen kollision)");
                        }
                    }
                    table[idx] = key;
                    return idx;
                } else {
                    if (debugMode) {
                        System.out.println("  i=" + i + ": (" + startIdx + " + " + i + "×" + h2 + ") mod " + tableSize + " = " + idx + " → optaget af '" + keyToChar(table[idx]) + "'");
                    }
                }
                i++;
            }

            if (debugMode) System.out.println("  Fejl: Tabel fuld!");
            return -1;
        }

        private int insertChaining(int key, int startIdx) {
            if (debugMode) {
                System.out.println("Chaining:");
                System.out.println("  Tilføjer '" + keyToChar(key) + "' til kæden ved indeks " + startIdx);
            }
            chains[startIdx].add(key);
            return startIdx;
        }

        // Display current hash table
        public void display() {
            System.out.println("\n=== HASH TABEL ===");
            System.out.println("Indeks : Værdi");
            System.out.println("---------------");

            if (method == Method.CHAINING) {
                for (int i = 0; i < tableSize; i++) {
                    if (chains[i].isEmpty()) {
                        System.out.printf("%2d : #%n", i);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (int k : chains[i]) {
                            sb.append(keyToChar(k));
                            sb.append(" → ");
                        }
                        if (sb.length() > 0) {
                            sb.delete(sb.length() - 3, sb.length()); // Remove last arrow
                        }
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
            System.out.println("---------------");
        }

        // Vis mellemstatus (efter manuelle indsættelser)
        public void displayMellemstatus() {
            System.out.println("\n=== MELLEMSTATUS (efter manuelle indsættelser) ===");
            System.out.println("Indeks : Værdi");
            System.out.println("---------------");

            if (method == Method.CHAINING) {
                for (int i = 0; i < tableSize; i++) {
                    if (chains[i].isEmpty()) {
                        System.out.printf("%2d : #%n", i);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        for (int k : chains[i]) {
                            sb.append(keyToChar(k));
                            sb.append(" → ");
                        }
                        if (sb.length() > 0) {
                            sb.delete(sb.length() - 3, sb.length());
                        }
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
            System.out.println("---------------");
        }
    }

    // ====== KONFIGURATION AF OPGAVER ======
    public static void main(String[] args) {
        // ====== OPGAVE 1: KORREKT KONFIGURATION FOR DIN OPGAVE ======
        System.out.println("========== OPGAVE 1: KVADRATISK PROBING (KORREKT) ==========");

        // Konfiguration for opgave 1
        int tableSize1 = 11;
        Method method1 = Method.QUADRATIC;
        Integer secondaryHash1 = null;
        boolean debugMode1 = true;

        // Opret hash-tabel (uden fast hash-index, da vi bruger individuelle)
        HashTable ht1 = new HashTable(tableSize1, method1, -1, secondaryHash1, debugMode1);

        // Manuelle indsættelser (initial tabel)
        int[][] manualInserts1 = {
                {2, 'V'},
                {3, 'R'},
                {6, 'P'},
                {8, 'E'},
                {10, 'F'}
        };

        // Udfør manuelle indsættelser
        System.out.println("\n--- MANUELLE INDSÆTTELSER ---");
        for (int[] insert : manualInserts1) {
            ht1.manualInsert(insert[0], (char) insert[1]);
        }
        ht1.displayMellemstatus();

        // Automatiske indsættelser med SPECIFIKKE HASH-VÆRDIER fra opgaven
        System.out.println("\n--- AUTOMATISKE INDSÆTTELSER MED SPECIFIKKE HASH-VÆRDIER ---");
        System.out.println("Indsætter: Q (hash=7), C (hash=8), H (hash=2)");

        // Q hasher til 7
        ht1.insertWithHash('Q', 7);
        // C hasher til 8
        ht1.insertWithHash('C', 8);
        // H hasher til 2
        ht1.insertWithHash('H', 2);

        // Vis slutresultat
        System.out.println("\n--- SLUTRESULTAT (KORREKT IFØLGE OPGAVEN) ---");
        ht1.display();

        // ====== OPGAVE 2: LINEÆR PROBING ======
        System.out.println("\n\n========== OPGAVE 2: LINEÆR PROBING ==========");

        int tableSize2 = 10;
        Method method2 = Method.LINEAR;
        int hashIndex2 = -1;
        Integer secondaryHash2 = null;
        boolean debugMode2 = true;

        int[][] manualInserts2 = {
                {0, 'A'},
                {1, 'B'},
                {4, 'E'}
        };

        char[] autoInserts2 = {'C', 'D', 'F', 'G'};

        runOpgave("Opgave 2: Lineær Probing", tableSize2, method2, hashIndex2, secondaryHash2, manualInserts2, autoInserts2, debugMode2);

        // ====== OPGAVE 3: DOBBELT HASHING ======
        System.out.println("\n\n========== OPGAVE 3: DOBBELT HASHING ==========");

        int tableSize3 = 7;
        Method method3 = Method.DOUBLE;
        int hashIndex3 = -1;
        Integer secondaryHash3 = null; // Bruger default: 1 + (key % (tableSize-1))
        boolean debugMode3 = true;

        int[][] manualInserts3 = {
                {1, 'A'},
                {3, 'C'},
                {5, 'E'}
        };

        char[] autoInserts3 = {'B', 'D', 'F'};

        runOpgave("Opgave 3: Dobbelt Hashing", tableSize3, method3, hashIndex3, secondaryHash3, manualInserts3, autoInserts3, debugMode3);

        // ====== OPGAVE 4: CHAINING ======
        System.out.println("\n\n========== OPGAVE 4: CHAINING ==========");

        int tableSize4 = 5;
        Method method4 = Method.CHAINING;
        int hashIndex4 = -1;
        Integer secondaryHash4 = null;
        boolean debugMode4 = true;

        int[][] manualInserts4 = {
                {0, 'A'},
                {1, 'B'},
                {1, 'C'}  // B og C har samme hash
        };

        char[] autoInserts4 = {'D', 'E', 'F'};

        runOpgave("Opgave 4: Chaining", tableSize4, method4, hashIndex4, secondaryHash4, manualInserts4, autoInserts4, debugMode4);
    }

    // ====== HJÆLPEFUNKTION TIL AT KØRE EN OPGAVE ======
    public static void runOpgave(String opgaveNavn, int tableSize, Method method, int hashIndex,
                                 Integer secondaryHash, int[][] manualInserts, char[] autoInserts, boolean debugMode) {
        System.out.println("\n" + opgaveNavn);
        System.out.println("=".repeat(opgaveNavn.length()));

        // Opret hash-tabel
        HashTable ht = new HashTable(tableSize, method, hashIndex, secondaryHash, debugMode);

        // Udfør manuelle indsættelser
        System.out.println("\n--- MANUELLE INDSÆTTELSER ---");
        for (int[] insert : manualInserts) {
            ht.manualInsert(insert[0], (char) insert[1]);
        }
        ht.displayMellemstatus();

        // Udfør automatiske indsættelser
        System.out.println("\n--- AUTOMATISKE INDSÆTTELSER ---");
        System.out.println("Indsætter: " + Arrays.toString(autoInserts));
        for (char c : autoInserts) {
            ht.insert(c);
        }

        // Vis slutresultat
        System.out.println("\n--- SLUTRESULTAT ---");
        ht.display();
    }
}       