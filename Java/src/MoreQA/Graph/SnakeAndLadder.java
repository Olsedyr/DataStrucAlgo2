package MoreQA.Graph;

import java.util.*;

public class SnakeAndLadder {

    // Function to find the minimum number of dice rolls to reach the last cell
    public static int minDiceRolls(int N, int[] moves) {
        // BFS to find the shortest path to the last cell (N)
        boolean[] visited = new boolean[N + 1];  // To track visited cells
        Queue<Integer> queue = new LinkedList<>();
        queue.add(1); // Start from cell 1
        visited[1] = true;

        int rolls = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();

            // Process all nodes at the current level (current number of dice rolls)
            for (int i = 0; i < size; i++) {
                int current = queue.poll();

                // If we've reached the last cell, return the number of rolls
                if (current == N) {
                    return rolls;
                }

                // Try all dice rolls from 1 to 6
                for (int dice = 1; dice <= 6; dice++) {
                    int next = current + dice;
                    if (next <= N && !visited[next]) {
                        visited[next] = true;

                        // If there is a ladder or snake at the next position, move accordingly
                        if (moves[next] != -1) {
                            next = moves[next];
                        }

                        queue.add(next);
                    }
                }
            }

            // Increase the dice roll count after processing all positions at the current level
            rolls++;
        }

        // If we can't reach the last cell, return -1 (should not happen in a valid game)
        return -1;
    }

    public static void main(String[] args) {
        // Example: N = 30, 6 snakes and ladders
        int N = 30;
        int[] moves = new int[N + 1];

        // -1 means no snake or ladder at the given position
        Arrays.fill(moves, -1);

        // Ladders
        moves[2] = 21;   // Ladder from 2 to 21
        moves[4] = 7;    // Ladder from 4 to 7
        moves[10] = 25;  // Ladder from 10 to 25
        moves[19] = 28;  // Ladder from 19 to 28

        // Snakes
        moves[16] = 3;   // Snake from 16 to 3
        moves[18] = 6;   // Snake from 18 to 6
        moves[20] = 8;   // Snake from 20 to 8
        moves[26] = 0;   // Snake from 26 to 0
        moves[21] = 9;   // Snake from 21 to 9
        moves[19] = 7;   // Snake from 19 to 7

        System.out.println("Minimum Dice Rolls (Example): " + minDiceRolls(N, moves));
    }
}
