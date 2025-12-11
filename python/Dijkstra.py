import networkx as nx
import heapq
from tabulate import tabulate
import matplotlib.pyplot as plt

# ----------------------------------------------------
# 1. Edge-list (fælles til begge grafer)
# ----------------------------------------------------
edges = [
    ('A', 'B', 5),
    ('A', 'C', 3),
    ('B', 'G', 1),
    ('B', 'C', 2),
    ('B', 'E', 3),
    ('C', 'E', 7),
    ('C', 'D', 7),
    ('D', 'A', 2),
    ('D', 'F', 6),
    ('E', 'F', 1),
    ('E', 'D', 2),
    ('G', 'E', 1),

]

# Lav både rettet og urettet graf
G_directed = nx.DiGraph()
G_undirected = nx.Graph()

G_directed.add_weighted_edges_from(edges)
G_undirected.add_weighted_edges_from(edges)

START_NODE = 'A'


# ----------------------------------------------------
# 2. Dijkstra algoritme (gælder begge grafer)
# ----------------------------------------------------
def dijkstra_with_table(G, source):
    dist = {node: float('inf') for node in G.nodes()}
    dist[source] = 0
    prev = {node: None for node in G.nodes()}
    visited = set()
    heap = [(0, source)]

    while heap:
        current_dist, u = heapq.heappop(heap)
        if u in visited:
            continue
        visited.add(u)

        for v in G.neighbors(u):
            w = G[u][v]['weight']
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                prev[v] = u
                heapq.heappush(heap, (dist[v], v))

    table = []
    for node in sorted(G.nodes()):
        table.append([
            node,
            'Ja' if node in visited else 'Nej',
            dist[node] if dist[node] != float('inf') else '∞',
            prev[node] if prev[node] is not None else '-'
        ])

    return dist, prev, table


# ----------------------------------------------------
# 3. MST for undirected graf (Kruskal)
# ----------------------------------------------------
def mst_kruskal(G):
    mst_edges = list(nx.minimum_spanning_edges(G, data=True, algorithm='kruskal'))
    mst_edges_sorted = sorted(mst_edges, key=lambda x: x[2]['weight'])

    total_weight = sum(data['weight'] for _, _, data in mst_edges_sorted)
    return mst_edges_sorted, total_weight


# ----------------------------------------------------
# 4. Visualisering
# ----------------------------------------------------
def visualize_graph(G, dist, title):
    pos = nx.spring_layout(G, seed=42)
    plt.figure(figsize=(8, 6))
    plt.title(title)

    node_colors = ["lightgreen" if dist[n] != float('inf') else "red" for n in G.nodes()]

    nx.draw(G, pos, node_color=node_colors, node_size=1200, arrows=True if G.is_directed() else False, with_labels=True)
    nx.draw_networkx_edge_labels(G, pos, edge_labels=nx.get_edge_attributes(G, "weight"))

    for node, (x, y) in pos.items():
        d = dist[node]
        txt = f"{d}" if d != float('inf') else "∞"
        plt.text(x, y + 0.10, txt, fontsize=12, ha='center')

    plt.show()


def visualize_mst(G, mst_edges):
    pos = nx.spring_layout(G, seed=42)
    plt.figure(figsize=(8, 6))
    plt.title("MST (Undirected Graph)")

    nx.draw(G, pos, node_color='lightblue', node_size=1200, with_labels=True)
    nx.draw_networkx_edges(G, pos, alpha=0.3)

    mst_list = [(u, v) for u, v, _ in mst_edges]
    nx.draw_networkx_edges(G, pos, edgelist=mst_list, edge_color='blue', width=3)

    mst_edge_labels = {(u, v): data['weight'] for u, v, data in mst_edges}
    nx.draw_networkx_edge_labels(G, pos, edge_labels=mst_edge_labels, font_color='blue')

    plt.show()


# ----------------------------------------------------
# 5. RUN EVERYTHING
# ----------------------------------------------------
if __name__ == "__main__":

    print("\n=== DIJKSTRA PÅ RETTET GRAF (Med pile)===")
    dist_dir, prev_dir, table_dir = dijkstra_with_table(G_directed, START_NODE)
    print(tabulate(table_dir, headers=["Node", "Known", "dv", "pv"], tablefmt="pretty"))
    visualize_graph(G_directed, dist_dir, "Directed Graph – Dijkstra")

    print("\n=== DIJKSTRA PÅ URETTET GRAF (Uden pile) ===")
    dist_undir, prev_undir, table_undir = dijkstra_with_table(G_undirected, START_NODE)
    print(tabulate(table_undir, headers=["Node", "Known", "dv", "pv"], tablefmt="pretty"))
    visualize_graph(G_undirected, dist_undir, "Undirected Graph – Dijkstra")

    print("\n=== MST (URETTET GRAF) ===")
    mst_edges, mst_weight = mst_kruskal(G_undirected)
    print("\nMST edges:")
    for u, v, data in mst_edges:
        print(f"{u} - {v}  (weight {data['weight']})")

    print(f"\nTotal MST weight: {mst_weight}")
    visualize_mst(G_undirected, mst_edges)

    print("""
------------------------------------------------------------
📌 FORKLARING
Directed graf:
   - Bruges til navigation, trafikrouting, algoritmer med retning.
   - Dijkstra tager højde for retning.

Undirected graf:
   - Bruges til netværk, kabling, forbindelser.
   - Dijkstra kan køre, men kanter virker begge veje.
   - MST giver kun mening på undirected graf.

Scriptet viser alle forskelle tydeligt både i tabel og grafik.
------------------------------------------------------------
    """)
