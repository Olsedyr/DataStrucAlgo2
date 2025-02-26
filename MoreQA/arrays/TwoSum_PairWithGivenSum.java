package MoreQA.arrays;
import java.util.Arrays;
import java.util.HashSet;



//Given an array arr[] of n integers and a
// target value, the task is to find whether
// there is a pair of elements in the array
// whose sum is equal to target.
// This problem is a variation of 2Sum problem.
public class TwoSum_PairWithGivenSum {

    // Brute Force Approach: O(N^2)
    public static boolean hasPairBruteForce(int[] arr, int sum) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (arr[i] + arr[j] == sum) {
                    return true;
                }
            }
        }
        return false;
    }

    // Sorting + Two Pointers Approach: O(N log N) (sorting) + O(N) = O(N log N)
    public static boolean hasPairTwoPointers(int[] arr, int sum) {
        Arrays.sort(arr); // Sort array first
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int currentSum = arr[left] + arr[right];
            if (currentSum == sum) {
                return true;
            } else if (currentSum < sum) {
                left++;
            } else {
                right--;
            }
        }
        return false;
    }

    // Hashing Approach: O(N) time and O(N) space
    public static boolean hasPairHashing(int[] arr, int sum) {
        HashSet<Integer> set = new HashSet<>();
        for (int num : arr) {
            int complement = sum - num;
            if (set.contains(complement)) {
                return true;
            }
            set.add(num);
        }
        return false;
    }

    public static void main(String[] args) {
        int[] arr = {1, 4, 45, 6, 10, 8};

        int sum = 16;

        System.out.println("Brute Force Approach: " + hasPairBruteForce(arr, sum));
        System.out.println("Two Pointers Approach: " + hasPairTwoPointers(arr.clone(), sum)); // clone to avoid sorting affecting later checks
        System.out.println("Hashing Approach: " + hasPairHashing(arr, sum));
    }
}




