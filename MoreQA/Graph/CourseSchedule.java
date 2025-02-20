package MoreQA.Graph;

import java.util.*;

public class CourseSchedule {

    // Function to check if it's possible to finish all courses (cycle detection in a directed graph)
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        // Step 1: Build the graph and calculate the in-degree of each node
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] inDegree = new int[numCourses];

        // Build the graph and fill the in-degree array
        for (int[] prereq : prerequisites) {
            int course = prereq[0];
            int prerequisite = prereq[1];
            graph.putIfAbsent(prerequisite, new ArrayList<>());
            graph.get(prerequisite).add(course);
            inDegree[course]++;
        }

        // Step 2: Initialize a queue for BFS with courses that have no prerequisites (in-degree 0)
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        // Step 3: Process the queue and reduce the in-degree of neighboring nodes
        int completedCourses = 0;

        while (!queue.isEmpty()) {
            int currentCourse = queue.poll();
            completedCourses++;

            if (graph.containsKey(currentCourse)) {
                for (int neighbor : graph.get(currentCourse)) {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }
        }

        // Step 4: If we processed all courses, return true (no cycle)
        return completedCourses == numCourses;
    }

    public static void main(String[] args) {
        // Example 1: No cycle, possible to finish all courses
        int numCourses1 = 2;
        int[][] prerequisites1 = {{1, 0}};
        System.out.println("Can finish all courses (Example 1): " + canFinish(numCourses1, prerequisites1)); // true

        // Example 2: Cycle exists, cannot finish all courses
        int numCourses2 = 2;
        int[][] prerequisites2 = {{1, 0}, {0, 1}};
        System.out.println("Can finish all courses (Example 2): " + canFinish(numCourses2, prerequisites2)); // false
    }
}
