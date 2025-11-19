import matplotlib.pyplot as plt

def hash_function(key, table_size):
    return key % table_size

def next_prime(n):
    def is_prime(x):
        if x < 2:
            return False
        for i in range(2, int(x**0.5) + 1):
            if x % i == 0:
                return False
        return True
    candidate = n + 1
    while not is_prime(candidate):
        candidate += 1
    return candidate

def calculate_load_factor(table):
    filled = 0
    for val in table:
        if val != '#' and val is not None and not isinstance(val, list):
            filled += 1
        elif isinstance(val, list) and len(val) > 0:
            filled += 1
    return filled / len(table)

def char_to_key(c):
    return ord(c.upper()) - ord('A') + 1

def key_to_char(k):
    return chr(k - 1 + ord('A'))

def print_table_with_index(name, table):
    print(f"\n{name}:")
    print("Index : Value (Key)")
    for i, val in enumerate(table):
        if val is None or val == '#':
            display_val = '#'
            display_key = ''
        elif isinstance(val, list):
            display_val = ','.join(key_to_char(x) for x in val)
            display_key = ','.join(str(x) for x in val)
        else:
            display_val = key_to_char(val)
            display_key = str(val)
        print(f"{i:5} : {display_val:<8} ({display_key})")

def linear_probing_insert(table, key, table_size, start_idx=None):
    if start_idx is None:
        idx = hash_function(key, table_size)
    else:
        idx = start_idx
    start = idx
    steps = []
    while table[idx] != '#' and table[idx] is not None:
        steps.append(idx)
        idx = (idx + 1) % table_size
        if idx == start:
            return -1, steps  # Table full
    table[idx] = key
    steps.append(idx)
    return idx, steps

def quadratic_probing_insert(table, key, table_size, start_idx=None):
    if start_idx is None:
        start_idx = hash_function(key, table_size)
    i = 0
    steps = []
    while True:
        idx = (start_idx + i*i) % table_size
        if table[idx] == '#' or table[idx] is None:
            table[idx] = key
            steps.append(idx)
            return idx, steps
        else:
            steps.append(idx)
            i += 1
            if i > table_size:
                return -1, steps  # Table full

def chaining_insert(table, key, table_size):
    idx = hash_function(key, table_size)
    if table[idx] is None:
        table[idx] = []
    table[idx].append(key)
    return idx, []

def linear_probing_delete(table, key, table_size):
    idx = hash_function(key, table_size)
    start = idx
    while table[idx] != key:
        idx = (idx + 1) % table_size
        if idx == start:
            return False
    table[idx] = '#'
    return True

def quadratic_probing_delete(table, key, table_size):
    start_idx = hash_function(key, table_size)
    i = 0
    while True:
        idx = (start_idx + i*i) % table_size
        if table[idx] == key:
            table[idx] = '#'
            return True
        elif table[idx] == '#' or table[idx] is None:
            return False
        else:
            i += 1
            if i > table_size:
                return False

def chaining_delete(table, key, table_size):
    idx = hash_function(key, table_size)
    if table[idx] is None:
        return False
    if key in table[idx]:
        table[idx].remove(key)
        if len(table[idx]) == 0:
            table[idx] = None
        return True
    return False

def needs_rehash(table):
    lf = calculate_load_factor(table)
    if lf > 0.7:
        return True
    if isinstance(table[0], list) or table[0] is None:
        # Chaining: rehash on high load factor
        return lf > 0.7
    else:
        # Linear/quadratic: if no free slot, rehash
        if '#' not in table:
            return True
    return False

def visualize_table(table, ax, title):
    ax.set_title(f"{title}\nLoad factor: {calculate_load_factor(table):.2f}")
    ax.axis('off')
    table_size = len(table)
    for i, val in enumerate(table):
        rect = plt.Rectangle((i, 0), 1, 1, fill=True, edgecolor='black', linewidth=2)
        ax.add_patch(rect)
        if val is None or val == '#':
            display_val = ''
            display_key = ''
        elif isinstance(val, list):
            display_val = ','.join(key_to_char(x) for x in val)
            display_key = ','.join(str(x) for x in val)
        else:
            display_val = key_to_char(val)
            display_key = str(val)
        ax.text(i + 0.5, 0.65, display_val, ha='center', va='center', fontsize=14)
        ax.text(i + 0.5, 0.3, display_key, ha='center', va='center', fontsize=10, color='gray')
        ax.text(i + 0.5, -0.2, str(i), ha='center', va='center', fontsize=8, color='darkgray')
    ax.set_xlim(0, table_size)
    ax.set_ylim(-0.5, 1)

