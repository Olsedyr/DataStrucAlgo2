import matplotlib.pyplot as plt
import networkx as nx
import math

def heap_to_tree_graph(arr):
    G = nx.DiGraph()
    n = len(arr)
    for i in range(n):
        G.add_node(i, value=arr[i])
        left = 2*i + 1
        right = 2*i + 2
        if left < n:
            G.add_edge(i, left)
        if right < n:
            G.add_edge(i, right)
    return G

def draw_heap(arr, title="Heap Visualization"):
    G = heap_to_tree_graph(arr)
    pos = hierarchy_pos(G, 0)

    labels = {i: arr[i] for i in range(len(arr))}

    plt.figure(figsize=(10,6))
    nx.draw(G, pos, with_labels=True, labels=labels, node_size=1500, node_color='skyblue', font_size=14, font_weight='bold', arrows=False)
    plt.title(title)
    plt.show()

# Hierarchical layout for binary tree
def hierarchy_pos(G, root, width=1., vert_gap=0.2, vert_loc=0, xcenter=0.5, pos=None, parent=None):
    if pos is None:
        pos = {root: (xcenter, vert_loc)}
    else:
        pos[root] = (xcenter, vert_loc)
    children = list(G.neighbors(root))
    if parent is not None and parent in children:
        children.remove(parent)
    if len(children) != 0:
        dx = width / len(children)
        nextx = xcenter - width/2 - dx/2
        for child in children:
            nextx += dx
            pos = hierarchy_pos(G, child, width=dx, vert_gap=vert_gap, vert_loc=vert_loc - vert_gap,
                                xcenter=nextx, pos=pos, parent=root)
    return pos

# Min-heapify for index i
def min_heapify(arr, n, i):
    smallest = i
    l = 2*i + 1
    r = 2*i + 2

    if l < n and arr[l] < arr[smallest]:
        smallest = l
    if r < n and arr[r] < arr[smallest]:
        smallest = r

    if smallest != i:
        arr[i], arr[smallest] = arr[smallest], arr[i]
        min_heapify(arr, n, smallest)

# Max-heapify for index i
def max_heapify(arr, n, i):
    largest = i
    l = 2*i + 1
    r = 2*i + 2

    if l < n and arr[l] > arr[largest]:
        largest = l
    if r < n and arr[r] > arr[largest]:
        largest = r

    if largest != i:
        arr[i], arr[largest] = arr[largest], arr[i]
        max_heapify(arr, n, largest)

# Build min heap
def build_min_heap(arr):
    n = len(arr)
    for i in range(n//2 -1, -1, -1):
        min_heapify(arr, n, i)

# Build max heap
def build_max_heap(arr):
    n = len(arr)
    for i in range(n//2 -1, -1, -1):
        max_heapify(arr, n, i)


def heapsort(arr):
    n = len(arr)

    # Step 1: Byg max heap
    for i in range(n // 2 - 1, -1, -1):
        max_heapify(arr, n, i)

    # Step 2-5: Ekstraher elementer én efter én
    for i in range(n - 1, 0, -1):
        # Byt øverste (største) med element i slutningen
        arr[0], arr[i] = arr[i], arr[0]

        # Heap-størrelse reduceres
        max_heapify(arr, i, 0)

    return arr


# Insert value in min heap
def min_heap_insert(arr, val):
    arr.append(val)
    i = len(arr) - 1
    # Bubble up
    while i > 0 and arr[(i-1)//2] > arr[i]:
        arr[i], arr[(i-1)//2] = arr[(i-1)//2], arr[i]
        i = (i-1)//2

# Insert value in max heap
def max_heap_insert(arr, val):
    arr.append(val)
    i = len(arr) - 1
    # Bubble up
    while i > 0 and arr[(i-1)//2] < arr[i]:
        arr[i], arr[(i-1)//2] = arr[(i-1)//2], arr[i]
        i = (i-1)//2

# Extract min from min heap
def min_heap_extract(arr):
    if len(arr) == 0:
        return None
    root = arr[0]
    arr[0] = arr[-1]
    arr.pop()
    min_heapify(arr, len(arr), 0)
    return root

# Extract max from max heap
def max_heap_extract(arr):
    if len(arr) == 0:
        return None
    root = arr[0]
    arr[0] = arr[-1]
    arr.pop()
    max_heapify(arr, len(arr), 0)
    return root

# Demo run
if __name__ == "__main__":
    arr = [14,17,16,28,22,65,29,31,30,26,23]
    print("Original array:", arr)

    # Min heap build and visualize
    min_heap = arr.copy()
    build_min_heap(min_heap)
    print("Min Heap:", min_heap)
    draw_heap(min_heap, "Min Heap")

    # Insert into min heap
    print("\nInserting 0 into Min Heap")
    min_heap_insert(min_heap, 0)
    print("Min Heap after insert:", min_heap)
    draw_heap(min_heap, "Min Heap after Insert")

    # Extract min
    min_val = min_heap_extract(min_heap)
    print(f"\nExtracted min value: {min_val}")
    print("Min Heap after extract:", min_heap)
    draw_heap(min_heap, "Min Heap after Extract")

    # Max heap build and visualize
    max_heap = arr.copy()
    build_max_heap(max_heap)
    print("\nMax Heap:", max_heap)
    draw_heap(max_heap, "Max Heap")

    # Insert into max heap
    print("\nInserting 10 into Max Heap")
    max_heap_insert(max_heap, 10)
    print("Max Heap after insert:", max_heap)
    draw_heap(max_heap, "Max Heap after Insert")

    # Extract max
    max_val = max_heap_extract(max_heap)
    print(f"\nExtracted max value: {max_val}")
    print("Max Heap after extract:", max_heap)
    draw_heap(max_heap, "Max Heap after Extract")


    sorted_arr = heapsort(arr.copy())
    print("Sorted array (heapsort):", sorted_arr)