import java.util.*;
import java.io.*;

public class Heap {

    // ==================== KONFIGURATION ====================
    static class Config {
        boolean useZeroBasedIndexing = false;  // Vi bruger 1-baseret indeksering
        boolean logToFile = true;             // Skriver til fil
        String heapType = "both";
        String logFileName = "heap_log.txt";
    }

    static Config config = new Config();
    static PrintWriter fileWriter = null;

    // ==================== LOGGING FUNKTION ====================
    private static void log(String message) {
        System.out.println(message);
        if (config.logToFile && fileWriter != null) {
            fileWriter.println(message);
            fileWriter.flush(); // Sikrer at data skrives til fil med det samme
        }
    }

    // ==================== HEAP BUILDING & HEAPIFY ====================
    static class HeapBuilder {

        // Simpel forklaring på heapify
        public static void explainHeapify() {
            log("\n📚 SIMPEL FORKLARING PÅ HEAPIFY:");
            log("=".repeat(60));
            log("""
                HEAPIFY - HVORDAN MAN LAVER ET ARRAY TIL EN MIN-HEAP ELLER MAX-HEAP:
                
                1. START FRA BUNDEN OG GÅ OP:
                   • Start ved den sidste node der har børn (floor(n/2))
                   • Arbejd baglæns til roden (index 1 for 1-baseret)
                
                2. MIN-HEAP (mindste værdi i toppen):
                   • Hvert barn skal være STØRRE eller LIG med forælder
                   • Hvis barn < forælder: BYT dem
                   • Gentag nedad indtil heap-egenskaben er opfyldt
                
                3. MAX-HEAP (største værdi i toppen):
                   • Hvert barn skal være MINDRE eller LIG med forælder
                   • Hvis barn > forælder: BYT dem
                   • Gentag nedad indtil heap-egenskaben er opfyldt
                
                4. FORMEL FOR 1-BASERET INDEKSERING:
                   • Forælder til index i: floor(i/2)
                   • Venstre barn til index i: 2*i
                   • Højre barn til index i: 2*i + 1
                
                5. TIDSKOMPLEKSITET:
                   • Build-heap: O(n) - ikke O(n log n)!
                   • Heapify-up: O(log n)
                   • Heapify-down: O(log n)
                """);
            log("=".repeat(60));
        }

        // Byg min-heap fra array
        public static void buildMinHeap(int[] arr) {
            log("\n" + "=".repeat(60));
            log("BYGGER MIN-HEAP FRA ARRAY");
            log("=".repeat(60));

            int n = arr.length;
            int[] heap = arr.clone(); // Arbejder på kopi

            log("Start array: " + Arrays.toString(heap));
            log("Størrelse: " + n);
            log("\nTRIN-FOR-TRIN PROCESS:");

            // Start fra sidste forælder og arbejd opad
            int startIdx = n / 2; // floor(n/2) for 1-baseret (men vi arbejder 0-baseret internt)

            for (int i = startIdx - 1; i >= 0; i--) {
                log("\n▶️  Heapify på index " + (i+1) + " (værdi: " + heap[i] + ")");
                minHeapify(heap, n, i);
                log("   Mellemresultat: " + arrayToString(heap, true));
            }

            log("\n✅ MIN-HEAP FÆRDIG:");
            log("Resultat: " + arrayToString(heap, true));
            log("Gyldig min-heap? " + isMinHeap(heap));

            // Vis heap som træ
            log("\n🌳 MIN-HEAP SOM TRÆ:");
            printHeapAsTree(heap, "min");
        }

        // Byg max-heap fra array
        public static void buildMaxHeap(int[] arr) {
            log("\n" + "=".repeat(60));
            log("BYGGER MAX-HEAP FRA ARRAY");
            log("=".repeat(60));

            int n = arr.length;
            int[] heap = arr.clone(); // Arbejder på kopi

            log("Start array: " + Arrays.toString(heap));
            log("Størrelse: " + n);
            log("\nTRIN-FOR-TRIN PROCESS:");

            // Start fra sidste forælder og arbejd opad
            int startIdx = n / 2;

            for (int i = startIdx - 1; i >= 0; i--) {
                log("\n▶️  Heapify på index " + (i+1) + " (værdi: " + heap[i] + ")");
                maxHeapify(heap, n, i);
                log("   Mellemresultat: " + arrayToString(heap, true));
            }

            log("\n✅ MAX-HEAP FÆRDIG:");
            log("Resultat: " + arrayToString(heap, true));
            log("Gyldig max-heap? " + isMaxHeap(heap));

            // Vis heap som træ
            log("\n🌳 MAX-HEAP SOM TRÆ:");
            printHeapAsTree(heap, "max");
        }

