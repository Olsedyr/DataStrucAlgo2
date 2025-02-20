package MoreQA.Matrix;

public class WordSearch2D {

    /**
     * Searches for a given word in all 8 possible directions
     * from each cell of the grid.
     * Time Complexity: O(R * C * 8 * len(word))
     */
    public static boolean searchWord(char[][] grid, String word, int row, int col, int rowDir, int colDir) {
        int len = word.length();
        int rows = grid.length;
        int cols = grid[0].length;

        for (int i = 0; i < len; i++) {
            int newRow = row + i * rowDir;
            int newCol = col + i * colDir;

            if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols || grid[newRow][newCol] != word.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Finds all occurrences of a word in a 2D grid.
     */
    public static void findWordInGrid(char[][] grid, String word) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[] rowDir = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] colDir = {-1, 0, 1, -1, 1, -1, 0, 1};

        System.out.println("Positions where word \"" + word + "\" is found:");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == word.charAt(0)) {
                    for (int dir = 0; dir < 8; dir++) {
                        if (searchWord(grid, word, i, j, rowDir[dir], colDir[dir])) {
                            System.out.println("Found at row " + i + " and column " + j);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        char[][] grid = {
                {'G', 'E', 'E', 'K', 'S'},
                {'F', 'O', 'R', 'G', 'E'},
                {'E', 'K', 'S', 'Q', 'U'},
                {'O', 'R', 'G', 'E', 'K'},
                {'E', 'E', 'K', 'S', 'G'}
        };
        String word = "GEEKS";
        findWordInGrid(grid, word);
    }
}
