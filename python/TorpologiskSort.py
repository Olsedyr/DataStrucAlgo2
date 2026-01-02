import networkx as nx
from tabulate import tabulate
import matplotlib.pyplot as plt

# ----------------------------------------------------
# 1. Edge-list til en rettet graf (DAG)
# ----------------------------------------------------
edges = [
    ('A', 'C'),
    ('B', 'C'),
    ('B', 'D'),
    ('C', 'E'),
    ('D', 'F'),
    ('E', 'F'),
]

G = nx.DiGraph()
G.add_edges_from(edges)

# ----------------------------------------------------
# 2. Topologisk sortering
# ----------------------------------------------------
def topological_sort_with_table(G):
    try:
        topo_order = list(nx.topological_sort(G))
    except nx.NetworkXUnfeasible:
        # Grafen indeholder en cyklus => topologisk sortering ikke muligt
        return None

    table = []
    for i, node in enumerate(topo_order):
        table.append([i + 1, node])
    return topo_order, table

# ----------------------------------------------------
# 3. Visualisering af graf med topologisk orden
# ----------------------------------------------------
def visualize_graph_with_order(G, topo_order):
    pos = nx.spring_layout(G, seed=42)
    plt.figure(figsize=(8, 6))
    plt.title("Directed Acyclic Graph (DAG) med topologisk sortering")

    nx.draw(G, pos, node_color='lightgreen', node_size=1200, arrows=True, with_labels=True)
    nx.draw_networkx_edges(G, pos, arrows=True)

    # Skriv topologisk position over hver node
    if topo_order:
        for i, node in enumerate(topo_order):
            x, y = pos[node]
            plt.text(x, y + 0.1, str(i+1), fontsize=14, fontweight='bold', ha='center')

    plt.show()

# ----------------------------------------------------
# 4. RUN
# ----------------------------------------------------
if __name__ == "__main__":
    result = topological_sort_with_table(G)
    if result is None:
        print("Grafen indeholder en cyklus, topologisk sortering er ikke mulig.")
    else:
        topo_order, table = result
        print("Topologisk sortering (rækkefølge):")
        print(topo_order)
        print("\nTopologisk sortering tabel:")
        print(tabulate(table, headers=["Position", "Node"], tablefmt="pretty"))
        visualize_graph_with_order(G, topo_order)
