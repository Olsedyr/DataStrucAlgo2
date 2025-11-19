import matplotlib.pyplot as plt
import networkx as nx
from collections import deque

# BST Node
class BSTNode:
    def __init__(self, key):
        self.key = key
        self.left = None
        self.right = None

# BST with insert, delete, rotations
class BST:
    def __init__(self):
        self.root = None

    # Insert key into BST
    def insert(self, key):
        self.root = self._insert_recursive(self.root, key)

    def _insert_recursive(self, node, key):
        if node is None:
            return BSTNode(key)
        if key < node.key:
            node.left = self._insert_recursive(node.left, key)
        elif key > node.key:
            node.right = self._insert_recursive(node.right, key)
        # if key == node.key, do nothing (no duplicates)
        return node

    # Delete key from BST
    def delete(self, key):
        self.root = self._delete_recursive(self.root, key)

    def _delete_recursive(self, node, key):
        if node is None:
            return None

        if key < node.key:
            node.left = self._delete_recursive(node.left, key)
        elif key > node.key:
            node.right = self._delete_recursive(node.right, key)
        else:
            # Node to delete found
            if node.left is None:
                return node.right
            elif node.right is None:
                return node.left
            else:
                # Node with two children: Get inorder successor (smallest in right subtree)
                succ = self._min_value_node(node.right)
                node.key = succ.key
                node.right = self._delete_recursive(node.right, succ.key)
        return node

    def _min_value_node(self, node):
        current = node
        while current.left is not None:
            current = current.left
        return current

    # Rotate left around node with key
    def rotate_left(self, root, key):
        if root is None:
            return root

        if key < root.key:
            root.left = self.rotate_left(root.left, key)
        elif key > root.key:
            root.right = self.rotate_left(root.right, key)
        else:
            # Rotate at this node if possible
            if root.right is None:
                print(f"Cannot rotate left at node {key} because right child is None.")
                return root

            new_root = root.right
            root.right = new_root.left
            new_root.left = root
            return new_root

        return root

    # Rotate right around node with key
    def rotate_right(self, root, key):
        if root is None:
            return root

        if key < root.key:
            root.left = self.rotate_right(root.left, key)
        elif key > root.key:
            root.right = self.rotate_right(root.right, key)
        else:
            # Rotate at this node if possible
            if root.left is None:
                print(f"Cannot rotate right at node {key} because left child is None.")
                return root

            new_root = root.left
            root.left = new_root.right
            new_root.right = root
            return new_root

        return root

    # Public rotate functions that update root
    def rotate_left_at(self, key):
        self.root = self.rotate_left(self.root, key)

    def rotate_right_at(self, key):
        self.root = self.rotate_right(self.root, key)

# Convert BST to NetworkX graph for visualization
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

# Hierarchical layout for visualization
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

# Visualize BST
def draw_bst(bst):
    if bst.root is None:
        print("Tree is empty")
        return

    G, edge_labels = bst_to_graph(bst.root)
    pos = hierarchy_pos(G, bst.root.key)
    plt.figure(figsize=(12, 8))

    # Draw nodes and labels
    nx.draw_networkx_nodes(G, pos, node_size=1500, node_color='lightgreen')
    nx.draw_networkx_labels(G, pos, font_weight='bold', font_size=12)

    # Edge colors: Left child = blue, right child = red
    edge_colors = ['blue' if edge_labels.get(edge) == "L" else 'red' for edge in G.edges()]
    nx.draw_networkx_edges(G, pos, edge_color=edge_colors, arrows=True, arrowstyle='-|>', width=2,
                           connectionstyle='arc3,rad=0.15')

    # Edge labels
    nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels, font_color='black', font_size=10)

    plt.title("Binary Search Tree (blue=left child, red=right child)")
    plt.axis('off')
    plt.show()

# Example usage:
if __name__ == "__main__":
    # Build initial BST
    numbers = [50, 48, 70, 30, 65, 90, 20,
               32, 67, 98, 15, 25, 31, 35,
               66, 69, 94, 99]

    bst = BST()
    for num in numbers:
        bst.insert(num)

    print("Initial tree:")
    draw_bst(bst)

    # Insert a new node
    bst.insert(60)
    print("After inserting 60:")
    draw_bst(bst)

    # Delete a node
    bst.delete(30)
    print("After deleting 30:")
    draw_bst(bst)

    # Rotate left around node 65
    bst.rotate_left_at(65)
    print("After rotating left at node 65:")
    draw_bst(bst)

    # Rotate right around node 50
    bst.rotate_right_at(50)
    print("After rotating right at node 50:")
    draw_bst(bst)
