package Sorting;

public class TableContainsTwoNumbers {

    public static int[] findPairQuadratic(int[] arr, int X) {
        int N = arr.length;
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                if (arr[i] + arr[j] == X) {
                    return new int[]{arr[i], arr[j]};  // Return array containing the pair
                }
            }
        }
        return null;  // No pairs found, return null
    }



    public static int[] findPairLinear(int[] arr, int X) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int currentSum = arr[left] + arr[right];

            if (currentSum == X) {
                return new int[]{arr[left], arr[right]};  // Return array containing the pair
            } else if (currentSum < X) {
                left++;  // Increase sum by moving left pointer
            } else {
                right--;  // Decrease sum by moving right pointer
            }
        }
        return null;  // No pairs found
    }



        public static void main(String[] args) {


        int[] arr = {1, 2, 3, 4, 5};
        int X = 8;

        //Quadratic
        int[] result = findPairQuadratic(arr, X);

        if (result != null) {
            System.out.println("Quadratic - Pair found: " + result[0] + ", " + result[1]);
        } else {
            System.out.println("Quadratic - No pair found");
        }


        //Linear
            int[] resultL = findPairLinear(arr, X);
            if (resultL != null) {
                System.out.println("Linear - Pair found: " + resultL[0] + ", " + resultL[1]);
            } else {
                System.out.println("Linear - No pair found");
            }
    }
}
