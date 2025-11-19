import networkx as nx
import matplotlib.pyplot as plt
import heapq

# ----------------------------------------------------
# SETTINGS — vælg startnode
# ----------------------------------------------------
START_NODE = 'A'


# ----------------------------------------------------
# 1. Graf-definition (må ikke have negative vægte)
# ----------------------------------------------------
G = nx.DiGraph()

edges = [
    ('A', 'B', 4),
    ('A', 'C', 2),
    ('B', 'C', 5),
    ('B', 'D', 10),
    ('C', 'E', 3),
    ('E', 'D', 4),
    ('D', 'F', 11)
]

G.add_weighted_edges_from(edges)


# ----------------------------------------------------
# 2. Dijkstra algoritme med trin og print
# ----------------------------------------------------
def dijkstra_with_steps(G, source):
    dist = {node: float('inf') for node in G.nodes()}
    dist[source] = 0
    prev = {node: None for node in G.nodes()}
    visited = set()
    heap = [(0, source)]
    steps = []

    print(f"\nStart Dijkstra fra node '{source}':\n")

    while heap:
        current_dist, u = heapq.heappop(heap)
        if u in visited:
            continue
        visited.add(u)

        changed = []

        for v in G.neighbors(u):
            weight = G[u][v]['weight']
            if dist[u] + weight < dist[v]:
                old_dist = dist[v]
                dist[v] = dist[u] + weight
                prev[v] = u
                heapq.heappush(heap, (dist[v], v))
                changed.append((u, v, weight, old_dist, dist[v]))

        # Gem trin: hvilke kanter fik opdateret dist
        steps.append((u, changed, dist.copy()))

        # Print trin detaljer
        print(f"Besøger node: {u} med afstand {current_dist}")
        if changed:
            for cu, cv, w, oldd, newd in changed:
                print(f"  Opdaterer afstand til {cv} via {cu}: {oldd} -> {newd} (kant vægt={w})")
        else:
            print("  Ingen afstandsopdateringer her.")

    return dist, prev, steps


# ----------------------------------------------------
# 3. Visualisering af trin
# ----------------------------------------------------
def visualize_steps(G, pos, steps):
    node_colors = ['lightgreen' for _ in G.nodes()]
    edge_labels = nx.get_edge_attributes(G, 'weight')

    for i, (current_node, changed_edges, dist_state) in enumerate(steps, start=1):
        plt.figure(figsize=(8, 6))
        plt.title(f"Dijkstra Step {i}: Besøger '{current_node}'")

        # Tegn alle noder
        nx.draw(G, pos, with_labels=True, node_color=node_colors, node_size=1200)

        # Tegn kanter med vægte
        nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels)

        # Tegn kanter der fik afstand opdateret
        if changed_edges:
            edges_to_highlight = [(u, v) for (u, v, _, _, _) in changed_edges]
            nx.draw_networkx_edges(G, pos, edgelist=edges_to_highlight, edge_color='blue', width=3)

        # Vis afstand ved hver node
        for node, (x, y) in pos.items():
            d = dist_state[node]
            txt = f"{d if d != float('inf') else '∞'}"
            plt.text(x, y + 0.1, txt, fontsize=12, ha='center', color='black')

        plt.show()


# ----------------------------------------------------
# 4. Udskriv resultater og forklaringer (eksamensstil)
# ----------------------------------------------------
def print_results(dist, prev):
    print("\n=== Dijkstra RESULTATER ===\n")
    for node in dist:
        path = []
        cur = node
        while cur is not None:
            path.append(cur)
            cur = prev[cur]
        path.reverse()
        path_str = " → ".join(path)
        dist_str = dist[node] if dist[node] != float('inf') else "∞"
        print(f"Node {node}: afstand = {dist_str}, sti = {path_str}")

    print("""
--- Forklaring på Dijkstra-algoritmen ---
🔸 Algoritmen finder korteste vej fra startnode til alle andre noder
🔸 Den bruger en prioriteret kø (heap) til altid at vælge den nærmeste node næste gang
🔸 Kanter må IKKE have negative vægte, da det kan give forkerte resultater
🔸 Algoritmen opdaterer afstande til nabonoder, hvis en kortere vej findes
🔸 Den stopper når alle noder er besøgt (eller uopnåelige)
""")


# ----------------------------------------------------
# Kør scriptet
# ----------------------------------------------------
distances, predecessors, steps = dijkstra_with_steps(G, START_NODE)

pos = nx.spring_layout(G, seed=42)
visualize_steps(G, pos, steps)
print_results(distances, predecessors)