        // Min-heapify funktion
        private static void minHeapify(int[] arr, int n, int i) {
            int smallest = i;
            int left = 2 * i + 1;  // Venstre barn i 0-baseret
            int right = 2 * i + 2; // Højre barn i 0-baseret

            log("   Forælder: arr[" + (i+1) + "] = " + arr[i]);

            if (left < n) {
                log("   Venstre barn: arr[" + (left+1) + "] = " + arr[left]);
                if (arr[left] < arr[smallest]) {
                    smallest = left;
                }
            }

            if (right < n) {
                log("   Højre barn: arr[" + (right+1) + "] = " + arr[right]);
                if (arr[right] < arr[smallest]) {
                    smallest = right;
                }
            }

            if (smallest != i) {
                log("   BYT: " + arr[i] + " ↔ " + arr[smallest] +
                        " (index " + (i+1) + " ↔ " + (smallest+1) + ")");
                swap(arr, i, smallest);
                // Rekursivt heapify på det berørte subtræ
                minHeapify(arr, n, smallest);
            } else {
                log("   OK: Heap-egenskab opfyldt");
            }
        }

        // Max-heapify funktion
        private static void maxHeapify(int[] arr, int n, int i) {
            int largest = i;
            int left = 2 * i + 1;  // Venstre barn i 0-baseret
            int right = 2 * i + 2; // Højre barn i 0-baseret

            log("   Forælder: arr[" + (i+1) + "] = " + arr[i]);

            if (left < n) {
                log("   Venstre barn: arr[" + (left+1) + "] = " + arr[left]);
                if (arr[left] > arr[largest]) {
                    largest = left;
                }
            }

            if (right < n) {
                log("   Højre barn: arr[" + (right+1) + "] = " + arr[right]);
                if (arr[right] > arr[largest]) {
                    largest = right;
                }
            }

            if (largest != i) {
                log("   BYT: " + arr[i] + " ↔ " + arr[largest] +
                        " (index " + (i+1) + " ↔ " + (largest+1) + ")");
                swap(arr, i, largest);
                // Rekursivt heapify på det berørte subtræ
                maxHeapify(arr, n, largest);
            } else {
                log("   OK: Heap-egenskab opfyldt");
            }
        }

        private static void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        private static boolean isMinHeap(int[] arr) {
            int n = arr.length;
            for (int i = 0; i <= n / 2 - 1; i++) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;

                if (left < n && arr[i] > arr[left]) return false;
                if (right < n && arr[i] > arr[right]) return false;
            }
            return true;
        }

        private static boolean isMaxHeap(int[] arr) {
            int n = arr.length;
            for (int i = 0; i <= n / 2 - 1; i++) {
                int left = 2 * i + 1;
                int right = 2 * i + 2;

                if (left < n && arr[i] < arr[left]) return false;
                if (right < n && arr[i] < arr[right]) return false;
            }
            return true;
        }

        private static String arrayToString(int[] arr, boolean oneBased) {
            StringBuilder sb = new StringBuilder("[");
            if (oneBased) {
                sb.append("-");
                for (int val : arr) {
                    sb.append(", ").append(val);
                }
            } else {
                for (int i = 0; i < arr.length; i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(arr[i]);
                }
            }
            sb.append("]");
            return sb.toString();
        }

