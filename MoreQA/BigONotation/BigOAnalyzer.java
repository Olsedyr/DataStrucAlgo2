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
        Pattern pattern = Pattern.compile("for\\s*\\(([^;]+);([^;]+);([^)]*)\\)");
        Matcher matcher = pattern.matcher(code);
        int index = 0;
        int depth = 0;

        while (matcher.find()) {
            String init = matcher.group(1).trim();
            String condition = matcher.group(2).trim();
            String update = matcher.group(3).trim();

            depth = nestingStack.size();
            loops.add(new LoopInfo(init, condition, update, depth));
            nestingStack.push(index++);
        }
        return loops;
    }

    private static String computeComplexity(List<LoopInfo> loops) {
        int depth = 0;
        int logCount = 0;
        int sqrtCount = 0;
        int polyExponent = 0;
        boolean hasLinear = false;

        for (LoopInfo loop : loops) {
            depth = Math.max(depth, loop.depth + 1);
            if (loop.isLogarithmic()) logCount++;
            if (loop.isSqrt()) sqrtCount++;
            if (loop.isLinear()) hasLinear = true;
            if (loop.isPolynomial()) polyExponent += loop.getPolynomialExponent();
        }

        if (polyExponent > 1) return "O(N^" + polyExponent + ")";
        if (depth > 1) {
            if (logCount == 2) return "O(N log² N)";
            if (sqrtCount > 0) return "O(N sqrt N)";
            if (logCount == 1) return "O(N log N)";
            return "O(N^" + depth + ")";
        }
        if (hasLinear && logCount > 1) return "O(N log² N)";
        if (hasLinear && logCount == 1) return "O(N log N)";
        if (hasLinear && sqrtCount > 0) return "O(N sqrt N)";
        if (logCount > 1) return "O(log² N)";
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
            return update.matches("\\w+\\s*/=\\s*\\d+") || update.matches("\\w+\\s*=\\s*\\w+\\s*/\\s*\\d+");
        }

        boolean isSqrt() {
            return update.contains("Math.sqrt") || condition.contains("Math.sqrt");
        }

        boolean isLinear() {
            return update.matches("\\w+\\s*\\+=\\s*[^/]") || condition.matches("\\w+\\s*<\\s*N");
        }

        boolean isPolynomial() {
            return condition.matches("\\w+\\s*<\\s*N\\s*\\*\\*\\s*\\d+") || condition.matches("\\w+\\s*<\\s*N\\^\\d+");
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
