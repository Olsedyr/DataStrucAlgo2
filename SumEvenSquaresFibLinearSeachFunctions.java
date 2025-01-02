
public class SumEvenSquaresFibLinearSeachFunctions {

    //Sum of the first n natural numbers
    public int sum(int n) {
        if (n == 0) {
            return 0;
        } else {
            return n + sum(n - 1);
        }
    }

    //Sum of squares of the first n even numbers
    public int evenSquares(int n) {
        if (n == 0) {
            return 0;
        } else {
            int evenNumber = 2 * n;
            return (evenNumber * evenNumber) + evenSquares(n - 1);
        }
    }

    //n'th Fibonacci number
    public int fib(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fib(n - 1) + fib(n - 2);
        }
    }


    //Check if a string contains a character using recursion
    public boolean linear(String s, char c, int l) {
        if (l == 0) {
            return false;
        } else if (s.charAt(l - 1) == c) {
            return true;
        } else {
            return linear(s, c, l - 1);
        }
    }

    //Binary search for a value in a sorted array
    public boolean binarySearch(int[] arr, int left, int right, int value) {
        if (left > right) {
            return false;
        }
        int mid = left + (right - left) / 2;

        if (arr[mid] == value) {
            return true;
        } else if (arr[mid] > value) {
            return binarySearch(arr, left, mid - 1, value);
        } else {
            return binarySearch(arr, mid + 1, right, value);
        }
    }



    public static void main(String[] args) {
        SumEvenSquaresFibLinearSeachFunctions rec = new SumEvenSquaresFibLinearSeachFunctions();

        // Test sum function
        System.out.println("Sum of first 5 natural numbers: " + rec.sum(5)); // Output: 15

        // Test evenSquares function
        System.out.println("Sum of squares of first 3 even numbers: " + rec.evenSquares(3)); // Output: 56 (2^2 + 4^2 + 6^2)

        // Test fib function
        System.out.println("5th Fibonacci number: " + rec.fib(5)); // Output: 5

        // Test linear search function
        String testString = "recursion";
        char testChar = 'r';
        System.out.println("Does 'recursion' contain 'r'?: " + rec.linear(testString, testChar, testString.length())); // Output: true

        // Test binarySearch function
        int[] sortedArray = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        int valueToSearch = 5;
        System.out.println("Is 5 in the array?: " + rec.binarySearch(sortedArray, 0, sortedArray.length - 1, valueToSearch)); // Output: true

        valueToSearch = 10;
        System.out.println("Is 10 in the array?: " + rec.binarySearch(sortedArray, 0, sortedArray.length - 1, valueToSearch)); // Output: false
    }
}




