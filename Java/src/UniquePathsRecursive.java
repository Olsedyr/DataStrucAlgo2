public class UniquePathsRecursive {

    // Recursive function to count unique paths in n x m grid
    // Starting from top-left (0,0) to bottom-right (n-1,m-1)
    public static int countPaths(int n, int m) {
        // Base cases:
        // If either dimension is 1, there is only 1 path (all the way down or all the way right)
        if (n == 1 || m == 1) {
            return 1;
        }
        // Recursive calls:
        // Number of paths = paths if move down + paths if move right
        return countPaths(n - 1, m) + countPaths(n, m - 1);
    }

    public static void main(String[] args) {
        int n = 3; // rows
        int m = 3; // columns

        int result = countPaths(n, m);
        System.out.printf("Number of unique paths in a %dx%d grid: %d%n", n, m, result);
    }
}
