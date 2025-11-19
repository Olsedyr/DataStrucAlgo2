public class Reeksamen2024 {
    // ADA Reeksamen - 27. februar 2024
// Algoritmer og datastrukturer

    // Opgave 1: Sum af to tal lig parameter
// Version 1: Kvadratisk tidskompleksitet
    public static boolean sumAfToTalLigParameterQuadratic(int[] arr, int l, int X) {
        for (int i = 0; i < l; i++) {
            for (int j = i + 1; j < l; j++) {
                if (arr[i] + arr[j] == X) {
                    return true;
                }
            }
        }
        return false;
    }

    // Version 2: Lineær tidskompleksitet
    public static boolean sumAfToTalLigParameterLinear(int[] arr, int l, int X) {
        int left = 0, right = l - 1;
        while (left < right) {
            int sum = arr[left] + arr[right];
            if (sum == X) return true;
            else if (sum < X) left++;
            else right--;
        }
        return false;
    }

    // Opgave 2: Big-O tidskompleksitet
    public static long myM(int N) {
        long x = 0;
        for (int i = 0; i < 10000; i++) {
            for (int j = 0; j < N * 10; j++) {
                for (int k = N; k > 0; k /= 10) {
                    x++;
                }
            }
        }
        return x;
    }
// Tidskompleksitet: O(N * log(N))

    // Opgave 3: Antal vokaler i en streng
    public static int antalVokaler(String str, int l) {
        if (l < 0) return 0;
        char c = str.charAt(l);
        return (erVokal(c) ? 1 : 0) + antalVokaler(str, l - 1);
    }

    private static boolean erVokal(char c) {
        return "aeiouy".indexOf(c) != -1;
    }

    // Opgave 4: Lineær sortering
    public static void minSortering(int[] arr) {
        int[] count = new int[201];
        for (int num : arr) {
            count[num]++;
        }
        for (int i = 1; i < count.length; i++) {
            while (count[i]-- > 0) {
                System.out.print(i + " ");
            }
        }
    }

// Opgave 5: Heap-order overtrædelser
// Funktionen identificerer og korrigerer værdier for at genoprette heap-order.
    //Elementer der bryder heap-order: 106, 10
    //De bør ændres for at passe til heap-struktur

    // Opgave 6: Hash-tabel implementering
// Tabel 1: Linear probing
    public static void hashTableLinearProbing(String keys, int tableSize) {
        String[] table = new String[tableSize];
        for (char key : keys.toCharArray()) {
            int hash = (11 * (key - 'A' + 1)) % tableSize;
            while (table[hash] != null) {
                hash = (hash + 1) % tableSize;
            }
            table[hash] = Character.toString(key);
        }
        printTable(table);
    }

    // Tabel 2: Quadratic probing
    public static void hashTableQuadraticProbing(String keys, int tableSize) {
        String[] table = new String[tableSize];
        for (char key : keys.toCharArray()) {
            int hash = (11 * (key - 'A' + 1)) % tableSize;
            int i = 0;
            while (table[hash] != null) {
                hash = (hash + i * i) % tableSize;
                i++;
            }
            table[hash] = Character.toString(key);
        }
        printTable(table);
    }

    private static void printTable(String[] table) {
        for (int i = 0; i < table.length; i++) {
            System.out.println(i + ": " + (table[i] == null ? "null" : table[i]));
        }
    }

// Opgave 7: Dijkstra og MST
// Implementer Dijkstra-algoritme og lav MST med Kruskal/Prim algoritme.


}
