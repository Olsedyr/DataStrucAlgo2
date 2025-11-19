import matplotlib.pyplot as plt
import networkx as nx
from collections import deque


#Make in-order traversal of tree first and use for list!

# --- BST Node with height for AVL ---
class BSTNode:
    def __init__(self, key):
        self.key = key
        self.left = None
        self.right = None
        self.height = 1  # Needed for AVL balance

# --- BST class with AVL insert and rotations ---
class AVLTree:
    def __init__(self):
        self.root = None

    def insert(self, key):
        print(f"\nInserting {key}...")
        self.root = self._insert(self.root, key)
        print(f"Inserted {key}.\n")

    def _insert(self, node, key):
        # Normal BST insertion
        if node is None:
            print(f"  Creating new node {key}.")
            return BSTNode(key)

        if key < node.key:
            print(f"  Go left from {node.key}")
            node.left = self._insert(node.left, key)
        elif key > node.key:
            print(f"  Go right from {node.key}")
            node.right = self._insert(node.right, key)
        else:
            # Duplicate keys not allowed
            print(f"  Key {key} already in the tree, skipping.")
            return node

        # Update height
        node.height = 1 + max(self.get_height(node.left), self.get_height(node.right))

        # Check balance factor
        balance = self.get_balance(node)
        print(f"  Node {node.key} balance after insertion: {balance}")

        # If unbalanced, 4 cases:

        # Left Left Case
        if balance > 1 and key < node.left.key:
            print(f"  Left Left case at {node.key}, performing right rotation")
            return self.right_rotate(node)

        # Right Right Case
        if balance < -1 and key > node.right.key:
            print(f"  Right Right case at {node.key}, performing left rotation")
            return self.left_rotate(node)

        # Left Right Case
        if balance > 1 and key > node.left.key:
            print(f"  Left Right case at {node.key}, performing left rotation on {node.left.key} then right rotation on {node.key}")
            node.left = self.left_rotate(node.left)
            return self.right_rotate(node)

        # Right Left Case
        if balance < -1 and key < node.right.key:
            print(f"  Right Left case at {node.key}, performing right rotation on {node.right.key} then left rotation on {node.key}")
            node.right = self.right_rotate(node.right)
            return self.left_rotate(node)

        return node

    def left_rotate(self, z):
        print(f"    Left rotate on node {z.key}")
        y = z.right
        T2 = y.left

        # Perform rotation
        y.left = z
        z.right = T2

        # Update heights
        z.height = 1 + max(self.get_height(z.left), self.get_height(z.right))
        y.height = 1 + max(self.get_height(y.left), self.get_height(y.right))

        return y

    def right_rotate(self, z):
        print(f"    Right rotate on node {z.key}")
        y = z.left
        T3 = y.right

        # Perform rotation
        y.right = z
        z.left = T3

        # Update heights
        z.height = 1 + max(self.get_height(z.left), self.get_height(z.right))
        y.height = 1 + max(self.get_height(y.left), self.get_height(y.right))

        return y

    def get_height(self, node):
        if node is None:
            return 0
        return node.height

    def get_balance(self, node):
        if node is None:
            return 0
        return self.get_height(node.left) - self.get_height(node.right)

# --- Build a normal BST without balancing for comparison ---
class BST:
    def __init__(self):
        self.root = None

    def insert(self, key):
        if self.root is None:
            self.root = BSTNode(key)
        else:
            self._insert(self.root, key)

    def _insert(self, node, key):
        if key < node.key:
            if node.left is None:
                node.left = BSTNode(key)
            else:
                self._insert(node.left, key)
        elif key > node.key:
            if node.right is None:
                node.right = BSTNode(key)
            else:
                self._insert(node.right, key)
        # Ignore duplicates

# --- Visualize BST/AVL with networkx ---
def bst_to_graph(node, G=None, edge_labels=None):
    if G is None:
        G = nx.DiGraph()
    if edge_labels is None:
        edge_labels = {}

    if node is None:
        return G, edge_labels

    G.add_node(node.key)

    if node.left:
        G.add_edge(node.key, node.left.key)
        edge_labels[(node.key, node.left.key)] = "L"
        bst_to_graph(node.left, G, edge_labels)

    if node.right:
        G.add_edge(node.key, node.right.key)
        edge_labels[(node.key, node.right.key)] = "R"
        bst_to_graph(node.right, G, edge_labels)

    return G, edge_labels

def hierarchy_pos(G, root, width=1., vert_gap=0.2, vert_loc=0, xcenter=0.5, pos=None, parent=None):
    if pos is None:
        pos = {root: (xcenter, vert_loc)}
    else:
        pos[root] = (xcenter, vert_loc)
    neighbors = list(G.neighbors(root))
    if parent is not None and parent in neighbors:
        neighbors.remove(parent)
    if len(neighbors) != 0:
        dx = width / len(neighbors)
        nextx = xcenter - width/2 - dx/2
        for neighbor in neighbors:
            nextx += dx
            pos = hierarchy_pos(G, neighbor, width=dx, vert_gap=vert_gap,
                                vert_loc=vert_loc - vert_gap, xcenter=nextx, pos=pos, parent=root)
    return pos

def draw_bst(G, edge_labels, title, subplot):
    pos = hierarchy_pos(G, list(G.nodes)[0])
    nx.draw_networkx_nodes(G, pos, node_size=1500, node_color='lightblue', ax=subplot)
    nx.draw_networkx_labels(G, pos, font_weight='bold', font_size=12, ax=subplot)

    edge_colors = []
    for edge in G.edges():
        if edge_labels.get(edge) == "L":
            edge_colors.append('blue')
        else:
            edge_colors.append('red')

    nx.draw_networkx_edges(
        G, pos,
        edgelist=G.edges(),
        edge_color=edge_colors,
        arrows=True,
        arrowstyle='-|>',
        connectionstyle='arc3,rad=0.15',
        width=2,
        ax=subplot
    )

    nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels, font_color='black', font_size=10, ax=subplot)

    subplot.set_title(title)
    subplot.axis('off')

# --- Main program ---

input_numbers = [50, 48, 70, 30, 65, 90, 20, 32, 67, 98, 15, 25, 31, 35, 66, 69, 94, 99]

# Build original BST
print("Building original BST...")
bst = BST()
for n in input_numbers:
    bst.insert(n)

# Build AVL tree (balanced)
print("\nBuilding AVL Tree with balancing...")
avl = AVLTree()
for n in input_numbers:
    avl.insert(n)

# Visualize both trees side by side
G_bst, labels_bst = bst_to_graph(bst.root)
G_avl, labels_avl = bst_to_graph(avl.root)

fig, axs = plt.subplots(1, 2, figsize=(18, 8))
draw_bst(G_bst, labels_bst, "Original BST", axs[0])
draw_bst(G_avl, labels_avl, "Balanced AVL Tree", axs[1])
plt.show()
