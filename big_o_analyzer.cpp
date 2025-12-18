#include <iostream>
#include <string>
#include <vector>
#include <sstream>
#include <algorithm>
#include <regex>

using namespace std;

struct AnalysisResult {
    string complexity;
    string explanation;
    int maxDepth;
    bool hasRecursion;
    vector<string> loopDetails;
    vector<string> analysisDetails;
};

class BigOAnalyzer {
private:
    string trimString(const string& str) {
        size_t first = str.find_first_not_of(" \t\n\r");
        if (first == string::npos) return "";
        size_t last = str.find_last_not_of(" \t\n\r");
        return str.substr(first, last - first + 1);
    }

    bool containsPattern(const string& line, const string& pattern) {
        return line.find(pattern) != string::npos;
    }

    bool isForLoop(const string& line) {
        regex forPattern(R"(\bfor\s*\()");
        regex rangePattern(R"(\bfor\s*\(\s*(?:auto|const\s+auto|[a-zA-Z_][\w]*)\s*(?:&|&&)?\s*:\s*)");
        return regex_search(line, forPattern) || regex_search(line, rangePattern);
    }

    bool isWhileLoop(const string& line) {
        regex whilePattern(R"(\bwhile\s*\()");
        regex doWhilePattern(R"(\bdo\s*\{)");
        return regex_search(line, whilePattern) || regex_search(line, doWhilePattern);
    }

    string extractFunctionName(const string& code) {
        regex funcPattern(R"((?:void|int|long|double|float|bool|string|char|auto)\s+(\w+)\s*\([^)]*\))");
        smatch match;
        if (regex_search(code, match, funcPattern)) {
            return match[1].str();
        }
        return "";
    }

    bool hasLogarithmicPattern(const string& code) {
        regex dividePattern(R"(\b(?:i|j|k|left|right|mid)\s*(?:\/=|>>=)\s*2\b)");
        regex binarySearchPattern(R"(\b(?:left|right|mid|start|end)\b)");

        if (regex_search(code, dividePattern)) return true;

        int bsCount = 0;
        string::const_iterator searchStart(code.cbegin());
        smatch match;
        while (regex_search(searchStart, code.cend(), match, binarySearchPattern)) {
            bsCount++;
            searchStart = match.suffix().first;
        }

        return bsCount >= 3;
    }

public:
    AnalysisResult analyze(const string& code) {
        AnalysisResult result;
        result.maxDepth = 0;
        result.hasRecursion = false;

        istringstream stream(code);
        string line;
        int currentDepth = 0;
        int lineNumber = 0;

        string functionName = extractFunctionName(code);

        while (getline(stream, line)) {
            lineNumber++;
            string trimmed = trimString(line);
            if (trimmed.empty()) continue;

            // Check for loops
            if (isForLoop(trimmed)) {
                currentDepth++;
                result.maxDepth = max(result.maxDepth, currentDepth);
                result.loopDetails.push_back("Line " + to_string(lineNumber) + ": FOR loop (depth " + to_string(currentDepth) + ")");
                result.analysisDetails.push_back("Line " + to_string(lineNumber) + ": For loop detected (depth " + to_string(currentDepth) + ")");
            } else if (isWhileLoop(trimmed)) {
                currentDepth++;
                result.maxDepth = max(result.maxDepth, currentDepth);
                result.loopDetails.push_back("Line " + to_string(lineNumber) + ": WHILE loop (depth " + to_string(currentDepth) + ")");
                result.analysisDetails.push_back("Line " + to_string(lineNumber) + ": While loop detected (depth " + to_string(currentDepth) + ")");
            }

            // Check for recursion
            if (!functionName.empty() && containsPattern(trimmed, functionName + "(")) {
                regex declPattern(R"((?:void|int|long|double|float|bool|string|char|auto)\s+\w+\s*\([^)]*\))");
                if (!regex_search(trimmed, declPattern)) {
                    result.hasRecursion = true;
                    result.analysisDetails.push_back("Line " + to_string(lineNumber) + ": Recursive call detected");
                }
            }

            // Track closing braces
            if (containsPattern(trimmed, "}")) {
                currentDepth = max(0, currentDepth - 1);
            }
        }

        // Determine complexity
        bool hasLogPattern = hasLogarithmicPattern(code);

        if (result.hasRecursion) {
            size_t callCount = 0;
            size_t pos = 0;
            while ((pos = code.find(functionName + "(", pos)) != string::npos) {
                callCount++;
                pos += functionName.length();
            }

            if (callCount > 2) {
                result.complexity = "O(2^n)";
                result.explanation = "Exponential time - multiple recursive calls detected (e.g., naive Fibonacci)";
            } else {
                result.complexity = "O(n)";
                result.explanation = "Linear time - single recursive call detected";
            }
        } else if (result.maxDepth == 0) {
            result.complexity = "O(1)";
            result.explanation = "Constant time - no loops detected";
        } else if (result.maxDepth == 1) {
            if (hasLogPattern) {
                result.complexity = "O(log n)";
                result.explanation = "Logarithmic time - divide-and-conquer pattern detected (e.g., binary search)";
            } else {
                result.complexity = "O(n)";
                result.explanation = "Linear time - single loop detected";
            }
        } else if (result.maxDepth == 2) {
            if (hasLogPattern) {
                result.complexity = "O(n log n)";
                result.explanation = "Linearithmic time - nested loop with logarithmic pattern (e.g., merge sort)";
            } else {
                result.complexity = "O(n²)";
                result.explanation = "Quadratic time - nested loops detected";
            }
        } else if (result.maxDepth == 3) {
            result.complexity = "O(n³)";
            result.explanation = "Cubic time - triple-nested loops detected";
        } else {
            result.complexity = "O(n^" + to_string(result.maxDepth) + ")";
            result.explanation = "Polynomial time - " + to_string(result.maxDepth) + " levels of nested loops detected";
        }

        return result;
    }
};

