public class SumAfToTal {

    public static boolean sumAfToTalLigParameterQuadratic(int[] arr, int l, int X) {
        for (int i = 0; i < l; i++) {
            for (int j = i + 1; j < l; j++) {
                if (arr[i] + arr[j] == X) {
                    return true;
                }
            }
        }
        return false;
    }

    // Version 2: Lineær tidskompleksitet
    public static boolean sumAfToTalLigParameterLinear(int[] arr, int l, int X) {
        int left = 0, right = l - 1;
        while (left < right) {
            int sum = arr[left] + arr[right];
            if (sum == X) return true;
            else if (sum < X) left++;
            else right--;
        }
        return false;
    }


    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 5, 8};
        System.out.println(sumAfToTalLigParameterLinear(arr, arr.length, 10));
        System.out.println(sumAfToTalLigParameterQuadratic(arr, arr.length, 4));
    }
}
