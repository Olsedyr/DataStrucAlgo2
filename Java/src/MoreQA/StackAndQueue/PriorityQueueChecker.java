package MoreQA.StackAndQueue;

public class PriorityQueueChecker {

    public static String checkMaxHeap(int[] priorities) {
        int n = priorities.length;
        StringBuilder explanation = new StringBuilder();
        boolean isMaxHeap = true;

        for (int i = 0; i <= (n - 2) / 2; i++) {
            // Check if left child exists and is greater than parent
            if (2 * i + 1 < n && priorities[i] < priorities[2 * i + 1]) {
                explanation.append(String.format("Forælder på indeks %d (%d) er mindre end venstre barn på indeks %d (%d).\n",
                        i, priorities[i], 2 * i + 1, priorities[2 * i + 1]));
                isMaxHeap = false;
            }
            // Check if right child exists and is greater than parent
            if (2 * i + 2 < n && priorities[i] < priorities[2 * i + 2]) {
                explanation.append(String.format("Forælder på indeks %d (%d) er mindre end højre barn på indeks %d (%d).\n",
                        i, priorities[i], 2 * i + 2, priorities[2 * i + 2]));
                isMaxHeap = false;
            }
        }

        if (isMaxHeap) {
            explanation.append("Tabellen repræsenterer en maks-heap (prioritetskø).");
        } else {
            explanation.append("Tabellen repræsenterer ikke en maks-heap (prioritetskø).");
        }

        return explanation.toString();
    }

    public static void main(String[] args) {
        int[] priorities = {0,17,21,23,44,32,65,38,56,46,69,33,77,67,56,39,61,60,62,50,71};
        System.out.println(checkMaxHeap(priorities));
    }
}