import matplotlib.pyplot as plt
def find_error_chaining(hash_table, hash_index):
    """Analyze chaining - all elements should be in a chain at hash_index"""
    table = hash_table.get_table()
    insertion_order = hash_table.get_insertion_order()

    print(f"\n{'='*70}")
    print(f"ANALYZING TABLE - All elements hash to index {hash_index}")
    print(f"Method: CHAINING")
    print(f"{'='*70}")

    print("\nWith chaining, all elements that hash to the same index")
    print(f"should be in a chain at index {hash_index}.")

    elements_at_hash = []
    elements_elsewhere = []

    for idx, val in enumerate(table):
        if val != '#' and val is not None:
            if isinstance(val, list):
                for v in val:
                    if idx == hash_index:
                        elements_at_hash.append(key_to_char(v))
                    else:
                        elements_elsewhere.append((key_to_char(v), idx))
            else:
                if idx == hash_index:
                    elements_at_hash.append(key_to_char(val))
                else:
                    elements_elsewhere.append((key_to_char(val), idx))

    print(f"\nElements at index {hash_index}: {elements_at_hash}")
    print(f"Elements elsewhere: {elements_elsewhere}")

    if elements_elsewhere:
        print(f"\n{'='*70}")
        print("ERROR FOUND:")
        for char, idx in elements_elsewhere:
            print(f"  '{char}' is at index {idx}, should be at index {hash_index}")
        print(f"{'='*70}")
        return elements_elsewhere[0][0], elements_elsewhere[0][1], hash_index

    print("\nAll elements correctly placed!")
    return None, None, None

def hash_function(key, table_size):
    """Default hash function"""
    return key % table_size

def char_to_key(c):
    return ord(c.upper()) - ord('A') + 1

def key_to_char(k):
    return chr(k - 1 + ord('A'))

def calculate_load_factor(table):
    filled = 0
    for val in table:
        if val != '#' and val is not None and not isinstance(val, list):
            filled += 1
        elif isinstance(val, list) and len(val) > 0:
            filled += 1
    return filled / len(table)

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

class HashTable:
    def __init__(self, table_size, method='quadratic', hash_index=None, secondary_hash=None):
        self.table_size = table_size
        self.table = ['#'] * table_size
        self.insertion_order = []
        self.method = method
        self.hash_index = hash_index  # If None, uses hash_function; if set, all hash here
        self.secondary_hash = secondary_hash  # For double hashing, if None uses default

    def insert(self, letter_or_index, letter=None):
        """
        Two modes:
        1. insert(letter) - automatically places using probing method
        2. insert(index, letter) - manually place at specific index (for given tables)
        """
        if letter is None:
            # Mode 1: insert(letter) - auto placement
            letter = letter_or_index
            key = char_to_key(letter)

            # Determine starting hash index
            if self.hash_index is not None:
                start_idx = self.hash_index
            else:
                start_idx = hash_function(key, self.table_size)

            # Insert based on method
            if self.method == 'quadratic':
                idx = self._insert_quadratic(key, start_idx)
            elif self.method == 'linear':
                idx = self._insert_linear(key, start_idx)
            elif self.method == 'chaining':
                idx = self._insert_chaining(key, start_idx)
            elif self.method == 'double':
                idx = self._insert_double_hashing(key, start_idx)
            else:
                print(f"Unknown method: {self.method}")
                return

            self.insertion_order.append(letter)
            print(f"Inserted '{letter}' (key={key}) at index {idx} using {self.method} probing")
        else:
            # Mode 2: insert(index, letter) - manual placement
            index = letter_or_index
            key = char_to_key(letter)
            self.table[index] = key
            self.insertion_order.append(letter)
            print(f"Manually inserted '{letter}' (key={key}) at index {index}")

    def _get_secondary_hash(self, key):
        """Calculate secondary hash for double hashing"""
        if self.secondary_hash is not None:
            # Use provided secondary hash value
            return self.secondary_hash
        else:
            # Default secondary hash: h2(k) = 1 + (k % (table_size - 1))
            return 1 + (key % (self.table_size - 1))

    def _insert_linear(self, key, start_idx):
        """Insert using linear probing"""
        idx = start_idx
        while self.table[idx] != '#' and self.table[idx] is not None:
            idx = (idx + 1) % self.table_size
        self.table[idx] = key
        return idx

    def _insert_quadratic(self, key, start_idx):
        """Insert using quadratic probing"""
        i = 0
        while i <= self.table_size:
            idx = (start_idx + i*i) % self.table_size
            if self.table[idx] == '#' or self.table[idx] is None:
                self.table[idx] = key
                return idx
            i += 1
        return -1  # Table full

    def _insert_double_hashing(self, key, start_idx):
        """Insert using double hashing"""
        h2 = self._get_secondary_hash(key)
        i = 0
        while i <= self.table_size:
            idx = (start_idx + i * h2) % self.table_size
            if self.table[idx] == '#' or self.table[idx] is None:
                self.table[idx] = key
                return idx
            i += 1
        return -1  # Table full

    def _insert_chaining(self, key, start_idx):
        """Insert using chaining"""
        if self.table[start_idx] == '#' or self.table[start_idx] is None:
            self.table[start_idx] = []
        if not isinstance(self.table[start_idx], list):
            # Convert single element to list
            old_val = self.table[start_idx]
            self.table[start_idx] = [old_val]
        self.table[start_idx].append(key)
        return start_idx

    def get_table(self):
        return self.table

    def get_insertion_order(self):
        return self.insertion_order

    def display(self):
        print_table_with_index("Current Hash Table", self.table)

    def visualize(self):
        fig, ax = plt.subplots(1, 1, figsize=(15, 2))
        visualize_table(self.table, ax, "Hash Table")
        plt.tight_layout()
        plt.show()

