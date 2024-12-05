package media_2024;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService implements Serializable {
    private List<Task> tasks = new ArrayList<>();
    private static final String TASKS_FILE = "Task.json";
    private int nextId = 1; // Keeps track of the next available ID

    public TaskService() {
        tasks = JSONHandler.readData(TASKS_FILE, Task.class);
        // Initialize nextId based on existing ID
        tasks.forEach(task -> nextId = Math.max(nextId, task.getId() + 1));
        // Update Status for delayed tasks
        tasks.forEach(Task::checkAndUpdateStatus);
    }

    // Add new Task
    public void addTask(String title, String description, Category category, Priority priority, LocalDate dueDate) {
        tasks.add(new Task(nextId++, title, description, category, priority, dueDate));
        saveTasks();
    }

    // Add new Notification to Task
    public void addNotificationToTask(int id, Notification notifications) {
        Task task = tasks.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Task not found."));
        task.setNotification(notifications);
        saveTasks();
    }

    // Update Task with the specified id 
    public void updateTask(int id, String title, String description, Category category, Priority priority, LocalDate dueDate, String status) {
        Task task = tasks.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Task not found."));
        task.setTitle(title);
        task.setDescription(description);
        task.setCategory(category);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setStatus(status);
        saveTasks();
    }

    // Update Tasks with changed Priority
    public static void updateTasksWithChangedPriority(Priority updatedpriority) {
        List<Task> tasks = JSONHandler.readData("Task.json", Task.class);
    
        tasks.forEach(task -> {
            if (task.getPriority().getId() == updatedpriority.getId()) {
                task.setPriority(updatedpriority);
            }
        });
    
        JSONHandler.writeData("Task.json", tasks);
    }
    

    // Update Tasks with changed Category
    public static void updateTasksWithChangedCategory(Category updatedCategory) {
        List<Task> tasks = JSONHandler.readData("Task.json", Task.class);
    
        tasks.forEach(task -> {
            if (task.getCategory().getId() == updatedCategory.getId()) {
                task.setCategory(updatedCategory);
            }
        });
    
        JSONHandler.writeData("Task.json", tasks);
    }
    
    // Delete Task with the specified id
    public void deleteTask(int id) {
        tasks.removeIf(t -> t.getId() == id);
        saveTasks();
    }

    // Delete all Tasks with the specified Category
    public static void deleteTasksByCategory(Category category) {
        List<Task> tasks = JSONHandler.readData("Task.json", Task.class);
    
        tasks.removeIf(task -> task.getCategory().equals(category));
        JSONHandler.writeData("Task.json", tasks);
    }
    
    // Delete Notification from Task
    public void deleteNotificationFromTask(int taskId, int notificationId) {
        Task task = tasks.stream()
            .filter(t -> t.getId() == taskId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Task not found."));
        task.deleteNotification(notificationId);
    }

    // Return List of Tasks
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    // Return List of Tasks with a specified Status
    public List<Task> getTasksByStatus(String status) {
        return tasks.stream()
            .filter(t -> t.getStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }

    // Return the Task with this Title
    public Task getTaskByTitle(String title) {
        return tasks.stream()
            .filter(t -> t.getTitle().equals(title))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Task not found."));
    }

    // Search Tasks with title 
    public List<Task> searchTasksByTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title for search cannot be null or empty.");
        }

        return tasks.stream()
            .filter(task -> task.getTitle().toLowerCase().contains(title.toLowerCase()))
            .collect(Collectors.toList());
    }

    // Save Tasks
    private void saveTasks() {
        JSONHandler.writeData(TASKS_FILE, tasks);
    }
}

