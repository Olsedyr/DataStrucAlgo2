#!/usr/bin/env python3
"""
Empirical Big-O Complexity Estimator for ANY C++ Function
---------------------------------------------------------
Paste any C++ function into CPP_FUNCTION (supports int, std::vector<int>, etc.)
The script generates a wrapper that internally times repeated calls and outputs the timing.
"""

import subprocess
import tempfile
import os
import time
import math
import statistics
import shutil
import re

# =============== USER PASTE YOUR C++ FUNCTION HERE ===============

CPP_FUNCTION = r"""
// Example function - replace with any function you want to test
int foo(int n) {
    int s = 0;
    for (int i = 0; i < n; ++i) s += i;
    return s;
}
"""

# =============== CONFIGURATION ===============

TEST_SIZES = [10000, 50000, 100000, 200000, 500000, 1000000]
WARMUP_RUNS = 5
INNER_REPEAT = 1000000  # Number of times to call function inside C++ timing loop

COMPILER = "g++"
CXX_FLAGS = ["-O2", "-std=c++17"]

# =============== CORE FUNCTIONS ===============

def parse_function_signature(code):
    """Extract function name and parameters from first function in the code."""
    func_pattern = re.compile(
        r'(\w[\w\s:&<>]*)\s+(\w+)\s*\(([^)]*)\)', re.MULTILINE)
    m = func_pattern.search(code)
    if not m:
        raise ValueError("Could not parse function signature from code.")
    ret_type = m.group(1).strip()
    func_name = m.group(2).strip()
    params_str = m.group(3).strip()
    if params_str == "":
        params = []
    else:
        params = []
        parts = [p.strip() for p in params_str.split(',')]
        for p in parts:
            tokens = p.split()
            if len(tokens) < 2:
                param_type = p
                param_name = "arg"
            else:
                param_name = tokens[-1]
                param_type = " ".join(tokens[:-1])
            params.append((param_type, param_name))
    return func_name, params

def generate_wrapper(func_name, params):
    """
    Generate a test_function that accepts int n,
    calls your function INNER_REPEAT times,
    measures total elapsed time and prints it.
    """
    setup_lines = []
    call_args = []

    for (typ, var) in params:
        if "int" in typ and "std::vector" not in typ:
            call_args.append("n")
        elif "std::vector<int>" in typ:
            setup_lines.append(f"    std::vector<int> {var}(n, 42);")
            call_args.append(var)
        else:
            setup_lines.append(f"    {typ} {var}{{}};")
            call_args.append(var)

    setup_code = "\n".join(setup_lines)
    call_code = f"{func_name}({', '.join(call_args)});"

    wrapper_code = f"""
#include <iostream>
#include <chrono>
#include <vector>
int main(int argc, char** argv) {{
    if (argc < 2) {{
        std::cerr << "Usage: " << argv[0] << " <n>\\n";
        return 1;
    }}
    int n = std::atoi(argv[1]);

    {setup_code}

    const int INNER_REPEAT = {INNER_REPEAT};

    // Warmup run to avoid cold start effects
    for (int i = 0; i < INNER_REPEAT / 10; ++i) {{
        {call_code}
    }}

    auto start = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < INNER_REPEAT; ++i) {{
        {call_code}
    }}

    auto end = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> elapsed = end - start;

    // Print total time in seconds
    std::cout << elapsed.count() << std::endl;

    return 0;
}}
"""
    return wrapper_code

def compile_cpp(full_code):
    tmpdir = tempfile.mkdtemp(prefix="big_o_")
    cpp_path = os.path.join(tmpdir, "test.cpp")
    exe_path = os.path.join(tmpdir, "test.exe" if os.name == "nt" else "test")

    with open(cpp_path, "w") as f:
        f.write(full_code)

    try:
        subprocess.check_call(
            [COMPILER, *CXX_FLAGS, cpp_path, "-o", exe_path],
            stdout=subprocess.DEVNULL,
            stderr=subprocess.DEVNULL
        )
    except subprocess.CalledProcessError as e:
        shutil.rmtree(tmpdir)
        raise RuntimeError("Compilation failed. Check your C++ code.") from e

    return exe_path, tmpdir

