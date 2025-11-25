public class PartitionCountRecursive {

    /**
     * Counts the number of ways to partition n using parts up to m.
     *
     * @param n total objects to partition
     * @param m max part size allowed
     * @return number of partitions
     */
    public static int countPartitions(int n, int m) {
        // Base cases
        if (n == 0) {
            return 1;  // exactly one way to partition zero: empty partition
        }
        if (n < 0 || m == 0) {
            return 0;  // no way to partition negative or no parts allowed
        }

        // Recurrence:
        // Count partitions that include at least one part of size m
        // plus partitions that do not include any part of size m
        return countPartitions(n - m, m) + countPartitions(n, m - 1);
    }

    public static void main(String[] args) {
        int n = 6;
        int m = 4;

        int result = countPartitions(n, m);
        System.out.printf("Number of partitions of %d using parts up to %d: %d%n", n, m, result);
    }
}
