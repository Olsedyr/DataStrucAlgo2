package MoreQA.Matrix;

import java.util.Arrays;

public class MatrixTranspose {

    /**
     * Brute Force Approach: O(R * C) time, O(R * C) space
     * Creates a new matrix where rows become columns.
     */
    public static int[][] transposeBruteForce(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] transposed = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }

    /**
     * In-Place Approach (for square matrices): O(N^2) time, O(1) space
     * Swaps elements across the diagonal.
     */
    public static void transposeInPlace(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = temp;
            }
        }
    }

    /**
     * Helper method to print a matrix.
     */
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        System.out.println("Original Matrix:");
        printMatrix(matrix);

        // Test Brute Force Approach
        int[][] transposedBruteForce = transposeBruteForce(matrix);
        System.out.println("After Brute Force Transpose:");
        printMatrix(transposedBruteForce);

        // Test In-Place Approach (Only for square matrices)
        int[][] matrixInPlace = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        transposeInPlace(matrixInPlace);
        System.out.println("After In-Place Transpose:");
        printMatrix(matrixInPlace);
    }
}
