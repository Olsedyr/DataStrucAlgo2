        import java.util.*;
        import java.io.*;

        public class HeapAndPriorityQueue {

            // ==================== KONFIGURATION ====================
            static class Config {
                boolean useZeroBasedIndexing = false;  // Vi bruger 1-baseret indeksering
                boolean logToFile = true;
                String heapType = "both";
                String logFileName = "heap_log.txt";
                boolean verboseOperations = true;
            }

            static Config config = new Config();
            static PrintWriter fileWriter = null;

            // ==================== LOGGING FUNKTION ====================
            private static void log(String message) {
                System.out.println(message);
                if (config.logToFile && fileWriter != null) {
                    fileWriter.println(message);
                    fileWriter.flush();
                }
            }

            // ==================== HEAP BUILDING & HEAPIFY ====================
            static class HeapBuilder {

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
                           • Build-heap: O(n)
                           • Insert: O(log n) - heapify-up
                           • Delete: O(log n) - heapify-down
                           • Peek: O(1)
                        """);
                    log("=".repeat(60));
                }

                public static void buildMinHeap(int[] arr) {
                    log("\n" + "=".repeat(60));
                    log("BYGGER MIN-HEAP FRA ARRAY");
                    log("=".repeat(60));

                    int n = arr.length;
                    int[] heap = arr.clone();

                    log("Start array: " + Arrays.toString(heap));
                    log("Størrelse: " + n);
                    log("\nTRIN-FOR-TRIN PROCESS:");

                    int startIdx = n / 2;

                    for (int i = startIdx - 1; i >= 0; i--) {
                        log("\n▶️  Heapify på index " + (i+1) + " (værdi: " + heap[i] + ")");
                        minHeapify(heap, n, i);
                        log("   Mellemresultat: " + arrayToString(heap, true));
                    }

                    log("\n✅ MIN-HEAP FÆRDIG:");
                    log("Resultat: " + arrayToString(heap, true));
                    log("Gyldig min-heap? " + isMinHeap(heap));
                }

                public static void buildMaxHeap(int[] arr) {
                    log("\n" + "=".repeat(60));
                    log("BYGGER MAX-HEAP FRA ARRAY");
                    log("=".repeat(60));

                    int n = arr.length;
                    int[] heap = arr.clone();

                    log("Start array: " + Arrays.toString(heap));
                    log("Størrelse: " + n);
                    log("\nTRIN-FOR-TRIN PROCESS:");

                    int startIdx = n / 2;

                    for (int i = startIdx - 1; i >= 0; i--) {
                        log("\n▶️  Heapify på index " + (i+1) + " (værdi: " + heap[i] + ")");
                        maxHeapify(heap, n, i);
                        log("   Mellemresultat: " + arrayToString(heap, true));
                    }

                    log("\n✅ MAX-HEAP FÆRDIG:");
                    log("Resultat: " + arrayToString(heap, true));
                    log("Gyldig max-heap? " + isMaxHeap(heap));
                }

                private static void minHeapify(int[] arr, int n, int i) {
                    int smallest = i;
                    int left = 2 * i + 1;
                    int right = 2 * i + 2;

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
                        minHeapify(arr, n, smallest);
                    } else {
                        log("   OK: Heap-egenskab opfyldt");
                    }
                }

                private static void maxHeapify(int[] arr, int n, int i) {
                    int largest = i;
                    int left = 2 * i + 1;
                    int right = 2 * i + 2;

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
            }

            // ==================== HEAP ANALYZER ====================
            static class HeapAnalyzer {

                public static void printArray(int[] arr, boolean zeroBased) {
                    log("\n📊 ORIGINALT ARRAY:");

                    if (zeroBased) {
                        log("Indeks: " + getIndexString(arr.length, true));
                        log("Værdi:  " + getValueString(arr, true));
                    } else {
                        log("Indeks:  " + getIndexString(arr.length, false));
                        log("Værdi:   " + getValueString(arr, false));
                    }

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

                public static void printSorted(int[] arr) {
                    int[] sorted = arr.clone();
                    Arrays.sort(sorted);
                    log("\n📊 SORTERET ARRAY (stigende):");
                    log("Indeks:  " + getIndexString(sorted.length, false));
                    log("Værdi:   " + getValueString(sorted, false));
                }

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
                    // Simplified
                }

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
                    // Simplified
                }
            }

            // ==================== HEAP OPERATIONS (INSERT & DELETE) ====================
            static class HeapOperations {

                // Helper methods for building heaps silently
                private static void minHeapifyForBuild(int[] arr, int n, int i) {
                    int smallest = i;
                    int left = 2 * i + 1;
                    int right = 2 * i + 2;

                    if (left < n && arr[left] < arr[smallest]) {
                        smallest = left;
                    }
                    if (right < n && arr[right] < arr[smallest]) {
                        smallest = right;
                    }

                    if (smallest != i) {
                        HeapBuilder.swap(arr, i, smallest);
                        minHeapifyForBuild(arr, n, smallest);
                    }
                }

                private static void maxHeapifyForBuild(int[] arr, int n, int i) {
                    int largest = i;
                    int left = 2 * i + 1;
                    int right = 2 * i + 2;

                    if (left < n && arr[left] > arr[largest]) {
                        largest = left;
                    }
                    if (right < n && arr[right] > arr[largest]) {
                        largest = right;
                    }

                    if (largest != i) {
                        HeapBuilder.swap(arr, i, largest);
                        maxHeapifyForBuild(arr, n, largest);
                    }
                }

                // DELETE-MAX operation
                public static int[] deleteMax(int[] arr) {
                    log("\n" + "=".repeat(60));
                    log("DELETE-MAX OPERATION");
                    log("=".repeat(60));

                    if (arr.length == 0) {
                        log("⚠ Heap er tom!");
                        return arr;
                    }

                    int[] heap = arr.clone();
                    int max = heap[0];

                    log("→ Fjerner max element: " + max + " (index 1 i 1-baseret)");
                    log("→ Flytter sidste element " + heap[heap.length-1] + " til toppen");

                    // Flyt sidste element til toppen
                    heap[0] = heap[heap.length - 1];
                    int[] newHeap = Arrays.copyOf(heap, heap.length - 1);

                    log("\nFør heapify-down: " + HeapBuilder.arrayToString(newHeap, true));

                    // Heapify-down
                    if (newHeap.length > 0) {
                        log("\n--- HEAPIFY-DOWN PROCESS ---");
                        maxHeapifyDown(newHeap, 0);
                    }

                    log("\n✅ DELETE-MAX FÆRDIG!");
                    log("Fjernet element: " + max);
                    log("Ny heap: " + HeapBuilder.arrayToString(newHeap, true));
                    log("Gyldig max-heap? " + HeapBuilder.isMaxHeap(newHeap));

                    return newHeap;
                }

                // DELETE-MIN operation
                public static int[] deleteMin(int[] arr) {
                    log("\n" + "=".repeat(60));
                    log("DELETE-MIN OPERATION");
                    log("=".repeat(60));

                    if (arr.length == 0) {
                        log("⚠ Heap er tom!");
                        return arr;
                    }

                    int[] heap = arr.clone();
                    int min = heap[0];

                    log("→ Fjerner min element: " + min + " (index 1 i 1-baseret)");
                    log("→ Flytter sidste element " + heap[heap.length-1] + " til toppen");

                    // Flyt sidste element til toppen
                    heap[0] = heap[heap.length - 1];
                    int[] newHeap = Arrays.copyOf(heap, heap.length - 1);

                    log("\nFør heapify-down: " + HeapBuilder.arrayToString(newHeap, true));

                    // Heapify-down
                    if (newHeap.length > 0) {
                        log("\n--- HEAPIFY-DOWN PROCESS ---");
                        minHeapifyDown(newHeap, 0);
                    }

                    log("\n✅ DELETE-MIN FÆRDIG!");
                    log("Fjernet element: " + min);
                    log("Ny heap: " + HeapBuilder.arrayToString(newHeap, true));
                    log("Gyldig min-heap? " + HeapBuilder.isMinHeap(newHeap));

                    return newHeap;
                }

                // INSERT operation (for både min og max heap)
                public static int[] insert(int[] arr, int value, boolean isMaxHeap) {
                    log("\n" + "=".repeat(60));
                    log("INSERT OPERATION: " + value + " i " + (isMaxHeap ? "MAX-HEAP" : "MIN-HEAP"));
                    log("=".repeat(60));

                    // Tilføj element til slutningen
                    int[] heap = Arrays.copyOf(arr, arr.length + 1);
                    heap[heap.length - 1] = value;

                    log("→ Tilføjer " + value + " til slutningen (index " + heap.length + " i 1-baseret)");
                    log("\nFør heapify-up: " + HeapBuilder.arrayToString(heap, true));

                    // Heapify-up
                    log("\n--- HEAPIFY-UP PROCESS ---");
                    if (isMaxHeap) {
                        maxHeapifyUp(heap, heap.length - 1);
                    } else {
                        minHeapifyUp(heap, heap.length - 1);
                    }

                    log("\n✅ INSERT FÆRDIG!");
                    log("Ny heap: " + HeapBuilder.arrayToString(heap, true));
                    log("Gyldig " + (isMaxHeap ? "max-heap" : "min-heap") + "? " +
                            (isMaxHeap ? HeapBuilder.isMaxHeap(heap) : HeapBuilder.isMinHeap(heap)));

                    return heap;
                }

                private static void maxHeapifyDown(int[] arr, int i) {
                    int n = arr.length;
                    int largest = i;
                    int left = 2 * i + 1;
                    int right = 2 * i + 2;

                    log("  Node[" + (i+1) + "]=" + arr[i]);

                    if (left < n) {
                        log("    Venstre barn[" + (left+1) + "]=" + arr[left]);
                        if (arr[left] > arr[largest]) {
                            largest = left;
                        }
                    }

                    if (right < n) {
                        log("    Højre barn[" + (right+1) + "]=" + arr[right]);
                        if (arr[right] > arr[largest]) {
                            largest = right;
                        }
                    }

                    if (largest != i) {
                        log("  → BYT: " + arr[i] + " ↔ " + arr[largest] +
                                " (index " + (i+1) + " ↔ " + (largest+1) + ")");
                        HeapBuilder.swap(arr, i, largest);
                        log("    Mellemresultat: " + HeapBuilder.arrayToString(arr, true));
                        maxHeapifyDown(arr, largest);
                    } else {
                        log("  ✓ Heap-egenskab opfyldt");
                    }
                }

                private static void minHeapifyDown(int[] arr, int i) {
                    int n = arr.length;
                    int smallest = i;
                    int left = 2 * i + 1;
                    int right = 2 * i + 2;

                    log("  Node[" + (i+1) + "]=" + arr[i]);

                    if (left < n) {
                        log("    Venstre barn[" + (left+1) + "]=" + arr[left]);
                        if (arr[left] < arr[smallest]) {
                            smallest = left;
                        }
                    }

                    if (right < n) {
                        log("    Højre barn[" + (right+1) + "]=" + arr[right]);
                        if (arr[right] < arr[smallest]) {
                            smallest = right;
                        }
                    }

                    if (smallest != i) {
                        log("  → BYT: " + arr[i] + " ↔ " + arr[smallest] +
                                " (index " + (i+1) + " ↔ " + (smallest+1) + ")");
                        HeapBuilder.swap(arr, i, smallest);
                        log("    Mellemresultat: " + HeapBuilder.arrayToString(arr, true));
                        minHeapifyDown(arr, smallest);
                    } else {
                        log("  ✓ Heap-egenskab opfyldt");
                    }
                }

                private static void maxHeapifyUp(int[] arr, int i) {
                    int current = i;

                    while (current > 0) {
                        int parent = (current - 1) / 2;

                        log("  Sammenligner: arr[" + (current+1) + "]=" + arr[current] +
                                " med parent arr[" + (parent+1) + "]=" + arr[parent]);

                        if (arr[current] > arr[parent]) {
                            log("  → BYT: " + arr[current] + " ↔ " + arr[parent] +
                                    " (index " + (current+1) + " ↔ " + (parent+1) + ")");
                            HeapBuilder.swap(arr, current, parent);
                            log("    Mellemresultat: " + HeapBuilder.arrayToString(arr, true));
                            current = parent;
                        } else {
                            log("  ✓ Heap-egenskab opfyldt, stopper");
                            break;
                        }
                    }
                }

                private static void minHeapifyUp(int[] arr, int i) {
                    int current = i;

                    while (current > 0) {
                        int parent = (current - 1) / 2;

                        log("  Sammenligner: arr[" + (current+1) + "]=" + arr[current] +
                                " med parent arr[" + (parent+1) + "]=" + arr[parent]);

                        if (arr[current] < arr[parent]) {
                            log("  → BYT: " + arr[current] + " ↔ " + arr[parent] +
                                    " (index " + (current+1) + " ↔ " + (parent+1) + ")");
                            HeapBuilder.swap(arr, current, parent);
                            log("    Mellemresultat: " + HeapBuilder.arrayToString(arr, true));
                            current = parent;
                        } else {
                            log("  ✓ Heap-egenskab opfyldt, stopper");
                            break;
                        }
                    }
                }
            }

            // ==================== MAIN ====================
            public static void main(String[] args) {
                try {
                    if (config.logToFile) {
                        fileWriter = new PrintWriter(new FileWriter(config.logFileName));
                        log("📝 LOGFIL OPRETTET: " + config.logFileName);
                        log("=".repeat(60));
                    }

                    log("=".repeat(60));
                    log("HEAP ANALYSE PROGRAM - 1-BASERET INDEKSERING");
                    log("=".repeat(60));

                    // Original array
                    int[] originalArray = {11, 17, 28, 23,68, 49, 29, 25, 56, 97, 106, 118, 48, 65, 76, 69, 27, 68, 62};


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

                    // ==================== HEAP OPERATIONS DEMO ====================
                    log("\n\n" + "=".repeat(60));
                    log("HEAP OPERATIONS DEMO PÅ BYGGEDE HEAPS");
                    log("=".repeat(60));

                    // Byg min-heap og max-heap først
                    int[] minHeapBuilt = originalArray.clone();
                    int[] maxHeapBuilt = originalArray.clone();

                    // Byg min-heap
                    int n = minHeapBuilt.length;
                    for (int i = n / 2 - 1; i >= 0; i--) {
                        HeapOperations.minHeapifyForBuild(minHeapBuilt, n, i);
                    }

                    // Byg max-heap
                    n = maxHeapBuilt.length;
                    for (int i = n / 2 - 1; i >= 0; i--) {
                        HeapOperations.maxHeapifyForBuild(maxHeapBuilt, n, i);
                    }

                    log("\n📊 BYGGET MIN-HEAP (fra original array):");
                    log("  " + HeapBuilder.arrayToString(minHeapBuilt, true));
                    log("  Gyldig min-heap? " + HeapBuilder.isMinHeap(minHeapBuilt));

                    log("\n📊 BYGGET MAX-HEAP (fra original array):");
                    log("  " + HeapBuilder.arrayToString(maxHeapBuilt, true));
                    log("  Gyldig max-heap? " + HeapBuilder.isMaxHeap(maxHeapBuilt));



                    // Test DELETE-MAX på den byggede max-heap
                    //log("\n" + "─".repeat(60));
                    //int[] afterDeleteMax = HeapOperations.deleteMax(maxHeapBuilt);

                    // Test INSERT på den opdaterede max-heap
                    //log("\n" + "─".repeat(60));
                    //int[] afterInsert = HeapOperations.insert(maxHeapBuilt, 12, true);


                    // Test DELETE-MIN på den byggede max-heap
                    //log("\n" + "─".repeat(60));
                    //int[] afterDeleteMin = HeapOperations.deleteMin(maxHeapBuilt);
                    log("\n" + "─".repeat(60));



                    // ==================== SPECIFIK OPGAVE: INSERT 7, INSERT 15, DELETE-MIN ====================
                    log("\n\n" + "=".repeat(60));
                    log("SPECIFIK OPGAVE: Priority Queue Operations");
                    log("=".repeat(60));
                    log("Operationer: 1. insert(7), 2. insert(15), 3. deleteMin()");

                    log("\n📊 START-MIN-HEAP (før operationer):");
                    log("  " + HeapBuilder.arrayToString(minHeapBuilt, true));
                    log("  Gyldig min-heap? " + HeapBuilder.isMinHeap(minHeapBuilt));

        // Trin 1: Insert 7
                    log("\n" + "─".repeat(40));
                    log("TRIN 1: insert(7)");
                    int[] afterInsert7 = HeapOperations.insert(minHeapBuilt, 7, false);
                    log("\n✅ Min-heap efter insert(7):");
                    log("  " + HeapBuilder.arrayToString(afterInsert7, true));
                    log("  Gyldig min-heap? " + HeapBuilder.isMinHeap(afterInsert7));

        // Trin 2: Insert 15 (PÅ DET NYE ARRAY fra insert(7))
                    log("\n" + "─".repeat(40));
                    log("TRIN 2: insert(15)");
                    int[] afterInsert15 = HeapOperations.insert(afterInsert7, 15, false); // Brug afterInsert7, ikke minHeapBuilt!
                    log("\n✅ Min-heap efter insert(15):");
                    log("  " + HeapBuilder.arrayToString(afterInsert15, true));
                    log("  Gyldig min-heap? " + HeapBuilder.isMinHeap(afterInsert15));

        // Trin 3: Delete min (PÅ DET NYE ARRAY fra insert(15))
                    log("\n" + "─".repeat(40));
                    log("TRIN 3: deleteMin()");
                    int[] afterDeleteMin = HeapOperations.deleteMin(afterInsert15); // Brug afterInsert15!
                    log("\n✅ Min-heap efter deleteMin():");
                    log("  " + HeapBuilder.arrayToString(afterDeleteMin, true));
                    log("  Gyldig min-heap? " + HeapBuilder.isMinHeap(afterDeleteMin));



                    log("\n" + "=".repeat(60));
                    log("ANALYSE FÆRDIG");
                    log("=".repeat(60));

                } catch (IOException e) {
                    System.err.println("Fejl ved oprettelse af logfil: " + e.getMessage());
                } finally {
                    if (fileWriter != null) {
                        fileWriter.close();
                        System.out.println("\n✅ Logfil gemt som: " + config.logFileName);
                    }
                }
            }
        }