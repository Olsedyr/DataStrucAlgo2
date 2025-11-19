import networkx as nx
import heapq
from tabulate import tabulate
import matplotlib.pyplot as plt

# ----------------------------------------------------
# 1. Graf-definition (rettet graf)
# ----------------------------------------------------
G_directed = nx.DiGraph()

edges = [
    ('A', 'B', 10),
    ('A', 'F', 5),
    ('B', 'C', 3),
    ('B', 'E', 3),
    ('C', 'D', 4),
    ('C', 'H', 5),
    ('E', 'C', 4),
    ('E', 'G', 2),
    ('F', 'B', 3),
    ('F', 'J', 2),
    ('G', 'D', 7),
    ('H', 'D', 4),
    ('H', 'I', 3),
]

G_directed.add_weighted_edges_from(edges)

# ----------------------------------------------------
# 2. Dijkstra algoritme med tabel-output
# ----------------------------------------------------
def dijkstra_with_table(G, source):
    dist = {node: float('inf') for node in G.nodes()}
    dist[source] = 0
    prev = {node: None for node in G.nodes()}
    visited = set()
    heap = [(0, source)]

    known_order = []  # For at holde styr på hvilke noder der blev "known"

    while heap:
        current_dist, u = heapq.heappop(heap)
        if u in visited:
            continue
        visited.add(u)
        known_order.append(u)

        for v in G.neighbors(u):
            weight = G[u][v]['weight']
            if dist[u] + weight < dist[v]:
                dist[v] = dist[u] + weight
                prev[v] = u
                heapq.heappush(heap, (dist[v], v))

    # Lav tabeldata
    table = []
    for node in sorted(G.nodes()):
        table.append([
            node,
            'Ja' if node in visited else 'Nej',
            dist[node] if dist[node] != float('inf') else '∞',
            prev[node] if prev[node] is not None else '-'
        ])

    print("\nDijkstra Tabel (known, dv=distance, pv=predecessor):\n")
    print(tabulate(table, headers=["Node", "Known", "dv (distance)", "pv (predecessor)"], tablefmt="pretty"))
    return dist, prev

# ----------------------------------------------------
# 3. MST Kruskal på urettet version
# ----------------------------------------------------
def mst_kruskal_undirected(G):
    G_undirected = G.to_undirected()
    mst_edges = list(nx.minimum_spanning_edges(G_undirected, data=True, algorithm='kruskal'))
    mst_edges_sorted = sorted(mst_edges, key=lambda x: x[2]['weight'])

    print("\nMinimum Spanning Tree (MST) kanter i rækkefølge:")
    print("{:<5} {:<5} {:<5} {:<7}".format("Nr.", "Fra", "Til", "Vægt"))
    total_weight = 0
    for i, (u, v, data) in enumerate(mst_edges_sorted, start=1):
        print(f"{i:<5} {u:<5} {v:<5} {data['weight']:<7}")
        total_weight += data['weight']

    print(f"\nTotal vægt MST: {total_weight}")
    return mst_edges_sorted, total_weight

# ----------------------------------------------------
# 4. Visualisering af rettet graf med Dijkstra-resultater
# ----------------------------------------------------
def visualize_directed_graph(G, dist):
    pos = nx.spring_layout(G, seed=42)
    plt.figure(figsize=(10, 7))
    plt.title("Rettet Graf med Dijkstra afstande fra startnode")

    # Noder farves ift. afstand (kort afstand = lys grøn, uendelig = rød)
    node_colors = []
    for n in G.nodes():
        d = dist[n]
        if d == float('inf'):
            node_colors.append('red')
        else:
            # Gradient grøn baseret på afstand (lav afstand -> grøn)
            node_colors.append('lightgreen')

    nx.draw(G, pos, with_labels=True, node_color=node_colors, node_size=1200, arrowsize=20)
    edge_labels = nx.get_edge_attributes(G, 'weight')
    nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels)

    # Tegn afstand ved hver node
    for node, (x, y) in pos.items():
        d = dist[node]
        txt = f"{d if d != float('inf') else '∞'}"
        plt.text(x, y + 0.1, txt, fontsize=12, ha='center', color='black')

    plt.show()

# ----------------------------------------------------
# 5. Visualisering af MST (urettet)
# ----------------------------------------------------
def visualize_mst(G, mst_edges):
    G_undirected = G.to_undirected()
    pos = nx.spring_layout(G_undirected, seed=42)
    plt.figure(figsize=(10, 7))
    plt.title("Minimum Spanning Tree (MST) i urettet graf")

    # Tegn alle kanter lysegrå
    nx.draw_networkx_edges(G_undirected, pos, alpha=0.3)
    nx.draw_networkx_nodes(G_undirected, pos, node_color='lightblue', node_size=1200)
    nx.draw_networkx_labels(G_undirected, pos)

    # Highlight MST kanter med tykkere blå linjer
    mst_edge_list = [(u, v) for u, v, _ in mst_edges]
    nx.draw_networkx_edges(G_undirected, pos, edgelist=mst_edge_list, edge_color='blue', width=3)

    # Tegn vægte på MST kanter
    edge_labels = {(u, v): data['weight'] for u, v, data in mst_edges}
    nx.draw_networkx_edge_labels(G_undirected, pos, edge_labels=edge_labels, font_color='blue')

    plt.show()

# ----------------------------------------------------
# 6. Forklaringstekst
# ----------------------------------------------------
def print_explanation():
    print("""
--- Forklaring ---
🔸 Dijkstra kører på den rettede graf og finder korteste afstande fra startnode til alle andre noder.
   Den tager højde for retninger på kanterne (fx trafik hvor retning betyder noget).

🔸 MST (Minimum Spanning Tree) kører på den urettede version af grafen, hvor retning ignoreres.
   MST finder det mindste sammenhængende træ, der forbinder alle noder med minimal total kantvægt (fx elektrisk netværk).

🔸 Derfor kan MST være anderledes og give mening i helt andre kontekster end korteste vej-algoritmer.
""")


# ----------------------------------------------------
# 7. Kør hele flowet
# ----------------------------------------------------
if __name__ == "__main__":
    START_NODE = 'A'

    distances, predecessors = dijkstra_with_table(G_directed, START_NODE)
    visualize_directed_graph(G_directed, distances)

    mst_edges, mst_weight = mst_kruskal_undirected(G_directed)
    visualize_mst(G_directed, mst_edges)

    print_explanation()