def find_error_quadratic(hash_table, hash_index):
    """Find the ONE incorrectly placed element using quadratic probing"""
    table = hash_table.get_table()
    insertion_order = hash_table.get_insertion_order()
    table_size = hash_table.table_size

    print(f"\n{'='*70}")
    print(f"ANALYZING TABLE - All elements hash to index {hash_index}")
    print(f"Method: QUADRATIC PROBING")
    print(f"{'='*70}")

    # Get current positions
    current_positions = {}
    for idx, val in enumerate(table):
        if val != '#' and val is not None and not isinstance(val, list):
            current_positions[key_to_char(val)] = idx

    print(f"\nCurrent table state:")
    for char in insertion_order:
        if char in current_positions:
            print(f"  '{char}' is at index {current_positions[char]}")

    print(f"\n{'='*70}")
    print("STRATEGY: Find which ONE element, if removed, allows all others to fit correctly")
    print(f"{'='*70}")

    for suspect_char in insertion_order:
        print(f"\nTrying: What if '{suspect_char}' is the wrong one?")

        test_order = [c for c in insertion_order if c != suspect_char]
        occupied = set()
        all_correct = True

        for char in test_order:
            current_idx = current_positions[char]

            i = 0
            expected_idx = None
            while i <= table_size:
                probe_idx = (hash_index + i*i) % table_size
                if probe_idx not in occupied:
                    expected_idx = probe_idx
                    occupied.add(probe_idx)
                    break
                i += 1

            if expected_idx != current_idx:
                all_correct = False
                print(f"  '{char}' at {current_idx}, expected {expected_idx} - doesn't match")
                break

        if all_correct:
            print(f"  ✓ All other elements fit correctly!")

            suspect_current = current_positions[suspect_char]

            i = 0
            suspect_correct = None
            while i <= table_size:
                probe_idx = (hash_index + i*i) % table_size
                if probe_idx not in occupied:
                    suspect_correct = probe_idx
                    break
                i += 1

            print(f"\n{'='*70}")
            print(f"ERROR FOUND:")
            print(f"  Element: '{suspect_char}'")
            print(f"  Currently at index: {suspect_current}")
            print(f"  Should be at index: {suspect_correct}")
            print(f"  Calculation: ({hash_index} + {i}²) % {table_size} = ({hash_index} + {i*i}) % {table_size} = {suspect_correct}")
            print(f"{'='*70}")

            return suspect_char, suspect_current, suspect_correct

    print(f"\nNo single error found")
    return None, None, None

