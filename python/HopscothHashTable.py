import matplotlib.pyplot as plt

class HopscotchHashTable:
    def __init__(self, size=16, hop_range=4):
        self.size = size
        self.hop_range = hop_range
        self.table = [None] * size
        # hop_info[i] = bitmask, hvor bit j=1 betyder at position i har et element fra i+j
        self.hop_info = [0] * size

    def hash(self, key):
        # Simpel hash: key modulo size
        return key % self.size

    def _set_hop(self, home_idx, actual_idx):
        offset = actual_idx - home_idx
        self.hop_info[home_idx] |= (1 << offset)

    def _clear_hop(self, home_idx, actual_idx):
        offset = actual_idx - home_idx
        self.hop_info[home_idx] &= ~(1 << offset)

    def insert(self, key):
        home_idx = self.hash(key)

        if self.table[home_idx] is None:
            # Plads lige ved home_idx
            self.table[home_idx] = key
            self._set_hop(home_idx, home_idx)
            print(f"Indsat {key} direkte i bucket {home_idx}")
            return True

        # Find ledig plads indenfor hop_range fra home_idx
        free_idx = -1
        for i in range(home_idx, min(home_idx + self.hop_range, self.size)):
            if self.table[i] is None:
                free_idx = i
                break
        if free_idx == -1:
            # Prøv at finde ledig plads længere væk (op til table size)
            for i in range(min(home_idx + self.hop_range, self.size), self.size):
                if self.table[i] is None:
                    free_idx = i
                    break
            if free_idx == -1:
                print(f"FEJL: Ingen ledig plads til indsættelse af {key} (tabel fuld)")
                return False

        # Hvis free_idx ligger udenfor hop_range, skal vi hopscotch'ere elementer
        current_idx = free_idx
        while current_idx - home_idx >= self.hop_range:
            moved = False
            # Prøv at flytte et element indenfor hop_range til ledig plads
            for i in range(current_idx - (self.hop_range - 1), current_idx):
                if i < 0 or i >= self.size:
                    continue
                home_of_i = self.hash(self.table[i]) if self.table[i] is not None else None
                if home_of_i is not None and home_of_i <= i and current_idx - home_of_i < self.hop_range:
                    # Flyt element i til current_idx
                    print(f"Flytter nøgle {self.table[i]} fra {i} til {current_idx} for at gøre plads")
                    self.table[current_idx] = self.table[i]
                    self._set_hop(home_of_i, current_idx)
                    self._clear_hop(home_of_i, i)
                    self.table[i] = None
                    current_idx = i
                    moved = True
                    break
            if not moved:
                print(f"FEJL: Kan ikke hopscotch'ere plads til nøgle {key}")
                return False

        # Nu kan vi indsætte nøglen på current_idx (indenfor hop_range)
        self.table[current_idx] = key
        self._set_hop(home_idx, current_idx)
        print(f"Indsat nøgle {key} i bucket {current_idx} (home bucket: {home_idx})")
        return True

    def delete(self, key):
        # Find og slet nøglen
        for idx in range(self.size):
            if self.table[idx] == key:
                home_idx = self.hash(key)
                self.table[idx] = None
                self._clear_hop(home_idx, idx)
                print(f"Slettet nøgle {key} fra bucket {idx} (home bucket: {home_idx})")
                return True
        print(f"Kunne ikke finde nøgle {key} til sletning")
        return False

    def visualize(self):
        plt.figure(figsize=(12, 2))
        plt.title(f"Hopscotch Hash Table (size={self.size}, hop_range={self.hop_range})")
        ax = plt.gca()
        ax.set_xlim(0, self.size)
        ax.set_ylim(0, 1)
        ax.axis('off')

        for i, val in enumerate(self.table):
            rect = plt.Rectangle((i, 0), 1, 1, fill=True, edgecolor='black', linewidth=2, facecolor='lightblue' if val is not None else 'white')
            ax.add_patch(rect)
            ax.text(i + 0.5, 0.5, str(val) if val is not None else '', ha='center', va='center', fontsize=14)
            ax.text(i + 0.5, -0.2, str(i), ha='center', va='center', fontsize=10, color='gray')

            # Vis hop bits
            hop_mask = self.hop_info[i]
            bits = bin(hop_mask)[2:].zfill(self.hop_range)
            bits_display = bits[::-1]  # Omvendt, så bit 0 er tættest på
            ax.text(i + 0.5, 1.1, bits_display, ha='center', va='center', fontsize=10, color='darkgreen')

        plt.show()

    def explain_next_insertion_issue(self, next_hash):
        home_idx = next_hash % self.size
        print(f"\nForklaring for indsættelse med hash {next_hash} (bucket {home_idx}):")

        # Check om der er ledig plads indenfor hop range
        free_spot = None
        for i in range(home_idx, min(home_idx + self.hop_range, self.size)):
            if self.table[i] is None:
                free_spot = i
                break
        if free_spot is not None:
            print(f"Der er ledig plads i bucket {free_spot} indenfor hop range {self.hop_range}, så indsættelsen kan ske uden problemer.")
        else:
            print(f"Der ER IKKE ledig plads indenfor hop range {self.hop_range} (bucket {home_idx} til {home_idx + self.hop_range - 1}).")
            print("Dette kaldes et 'neighborhood overflow', hvilket betyder at hopscotch hashing ikke kan finde plads til nøglen")
            print("Selvom der kan være plads længere væk, kan den ikke hopscotch'eres ind i det tilladte hop-område.")
            print("Typisk kræver dette at tabellen resize's eller at en anden konfliktløsning anvendes.")

    def explain_deletion(self, key):
        # Tjek om nøglen findes, hvis ikke, forklar at sletning ikke kan ske
        found = False
        for idx in range(self.size):
            if self.table[idx] == key:
                found = True
                home_idx = self.hash(key)
                print(f"\nSletning af nøgle {key} fra bucket {idx} (home bucket: {home_idx}) sker.")
                print("Sletning fjerner nøglen og opdaterer hop-info for home bucket, så det afspejler at denne position nu er tom.")
                break
        if not found:
            print(f"\nNøglen {key} findes ikke i tabellen, så sletning kan ikke foretages.")

# --------------------------
# Hovedprogram til test
# --------------------------

def key_to_char(k):
    return chr(k)

def char_to_key(c):
    return ord(c)

def main():
    # Init hopscotch table med størrelse 16 og hop_range 4
    table = HopscotchHashTable(size=20, hop_range=4)

    # Elementer og deres hash værdier (typisk key % size, men her som opgaven)
    elements = {
        'A': 31,
        'B': 25,
        'C': 34,
        'D': 27,
        'E': 39,
        'F': 23,
        'G': 41,
        'H': 29,
        'I': 32,
        'J': 31,
        'K': 36,
        'L': 27,


    }

    print("Indsætter elementer:")
    for c, h in elements.items():
        print(f"Indsætter {c} med hash {h}")
        table.insert(h)

    table.visualize()

    # Forklaring for næste indsættelse med hash 50
    table.explain_next_insertion_issue(50)

    # Test sletning af et element
    print("\nTester sletning:")
    table.explain_deletion(51)  # Slet en 51 (B, D, F eller J)
    table.delete(51)
    table.visualize()

    # Forklaring for næste indsættelse efter sletning
    table.explain_next_insertion_issue(50)

if __name__ == "__main__":
    main()
