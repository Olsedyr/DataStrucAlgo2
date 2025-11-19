import java.util.*;

class Task {
    String name; // Name of the task
    int duration; // Duration of the task
    List<Task> dependencies; // List of tasks that this task depends on

    // Constructor to create a new task
    Task(String name, int duration) {
        this.name = name;
        this.duration = duration;
        this.dependencies = new ArrayList<>(); // Initialize the dependencies list
    }

    // Method to add a dependency to this task
    void addDependency(Task task) {
        dependencies.add(task);
    }
}

public class CriticalPathMethod {
    public static void main(String[] args) {
        // Create tasks
        Task a = new Task("A", 3);
        Task b = new Task("B", 2);
        Task c = new Task("C", 1);
        Task d = new Task("D", 4);
        Task e = new Task("E", 2);

        // Define dependencies
        b.addDependency(a); // Task B depends on Task A
        c.addDependency(a); // Task C depends on Task A
        d.addDependency(b); // Task D depends on Task B
        d.addDependency(c); // Task D depends on Task C
        e.addDependency(d); // Task E depends on Task D

        // Calculate and print the critical path
        List<Task> tasks = Arrays.asList(a, b, c, d, e);
        List<Task> criticalPath = findCriticalPath(tasks);
        System.out.println("Critical Path:");
        for (Task task : criticalPath) {
            System.out.print(task.name + " ");
        }
    }

    // Method to find the critical path in the list of tasks
    public static List<Task> findCriticalPath(List<Task> tasks) {
        Map<Task, Integer> taskCompletionTime = new HashMap<>();
        for (Task task : tasks) {
            calculateCompletionTime(task, taskCompletionTime);
        }

        // Find the task with the maximum completion time
        Task endTask = Collections.max(taskCompletionTime.entrySet(), Map.Entry.comparingByValue()).getKey();

        // Trace back the critical path
        List<Task> criticalPath = new ArrayList<>();
        while (endTask != null) {
            criticalPath.add(0, endTask); // Add the task to the beginning of the critical path
            endTask = endTask.dependencies.stream()
                    .max(Comparator.comparingInt(taskCompletionTime::get))
                    .orElse(null); // Move to the dependency with the maximum completion time
        }

        return criticalPath;
    }

    // Recursive method to calculate the completion time of a task
    private static int calculateCompletionTime(Task task, Map<Task, Integer> taskCompletionTime) {
        if (taskCompletionTime.containsKey(task)) {
            return taskCompletionTime.get(task); // Return the cached completion time if already calculated
        }

        int maxDependencyTime = 0;
        for (Task dependency : task.dependencies) {
            maxDependencyTime = Math.max(maxDependencyTime, calculateCompletionTime(dependency, taskCompletionTime));
        }

        int completionTime = task.duration + maxDependencyTime; // Calculate the completion time
        taskCompletionTime.put(task, completionTime); // Cache the completion time
        return completionTime;
    }
}