package MoreQA.BigONotation;

import java.io.*;
import java.util.*;
import java.util.regex.*;

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
        List<LoopInfo> loops = parseLoops(code);
        String complexity = computeComplexity(loops);
        System.out.println("Tidskompleksitet: " + complexity);
    }

    private static List<LoopInfo> parseLoops(String code) {
        List<LoopInfo> loops = new ArrayList<>();
        Stack<Integer> nestingStack = new Stack<>();
        Pattern pattern = Pattern.compile("for\\s*\\(([^;]+);([^;]+);([^)]*)\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(code);

        int depth = 0;

        while (matcher.find()) {
            String init = matcher.group(1).trim();
            String condition = matcher.group(2).trim();
            String update = matcher.group(3).trim();

            if (!nestingStack.isEmpty()) {
                depth = nestingStack.size();
            }

            loops.add(new LoopInfo(init, condition, update, depth));
            nestingStack.push(loops.size() - 1);
        }

        return loops;
    }

    private static String computeComplexity(List<LoopInfo> loops) {
        int maxDepth = 0;
        int logCount = 0;
        int sqrtCount = 0;
        int polyExponent = 0;
        boolean hasLinear = false;
        boolean hasBreakCondition = false;

        for (LoopInfo loop : loops) {
            maxDepth = Math.max(maxDepth, loop.depth + 1);
            if (loop.isLogarithmic()) {
                logCount++;
            }
            if (loop.isSqrt()) sqrtCount++;
            if (loop.isLinear()) hasLinear = true;
            if (loop.isPolynomial()) polyExponent += loop.getPolynomialExponent();
            if (loop.hasBreakCondition()) hasBreakCondition = true;
        }

        // Adjust for break conditions (early loop exit)
        if (hasBreakCondition) {
            return "O(N^2)";  // Handling cases where break occurs
        }

        // Handling the cases with logarithmic growth (log N)
        if (logCount == 1 && hasLinear) return "O(N log N)";
        if (logCount > 1) return "O(N log^2 N)";

        // General complexity classification
        if (polyExponent > 0 && logCount > 0) return "O(N^" + polyExponent + " log N)";
        if (polyExponent > 1) return "O(N^" + polyExponent + ")";
        if (logCount >= 2) return "O(N log^" + logCount + " N)";
        if (maxDepth > 1) {
            if (sqrtCount > 0) return "O(N sqrt N)";
            if (logCount == 1) return "O(N log N)";
            return "O(N^" + maxDepth + ")";
        }
        if (hasLinear && logCount > 1) return "O(N log^2 N)";
        if (hasLinear && logCount == 1) return "O(N log N)";
        if (hasLinear && sqrtCount > 0) return "O(N sqrt N)";
        if (logCount > 1) return "O(log^2 N)";
        if (logCount > 0) return "O(log N)";
        if (sqrtCount > 0) return "O(sqrt N)";
        if (hasLinear) return "O(N)";
        return "O(1)";
    }



    static class LoopInfo {
        String init, condition, update;
        int depth;

        LoopInfo(String init, String condition, String update, int depth) {
            this.init = init;
            this.condition = condition;
            this.update = update;
            this.depth = depth;
        }

        boolean isLogarithmic() {
            return update.matches(".*\\b\\w+\\s*[*\\/]=\\s*\\d+.*") ||
                    update.matches(".*\\b\\w+\\s*=\\s*\\w+\\s*[*\\/]\\s*\\d+.*") ||
                    update.matches(".*\\b\\w+\\s*=\\s*\\w+\\s*<<\\s*\\d+.*") ||
                    update.matches(".*\\b\\w+\\s*=\\s*\\w+\\s*>>\\s*\\d+.*");
        }

        boolean isSqrt() {
            return update.contains("Math.sqrt") || condition.contains("Math.sqrt");
        }

        boolean isLinear() {
            return update.matches(".*\\b\\w+\\s*[+-]=\\s*\\d+.*") || condition.matches(".*\\b\\w+\\s*<\\s*N.*");
        }

        boolean isPolynomial() {
            return condition.matches(".*\\b\\w+\\s*<\\s*N\\s*\\^\\s*\\d+.*") ||
                    condition.matches(".*\\b\\w+\\s*<\\s*N\\s*[*]{2}\\s*\\d+.*");
        }

        boolean hasBreakCondition() {
            return update.contains("break") || condition.contains("==");
        }

        int getPolynomialExponent() {
            Pattern polyPattern = Pattern.compile("N\\s*\\^\\s*(\\d+)");
            Matcher matcher = polyPattern.matcher(condition);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
            return 1;
        }
    }


}