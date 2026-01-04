public class OccurenceInArray {

    public static void main(String[] args) {
        // Int array
        int[] intArr = {5,28,7,25,7,9,28,11,67,5,33,28};
        System.out.println("Int Iterativ: " + getNumberEqualIterative(intArr, intArr.length, 28));
        System.out.println("Int Rekursiv: " + getNumberEqual(intArr, intArr.length, 28));

        // Char array
        char[] charArr = {'a', 'b', 'a', 'c', 'd', 'a'};
        System.out.println("Char Iterativ: " + getNumberEqualIterative(charArr, charArr.length, 'a'));
        System.out.println("Char Rekursiv: " + getNumberEqual(charArr, charArr.length, 'a'));

        // String array
        String[] strArr = {"apple", "banana", "apple", "cherry", "banana"};
        System.out.println("String Iterativ: " + getNumberEqualIterative(strArr, strArr.length, "banana"));
        System.out.println("String Rekursiv: " + getNumberEqual(strArr, strArr.length, "banana"));
    }

    // -------- INT --------
    public static int getNumberEqual(int[] x, int n, int val) {
        if (n == 0) return 0;
        return (x[n - 1] == val ? 1 : 0) + getNumberEqual(x, n - 1, val);
    }

    public static int getNumberEqualIterative(int[] x, int n, int val) {
        int count = 0;
        for (int i = 0; i < n; i++)
            if (x[i] == val) count++;
        return count;
    }

    // -------- CHAR --------
    public static int getNumberEqual(char[] x, int n, char val) {
        if (n == 0) return 0;
        return (x[n - 1] == val ? 1 : 0) + getNumberEqual(x, n - 1, val);
    }

    public static int getNumberEqualIterative(char[] x, int n, char val) {
        int count = 0;
        for (int i = 0; i < n; i++)
            if (x[i] == val) count++;
        return count;
    }

    // -------- STRING --------
    public static int getNumberEqual(String[] x, int n, String val) {
        if (n == 0) return 0;
        // Brug equals til sammenligning af String!
        return (x[n - 1].equals(val) ? 1 : 0) + getNumberEqual(x, n - 1, val);
    }

    public static int getNumberEqualIterative(String[] x, int n, String val) {
        int count = 0;
        for (int i = 0; i < n; i++)
            if (x[i].equals(val)) count++;
        return count;
    }
}
