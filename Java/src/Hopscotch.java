import java.util.*;

public class Hopscotch {

    static class Element {
        String name;
        int hashValue;

        Element(String name, int hashValue) {
            this.name = name;
            this.hashValue = hashValue;
        }
    }

    static class HopscotchTable {
        private final int tableSize;
        private final int hopRange;
        private final boolean oneBased;
        private final String[] table;
        private final int[] hopBits;
        private final List<String> operations;

        public HopscotchTable(int tableSize, int hopRange, boolean oneBased) {
            this.tableSize = tableSize;
            this.hopRange = hopRange;
            this.oneBased = oneBased;
            int length = oneBased ? tableSize + 1 : tableSize;
            this.table = new String[length];
            this.hopBits = new int[length];
            this.operations = new ArrayList<>();
        }

        private int getHomeIndex(int hashValue) {
            return (hashValue % tableSize) + (oneBased ? 1 : 0);
        }

        public boolean insert(Element element) {
            int home = getHomeIndex(element.hashValue);
            operations.add(String.format("Indsætter %s (hash=%d → index %d)",
                    element.name, element.hashValue, home));

            // 1. Tjek home position
            if (table[home] == null) {
                table[home] = element.name;
                setHopBit(home, 0);
                operations.add(String.format("  ✓ Direkte på home %d", home));
                return true;
            }

            operations.add(String.format("  Home %d optaget af %s", home, table[home]));

            // 2. Tjek neighborhood
            for (int offset = 1; offset < hopRange; offset++) {
                int checkIdx = home + offset;

                if (checkIdx < table.length && table[checkIdx] == null) {
                    table[checkIdx] = element.name;
                    setHopBit(home, offset);
                    operations.add(String.format("  ✓ På plads %d (offset %d)", checkIdx, offset));
                    return true;
                }
            }

            // 3. Find første ledige plads
            int emptyIdx = findEmptySlot();
            if (emptyIdx == -1) {
                operations.add("  ✗ Ingen ledige pladser i hele tabellen!");
                return false;
            }

            int distance = Math.abs(emptyIdx - home);
            operations.add(String.format("  Første ledige: %d, afstand til home: %d",
                    emptyIdx, distance));

            // 4. Displacement
            return performDisplacement(element.name, home, emptyIdx);
        }

        private int findEmptySlot() {
            int start = oneBased ? 1 : 0;
            for (int i = start; i < table.length; i++) {
                if (table[i] == null) return i;
            }
            return -1;
        }

        private boolean performDisplacement(String key, int homeIdx, int emptyIdx) {
            operations.add("  Starter displacement...");
            int maxIterations = tableSize;
            int iterations = 0;

            while (Math.abs(emptyIdx - homeIdx) >= hopRange && iterations < maxIterations) {
                iterations++;

                // Find kandidater der kan flyttes til emptyIdx
                List<DisplacementCandidate> candidates = findDisplacementCandidates(emptyIdx);

                if (candidates.isEmpty()) {
                    operations.add("  ✗ Kan ikke finde element at flytte!");
                    return false;
                }

                // Vælg bedste kandidat (tættest på empty)
                DisplacementCandidate best = candidates.get(0);

                // Flyt elementet
                String movedKey = table[best.currentIdx];
                table[emptyIdx] = movedKey;
                table[best.currentIdx] = null;
                updateHopBitsAfterMove(best.currentIdx, emptyIdx, best.homeIdx);

                operations.add(String.format("  → Flyttede %s fra %d til %d",
                        movedKey, best.currentIdx, emptyIdx));
                emptyIdx = best.currentIdx;
            }

            if (iterations >= maxIterations) {
                operations.add("  ✗ Displacement-løkke afbrudt (max iterationer)");
                return false;
            }

            // Indsæt det nye element
            int finalOffset = emptyIdx - homeIdx;
            table[emptyIdx] = key;
            setHopBit(homeIdx, finalOffset);
            operations.add(String.format("  ✓ %s placeret på %d", key, emptyIdx));
            return true;
        }

        private static class DisplacementCandidate {
            int currentIdx;
            int homeIdx;
            int distance;

            DisplacementCandidate(int currentIdx, int homeIdx, int distance) {
                this.currentIdx = currentIdx;
                this.homeIdx = homeIdx;
                this.distance = distance;
            }
        }

        private List<DisplacementCandidate> findDisplacementCandidates(int emptyIdx) {
            List<DisplacementCandidate> candidates = new ArrayList<>();
            int start = oneBased ? 1 : 0;
            int end = oneBased ? tableSize : tableSize - 1;

            // Søg baglæns fra empty position
            int searchStart = Math.max(start, emptyIdx - (hopRange - 1));

            for (int i = searchStart; i < emptyIdx; i++) {
                if (table[i] != null) {
                    int elementHome = findHomeOfElement(i);
                    if (elementHome != -1) {
                        int distanceToEmpty = emptyIdx - elementHome;
                        // Tjek om elementet kan nå emptyIdx fra sin home
                        if (distanceToEmpty >= 0 && distanceToEmpty < hopRange) {
                            int currentDistance = emptyIdx - i;
                            candidates.add(new DisplacementCandidate(i, elementHome, currentDistance));
                        }
                    }
                }
            }

            // Sorter efter afstand (tættest til empty først)
            candidates.sort(Comparator.comparingInt(c -> c.distance));
            return candidates;
        }

