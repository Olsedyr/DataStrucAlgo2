import java.util.Arrays;

public class InversionCounter {

    public static void main(String[] args) {
        // Eksempel liste fra dit spørgsmål, hvis ingen argumenter gives
        int[] list;
        if (args.length == 0) {
            list = new int[]{78, 64, 61, 45, 25, 11};
            System.out.println("Ingen argumenter givet – bruger eksempel-listen: " + Arrays.toString(list));
        } else {
            list = new int[args.length];
            for (int i = 0; i < args.length; i++) {
                list[i] = Integer.parseInt(args[i]);
            }
            System.out.println("Givet liste: " + Arrays.toString(list));
        }

        // Iterativ metode (brute force)
        long iterativeCount = countInversionsIterative(list);
        System.out.println("Antal inversioner (iterativ metode): " + iterativeCount);

        // Rekursiv metode (modified Merge Sort)
        long recursiveCount = countInversionsRecursive(list);
        System.out.println("Antal inversioner (rekursiv metode): " + recursiveCount);
    }

    /**
     * Iterativ metode til at tælle inversioner.
     * En inversion er et par (i, j) hvor i < j og arr[i] > arr[j].
     * O(n²) tid, O(1) ekstra plads.
     */
    public static long countInversionsIterative(int[] arr) {
        long count = 0;
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[i] > arr[j]) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Rekursiv metode baseret på modified Merge Sort.
     * Tæller inversioner under sortering.
     * O(n log n) tid, O(n) ekstra plads.
     * Den originale liste ændres ikke – vi arbejder på en kopi.
     */
    public static long countInversionsRecursive(int[] original) {
        int[] arr = original.clone(); // Kopiér for ikke at ændre originalen
        int[] temp = new int[arr.length];
        return mergeSortAndCount(arr, temp, 0, arr.length - 1);
    }

    private static long mergeSortAndCount(int[] arr, int[] temp, int left, int right) {
        long invCount = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;

            invCount += mergeSortAndCount(arr, temp, left, mid);
            invCount += mergeSortAndCount(arr, temp, mid + 1, right);
            invCount += mergeAndCount(arr, temp, left, mid, right);
        }
        return invCount;
    }

    private static long mergeAndCount(int[] arr, int[] temp, int left, int mid, int right) {
        int i = left;    // Start på venstre del
        int j = mid + 1; // Start på højre del
        int k = left;    // Start på temp array
        long invCount = 0;

        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                // Inversion fundet: alle resterende elementer i venstre del er større end arr[j]
                temp[k++] = arr[j++];
                invCount += (mid - i + 1);
            }
        }

        // Kopiér resterende elementer fra venstre del
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // Kopiér resterende elementer fra højre del
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // Kopiér tilbage til original array
        for (int idx = left; idx <= right; idx++) {
            arr[idx] = temp[idx];
        }

        return invCount;
    }
}


//En inversion i en liste er et par af elementer, hvor det første element kommer før det andet i listen, men alligevel er større end det andet.
//
//Formelt: For en liste arr, er et inversion-par (i, j) sådan, at i < j og arr[i] > arr[j].
//
//Listen:
//[78, 64, 61, 45, 25, 11]
//
//
//Vi skal tælle alle par (i, j) med i < j hvor arr[i] > arr[j].
//
//Lad os tælle inversions trin for trin:
//
//78 (index 0) sammenlignes med alle til højre:
//
//64 (0 < 1), 78 > 64 → inversion
//
//61 (0 < 2), 78 > 61 → inversion
//
//45 (0 < 3), 78 > 45 → inversion
//
//25 (0 < 4), 78 > 25 → inversion
//
//11 (0 < 5), 78 > 11 → inversion
//5 inversions
//
//64 (index 1) sammenlignes med til højre:
//
//61 (1 < 2), 64 > 61 → inversion
//
//45 (1 < 3), 64 > 45 → inversion
//
//25 (1 < 4), 64 > 25 → inversion
//
//11 (1 < 5), 64 > 11 → inversion
//4 inversions
//
//61 (index 2) sammenlignes med til højre:
//
//45 (2 < 3), 61 > 45 → inversion
//
//25 (2 < 4), 61 > 25 → inversion
//
//11 (2 < 5), 61 > 11 → inversion
//3 inversions
//
//45 (index 3) sammenlignes med til højre:
//
//25 (3 < 4), 45 > 25 → inversion
//
//11 (3 < 5), 45 > 11 → inversion
//2 inversions
//
//25 (index 4) sammenlignes med til højre:
//
//11 (4 < 5), 25 > 11 → inversion
//1 inversion
//
//11 (index 5) har ingen til højre.
//
//Samlet:
//
//5 + 4 + 3 + 2 + 1 = 15 inversions