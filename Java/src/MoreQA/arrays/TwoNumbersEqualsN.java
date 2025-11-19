package MoreQA.arrays;

public class TwoNumbersEqualsN {


    // Metode med lineær tidskompleksitet
    public static boolean hasPairWithSumLinear(int[] array, int X) {
        int left = 0;
        int right = array.length - 1;

        while (left < right) {
            int sum = array[left] + array[right];
            if (sum == X) {
                return true;
            } else if (sum < X) {
                left++;
            } else {
                right--;
            }
        }
        return false;
    }

    // Metode med kvadratisk tidskompleksitet
    public static boolean hasPairWithSumQuadratic(int[] array, int X) {
        int n = array.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (array[i] + array[j] == X) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int X = 10;

        //linear
        boolean resultLinear = hasPairWithSumLinear(array, X);
        System.out.println("Lineær metode: " + resultLinear);


        //Quadratic
        boolean resultQuadratic = hasPairWithSumQuadratic(array, X);
        System.out.println("Kvadratisk metode: " + resultQuadratic);
    }

}