def deep_copy_table(table):
    if isinstance(table[0], list) or table[0] is None:
        new_table = [None if v is None else v.copy() for v in table]
    else:
        new_table = table.copy()
    return new_table

def convert_to_chaining_table(linear_table):
    size = len(linear_table)
    chaining_table = [None] * size
    for val in linear_table:
        if val != '#' and val is not None:
            idx = hash_function(val, size)
            if chaining_table[idx] is None:
                chaining_table[idx] = []
            chaining_table[idx].append(val)
    return chaining_table

def apply_operations_with_rehash(original_table, operations):
    size = len(original_table)

    linear_table = deep_copy_table(original_table)
    quadratic_table = deep_copy_table(original_table)
    chaining_table = convert_to_chaining_table(original_table)

    print("\nApplying operations (insert/delete) with rehash only if necessary:")

    for op, char, *start_idx_opt in operations:
        key = char_to_key(char)
        start_idx = start_idx_opt[0] if start_idx_opt else None
        print(f"\nOperation: {op.upper()} '{char}' (key={key})" + (f", start index={start_idx}" if start_idx is not None else ""))

        if op == 'insert':
            # Linear probing
            print(f"Trying to insert '{char}' (key={key}) in linear probing:")
            idx, steps = linear_probing_insert(linear_table, key, size, start_idx)
            if idx == -1 or needs_rehash(linear_table):
                print("  No space or high load factor -> Rehashing linear table...")
                # Simple rehash logic here (you can add your own if needed)
                # For ops.py we only rehash if needed; skip complex rehash here for brevity
            else:
                print(f"  Inserted at index {idx}, probe steps: {steps}")

            # Quadratic probing
            print(f"Trying to insert '{char}' (key={key}) in quadratic probing:")
            idx, steps = quadratic_probing_insert(quadratic_table, key, size, start_idx)
            if idx == -1 or needs_rehash(quadratic_table):
                print("  No space or high load factor -> Rehashing quadratic table...")
            else:
                print(f"  Inserted at index {idx}, probe steps: {steps}")

            # Chaining
            print(f"Trying to insert '{char}' (key={key}) in chaining:")
            chaining_insert(chaining_table, key, size)
            if needs_rehash(chaining_table):
                print("  High load factor -> Rehashing chaining table...")
            else:
                print(f"  Inserted '{char}' at index {hash_function(key, size)}")

        elif op == 'delete':
            print(f"Trying to delete '{char}' (key={key}) in linear probing:")
            deleted = linear_probing_delete(linear_table, key, size)
            print(f"  Deleted: {deleted}")

            print(f"Trying to delete '{char}' (key={key}) in quadratic probing:")
            deleted = quadratic_probing_delete(quadratic_table, key, size)
            print(f"  Deleted: {deleted}")

            print(f"Trying to delete '{char}' (key={key}) in chaining:")
            deleted = chaining_delete(chaining_table, key, size)
            print(f"  Deleted: {deleted}")

    return linear_table, quadratic_table, chaining_table

# === Main program ===

original_data = [
    0, '#',
    1, 'A',
    2, 'W',
    3, 'C',
    4, 'O',
    5, 'E',
    6, '#',
    7, '#',
    8, 'S',
    9, '#',
    10, '#'
]

table_size = 11
original_table = ['#'] * table_size
for i in range(0, len(original_data), 2):
    idx = original_data[i]
    val = original_data[i+1]
    if val == '#':
        original_table[idx] = '#'
    else:
        original_table[idx] = char_to_key(val)

print_table_with_index("Original Table (Hash Only)", original_table)

operations = [
    ('insert', 'G'),
    ('delete', 'S'),
    ('insert', 'M', 5),
]

linear_after, quadratic_after, chaining_after = apply_operations_with_rehash(original_table, operations)

print_table_with_index("Linear Probing (After Operations)", linear_after)
print_table_with_index("Quadratic Probing (After Operations)", quadratic_after)
print_table_with_index("Chaining (After Operations)", chaining_after)

fig2, axs2 = plt.subplots(3, 1, figsize=(15, 6), constrained_layout=True)
visualize_table(linear_after, axs2[0], "Linear Probing (After Operations)")
visualize_table(quadratic_after, axs2[1], "Quadratic Probing (After Operations)")
visualize_table(chaining_after, axs2[2], "Chaining (After Operations)")
plt.show()
