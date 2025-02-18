// Opgave 4: Counting Sort
public class CountingSort {
    public static void minSortering(int[] arr) {
        int[] count = new int[201];
        for (int num : arr) {
            count[num]++;
        }
        for (int i = 1; i <= 200; i++) {
            for (int j = 0; j < count[i]; j++) {
                System.out.print(i + " ");
            }
        }
    }
    public static void main(String[] args) {
        int[] arr = {5, 3, 100, 1, 99, 50};
        minSortering(arr);
    }
}