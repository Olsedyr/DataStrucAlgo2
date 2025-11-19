package MoreQA.Graph;

import java.util.*;

public class LargestRegion {

    // Directions array for 8 possible moves
    static int[] dx = {-1, -1, -1, 0, 1, 1, 1, 0};
    static int[] dy = {-1, 0, 1, 1, 1, 0, -1, -1};

    // DFS function to explore a region
    public static int dfs(int i, int j, int[][] mat, boolean[][] visited) {
        // Mark the current cell as visited
        visited[i][j] = true;
        int count = 1;  // Current cell is part of the region

        // Explore all 8 possible directions
        for (int d = 0; d < 8; d++) {
            int ni = i + dx[d];
            int nj = j + dy[d];

            // Check bounds and whether the adjacent cell is unvisited and is a 1
            if (ni >= 0 && nj >= 0 && ni < mat.length && nj < mat[0].length && mat[ni][nj] == 1 && !visited[ni][nj]) {
                count += dfs(ni, nj, mat, visited);
            }
        }

        return count;
    }

    // Function to find the largest region
    public static int findLargestRegion(int[][] mat) {
        int rows = mat.length;
        int cols = mat[0].length;

        boolean[][] visited = new boolean[rows][cols];
        int maxRegionSize = 0;

        // Traverse the matrix
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // If we find a 1 and it's not visited, start a DFS
                if (mat[i][j] == 1 && !visited[i][j]) {
                    int regionSize = dfs(i, j, mat, visited);
                    maxRegionSize = Math.max(maxRegionSize, regionSize);
                }
            }
        }

        return maxRegionSize;  // Return the size of the largest region
    }

    public static void main(String[] args) {
        // Example Boolean matrix
        int[][] mat = {
                {1, 1, 0, 0, 0},
                {1, 1, 0, 1, 1},
                {0, 0, 0, 1, 0},
                {0, 1, 1, 1, 1},
                {0, 0, 0, 0, 0}
        };

        // Find the largest region
        int largestRegion = findLargestRegion(mat);
        System.out.println("Largest Region Size: " + largestRegion);
    }
}
