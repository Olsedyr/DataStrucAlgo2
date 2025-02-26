package MoreQA.arrays;

import java.util.Arrays;

public class ClosestSumToPowerOfTwo {

    // Funktion til at finde de tre tal, hvis sum er tættest på en potens af 2
    public static int[] findClosestSum(int[] array) {
        int n = array.length;
        int[] result = new int[4]; // De tre tal og den tilhørende potens af 2
        int closestSum = Integer.MAX_VALUE;
        int closestPowerOfTwo = 0;

        // Find alle potenser af 2 op til en rimelig grænse
        int[] powersOfTwo = new int[31];
        for (int i = 0; i < 31; i++) {
            powersOfTwo[i] = 1 << i; // 2^i
        }

        // Gennemgå alle kombinationer af tre tal
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                for (int k = j + 1; k < n; k++) {
                    int sum = array[i] + array[j] + array[k];
                    // Find den potens af 2, der er tættest på summen
                    for (int power : powersOfTwo) {
                        if (Math.abs(sum - power) < Math.abs(closestSum - closestPowerOfTwo)) {
                            closestSum = sum;
                            closestPowerOfTwo = power;
                            result[0] = array[i];
                            result[1] = array[j];
                            result[2] = array[k];
                            result[3] = power;
                        }
                    }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[] array = {23, 56, 22, 11, 65, 89, 3, 44, 87, 910, 45, 35, 98};
        int[] result = findClosestSum(array);
        System.out.println("De tre tal: " + result[0] + ", " + result[1] + ", " + result[2]);
        System.out.println("Potens af 2: " + result[3]);
    }
}