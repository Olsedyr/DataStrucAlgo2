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
    """Beregner load factor - tæller kun optagede slots"""
    filled = 0
    for val in table:
        if val != '#' and val is not None and not isinstance(val, list):
            filled += 1
        elif isinstance(val, list) and len(val) > 0:
            filled += 1  # En kæde tæller som én fyldt plads
    return filled / len(table)

def calculate_load_factor_elements(table):
    """Beregner load factor baseret på antal elementer (kun for chaining)"""
    total_elements = 0
    for val in table:
        if val is None or val == '#':
            continue
        elif isinstance(val, list):
            total_elements += len(val)
        else:
            total_elements += 1
    return total_elements / len(table)

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

def linear_probing_insert(table, key, table_size):
    idx = hash_function(key, table_size)
    start = idx
    steps = [idx]

    while table[idx] != '#' and table[idx] is not None:
        idx = (idx + 1) % table_size
        steps.append(idx)
        if idx == start:
            return -1, steps

    table[idx] = key
    return idx, steps

def quadratic_probing_insert(table, key, table_size):
    start_idx = hash_function(key, table_size)
    i = 0
    steps = []

    while i < table_size:
        idx = (start_idx + i*i) % table_size
        steps.append(idx)

        if table[idx] == '#' or table[idx] is None:
            table[idx] = key
            return idx, steps

        i += 1

    return -1, steps

def double_hashing_insert(table, key, table_size):
    h1 = hash_function(key, table_size)
    h2 = 1 + (key % (table_size - 1))
    i = 0
    steps = []

    while i < table_size:
        idx = (h1 + i * h2) % table_size
        steps.append(idx)

        if table[idx] == '#' or table[idx] is None:
            table[idx] = key
            return idx, steps

        i += 1

    return -1, steps

def chaining_insert(table, key, table_size):
    idx = hash_function(key, table_size)

    if table[idx] is None or table[idx] == '#':
        table[idx] = []
    elif not isinstance(table[idx], list):
        old_val = table[idx]
        table[idx] = [old_val]

    table[idx].append(key)
    return idx, None

def rehash_until_below_threshold(original_table, insert_func, method_name, max_load_factor):
    """
    Rehasher igen og igen indtil load factor er under max_load_factor
    Returnerer (endelig_tabel, antal_rehashes)
    """
    table = original_table
    rehash_count = 0

    while True:
        # Beregn nuværende load factor
        if method_name == "Chaining":
            current_load = calculate_load_factor_elements(table)
        else:
            current_load = calculate_load_factor(table)

        print(f"  Check load factor: {current_load:.2f}")

        # Hvis load factor er OK, stop
        if current_load <= max_load_factor:
            break

        print(f"  ⚠️  Load factor {current_load:.2f} > {max_load_factor} → REHASHING igen!")
        rehash_count += 1

        # Saml alle elementer
        all_elements = []
        for val in table:
            if val is None or val == '#':
                continue
            elif isinstance(val, list):
                all_elements.extend(val)
            else:
                all_elements.append(val)

        # Beregn ny størrelse
        old_size = len(table)
        new_size = next_prime(old_size * 2)

        # Opret ny tabel
        if method_name == "Chaining":
            new_table = [None] * new_size
        else:
            new_table = ['#'] * new_size

        # Indsæt alle elementer i ny tabel
        print(f"  Rehash #{rehash_count}: {old_size} → {new_size}, {len(all_elements)} elementer")
        for key in all_elements:
            insert_func(new_table, key, new_size)

        table = new_table

    return table, rehash_count

