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

def rehash_table(original_table, probing_func):
    old_size = len(original_table)
    new_size = next_prime(old_size * 2)
    new_table = ['#'] * new_size
    rehash_map = {}
    for old_idx, val in enumerate(original_table):
        if val != '#' and val is not None and not isinstance(val, list):
            idx, steps = probing_func(new_table, val, new_size)
            rehash_map[val] = (old_idx, idx, steps)
    return new_table, rehash_map

def rehash_table_chaining(original_table):
    old_size = len(original_table)
    new_size = next_prime(old_size * 2)
    new_table = [None] * new_size
    rehash_map = {}
    for old_idx, val in enumerate(original_table):
        if val is not None and val != '#' and isinstance(val, list):
            for k in val:
                new_idx = hash_function(k, new_size)
                if new_table[new_idx] is None:
                    new_table[new_idx] = []
                new_table[new_idx].append(k)
                rehash_map[k] = (old_idx, new_idx)
        elif val != '#' and val is not None and not isinstance(val, list):
            new_idx = hash_function(val, new_size)
            if new_table[new_idx] is None:
                new_table[new_idx] = []
            new_table[new_idx].append(val)
            rehash_map[val] = (old_idx, new_idx)
    return new_table, rehash_map

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

if __name__ == "__main__":
    # Original data input (index, char)
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

    print("\nRehashing linear probing...")
    linear_rehash_table, linear_rehash_map = rehash_table(original_table, linear_probing_insert)
    print(f"Old size: {table_size}, New size: {len(linear_rehash_table)}")
    print(f"Load factor before: {calculate_load_factor(original_table):.2f}, after: {calculate_load_factor(linear_rehash_table):.2f}")
    print("Rehash details (key, old_idx, new_idx, probe_steps):")
    for k, (old_i, new_i, steps) in linear_rehash_map.items():
        print(f"  {key_to_char(k)}: {old_i} -> {new_i}, steps: {steps}")

    print("\nRehashing quadratic probing...")
    quad_rehash_table, quad_rehash_map = rehash_table(original_table, quadratic_probing_insert)
    print(f"Old size: {table_size}, New size: {len(quad_rehash_table)}")
    print(f"Load factor before: {calculate_load_factor(original_table):.2f}, after: {calculate_load_factor(quad_rehash_table):.2f}")
    print("Rehash details (key, old_idx, new_idx, probe_steps):")
    for k, (old_i, new_i, steps) in quad_rehash_map.items():
        print(f"  {key_to_char(k)}: {old_i} -> {new_i}, steps: {steps}")

    print("\nRehashing chaining...")
    chain_rehash_table, chain_rehash_map = rehash_table_chaining(original_table)
    print(f"Old size: {table_size}, New size: {len(chain_rehash_table)}")
    print(f"Load factor before: {calculate_load_factor(original_table):.2f}, after: {calculate_load_factor(chain_rehash_table):.2f}")
    print("Rehash details (key, old_idx, new_idx):")
    for k, (old_i, new_i) in chain_rehash_map.items():
        print(f"  {key_to_char(k)}: {old_i} -> {new_i}")

    # Visualize rehashed tables
    fig, axs = plt.subplots(4, 1, figsize=(15, 8), constrained_layout=True)
    visualize_table(original_table, axs[0], "Original Table (Hash Only)")
    visualize_table(linear_rehash_table, axs[1], "Linear Probing (After Rehash)")
    visualize_table(quad_rehash_table, axs[2], "Quadratic Probing (After Rehash)")
    visualize_table(chain_rehash_table, axs[3], "Chaining (After Rehash)")
    plt.show()
