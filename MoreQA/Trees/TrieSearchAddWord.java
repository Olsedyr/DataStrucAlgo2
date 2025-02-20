package MoreQA.Trees;

public class TrieSearchAddWord {
    // Define the Trie Node
    static class TrieNode {
        TrieNode[] children;
        boolean isEndOfWord;

        // Constructor for TrieNode
        public TrieNode() {
            children = new TrieNode[26]; // Assuming only lowercase English letters
            isEndOfWord = false;
        }
    }

    private TrieNode root;

    // Constructor for Trie
    public TrieSearchAddWord() {
        root = new TrieNode();
    }

    // Insert a word into the Trie
    // Time Complexity: O(m), where m is the length of the word
    // Space Complexity: O(m), where m is the length of the word
    public void insert(String word) {
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a'; // Calculate index for the character

            // If the character is not present, create a new TrieNode
            if (current.children[index] == null) {
                current.children[index] = new TrieNode();
            }

            current = current.children[index]; // Move to the next node
        }

        current.isEndOfWord = true; // Mark the end of the word
    }

    // Search for a word in the Trie
    // Time Complexity: O(m), where m is the length of the word
    // Space Complexity: O(1), no extra space used other than recursion stack
    public boolean search(String word) {
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a'; // Calculate index for the character

            // If the character is not found, return false
            if (current.children[index] == null) {
                return false;
            }

            current = current.children[index]; // Move to the next node
        }

        return current.isEndOfWord; // Check if it's the end of the word
    }

    public static void main(String[] args) {
        TrieSearchAddWord trie = new TrieSearchAddWord();

        // Insert words into the Trie
        trie.insert("hello");
        trie.insert("world");

        // Test search functionality
        System.out.println("Search for 'hello': " + trie.search("hello")); // Expected Output: true
        System.out.println("Search for 'world': " + trie.search("world")); // Expected Output: true
        System.out.println("Search for 'hell': " + trie.search("hell"));   // Expected Output: false
        System.out.println("Search for 'worlds': " + trie.search("worlds")); // Expected Output: false
    }
}