def visualize_rehashing_process(original_table, rehashed_table, method_name, max_load_factor, total_rehashes):
    """Visualiser processen med før/efter rehashing"""
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 5))

    # Original tabel
    if method_name == "Chaining":
        original_load = calculate_load_factor_elements(original_table)
    else:
        original_load = calculate_load_factor(original_table)

    ax1.set_title(f"{method_name} - Før Rehashing\nStørrelse: {len(original_table)}, Load: {original_load:.2f}",
                  fontsize=12, fontweight='bold')
    ax1.axis('off')

    for i in range(len(original_table)):
        x = i
        y = 0
        width = 1
        height = 1

        val = original_table[i]
        if val is None or val == '#':
            color = 'white'
        elif isinstance(val, list):
            if len(val) > 0:
                color = 'lightblue'
            else:
                color = 'white'
        else:
            color = 'lightgreen'

        rect = plt.Rectangle((x, y), width, height,
                             facecolor=color, edgecolor='black', linewidth=2)
        ax1.add_patch(rect)

        ax1.text(x + width/2, y - 0.15, str(i),
                 ha='center', va='center', fontsize=10, color='darkblue')

        if val is None or val == '#':
            display_text = ''
        elif isinstance(val, list):
            if len(val) == 0:
                display_text = ''
            else:
                display_text = ','.join(key_to_char(k) for k in val)
        else:
            display_text = key_to_char(val)

        ax1.text(x + width/2, y + height/2, display_text,
                 ha='center', va='center', fontsize=11, fontweight='bold')

    ax1.set_xlim(-0.5, len(original_table) - 0.5)
    ax1.set_ylim(-0.3, 1.1)

    # Rehashet tabel
    if method_name == "Chaining":
        rehashed_load = calculate_load_factor_elements(rehashed_table)
    else:
        rehashed_load = calculate_load_factor(rehashed_table)

    ax2.set_title(f"{method_name} - Efter {total_rehashes} Rehash(es)\nStørrelse: {len(rehashed_table)}, Load: {rehashed_load:.2f}",
                  fontsize=12, fontweight='bold')
    ax2.axis('off')

    for i in range(len(rehashed_table)):
        x = i
        y = 0
        width = 1
        height = 1

        val = rehashed_table[i]
        if val is None or val == '#':
            color = 'white'
        elif isinstance(val, list):
            if len(val) > 0:
                color = 'lightblue'
            else:
                color = 'white'
        else:
            color = 'lightgreen'

        rect = plt.Rectangle((x, y), width, height,
                             facecolor=color, edgecolor='black', linewidth=2)
        ax2.add_patch(rect)

        ax2.text(x + width/2, y - 0.15, str(i),
                 ha='center', va='center', fontsize=10, color='darkblue')

        if val is None or val == '#':
            display_text = ''
        elif isinstance(val, list):
            if len(val) == 0:
                display_text = ''
            else:
                display_text = ','.join(key_to_char(k) for k in val)
        else:
            display_text = key_to_char(val)

        ax2.text(x + width/2, y + height/2, display_text,
                 ha='center', va='center', fontsize=11, fontweight='bold')

    ax2.set_xlim(-0.5, len(rehashed_table) - 0.5)
    ax2.set_ylim(-0.3, 1.1)

    plt.suptitle(f"Rehashing Process - Max Load Factor: {max_load_factor}",
                 fontsize=14, fontweight='bold')
    plt.tight_layout()
    plt.show()