def find_error_linear(hash_table, hash_index):
    """Find the ONE incorrectly placed element using linear probing"""
    table = hash_table.get_table()
    insertion_order = hash_table.get_insertion_order()
    table_size = hash_table.table_size

    print(f"\n{'='*70}")
    print(f"ANALYZING TABLE - All elements hash to index {hash_index}")
    print(f"Method: LINEAR PROBING")
    print(f"{'='*70}")

    current_positions = {}
    for idx, val in enumerate(table):
        if val != '#' and val is not None and not isinstance(val, list):
            current_positions[key_to_char(val)] = idx

    print(f"\nCurrent table state:")
    for char in insertion_order:
        if char in current_positions:
            print(f"  '{char}' is at index {current_positions[char]}")

    print(f"\n{'='*70}")
    print("STRATEGY: Find which ONE element, if removed, allows all others to fit correctly")
    print(f"{'='*70}")

    for suspect_char in insertion_order:
        print(f"\nTrying: What if '{suspect_char}' is the wrong one?")

        test_order = [c for c in insertion_order if c != suspect_char]
        occupied = set()
        all_correct = True

        next_idx = hash_index
        for char in test_order:
            current_idx = current_positions[char]

            while next_idx in occupied:
                next_idx = (next_idx + 1) % table_size

            if next_idx != current_idx:
                all_correct = False
                print(f"  '{char}' at {current_idx}, expected {next_idx} - doesn't match")
                break

            occupied.add(next_idx)
            next_idx = (next_idx + 1) % table_size

        if all_correct:
            print(f"  ✓ All other elements fit correctly!")

            suspect_current = current_positions[suspect_char]

            suspect_correct = hash_index
            while suspect_correct in occupied:
                suspect_correct = (suspect_correct + 1) % table_size

            print(f"\n{'='*70}")
            print(f"ERROR FOUND:")
            print(f"  Element: '{suspect_char}'")
            print(f"  Currently at index: {suspect_current}")
            print(f"  Should be at index: {suspect_correct}")
            print(f"{'='*70}")

            return suspect_char, suspect_current, suspect_correct

    print(f"\nNo single error found")
    return None, None, None

def find_error_double_hashing(hash_table, hash_index, secondary_hash_func=None):
    """Find the ONE incorrectly placed element using double hashing"""
    table = hash_table.get_table()
    insertion_order = hash_table.get_insertion_order()
    table_size = hash_table.table_size

    print(f"\n{'='*70}")
    print(f"ANALYZING TABLE - All elements hash to index {hash_index}")
    print(f"Method: DOUBLE HASHING")
    print(f"{'='*70}")

    current_positions = {}
    for idx, val in enumerate(table):
        if val != '#' and val is not None and not isinstance(val, list):
            current_positions[key_to_char(val)] = idx

    print(f"\nCurrent table state:")
    for char in insertion_order:
        if char in current_positions:
            print(f"  '{char}' is at index {current_positions[char]}")

    print(f"\n{'='*70}")
    print("STRATEGY: Find which ONE element, if removed, allows all others to fit correctly")
    print(f"{'='*70}")

    for suspect_char in insertion_order:
        print(f"\nTrying: What if '{suspect_char}' is the wrong one?")

        test_order = [c for c in insertion_order if c != suspect_char]
        occupied = set()
        all_correct = True

        for char in test_order:
            current_idx = current_positions[char]
            key = char_to_key(char)

            # Calculate h2
            if secondary_hash_func is not None:
                h2 = secondary_hash_func
            else:
                h2 = hash_table._get_secondary_hash(key)

            i = 0
            expected_idx = None
            while i <= table_size:
                probe_idx = (hash_index + i * h2) % table_size
                if probe_idx not in occupied:
                    expected_idx = probe_idx
                    occupied.add(probe_idx)
                    break
                i += 1

            if expected_idx != current_idx:
                all_correct = False
                print(f"  '{char}' at {current_idx}, expected {expected_idx} - doesn't match")
                break

        if all_correct:
            print(f"  ✓ All other elements fit correctly!")

            suspect_current = current_positions[suspect_char]
            suspect_key = char_to_key(suspect_char)

            # Calculate h2 for suspect
            if secondary_hash_func is not None:
                h2 = secondary_hash_func
            else:
                h2 = hash_table._get_secondary_hash(suspect_key)

            i = 0
            suspect_correct = None
            while i <= table_size:
                probe_idx = (hash_index + i * h2) % table_size
                if probe_idx not in occupied:
                    suspect_correct = probe_idx
                    break
                i += 1

            print(f"\n{'='*70}")
            print(f"ERROR FOUND:")
            print(f"  Element: '{suspect_char}'")
            print(f"  Currently at index: {suspect_current}")
            print(f"  Should be at index: {suspect_correct}")
            print(f"  h2({suspect_key}) = {h2}")
            print(f"  Calculation: ({hash_index} + {i} × {h2}) % {table_size} = ({hash_index + i * h2}) % {table_size} = {suspect_correct}")
            print(f"{'='*70}")

            return suspect_char, suspect_current, suspect_correct

    print(f"\nNo single error found")
    return None, None, None

