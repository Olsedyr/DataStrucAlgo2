import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class ClosestSum {

    // Hovedmetode til at køre programmet - ændr her for dynamik
    public static void main(String[] args) {
        int[] array = {23, 56, 22, 11, 65, 89, 3, 44, 87, 910, 45, 35, 98}; // Ændr arrayet her
        int m = 3; // Antal tal at vælge (1, 2 eller 3)
        String targetType = "power_of_2"; // "power_of_2" eller "fixed"
        long fixedTarget = 150; // Kun brugt hvis targetType = "fixed"

        // Sorter arrayet
        Arrays.sort(array);

        // Kald iterativ metode
       // long[] iterativeResult = findClosestSumIterative(array, m, targetType, fixedTarget);
        //printResult("Iterative", iterativeResult, m, targetType);

        // Kald rekursiv metode
        long[] recursiveResult = findClosestSumRecursive(array, m, targetType, fixedTarget);
        printResult("Recursive", recursiveResult, m, targetType);
    }

    // Iterativ metode (optimeret for m<=3 med two-pointers)
    /*private static long[] findClosestSumIterative(int[] array, int m, String targetType, long fixedTarget) {
        if (m < 1 || m > 3) throw new IllegalArgumentException("m must be 1, 2 or 3");

        List<Long> targets = generateTargets(array, m, targetType, fixedTarget);
        long minDiff = Long.MAX_VALUE;
        long bestTarget = 0;
        int[] bestNums = new int[m];

        for (long target : targets) {
            if (m == 1) {
                for (int num : array) {
                    long diff = Math.abs(num - target);
                    if (diff < minDiff) {
                        minDiff = diff;
                        bestNums[0] = num;
                        bestTarget = target;
                    }
                }
            } else if (m == 2) {
                int left = 0, right = array.length - 1;
                while (left < right) {
                    long sum = (long) array[left] + array[right];
                    long diff = Math.abs(sum - target);
                    if (diff < minDiff) {
                        minDiff = diff;
                        bestNums[0] = array[left];
                        bestNums[1] = array[right];
                        bestTarget = target;
                    }
                    if (sum < target) left++;
                    else right--;
                }
            } else if (m == 3) {
                for (int i = 0; i < array.length - 2; i++) {
                    int left = i + 1, right = array.length - 1;
                    while (left < right) {
                        long sum = (long) array[i] + array[left] + array[right];
                        long diff = Math.abs(sum - target);
                        if (diff < minDiff) {
                            minDiff = diff;
                            bestNums[0] = array[i];
                            bestNums[1] = array[left];
                            bestNums[2] = array[right];
                            bestTarget = target;
                        }
                        if (sum < target) left++;
                        else right--;
                    }
                }
            }
        }

        long[] result = new long[m + 1];
        for (int i = 0; i < m; i++) result[i] = bestNums[i];
        result[m] = bestTarget;
        return result;
    }

     */

    // Rekursiv metode (backtracking for generel m)
    private static long[] findClosestSumRecursive(int[] array, int m, String targetType, long fixedTarget) {
        if (m < 1 || m > array.length) throw new IllegalArgumentException("Invalid m");

        List<Long> targets = generateTargets(array, m, targetType, fixedTarget);
        long[] minDiff = {Long.MAX_VALUE};
        long[] bestTarget = {0};
        List<Integer> current = new ArrayList<>();
        int[] bestNums = new int[m];

        recurse(array, 0, m, targets, current, minDiff, bestTarget, bestNums, targetType.equals("fixed") ? fixedTarget : 0);

        long[] result = new long[m + 1];
        for (int i = 0; i < m; i++) result[i] = bestNums[i];
        result[m] = bestTarget[0];
        return result;
    }

    private static void recurse(int[] array, int start, int remaining, List<Long> targets, List<Integer> current,
                                long[] minDiff, long[] bestTarget, int[] bestNums, long fixedTarget) {
        if (remaining == 0) {
            long sum = 0;
            for (int num : current) sum += num;
            for (long target : targets) {
                long diff = Math.abs(sum - target);
                if (diff < minDiff[0]) {
                    minDiff[0] = diff;
                    bestTarget[0] = target;
                    for (int i = 0; i < current.size(); i++) bestNums[i] = current.get(i);
                }
            }
            return;
        }

        for (int i = start; i < array.length; i++) {
            current.add(array[i]);
            recurse(array, i + 1, remaining - 1, targets, current, minDiff, bestTarget, bestNums, fixedTarget);
            current.remove(current.size() - 1);
        }
    }

    // Generer targets: Potenser af 2 eller fixed
    private static List<Long> generateTargets(int[] array, int m, String targetType, long fixedTarget) {
        List<Long> targets = new ArrayList<>();
        if (targetType.equals("fixed")) {
            targets.add(fixedTarget);
        } else if (targetType.equals("power_of_2")) {
            // Beregn max mulig sum (m største tal)
            long maxSum = 0;
            for (int i = array.length - 1; i >= array.length - m; i--) maxSum += array[i];
            long power = 1;
            while (power <= maxSum * 2) { // Lidt ekstra for at dække over
                targets.add(power);
                if (power > Long.MAX_VALUE / 2) break;
                power *= 2;
            }
        } else {
            throw new IllegalArgumentException("Invalid targetType");
        }
        return targets;
    }

    // Dynamisk print baseret på logik
    private static void printResult(String method, long[] result, int m, String targetType) {
        System.out.print(method + " result: ");
        for (int i = 0; i < m; i++) {
            System.out.print(result[i] + (i < m - 1 ? ", " : " "));
        }
        if (targetType.equals("power_of_2")) {
            System.out.println("and power of 2: " + result[m]);
        } else {
            System.out.println("and target: " + result[m]);
        }
    }
}

