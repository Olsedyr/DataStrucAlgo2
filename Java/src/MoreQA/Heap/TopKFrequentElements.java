package MoreQA.Heap;

import java.util.*;

// Class to find the top K frequent elements
public class TopKFrequentElements {

    // Approach 1: Using HashMap and Sorting
    // Time Complexity: O(n log n), where n is the size of the array
    // Space Complexity: O(n), as we store the frequencies of all elements
    public static List<Integer> topKFrequentUsingSorting(int[] nums, int k) {
        // Frequency map to store the frequency of each element
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        // Build the frequency map
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        // Convert the frequency map to a list of entries
        List<Map.Entry<Integer, Integer>> frequencyList = new ArrayList<>(frequencyMap.entrySet());

        // Sort the list based on frequency
        frequencyList.sort((a, b) -> b.getValue() - a.getValue());

        // Extract the top K frequent elements
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            result.add(frequencyList.get(i).getKey());
        }
        return result;
    }

    // Approach 2: Using a Min-Heap (Priority Queue)
    // Time Complexity: O(n log k), where n is the size of the array and k is the number of top elements to return
    // Space Complexity: O(n), as we store the frequencies of all elements
    public static List<Integer> topKFrequentUsingHeap(int[] nums, int k) {
        // Frequency map to store the frequency of each element
        Map<Integer, Integer> frequencyMap = new HashMap<>();

        // Build the frequency map
        for (int num : nums) {
            frequencyMap.put(num, frequencyMap.getOrDefault(num, 0) + 1);
        }

        // Min-heap to store the top k frequent elements
        PriorityQueue<Map.Entry<Integer, Integer>> minHeap = new PriorityQueue<>(k, (a, b) -> a.getValue() - b.getValue());

        // Add elements to the heap
        for (Map.Entry<Integer, Integer> entry : frequencyMap.entrySet()) {
            minHeap.add(entry);
            // If the heap exceeds size k, remove the element with the least frequency
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }

        // Extract the top k frequent elements
        List<Integer> result = new ArrayList<>();
        while (!minHeap.isEmpty()) {
            result.add(minHeap.poll().getKey());
        }

        return result;
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 1, 2, 2, 3};
        int k = 2;

        // Testing Approach 1: Using Sorting
        List<Integer> result1 = topKFrequentUsingSorting(nums, k);
        System.out.println("Top " + k + " frequent elements using sorting: " + result1);

        // Testing Approach 2: Using Min-Heap
        List<Integer> result2 = topKFrequentUsingHeap(nums, k);
        System.out.println("Top " + k + " frequent elements using heap: " + result2);
    }
}
