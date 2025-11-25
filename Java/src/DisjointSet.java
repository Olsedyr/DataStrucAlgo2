public class DisjointSet {

    private int[] parent;
    private int[] size;

    public DisjointSet(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if (rootA != rootB) {
            if (size[rootA] < size[rootB]) {
                parent[rootA] = rootB;
                size[rootB] += size[rootA];
            } else {
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
            }
        }
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    public int countSets() {
        int count = 0;
        for (int i = 0; i < parent.length; i++) {
            if (parent[i] == i) count++;
        }
        return count;
    }

    // Hjælpefunktion til at lave unioner i en gruppe
    public void makeGroup(int[] group) {
        for (int i = 1; i < group.length; i++) {
            union(group[i-1], group[i]);
        }
    }

    public static void main(String[] args) {
        int n = 10;  // Juster til det største element + 1 i dine grupper
        DisjointSet ds = new DisjointSet(n);

        // Her skriver du grupperne direkte som arrays:
        int[] group1 = {1, 2, 3, 4};
        int[] group2 = {7, 5, 6};

        // Lav unioner for grupperne automatisk
        ds.makeGroup(group1);
        ds.makeGroup(group2);

        // Test om to elementer er forbundet:
        System.out.println("Er 1 og 4 forbundet? " + ds.connected(1, 4));  // true
        System.out.println("Er 5 og 7 forbundet? " + ds.connected(5, 7));  // true
        System.out.println("Er 1 og 5 forbundet? " + ds.connected(1, 5));  // false

        System.out.println("Antal grupper: " + ds.countSets());  // f.eks. 3 (0 alene, group1, group2)
    }
}
