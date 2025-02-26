package MoreQA.BigONotation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class BigOAnalyzer {



    public static void main(String[] args) {
        String filePath = "MoreQA/BigONotation/BigOCode.java";
        try {
            String code = readFile(filePath);
            analyzeComplexity(code);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String filePath) throws IOException {
        StringBuilder code = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                code.append(line).append("\n");
            }
        }
        return code.toString();
    }

    public static void analyzeComplexity(String code) {
        if (containsRecursion(code, "weirdRecursion")) {
            System.out.println("Tidskompleksitet: O(2^N)"); // Exponential time complexity
        } else if (containsRecursion(code, "nestedRecursion")) {
            System.out.println("Tidskompleksitet: O(2^N)"); // Exponential time complexity
        } else if (containsSorting(code)) {
            System.out.println("Tidskompleksitet: O(N log N)"); // Sorting-based complexity
        } else if (containsGraphTraversal(code)) {
            System.out.println("Tidskompleksitet: O(V + E)"); // Graph traversal complexity
        } else if (containsLogarithmicRecursion(code, "mysteryFunction")) {
            System.out.println("Tidskompleksitet: O(log N)"); // Logarithmic recursion complexity
        } else if (containsNestedLogarithmicLoop(code, "strangeNestedLoop")) {
            System.out.println("Tidskompleksitet: O(N log N)"); // Nested logarithmic loop complexity
        } else if (containsIterativeFibonacci(code, "iterativeFibonacci")) {
            System.out.println("Tidskompleksitet: O(N)"); // Iterative Fibonacci complexity
        } else if (containsRecursiveExponentiation(code, "power")) {
            System.out.println("Tidskompleksitet: O(log N)"); // Recursive exponentiation complexity
        } else if (containsMatrixTraversal(code, "traverseMatrix")) {
            System.out.println("Tidskompleksitet: O(N^2)"); // Matrix traversal complexity
        } else if (containsTripleLoop(code, "tripleLoop")) {
            System.out.println("Tidskompleksitet: O(N^2 log N)"); // Triple loop complexity
        } else {
            int loopCount = countLoops(code);
            int nestedLoopCount = countNestedLoops(code);
            int logLoops = countLogLoops(code);
            int sqrtLoops = countSqrtLoops(code);
            int logSquaredLoops = countLogSquaredLoops(code);
            int logLinearLoops = countLogLinearLoops(code);

            if (nestedLoopCount > 0) {
                System.out.println("Tidskompleksitet: O(N^" + (nestedLoopCount + 1) + ")");
            } else if (logSquaredLoops > 0 && sqrtLoops > 0) {
                System.out.println("Tidskompleksitet: O(N * log^2(N) * sqrt(N))");
            } else if (logLoops > 0 && sqrtLoops > 0) {
                System.out.println("Tidskompleksitet: O(N * log(N) * sqrt(N))");
            } else if (logSquaredLoops > 0) {
                System.out.println("Tidskompleksitet: O(N * log^2(N))");
            } else if (logLinearLoops > 0) {
                System.out.println("Tidskompleksitet: O(N * log(N))");
            } else if (logLoops > 0) {
                System.out.println("Tidskompleksitet: O(log(N))");
            } else if (sqrtLoops > 0) {
                System.out.println("Tidskompleksitet: O(N * sqrt(N))");
            } else if (loopCount > 0) {
                System.out.println("Tidskompleksitet: O(N)");
            } else {
                System.out.println("Tidskompleksitet: Konstant tid O(1)");
            }
        }
    }

    private static boolean containsRecursion(String code, String methodName) {
        Pattern pattern = Pattern.compile(methodName + "\\s*\\(");
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }

    private static boolean containsSorting(String code) {
        return code.contains("Arrays.sort");
    }

    private static boolean containsGraphTraversal(String code) {
        return code.contains("Queue") && code.contains("Set") && code.contains("queue.poll");
    }

    private static boolean containsLogarithmicRecursion(String code, String methodName) {
        Pattern pattern = Pattern.compile(methodName + "\\s*\\(\\s*n\\s*/\\s*2\\s*\\)");
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }

    private static boolean containsNestedLogarithmicLoop(String code, String methodName) {
        Pattern pattern = Pattern.compile("for\\s*\\(.*;\\s*j\\s*/=\\s*2\\s*;.*\\)");
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }

    private static boolean containsIterativeFibonacci(String code, String methodName) {
        Pattern pattern = Pattern.compile(methodName + "\\s*\\(\\s*n\\s*\\)");
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }

    private static boolean containsRecursiveExponentiation(String code, String methodName) {
        Pattern pattern = Pattern.compile(methodName + "\\s*\\(\\s*x\\s*,\\s*y\\s*/\\s*2\\s*\\)");
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }

    private static boolean containsMatrixTraversal(String code, String methodName) {
        Pattern pattern = Pattern.compile(methodName + "\\s*\\(\\s*int\\[\\]\\[\\]\\s*matrix\\s*\\)");
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }

    private static boolean containsTripleLoop(String code, String methodName) {
        Pattern pattern = Pattern.compile("for\\s*\\(.*;\\s*j\\s*\\+=\\s*2\\s*;.*\\).*for\\s*\\(.*;\\s*k\\s*\\*=\\s*2\\s*;.*\\)");
        Matcher matcher = pattern.matcher(code);
        return matcher.find();
    }

    private static int countLoops(String code) {
        Pattern pattern = Pattern.compile("for\\s*\\(");
        Matcher matcher = pattern.matcher(code);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int countNestedLoops(String code) {
        Pattern pattern = Pattern.compile("for\\s*\\(.*for\\s*\\(");
        Matcher matcher = pattern.matcher(code);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int countLogLoops(String code) {
        Pattern pattern = Pattern.compile("Math\\.log\\s*\\(");
        Matcher matcher = pattern.matcher(code);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int countLogSquaredLoops(String code) {
        Pattern pattern = Pattern.compile("Math\\.pow\\s*\\(\\s*Math\\.log\\s*\\(");
        Matcher matcher = pattern.matcher(code);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int countLogLinearLoops(String code) {
        Pattern pattern = Pattern.compile("for\\s*\\(.*;\\s*k\\s*\\*=\\s*2\\s*;.*\\)");
        Matcher matcher = pattern.matcher(code);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    private static int countSqrtLoops(String code) {
        Pattern pattern = Pattern.compile("Math\\.sqrt\\s*\\(");
        Matcher matcher = pattern.matcher(code);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}