        private static void printHeapAsTree(int[] heap, String type) {
            int n = heap.length;
            int height = (int)(Math.log(n) / Math.log(2)) + 1;

            for (int i = 0; i < n; i++) {
                int level = (int)(Math.log(i + 1) / Math.log(2));
                int spaces = (int)Math.pow(2, height - level - 1) - 1;

                // Indrykning
                for (int s = 0; s < spaces; s++) {
                    log("  ");
                }

                // Print værdi
                log(heap[i] + " ");

                // Ny linje efter sidste node på hvert niveau
                if (i == (int)Math.pow(2, level + 1) - 2 || i == n - 1) {
                    log("");
                }
            }
        }
    }

    // ==================== SIMPLIFIED HEAP FOR ANALYSIS ====================
    static class HeapAnalyzer {

        // Print array med 1-baseret indeksering korrekt
        public static void printArray(int[] arr, boolean zeroBased) {
            log("\n📊 ORIGINALT ARRAY:");

            if (zeroBased) {
                log("Indeks: " + getIndexString(arr.length, true));
                log("Værdi:  " + getValueString(arr, true));
            } else {
                log("Indeks:  " + getIndexString(arr.length, false));
                log("Værdi:   " + getValueString(arr, false));
            }

            // Vis statistik
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (int val : arr) {
                if (val < min) min = val;
                if (val > max) max = val;
            }
            log("\n📈 STATISTIK:");
            log("   Minimum: " + min);
            log("   Maximum: " + max);
        }

        private static String getIndexString(int length, boolean zeroBased) {
            StringBuilder sb = new StringBuilder();
            if (zeroBased) {
                for (int i = 0; i < length; i++) {
                    sb.append(String.format("%3d ", i));
                }
            } else {
                for (int i = 0; i <= length; i++) {
                    sb.append(String.format("%3d ", i));
                }
            }
            return sb.toString();
        }

        private static String getValueString(int[] arr, boolean zeroBased) {
            StringBuilder sb = new StringBuilder();
            if (zeroBased) {
                for (int val : arr) {
                    sb.append(String.format("%3d ", val));
                }
            } else {
                sb.append("  - ");
                for (int val : arr) {
                    sb.append(String.format("%3d ", val));
                }
            }
            return sb.toString();
        }

        // Vis sorteret array med 1-baseret indeksering
        public static void printSorted(int[] arr) {
            int[] sorted = arr.clone();
            Arrays.sort(sorted);
            log("\n📊 SORTERET ARRAY (stigende):");
            log("Indeks:  " + getIndexString(sorted.length, false));
            log("Værdi:   " + getValueString(sorted, false));
        }

        // Tjek heap properties med 1-baseret indeksering
        public static void checkHeapProperties(int[] arr, boolean zeroBased) {
            log("\n🔍 HEAP EGENSKABS CHECK:");

            if (zeroBased) {
                checkZeroBased(arr);
            } else {
                checkOneBased(arr);
            }
        }

        private static void checkOneBased(int[] arr) {
            int n = arr.length;

            // Min-heap check
            log("\nMIN-HEAP:");
            String firstMinViolation = null;
            List<String> minViolations = new ArrayList<>();

            for (int i = 1; i <= n; i++) {
                int parent = i;
                int left = 2 * i;
                int right = 2 * i + 1;

                if (left <= n) {
                    if (arr[parent-1] > arr[left-1]) {
                        String violation = String.format("P[%d]=%d > L[%d]=%d",
                                parent, arr[parent-1], left, arr[left-1]);
                        if (firstMinViolation == null) {
                            firstMinViolation = violation;
                        }
                        minViolations.add(violation);
                    }
                }

                if (right <= n) {
                    if (arr[parent-1] > arr[right-1]) {
                        String violation = String.format("P[%d]=%d > R[%d]=%d",
                                parent, arr[parent-1], right, arr[right-1]);
                        if (firstMinViolation == null) {
                            firstMinViolation = violation;
                        }
                        minViolations.add(violation);
                    }
                }
            }

            if (minViolations.isEmpty()) {
                log("✅ ER EN GYLDIG MIN-HEAP");
            } else {
                log("❌ ER IKKE EN GYLDIG MIN-HEAP");
                log("   Første violation: " + firstMinViolation);
                log("   Resten (" + (minViolations.size() - 1) + "):");
                for (int i = 1; i < minViolations.size(); i++) {
                    log("     " + minViolations.get(i));
                }
            }

            // Max-heap check
            log("\nMAX-HEAP:");
            String firstMaxViolation = null;
            List<String> maxViolations = new ArrayList<>();

            for (int i = 1; i <= n; i++) {
                int parent = i;
                int left = 2 * i;
                int right = 2 * i + 1;

                if (left <= n) {
                    if (arr[parent-1] < arr[left-1]) {
                        String violation = String.format("P[%d]=%d < L[%d]=%d",
                                parent, arr[parent-1], left, arr[left-1]);
                        if (firstMaxViolation == null) {
                            firstMaxViolation = violation;
                        }
                        maxViolations.add(violation);
                    }
                }

                if (right <= n) {
                    if (arr[parent-1] < arr[right-1]) {
                        String violation = String.format("P[%d]=%d < R[%d]=%d",
                                parent, arr[parent-1], right, arr[right-1]);
                        if (firstMaxViolation == null) {
                            firstMaxViolation = violation;
                        }
                        maxViolations.add(violation);
                    }
                }
            }

            if (maxViolations.isEmpty()) {
                log("✅ ER EN GYLDIG MAX-HEAP");
            } else {
                log("❌ ER IKKE EN GYLDIG MAX-HEAP");
                log("   Første violation: " + firstMaxViolation);
                log("   Resten (" + (maxViolations.size() - 1) + "):");
                for (int i = 1; i < maxViolations.size(); i++) {
                    log("     " + maxViolations.get(i));
                }
            }
        }

        private static void checkZeroBased(int[] arr) {
            // Simplified for brevity
        }

        // Vis parent-child relationer med 1-baseret indeksering
        public static void printParentChildRelations(int[] arr, boolean zeroBased) {
            log("\n👨‍👧‍👦 PARENT-CHILD RELATIONER:");

            if (zeroBased) {
                printRelationsZeroBased(arr);
            } else {
                printRelationsOneBased(arr);
            }
        }

        private static void printRelationsOneBased(int[] arr) {
            int n = arr.length;

            for (int i = 1; i <= n; i++) {
                int parent = i;
                int left = 2 * i;
                int right = 2 * i + 1;

                String relation = String.format("P[%d]=%d → L[%d]=", parent, arr[parent-1], left);
                if (left <= n) {
                    relation += arr[left-1];
                } else {
                    relation += "-";
                }

                relation += String.format(", R[%d]=", right);
                if (right <= n) {
                    relation += arr[right-1];
                } else {
                    relation += "-";
                }
                log(relation);
            }
        }

        private static void printRelationsZeroBased(int[] arr) {
            // Simplified for brevity
        }
    }

    // ==================== MAIN ====================
    public static void main(String[] args) {
        try {
            // Opret logfil
            if (config.logToFile) {
                fileWriter = new PrintWriter(new FileWriter(config.logFileName));
                log("📝 LOGFIL OPRETTET: " + config.logFileName);
                log("=".repeat(60));
            }

            log("=".repeat(60));
            log("HEAP ANALYSE PROGRAM - 1-BASERET INDEKSERING");
            log("=".repeat(60));

            // Original array
            int[] originalArray = {7, 19, 11, 22, 28, 13, 26, 34, 26, 42, 27, 21, 14, 81, 18, 66, 38, 69, 67};


            log("\n⚠️  Vigtigt: Arrayet vises med 1-baseret indeksering");
            log("   Index 0 er altid tomt (-) i 1-baseret visning");
            log("   I hukommelsen er arrayet stadig 0-baseret");

            // 1. Vis original array med statistik
            HeapAnalyzer.printArray(originalArray, config.useZeroBasedIndexing);

            // 2. Vis sorteret array
            HeapAnalyzer.printSorted(originalArray);

            // 3. Vis parent-child relationer
            HeapAnalyzer.printParentChildRelations(originalArray, config.useZeroBasedIndexing);

            // 4. Tjek heap properties
            HeapAnalyzer.checkHeapProperties(originalArray, config.useZeroBasedIndexing);

            // 5. Forklar heapify
            HeapBuilder.explainHeapify();

            // 6. Byg min-heap
            if (config.heapType.equals("min") || config.heapType.equals("both")) {
                HeapBuilder.buildMinHeap(originalArray);
            }

            // 7. Byg max-heap
            if (config.heapType.equals("max") || config.heapType.equals("both")) {
                HeapBuilder.buildMaxHeap(originalArray);
            }

            log("\n" + "=".repeat(60));
            log("ANALYSE FÆRDIG");
            log("=".repeat(60));

        } catch (IOException e) {
            System.err.println("Fejl ved oprettelse af logfil: " + e.getMessage());
        } finally {
            // Luk filen
            if (fileWriter != null) {
                fileWriter.close();
                System.out.println("\n✅ Logfil gemt som: " + config.logFileName);
            }
        }
    }
}