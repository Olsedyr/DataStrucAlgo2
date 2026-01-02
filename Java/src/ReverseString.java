public class ReverseString {

    /**
     * Iterativ metode til at vende en String.
     * Tidskompleksitet: O(n), hvor n er længden af strengen.
     *
     * @param s Den originale streng.
     * @return Den omvendte streng.
     */
    public static String reverseIterative(String s) {
        StringBuilder reversed = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) {
            reversed.append(s.charAt(i));
        }
        return reversed.toString();
    }

    /**
     * Rekursiv metode til at vende en String.
     * Tidskompleksitet: O(n), hvor n er længden af strengen.
     *
     * @param s Den originale streng.
     * @return Den omvendte streng.
     */
    public static String reverseRecursive(String s) {
        if (s.isEmpty()) {
            return s;
        }
        return reverseRecursive(s.substring(1)) + s.charAt(0);
    }

    public static void main(String[] args) {
        String input = "Hej verden!";

        System.out.println("Original: " + input);

        String reversedIter = reverseIterative(input);
        System.out.println("Iterativt reverseret: " + reversedIter);

        String reversedRec = reverseRecursive(input);
        System.out.println("Rekursivt reverseret: " + reversedRec);
    }
}