def run_and_get_time(exe, n):
    proc = subprocess.run([exe, str(n)], stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    if proc.returncode != 0:
        raise RuntimeError(f"Execution failed for n={n}:\n{proc.stderr}")
    try:
        t = float(proc.stdout.strip())
        return t / INNER_REPEAT  # average time per call
    except ValueError:
        raise RuntimeError(f"Could not parse timing output: {proc.stdout}")

def linear_regression(x, y):
    n = len(x)
    sx = sum(x)
    sy = sum(y)
    sxy = sum(a * b for a, b in zip(x, y))
    sx2 = sum(a * a for a in x)
    denom = n * sx2 - sx * sx
    if abs(denom) < 1e-12:
        return 0, 0, 0
    a = (n * sxy - sx * sy) / denom
    b = (sy * sx2 - sx * sxy) / denom
    mean_y = sy / n
    ss_tot = sum((yi - mean_y) ** 2 for yi in y)
    ss_res = sum((yi - (a * xi + b)) ** 2 for xi, yi in zip(x, y))
    r2 = 0 if ss_tot == 0 else 1 - ss_res / ss_tot
    return a, b, max(0, min(1, r2))

def fit_models(ns, times):
    models = {
        "O(1)": lambda n: 1,
        "O(log n)": lambda n: math.log(n),
        "O(n)": lambda n: n,
        "O(n log n)": lambda n: n * math.log(n),
        "O(n²)": lambda n: n * n,
        "O(n³)": lambda n: n * n * n,
    }
    results = {}
    for name, f in models.items():
        x = [f(n) for n in ns]
        a, b, r2 = linear_regression(x, times)
        results[name] = r2
    return results

def log_log_fit(ns, times):
    log_n = []
    log_t = []
    for n, t in zip(ns, times):
        if t > 1e-9:
            log_n.append(math.log(n))
            log_t.append(math.log(t))
    if len(log_n) < 3:
        return 0, 0
    a, b, r2 = linear_regression(log_n, log_t)
    return a, r2

def interpret_exponent(k):
    if k < 0.3:
        return "O(1) or sub-logarithmic"
    elif k < 0.7:
        return "O(log n)"
    elif k < 1.05:
        return "O(n)"
    elif k < 1.4:
        return "O(n log n)"
    elif k < 2.2:
        return "O(n²)"
    elif k < 3.0:
        return "O(n³)"
    else:
        return "Super-cubic"

def main():
    print("\n══════════════════════════════════════════")
    print("   EMPIRICAL BIG-O ESTIMATOR (C++)")
    print("══════════════════════════════════════════\n")

    # Parse function signature
    func_name, params = parse_function_signature(CPP_FUNCTION)

    # Generate full code with user function + main wrapper timing code
    includes = "#include <iostream>\n#include <vector>\n#include <cstdlib>\n#include <chrono>\nusing namespace std;\n\n"
    wrapper = generate_wrapper(func_name, params)
    full_code = includes + CPP_FUNCTION + "\n" + wrapper

    # Compile
    try:
        exe, tmpdir = compile_cpp(full_code)
    except RuntimeError as e:
        print(e)
        return

    print("Warming up...")
    mid_size = TEST_SIZES[len(TEST_SIZES)//2]
    for _ in range(WARMUP_RUNS):
        subprocess.run([exe, str(mid_size)], stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

    print("\nMeasuring execution times:")
    times = []
    for n in TEST_SIZES:
        try:
            t = run_and_get_time(exe, n)
        except RuntimeError as e:
            print(e)
            shutil.rmtree(tmpdir, ignore_errors=True)
            return
        times.append(t)
        print(f"  n = {n:8d} → {t:.9f} s")

    print("\nModel fitting (R²):")
    fits = fit_models(TEST_SIZES, times)
    for k, v in sorted(fits.items(), key=lambda x: -x[1]):
        print(f"  {k:10s} : {v:.6f}")

    exponent, log_r2 = log_log_fit(TEST_SIZES, times)
    interpretation = interpret_exponent(exponent)

    print("\nLog–log analysis:")
    print(f"  Estimated exponent k = {exponent:.4f}")
    print(f"  R² = {log_r2:.4f}")
    print(f"  ⇒ Estimated complexity: {interpretation}")

    shutil.rmtree(tmpdir, ignore_errors=True)
    print("\n══════════════════════════════════════════")

if __name__ == "__main__":
    main()