def main():
    """Hovedfunktion - konfigurer og kør rehashing demonstration"""

    # ========================= KONFIGURATION =========================
    INITIAL_TABLE_SIZE = 5        # Startstørrelse af hashtable
    MAX_LOAD_FACTOR = 0.3         # Grænse for rehashing - PRØV 0.3!
    ELEMENTS_TO_INSERT = "AFKPBG" # Elementer der indsættes
    # ================================================================

    print("="*80)
    print("HASH TABLE REHASHING - GENTAGNE REHASHES")
    print("="*80)

    print(f"\nKONFIGURATION:")
    print(f"  Initial tabelstørrelse: {INITIAL_TABLE_SIZE}")
    print(f"  Maksimal load factor: {MAX_LOAD_FACTOR}")
    print(f"  Elementer at indsætte: {ELEMENTS_TO_INSERT}")
    print(f"  Keys: {[char_to_key(c) for c in ELEMENTS_TO_INSERT]}")

    keys = [char_to_key(c) for c in ELEMENTS_TO_INSERT]

    # Metoder vi vil teste
    methods = [
        ("Linear Probing", linear_probing_insert),
        ("Quadratic Probing", quadratic_probing_insert),
        ("Double Hashing", double_hashing_insert),
        ("Chaining", chaining_insert)
    ]

    for method_name, insert_func in methods:
        print(f"\n{'='*80}")
        print(f"METODE: {method_name}")
        print('='*80)

        # Opret original tabel
        if method_name == "Chaining":
            original_table = [None] * INITIAL_TABLE_SIZE
        else:
            original_table = ['#'] * INITIAL_TABLE_SIZE

        # Indsæt elementer i original tabel
        print(f"\nIndsætter {len(keys)} elementer i original tabel (størrelse {INITIAL_TABLE_SIZE}):")
        for i, (letter, key) in enumerate(zip(ELEMENTS_TO_INSERT, keys)):
            if method_name == "Chaining":
                idx, _ = insert_func(original_table, key, INITIAL_TABLE_SIZE)
                print(f"  {letter} (key={key}): hash = {key} % {INITIAL_TABLE_SIZE} = {key % INITIAL_TABLE_SIZE} → index {idx}")
            else:
                idx, steps = insert_func(original_table, key, INITIAL_TABLE_SIZE)
                if idx == -1:
                    print(f"  {letter} (key={key}): KUNNE IKKE INDSÆTTE!")
                else:
                    print(f"  {letter} (key={key}): hash = {key} % {INITIAL_TABLE_SIZE} = {key % INITIAL_TABLE_SIZE} → endelig position {idx}")

        # Beregn load factor for original tabel
        if method_name == "Chaining":
            original_load = calculate_load_factor_elements(original_table)
        else:
            original_load = calculate_load_factor(original_table)

        print(f"\nOriginal tabel (størrelse {INITIAL_TABLE_SIZE}):")
        print(f"  Load factor: {original_load:.2f}")

        # Tjek om rehashing er nødvendig
        if original_load > MAX_LOAD_FACTOR:
            print(f"  ⚠️  Load factor {original_load:.2f} > {MAX_LOAD_FACTOR} → REHASHING NØDVENDIG")
            print(f"  Starter rehashing process...")

            # Rehash indtil load factor er under grænsen
            rehashed_table, total_rehashes = rehash_until_below_threshold(
                original_table, insert_func, method_name, MAX_LOAD_FACTOR
            )

            # Beregn endelig load factor
            if method_name == "Chaining":
                final_load = calculate_load_factor_elements(rehashed_table)
            else:
                final_load = calculate_load_factor(rehashed_table)

            print(f"\nREHASHING FÆRDIG:")
            print(f"  Antal rehashes: {total_rehashes}")
            print(f"  Endelig tabelstørrelse: {len(rehashed_table)}")
            print(f"  Endelig load factor: {final_load:.2f}")
            print(f"  Forbedring: {original_load:.2f} → {final_load:.2f}")

            # Vis tabeller
            print_table_with_index(f"Original tabel ({method_name})", original_table)
            print_table_with_index(f"Endelig tabel efter {total_rehashes} rehash(es)", rehashed_table)

            # Visualiser
            visualize_rehashing_process(original_table, rehashed_table, method_name, MAX_LOAD_FACTOR, total_rehashes)
        else:
            print(f"  ✓ Load factor {original_load:.2f} ≤ {MAX_LOAD_FACTOR} → INGEN REHASHING NØDVENDIG")
            print_table_with_index(f"Original tabel ({method_name})", original_table)

    # Eksempel på gentagne rehashes med lav load factor
    print("\n" + "="*80)
    print("ANALYSE: GENTAGNE REHASHES MED LAV LOAD FACTOR")
    print("="*80)

    print(f"\nMed MAX_LOAD_FACTOR = {MAX_LOAD_FACTOR} og {len(keys)} elementer:")
    print(f"Vi starter med tabelstørrelse {INITIAL_TABLE_SIZE}")

    # Simulér hvor mange rehashes der skal til
    current_size = INITIAL_TABLE_SIZE
    rehash_sequence = []

    for rehash_num in range(1, 10):  # Maks 10 rehashes
        if method_name == "Chaining":
            current_load = len(keys) / current_size
        else:
            # For open addressing kan vi maks have 1 element per slot
            current_load = min(len(keys), current_size) / current_size

        rehash_sequence.append((current_size, current_load))

        if current_load <= MAX_LOAD_FACTOR:
            break

        current_size = next_prime(current_size * 2)

    print(f"\nRehash sekvens:")
    print(f"{'Rehash #':<10} {'Tabelstørrelse':<15} {'Load factor':<15} {'Over grænse?':<12}")
    print("-" * 52)

    for i, (size, load) in enumerate(rehash_sequence):
        over_limit = "JA" if load > MAX_LOAD_FACTOR else "NEJ"
        print(f"{i:<10} {size:<15} {load:.3f}<15 {over_limit:<12}")

    print("\n" + "="*80)
    print("KONKLUSION")
    print("="*80)

    print(f"""
Når MAX_LOAD_FACTOR er lav (f.eks. {MAX_LOAD_FACTOR}):

1. REHASHING UDLØSES TIDLIGERE:
   - Med {MAX_LOAD_FACTOR}=0.7: Rehashing ved ~70% fyldt
   - Med {MAX_LOAD_FACTOR}=0.3: Rehashing ved ~30% fyldt

2. GENTAGNE REHASHES KAN VÆRE NØDVENDIGE:
   - Efter 1. rehash: load factor = {len(keys)} / {next_prime(INITIAL_TABLE_SIZE*2)} = {len(keys)/next_prime(INITIAL_TABLE_SIZE*2):.2f}
   - Hvis dette stadig > {MAX_LOAD_FACTOR}: Rehash igen!
   
3. FORDEL/ULEMPER:
   + FORDELE: Bedre performance (færre kollisioner)
   - ULEMPER: Flere rehashes (dyrere), større tabeller (mere hukommelse)

4. REALISTISKE VÆRDIER:
   - 0.7-0.8: God balance (almindeligt)
   - 0.5-0.6: Konservativ (bedre performance)
   - 0.3-0.4: Meget konservativ (meget store tabeller, mange rehashes)
""")

# Kør demonstrationen
if __name__ == "__main__":
    main()

    print("\n" + "="*80)
    print("INSTRUKTIONER")
    print("="*80)
    print("""
Ændr denne linje i 'main()' for at teste forskellige scenarier:
    MAX_LOAD_FACTOR = 0.3  # Prøv 0.4, 0.5, 0.6, 0.7, 0.8

Eksempler:
1. MAX_LOAD_FACTOR = 0.3:
   - Rehashing udløses tidligt
   - MULIGE gentagne rehashes
   - Meget store endelige tabeller

2. MAX_LOAD_FACTOR = 0.7:
   - Rehashing udløses senere
   - Sjældent gentagne rehashes
   - Mindre endelige tabeller

3. MAX_LOAD_FACTOR = 0.8:
   - Rehashing udløses meget sent
   - Ingen gentagne rehashes (normalt)
   - Mindste endelige tabeller
""")