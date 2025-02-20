package MoreQA.Matrix;

import java.util.Arrays;
import java.util.HashSet;

public class BooleanMatrix {

    /**
     * Brute Force Approach: O(R * C * (R + C)) time, O(1) space
     * This method modifies the matrix by iterating through each element.
     * When a 1 is found, it updates the entire row and column to 1.
     */
    public static void modifyMatrixBruteForce(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 1) {
                    // Mark the entire row
                    for (int k = 0; k < cols; k++) {
                        if (matrix[i][k] != 1) {
                            matrix[i][k] = -1;
                        }
                    }
                    // Mark the entire column
                    for (int k = 0; k < rows; k++) {
                        if (matrix[k][j] != 1) {
                            matrix[k][j] = -1;
                        }
                    }
                }
            }
        }

        // Convert -1 markers to 1
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == -1) {
                    matrix[i][j] = 1;
                }
            }
        }
    }

    /**
     * Optimized Approach: O(R * C) time, O(R + C) space
     * This method uses two arrays to mark rows and columns that should be updated.
     */
    public static void modifyMatrixOptimized(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean[] rowFlags = new boolean[rows];
        boolean[] colFlags = new boolean[cols];

        // First pass: mark rows and columns
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix[i][j] == 1) {
                    rowFlags[i] = true;
                    colFlags[j] = true;
                }
            }
        }

        // Second pass: update the matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (rowFlags[i] || colFlags[j]) {
                    matrix[i][j] = 1;
                }
            }
        }
    }

    /**
     * Most Optimized Approach: O(R * C) time, O(1) space
     * This method uses the first row and column as markers instead of extra arrays.
     */
    public static void modifyMatrixMostOptimized(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean firstRowHasOne = false;
        boolean firstColHasOne = false;

        // Check if first row has any 1s
        for (int j = 0; j < cols; j++) {
            if (matrix[0][j] == 1) {
                firstRowHasOne = true;
                break;
            }
        }

        // Check if first column has any 1s
        for (int i = 0; i < rows; i++) {
            if (matrix[i][0] == 1) {
                firstColHasOne = true;
                break;
            }
        }

        // Use first row and column as markers
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][j] == 1) {
                    matrix[i][0] = 1;
                    matrix[0][j] = 1;
                }
            }
        }

        // Update matrix based on markers
        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                if (matrix[i][0] == 1 || matrix[0][j] == 1) {
                    matrix[i][j] = 1;
                }
            }
        }

        // Update first row if needed
        if (firstRowHasOne) {
            for (int j = 0; j < cols; j++) {
                matrix[0][j] = 1;
            }
        }

        // Update first column if needed
        if (firstColHasOne) {
            for (int i = 0; i < rows; i++) {
                matrix[i][0] = 1;
            }
        }
    }

    /**
     * Check if an array contains two numbers that sum to a target.
     * Uses a HashSet for O(n) time complexity.
     */
    public static boolean hasPairWithSum(int[] arr, int target) {
        HashSet<Integer> seen = new HashSet<>();
        for (int num : arr) {
            if (seen.contains(target - num)) {
                return true;
            }
            seen.add(num);
        }
        return false;
    }

    // Helper method to print the matrix
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {0, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 0, 0}
        };

        System.out.println("Original Matrix:");
        printMatrix(matrix);

        // Test Brute Force Approach
        int[][] bruteForceMatrix = Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new);
        modifyMatrixBruteForce(bruteForceMatrix);
        System.out.println("After Brute Force Approach:");
        printMatrix(bruteForceMatrix);

        // Test Optimized Approach
        int[][] optimizedMatrix = Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new);
        modifyMatrixOptimized(optimizedMatrix);
        System.out.println("After Optimized Approach:");
        printMatrix(optimizedMatrix);

        // Test Most Optimized Approach
        int[][] mostOptimizedMatrix = Arrays.stream(matrix).map(int[]::clone).toArray(int[][]::new);
        modifyMatrixMostOptimized(mostOptimizedMatrix);
        System.out.println("After Most Optimized Approach:");
        printMatrix(mostOptimizedMatrix);

        // Test hasPairWithSum function
        int[] array = {1, 4, 7, 8, 10};
        int target = 15;
        System.out.println("Does the array contain two numbers that sum to " + target + "? " + hasPairWithSum(array, target));
    }
}