        private int findHomeOfElement(int elementIdx) {
            int start = oneBased ? 1 : 0;
            int end = oneBased ? tableSize : tableSize - 1;

            // Tjek alle mulige homes
            for (int home = start; home <= end; home++) {
                int offset = elementIdx - home;
                // Tjek om dette home kan nå elementIdx og har hop-bit sat
                if (offset >= 0 && offset < hopRange && (hopBits[home] & (1 << offset)) != 0) {
                    return home;
                }
            }
            return -1;
        }

        private void setHopBit(int homeIdx, int offset) {
            if (offset < 32 && offset >= 0) {
                hopBits[homeIdx] |= (1 << offset);
            }
        }

        private void updateHopBitsAfterMove(int fromIdx, int toIdx, int homeIdx) {
            // Clear old bit
            int oldOffset = fromIdx - homeIdx;
            if (oldOffset >= 0 && oldOffset < 32) {
                hopBits[homeIdx] &= ~(1 << oldOffset);
            }

            // Set new bit
            int newOffset = toIdx - homeIdx;
            if (newOffset >= 0 && newOffset < 32) {
                hopBits[homeIdx] |= (1 << newOffset);
            }
        }

        public void printCurrentState() {
            System.out.println("\n" + "=".repeat(60));
            System.out.println("TABEL TILSTAND");
            System.out.println("=".repeat(60));

            int start = oneBased ? 1 : 0;

            System.out.print("Indeks: ");
            for (int i = start; i < table.length; i++) {
                System.out.printf("%4d ", i);
            }

            System.out.print("\nVærdi:  ");
            for (int i = start; i < table.length; i++) {
                System.out.printf("%4s ", table[i] == null ? "-" : table[i]);
            }

            System.out.print("\nHop:    ");
            for (int i = start; i < table.length; i++) {
                StringBuilder bits = new StringBuilder();
                for (int j = hopRange - 1; j >= 0; j--) {
                    bits.append((hopBits[i] & (1 << j)) != 0 ? "1" : "0");
                }
                System.out.printf("%4s ", bits);
            }
            System.out.println("\n");
        }

        public void printOperations() {
            for (String op : operations) {
                System.out.println(op);
            }
        }

        public void clearOperations() {
            operations.clear();
        }

        public void printDetailedAnalysis() {
            System.out.println("\n" + "▼".repeat(60));
            System.out.println("DETALJERET ANALYSE");
            System.out.println("▼".repeat(60));

            int start = oneBased ? 1 : 0;
            for (int i = start; i < table.length; i++) {
                if (table[i] != null) {
                    int home = findHomeOfElement(i);
                    int offset = i - home;
                    System.out.printf("Index %d: %s (home=%d, offset=%d, hop-bit=%d sat)\n",
                            i, table[i], home, offset, offset);
                }
            }
            System.out.println();
        }
    }

    public static void runExample(String title, int tableSize, int hopRange,
                                  boolean oneBased, List<Element> elements) {
        System.out.println("\n" + "█".repeat(70));
        System.out.println("█ " + title);
        System.out.println("█".repeat(70));
        System.out.println("Tabel størrelse: " + tableSize);
        System.out.println("Hop range: " + hopRange);
        System.out.println("Indeksering: " + (oneBased ? "1-baseret" : "0-baseret"));
        System.out.println();

        HopscotchTable table = new HopscotchTable(tableSize, hopRange, oneBased);

        for (Element element : elements) {
            table.clearOperations();
            boolean success = table.insert(element);
            table.printOperations();
            table.printCurrentState();

            if (!success) {
                System.out.println("⚠ FEJL: Kunne ikke indsætte element " + element.name);
                System.out.println("\nProblemet opstår fordi:");
                System.out.println("- Tabellen er fuld (ingen ledige pladser)");
                System.out.println("- Eller displacement ikke kan finde en gyldig kæde");
                break;
            }
        }

        // Print detaljeret analyse til sidst
        table.printDetailedAnalysis();
    }

    public static void main(String[] args) {
        // Original eksamen
        List<Element> examElements = new ArrayList<>();
        examElements.add(new Element("A", 29));
        examElements.add(new Element("B", 23));
        examElements.add(new Element("C", 31));
        examElements.add(new Element("D", 25));
        examElements.add(new Element("E", 37));
        examElements.add(new Element("F", 21));
        examElements.add(new Element("G", 39));
        examElements.add(new Element("H", 27));
        examElements.add(new Element("I", 30));
        examElements.add(new Element("J", 29));
        examElements.add(new Element("K", 34));
        examElements.add(new Element("K", 25));
        examElements.add(new Element("M", 25));
        examElements.add(new Element("N", 25));
        examElements.add(new Element("O", 25));
        examElements.add(new Element("P", 25));
        examElements.add(new Element("Q", 25));

        runExample("EKSAMENSOPGAVE", 20, 4, true, examElements);
    }
}

//Hvis opgaven bruger en anden hash-funktion end modulo, skal du ændre getHomeIndex()
//Hvis opgaven har andet hop range end 4, ændre parameteren