# ============================================================================
# MAIN PROGRAM - CONFIGURE YOUR PROBLEM HERE
# ============================================================================

# Configuration
TABLE_SIZE = 17
METHOD = 'quadratic'  # Options: 'quadratic', 'linear', 'chaining', 'double'
HASH_INDEX = 5        # Set to None for normal hashing, or a number if all elements hash to same index
SECONDARY_HASH = None # For double hashing: set to None for default h2(k) = 1 + (k % (size-1)), or specify a constant

# ============================================================================
# EXAMPLE 1: Given table (manual insertion) - for finding errors in existing tables
# ============================================================================
print("="*70)
print("EXAMPLE 1: Analyzing a given table with known positions")
print("="*70)

ht1 = HashTable(TABLE_SIZE, METHOD, HASH_INDEX, SECONDARY_HASH)

# Manual insertion at specific indices (use when you have a table from a problem)
ht1.insert(3, 'G')
ht1.insert(4, 'F')
ht1.insert(5, 'C')
ht1.insert(6, 'L')
ht1.insert(8, 'P')
ht1.insert(9, 'U')
ht1.insert(13, 'Y')
ht1.insert(14, 'M')

ht1.display()

# Find the error
if METHOD == 'quadratic':
    char, wrong_idx, correct_idx = find_error_quadratic(ht1, HASH_INDEX)
elif METHOD == 'linear':
    char, wrong_idx, correct_idx = find_error_linear(ht1, HASH_INDEX)
elif METHOD == 'chaining':
    char, wrong_idx, correct_idx = find_error_chaining(ht1, HASH_INDEX)
elif METHOD == 'double':
    char, wrong_idx, correct_idx = find_error_double_hashing(ht1, HASH_INDEX, SECONDARY_HASH)

if char:
    print(f"\n{'='*70}")
    print(f"FINAL ANSWER:")
    print(f"  Element: '{char}'")
    print(f"  Currently at index: {wrong_idx}")
    print(f"  Should be at index: {correct_idx}")
    print(f"{'='*70}")

ht1.visualize()

# ============================================================================
# EXAMPLE 2: Auto insertion - let the algorithm place elements
# ============================================================================
print("\n" + "="*70)
print("EXAMPLE 2: Building a table from scratch with auto-insertion")
print("="*70)

ht2 = HashTable(TABLE_SIZE, METHOD, HASH_INDEX, SECONDARY_HASH)

# Auto insertion - just provide letters, algorithm places them
print("\nInserting elements automatically:")
ht2.insert('G')
ht2.insert('F')
ht2.insert('C')
ht2.insert('L')
ht2.insert('P')
ht2.insert('U')
ht2.insert('Y')
ht2.insert('M')

ht2.display()
ht2.visualize()

# ============================================================================
# Choose which example to use:
# - Comment out Example 1 if you want to build from scratch
# - Comment out Example 2 if you have a given table to analyze
# ============================================================================