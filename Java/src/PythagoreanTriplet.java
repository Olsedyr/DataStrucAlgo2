public class PythagoreanTriplet {
    public static void main(String[] args) {
        int a = 0, b = 0, c = 0;
        boolean found = false;

        for (a = 1; a < 1000 / 3; a++) {
            for (b = a + 1; b < 1000 / 2; b++) {
                c = 1000 - a - b;
                if (a * a + b * b == c * c) {
                    found = true;
                    break;
                }
            }
            if (found) break;
        }

        System.out.println("a: " + a + ", b: " + b + ", c: " + c);
        System.out.println("Product abc: " + (a * b * c));
    }
}