void printBanner() {
    cout << "\n";
    cout << "╔════════════════════════════════════════════════════════════╗\n";
    cout << "║        C++ BIG O COMPLEXITY ESTIMATOR                      ║\n";
    cout << "╚════════════════════════════════════════════════════════════╝\n";
    cout << "\n";
}

void printComplexityReference() {
    cout << "\n";
    cout << "┌─────────────────────────────────────────────────────────────┐\n";
    cout << "│ COMPLEXITY REFERENCE                                        │\n";
    cout << "├─────────────────────────────────────────────────────────────┤\n";
    cout << "│ O(1)       - Constant      | Direct access, simple ops      │\n";
    cout << "│ O(log n)   - Logarithmic   | Binary search, balanced trees  │\n";
    cout << "│ O(n)       - Linear        | Single loop, iteration         │\n";
    cout << "│ O(n log n) - Linearithmic  | Merge sort, quick sort         │\n";
    cout << "│ O(n²)      - Quadratic     | Nested loops, bubble sort      │\n";
    cout << "│ O(n³)      - Cubic         | Triple nested loops            │\n";
    cout << "│ O(2^n)     - Exponential   | Recursive branching, subsets   │\n";
    cout << "└─────────────────────────────────────────────────────────────┘\n";
    cout << "\n";
}

void printResult(const AnalysisResult& result) {
    cout << "\n";
    cout << "╔════════════════════════════════════════════════════════════╗\n";
    cout << "║ ANALYSIS RESULTS                                           ║\n";
    cout << "╚════════════════════════════════════════════════════════════╝\n";
    cout << "\n";

    cout << "► TIME COMPLEXITY: " << result.complexity << "\n";
    cout << "  " << result.explanation << "\n\n";

    cout << "┌─────────────────────────────────────────────────────────────┐\n";
    cout << "│ LOOP ANALYSIS                                               │\n";
    cout << "├─────────────────────────────────────────────────────────────┤\n";
    cout << "│ Max Nesting Depth: " << result.maxDepth << "                                        │\n";
    cout << "│ Total Loops:       " << result.loopDetails.size() << "                                        │\n";

    if (!result.loopDetails.empty()) {
        cout << "│                                                             │\n";
        cout << "│ Detected Loops:                                             │\n";
        for (const auto& detail : result.loopDetails) {
            cout << "│   • " << detail;
            int padding = 57 - detail.length();
            for (int i = 0; i < padding; i++) cout << " ";
            cout << "│\n";
        }
    }
    cout << "└─────────────────────────────────────────────────────────────┘\n";

    cout << "\n";
    cout << "┌─────────────────────────────────────────────────────────────┐\n";
    cout << "│ ADDITIONAL PATTERNS                                         │\n";
    cout << "├─────────────────────────────────────────────────────────────┤\n";
    cout << "│ Recursion: " << (result.hasRecursion ? "YES" : "NO") << "                                               │\n";
    cout << "└─────────────────────────────────────────────────────────────┘\n";

    if (!result.analysisDetails.empty()) {
        cout << "\n";
        cout << "┌─────────────────────────────────────────────────────────────┐\n";
        cout << "│ DETAILED ANALYSIS                                           │\n";
        cout << "├─────────────────────────────────────────────────────────────┤\n";
        for (const auto& detail : result.analysisDetails) {
            cout << "│ • " << detail;
            int padding = 59 - detail.length();
            for (int i = 0; i < padding; i++) cout << " ";
            cout << "│\n";
        }
        cout << "└─────────────────────────────────────────────────────────────┘\n";
    }

    cout << "\n";
    cout << "NOTE: This is an estimation tool. Actual complexity may vary\n";
    cout << "      based on implementation details and STL operations.\n";
}

void interactiveMode() {
    BigOAnalyzer analyzer;

    while (true) {
        printBanner();
        cout << "Paste your C++ function (end with '###' on a new line):\n";
        cout << "Or type 'example' to see a sample, 'quit' to exit\n\n";

        string input;
        getline(cin, input);

        if (input == "quit" || input == "q" || input == "exit") {
            cout << "\nThank you for using Big O Estimator!\n\n";
            break;
        }

        if (input == "example" || input == "e") {
            string example = R"(int binarySearch(vector<int>& arr, int target) {
    int left = 0, right = arr.size() - 1;
    while (left <= right) {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target)
            return mid;
        else if (arr[mid] < target)
            left = mid + 1;
        else
            right = mid - 1;
    }
    return -1;
})";
            cout << "\nExample Binary Search Function:\n";
            cout << "─────────────────────────────────────\n";
            cout << example << "\n";
            cout << "─────────────────────────────────────\n";

            AnalysisResult result = analyzer.analyze(example);
            printResult(result);

            cout << "\nPress Enter to continue...";
            cin.ignore();
            continue;
        }

        string code = input + "\n";
        string line;

        while (getline(cin, line)) {
            if (line == "###") break;
            code += line + "\n";
        }

        if (code.length() <= 1) {
            cout << "\nNo code provided. Try again.\n";
            continue;
        }

        AnalysisResult result = analyzer.analyze(code);
        printResult(result);
        printComplexityReference();

        cout << "\nPress Enter to analyze another function...";
        cin.ignore();
    }
}

int main() {
    interactiveMode();
    return 0;
}