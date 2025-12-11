import networkx as nx
import matplotlib.pyplot as plt
import heapq

# ---------------------------------------------
# 1. Definer grafen
# ---------------------------------------------
G = nx.Graph()
edges = [
    ('0', '1', 1),
    ('0', '2', 6),
    ('0', '4', 1),
    ('1', '2', 8),
    ('0', '5', 3),
    ('2', '5', 3),
    ('2', '3', 14),
    ('3', '7', 1),
    ('3', '6', 1),
    ('4', '8', 7),
    ('5', '8', 3),
    ('5', '9', 5),
    ('5', '10', 8),
    ('5', '6', 20),
    ('6', '7', 4),
    ('6', '11', 8),
    ('7', '11', 3),
    ('8', '9', 4),
    ('9', '10', 2),
    ('10', '11', 5),

]
# Fjern dubletter (håndteres automatisk i nx.Graph, men vi gør det alligevel)
unique_edges = {}
for u, v, w in edges:
    edge_key = tuple(sorted([u, v]))
    if edge_key not in unique_edges:
        unique_edges[edge_key] = w
edges_no_duplicates = [(u, v, w) for (u, v), w in unique_edges.items()]
G.add_weighted_edges_from(edges_no_duplicates)

# ---------------------------------------------
# Manuel implementation af Prim, der husker rækkefølgen af tilføjede kanter OG noder
# ---------------------------------------------
def prim_manual_with_steps(graph, start):
    mst_edges = []
    visited = set([start])
    edges = []
    steps = []  # rækkefølge af kanter
    node_order = {start: 1}  # node -> step nummer
    current_step = 2

    for v, data in graph[start].items():
        heapq.heappush(edges, (data['weight'], start, v))

    while edges and len(visited) < graph.number_of_nodes():
        weight, u, v = heapq.heappop(edges)
        if v not in visited:
            visited.add(v)
            mst_edges.append((u, v, weight))
            steps.append((u, v, weight))
            node_order[v] = current_step
            current_step += 1
            for to_next, attr in graph[v].items():
                if to_next not in visited:
                    heapq.heappush(edges, (attr['weight'], v, to_next))

    mst = nx.Graph()
    mst.add_weighted_edges_from(mst_edges)
    return mst, steps, node_order

# ---------------------------------------------
# Beregn MST’er
# ---------------------------------------------
mst_prim_A, steps_A, node_order_A = prim_manual_with_steps(G, '0')
mst_prim_F, steps_F, node_order_F = prim_manual_with_steps(G, '1')
mst_kruskal = nx.minimum_spanning_tree(G, algorithm='kruskal')

# ---------------------------------------------
# Udskriv resultater (uændret)
# ---------------------------------------------
def print_mst_info(name, mst):
    print(f"\n{name}'s MST:")
    total_weight = 0
    for i, (u, v, data) in enumerate(mst.edges(data=True), start=1):
        print(f"  Step {i}: {u}-{v} (weight {data['weight']})")
        total_weight += data['weight']
    print(f"  => Total weight: {total_weight}")
    return total_weight

print("=== Minimum Spanning Tree Comparison ===")

wA = print_mst_info("Prim (startnode 'A')", mst_prim_A)
wF = print_mst_info("Prim (startnode 'F')", mst_prim_F)
wK = print_mst_info("Kruskal", mst_kruskal)

if wA == wF == wK:
    print("\n✅ Alle algoritmer giver samme totalvægt.")
else:
    print("\n⚠️ Forskelle i totalvægt - check grafer!")

print("\n=== Explanation ===")
print("Manuel Prim-implementering gør det muligt at vælge startnode.")
print("Prim's algoritme vokser MST fra startnoden ved altid at tilføje den billigste kant,")
print("der forbinder et nyt knude til træet.")
print("Kruskal sorterer alle kanter efter vægt og tilføjer dem uden at skabe cykler.")
print("Begge finder MST med samme totale vægt, men rækkefølge og valg kan variere.")

# ---------------------------------------------
# Visualisering med shell layout
# ---------------------------------------------
pos = nx.shell_layout(G)

def draw_mst_with_node_steps(mst, node_order, pos, node_color, title):
    plt.title(title)
    nx.draw_networkx_nodes(mst, pos, node_color=node_color, node_size=1200)
    nx.draw_networkx_labels(mst, pos, font_weight='bold')

    # Tegn vægte på kanter som før
    edge_labels = nx.get_edge_attributes(mst, 'weight')
    nx.draw_networkx_edge_labels(mst, pos, edge_labels=edge_labels)

    # Tegn kanter
    nx.draw_networkx_edges(mst, pos, edge_color='black', width=2)

    # Tegn step-nummer over hver node i firkantede parenteser
    for node, step_num in node_order.items():
        x, y = pos[node]
        plt.text(x, y + 0.07, f"[{step_num}]", fontsize=10, fontweight='bold', ha='center', color='red')

plt.figure(figsize=(18, 5))

plt.subplot(1, 3, 1)
draw_mst_with_node_steps(mst_prim_A, node_order_A, pos, node_color='lightgreen', title="Prim's MST (start='A')")

plt.subplot(1, 3, 2)
draw_mst_with_node_steps(mst_prim_F, node_order_F, pos, node_color='lightblue', title="Prim's MST (start='F')")

plt.subplot(1, 3, 3)
nx.draw_networkx(mst_kruskal, pos, with_labels=True, node_color='lightcoral', node_size=1200, font_weight='bold')
nx.draw_networkx_edge_labels(mst_kruskal, pos, edge_labels=nx.get_edge_attributes(mst_kruskal, 'weight'))
plt.title("Kruskal's MST (Ingen rækkefølge)")

plt.tight_layout()
plt.show()