//Rekursiv Metode (findClosestSumRecursive)
//
//Tidskompleksitet: O((n choose m) * m * log(MAX_SUM)) = O(n^3) ish
//
//Optimeringer:
//Pruning: Tilføj bounds i recurse: Beregn current sum + min/max mulige tilføjelser. Hvis det ikke kan slå current minDiff, skip branch (branch-and-bound). Reducerer til O(n²) i bedste tilfælde for m=3.
//Memoization: Svært for closest-sum, da det ikke er eksakt subset-sum, men cache partial sums for fixed m.
//Hybrid: Brug iterativ for m≤3, rekursiv kun for højere m. Eller skift til dynamisk programmering hvis target er fixed (knapsack-lignende), men for closest og power_of_2 er det kompleks (O(n * m * MAX_SUM), men MAX_SUM kan være stor).
//Approksimation: For store n/m, brug greedy (vælg tal closest til target/m) eller genetic algorithms for nær-optimal i O(n log n).
//Begrænsninger: Eksponentiel i m, så begræns m≤5 i kode. For m=n/2 bliver det NP-hårdt.

//Iterativ Metode (findClosestSumIterative)
//
//Tidskompleksitet: O(n² log(MAX_SUM)) = O(n^2) ish
//
//Optimeringer:
//Reducér targets: I stedet for alle potenser, estimér en grov target (f.eks. baseret på gennemsnitssum af triplets) og søg kun ±2-3 nærliggende potenser. Dette reducerer log(MAX_SUM) til O(1), gør total O(n²).
//Meet-in-the-middle: For m=3, del array i to halvdele, generér sums af 1+2 tal (O(n²/4)), sorter én (O((n²/4) log n)), og binær-søg for closest til target - sum. Total O(n² log n) per target, men højere plads (O(n²)).
//Parallelisering: Ydre loops per target kan køres parallelt (f.eks. med Java's ExecutorService), hvis n er stort.
//Tidlig stopping: Tilføj pruning: Hvis current diff er 0 (perfekt match), stop. Eller track global minDiff og skip hvis umuligt at slå.
//Begrænsninger: For n>10^4 bliver O(n²) langsom; brug approksimationer (f.eks. random sampling af triplets) for O(n log n) med lav nøjagtighed.

