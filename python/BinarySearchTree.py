import matplotlib.pyplot as plt
import networkx as nx
from collections import deque

# BST Node
class BSTNode:
    def __init__(self, key):
        self.key = key
        self.left = None
        self.right = None

# BST med insert
class BST:
    def __init__(self):
        self.root = None

    def insert(self, key):
        if self.root is None:
            self.root = BSTNode(key)
        else:
            self._insert_recursive(self.root, key)

    def _insert_recursive(self, node, key):
        if key < node.key:
            if node.left is None:
                node.left = BSTNode(key)
            else:
                self._insert_recursive(node.left, key)
        else:
            if node.right is None:
                node.right = BSTNode(key)
            else:
                self._insert_recursive(node.right, key)

# Traversaler
def preorder(node):
    if node is None:
        return []
    return [node.key] + preorder(node.left) + preorder(node.right)

def inorder(node):
    if node is None:
        return []
    return inorder(node.left) + [node.key] + inorder(node.right)

def postorder(node):
    if node is None:
        return []
    return postorder(node.left) + postorder(node.right) + [node.key]

def levelorder(root):
    if root is None:
        return []
    result = []
    queue = deque([root])
    while queue:
        current = queue.popleft()
        result.append(current.key)
        if current.left:
            queue.append(current.left)
        if current.right:
            queue.append(current.right)
    return result

# Intern sti-længde (sum af afstande fra rod til alle noder)
def internal_path_length(node, depth=0):
    if node is None:
        return 0
    return depth + internal_path_length(node.left, depth+1) + internal_path_length(node.right, depth+1)

# Træets højde
def tree_height(node):
    if node is None:
        return -1  # højde defineret som antal kanter, så tomt træ = -1
    return 1 + max(tree_height(node.left), tree_height(node.right))

# Antal noder
def count_nodes(node):
    if node is None:
        return 0
    return 1 + count_nodes(node.left) + count_nodes(node.right)

# Antal blade (noder uden børn)
def count_leaves(node):
    if node is None:
        return 0
    if node.left is None and node.right is None:
        return 1
    return count_leaves(node.left) + count_leaves(node.right)

# Simple balance-check (om forskellen i højde mellem subtræer max 1)
def is_balanced(node):
    def check(node):
        if node is None:
            return -1, True
        lh, lb = check(node.left)
        rh, rb = check(node.right)
        balanced = lb and rb and abs(lh - rh) <= 1
        return 1 + max(lh, rh), balanced
    _, balanced = check(node)
    return balanced

# Forklaring på level order traversal (generisk)
def explain_level_order():
    explanation = """
Level order traversal besøger alle noder niveau for niveau, startende fra roden.
Den bruger en FIFO-kø (First In, First Out), fordi vi skal besøge noder i den rækkefølge,
vi møder dem, altså ét niveau ad gangen.
"""
    return explanation.strip()

# Forklaring på BST-karakteristika (dynamisk)
def explain_bst_characteristics(root):
    num_nodes = count_nodes(root)
    height = tree_height(root)
    ipl = internal_path_length(root)
    leaves = count_leaves(root)
    balanced = is_balanced(root)

    explanation = f"""
BST karakteristika:
- Antal noder: {num_nodes}
- Træets højde (antal kanter): {height}
- Intern sti-længde (sum af afstande fra rod til alle noder): {ipl}
- Antal blade (noder uden børn): {leaves}
- Er træet balanceret?: {"Ja" if balanced else "Nej"}

En balanceret BST har højde tæt på log₂(antal noder),
hvilket sikrer effektiv søgning og indsættelse.
Intern sti-længde fortæller, hvor "dybt" noderne samlet set er.
"""
    return explanation.strip()

# Konverter BST til networkx graf med venstre/højre info
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

# Hierarkisk layout til visualisering
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

# Visualisering med farver og labels på kanter
def draw_bst(G, edge_labels):
    pos = hierarchy_pos(G, list(G.nodes)[0])
    plt.figure(figsize=(12,8))

    # Tegn noder
    nx.draw_networkx_nodes(G, pos, node_size=1500, node_color='lightblue')
    nx.draw_networkx_labels(G, pos, font_weight='bold', font_size=12)

    # Farvekod kanter: venstre blå, højre rød
    edge_colors = []
    for edge in G.edges():
        if edge_labels.get(edge) == "L":
            edge_colors.append('blue')
        else:
            edge_colors.append('red')

    # Tegn kanter med pile, buede linjer for bedre synlighed
    nx.draw_networkx_edges(
        G, pos,
        edgelist=G.edges(),
        edge_color=edge_colors,
        arrows=True,
        arrowstyle='-|>',
        connectionstyle='arc3,rad=0.15',
        width=2
    )

    # Tegn labels på kanterne (L/R)
    nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels, font_color='black', font_size=10)

    plt.title("Binary Search Tree (blå = venstre barn, rød = højre barn)")
    plt.axis('off')
    plt.show()

# Kør med input
def run_bst_analysis(numbers):
    bst = BST()
    for number in numbers:
        bst.insert(number)

    print("Traversal outputs:")
    print(f"Preorder:  {preorder(bst.root)}")
    print(f"Inorder:   {inorder(bst.root)}")
    print(f"Postorder: {postorder(bst.root)}")
    print(f"Levelorder:{levelorder(bst.root)}\n")

    print("Forklaring af level order traversal:")
    print(explain_level_order(), "\n")

    print("BST karakteristika:")
    print(explain_bst_characteristics(bst.root))

    G, edge_labels = bst_to_graph(bst.root)
    draw_bst(G, edge_labels)

# Skift til dit input til eksamen
input_numbers = [25,20,36,10,22,30,40,5,12,28,38,48,1,8,15,45,50]

run_bst_analysis(input_numbers)
