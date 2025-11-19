import java.util.HashMap;
import java.util.Map;
public class StartingNumberUnderNprodcuesLongestChain {

    private static Map<Integer, Integer> memo = new HashMap<>();

    public static void main(String[] args) {
        int limit = 10000;
        int startingNumber = 0;
        int maxLength = 0;

        for (int i = 1; i < limit; i++) {
            int length = collatzSequenceLength(i);
            if (length > maxLength) {
                maxLength = length;
                startingNumber = i;
            }
        }

        System.out.println("The starting number under " + limit + " that produces the longest chain is " + startingNumber + " with a chain length of " + maxLength + ".");
    }

    public static int collatzSequenceLength(int n) {
        if (n == 1) {
            return 1;
        }
        if (memo.containsKey(n)) {
            return memo.get(n);
        }
        int length;
        if (n % 2 == 0) {
            length = 1 + collatzSequenceLength(n / 2);
        } else {
            length = 1 + collatzSequenceLength(3 * n + 1);
        }
        memo.put(n, length);
        return length;
    }
}

