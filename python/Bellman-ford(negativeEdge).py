import networkx as nx
import matplotlib.pyplot as plt

# ----------------------------------------------------
# SETTINGS — choose start node
# ----------------------------------------------------
START_NODE = 'A'


# ----------------------------------------------------
# 1. Graph definition (supports negative weights)
# ----------------------------------------------------
G = nx.DiGraph()

edges = [
    ('A', 'B', 4),
    ('B', 'C', 3),
    ('C', 'D', -10),
    ('D', 'B', 1),   # creates reachable negative cycle
]

G.add_weighted_edges_from(edges)


# ----------------------------------------------------
# 2. Bellman–Ford with steps + negative cycle detection
# ----------------------------------------------------
def bellman_ford_with_steps(G, source):
    dist = {node: float('inf') for node in G.nodes()}
    pred = {node: None for node in G.nodes()}
    dist[source] = 0

    steps = []

    # Relax edges N-1 times
    for i in range(len(G.nodes()) - 1):
        changed = []
        for u, v, data in G.edges(data=True):
            w = data['weight']
            if dist[u] + w < dist[v]:
                dist[v] = dist[u] + w
                pred[v] = u
                changed.append((u, v, w))

        steps.append((i + 1, changed.copy(), dist.copy()))

    # Negative cycle detection
    negative_cycle_edges = []
    for u, v, data in G.edges(data=True):
        if dist[u] + data['weight'] < dist[v]:
            negative_cycle_edges.append((u, v, data['weight']))

    return dist, pred, steps, negative_cycle_edges


# Helper: reconstruct negative cycle path from edges
def find_negative_cycle(G, neg_cycle_edges):
    # negative_cycle_edges contains edges part of at least one negative cycle
    # We try to reconstruct one cycle by following predecessors in the cycle

    # Build adjacency for negative cycle edges only
    cycle_graph = nx.DiGraph()
    cycle_graph.add_weighted_edges_from(neg_cycle_edges)

    # Pick a start node from negative edges
    start_node = neg_cycle_edges[0][0]

    # To find cycle path: do DFS limited to cycle edges
    stack = [(start_node, [start_node])]
    visited = set()

    while stack:
        current, path = stack.pop()
        if current in visited:
            continue
        visited.add(current)

        for succ in cycle_graph.successors(current):
            if succ == path[0] and len(path) > 1:
                # Cycle found
                return path + [succ]
            elif succ not in path:
                stack.append((succ, path + [succ]))

    return None


# Calculate total weight of given cycle path
def cycle_total_weight(G, cycle_path):
    total = 0
    for i in range(len(cycle_path) - 1):
        u = cycle_path[i]
        v = cycle_path[i + 1]
        total += G[u][v]['weight']
    return total


# Run Bellman–Ford
bf_dist, bf_pred, steps, neg_cycle = bellman_ford_with_steps(G, START_NODE)

# Determine unreachable nodes
reachable_nodes = {n for n, d in bf_dist.items() if d != float('inf')}
unreachable_nodes = {n for n, d in bf_dist.items() if d == float('inf')}


# ----------------------------------------------------
# 3. Print results (EXAM style output)
# ----------------------------------------------------
print("\n=== Bellman–Ford RESULT (start at", START_NODE, ") ===")

for node in bf_dist:
    print(f"Distance to {node}: {bf_dist[node]} (predecessor: {bf_pred[node]})")

if neg_cycle:
    print("\n⚠️ NEGATIVE CYCLE DETECTED:")
    for u, v, w in neg_cycle:
        print(f"  Edge {u}->{v} (weight {w})")

    # Reconstruct and print the cycle path
    cycle_path = find_negative_cycle(G, neg_cycle)
    if cycle_path:
        cycle_str = " → ".join(cycle_path)
        print(f"\n🔹 Negative cycle route: {cycle_str}")

        total_weight = cycle_total_weight(G, cycle_path)
        print(f"🔹 Total weight of the negative cycle: {total_weight}")

else:
    print("\n✔️ No negative cycles.")

if unreachable_nodes:
    print("\n⚠️ WARNING: Unreachable nodes from", START_NODE)
    print("Unreachable:", ", ".join(sorted(unreachable_nodes)))


# ----------------------------------------------------
# 4. Why Dijkstra/Prim not usable in this case?
# ----------------------------------------------------
print("\n--- Explanation why Dijkstra and Prim algorithms cannot be used ---")
print("""
🔸 Dijkstra's algorithm requires all edge weights to be non-negative.
   Negative edge weights can cause Dijkstra to produce incorrect shortest paths.

🔸 Prim's algorithm is for finding Minimum Spanning Trees in undirected graphs
   and also assumes non-negative weights to work correctly.

🔸 In this graph, negative edge weights and a negative cycle exist,
   so both algorithms are not applicable here.

🔸 Bellman–Ford algorithm handles negative weights and detects negative cycles,
   which is why it is used instead.
""")


# ----------------------------------------------------
# 5. Visualization: Mark unreachable nodes red
# ----------------------------------------------------
pos = nx.spring_layout(G, seed=42)

plt.figure(figsize=(7, 5))
title_text = f"Graph from start node {START_NODE}"

if unreachable_nodes:
    title_text += "   (Unreachable → RED)"

plt.title(title_text)

node_colors = [
    'red' if node in unreachable_nodes else 'lightgreen'
    for node in G.nodes()
]

nx.draw(G, pos, with_labels=True, node_color=node_colors, node_size=1300)
edge_labels = nx.get_edge_attributes(G, 'weight')
nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels)

plt.show()


# ----------------------------------------------------
# 6. Visualize ONLY steps where changes happened
# ----------------------------------------------------
for step_num, changed_edges, dist_state in steps:

    if len(changed_edges) == 0:
        continue  # skip no-change steps

    plt.figure(figsize=(7, 5))
    plt.title(f"Bellman–Ford Step {step_num} (only changed edges)")

    # Draw all nodes (same as before, unreachable = red)
    nx.draw(G, pos, with_labels=True, node_color=node_colors, node_size=1300)
    nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels)

    # Draw only edges that changed something
    nx.draw_networkx_edges(
        G, pos,
        edgelist=changed_edges,
        edge_color='blue', width=3
    )

    # Draw updated distances above nodes
    for node, (x, y) in pos.items():
        d = dist_state[node]
        plt.text(x, y + 0.1, f"d={d}", fontsize=10, color='black', ha='center')

    plt.show()


# ----------------------------------------------------
# 7. Visualize negative cycle if found
# ----------------------------------------------------
if neg_cycle:
    plt.figure(figsize=(7, 5))
    plt.title("Negative Cycle Detected")

    nx.draw(G, pos, with_labels=True, node_color='orange', node_size=1300)
    nx.draw_networkx_edge_labels(G, pos, edge_labels=edge_labels)

    nx.draw_networkx_edges(G, pos, edgelist=neg_cycle,
                           edge_color='red', width=4)

    plt.show()
