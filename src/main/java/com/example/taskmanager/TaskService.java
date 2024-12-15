package com.example.taskmanager;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Provides services for managing Tasks.
 * 
 * <p>Allows adding, updating, deleting, and retrieving tasks.
 * It also supports operations for notifications, searching, and handling
 * category and priority updates for tasks.</p>
 */
public class TaskService implements Serializable {
    private List<Task> tasks = new ArrayList<>();
    private static final String TASKS_FILE = "Task.json";
    private int nextId = 1; // Keeps track of the next available ID
    
    /**
     * Constructs a new TaskService and loads tasks from storage.
     * Initializes the next task ID and updates the status of delayed tasks.
     */
    public TaskService() {
        tasks = JSONHandler.readData(TASKS_FILE, Task.class);
        // Initialize nextId based on existing ID
        tasks.forEach(task -> nextId = Math.max(nextId, task.getId() + 1));
        // Update Status for delayed tasks
        tasks.forEach(Task::checkAndUpdateStatus);
    }

    /**
     * Adds a new task with the specified attributes.
     * 
     * @param title       the title of the task
     * @param description the description of the task
     * @param category    the category of the task
     * @param priority    the priority of the task
     * @param dueDate     the due date of the task
     */
    public void addTask(String title, String description, Category category, Priority priority, LocalDate dueDate) {
        tasks.add(new Task(nextId++, title, description, category, priority, dueDate));
        saveTasks();
    }

    /**
     * Adds a new task with the specified attributes and status.
     * 
     * @param title       the title of the task
     * @param description the description of the task
     * @param category    the category of the task
     * @param priority    the priority of the task
     * @param dueDate     the due date of the task
     * @param status      the status of the task
     */
    public void addTask(String title, String description, Category category, Priority priority, LocalDate dueDate, String status) {
        try {
            tasks.add(new Task(nextId++, title, description, category, priority, dueDate, status));

        } catch (IllegalArgumentException  e) {
            System.err.println("Error creating task: " + e.getMessage());
        }
        saveTasks();
    }

    /**
     * Adds a notification to a task by its ID.
     * 
     * @param id                the ID of the task
     * @param reminderTime      the reminder time of the notification
     * @param message           the message of the notification
     * @throws IllegalArgumentException if the task is not found
     */
    public void addNotificationToTask(int taskId, LocalDateTime reminderTime, String message) {
        Task task = getTaskByid(taskId);
        int notId = task.getNotId() + 1; // ToDo: Problem if ids->1, 2 and delete 1
        Notification not = new Notification(notId, reminderTime, message);
        task.setNotId(notId);
        task.setNotification(not);
        saveTasks();
    }

    /**
     * Adds a notification to a task by its ID, without message.
     * 
     * @param id                the ID of the task
     * @param reminderTime      the reminder time of the notification
     * @throws IllegalArgumentException if the task is not found
     */
    public void addNotificationToTask(int taskId, LocalDateTime reminderTime) {
        Task task = getTaskByid(taskId);
        int notId = task.getNotId() + 1; // ToDo: Problem if ids->1, 2 and delete 1
        Notification not = new Notification(notId, reminderTime);
        task.setNotId(notId);
        task.setNotification(not);
        saveTasks();
    }

    /**
     * Updates the attributes of an existing task.
     * 
     * @param id          the ID of the task to update
     * @param title       the new title for the task
     * @param description the new description for the task
     * @param category    the new category for the task
     * @param priority    the new priority for the task
     * @param dueDate     the new due date for the task
     * @param status      the new status for the task
     * @throws IllegalArgumentException if the task is not found
     */
    public void updateTask(int id, String title, String description, Category category, Priority priority, LocalDate dueDate, String status) {
        Task task = getTaskByid(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setCategory(category);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setStatus(status);
        saveTasks();
    }

    /**
     * Updates tasks with a changed priority.
     * 
     * <p>It is a static method</p>
     * @param updatedPriority the updated priority
     */
    public static void updateTasksWithChangedPriority(Priority updatedpriority) {
        List<Task> tasks = JSONHandler.readData("Task.json", Task.class);
    
        tasks.forEach(task -> {
            if (task.getPriority().getId() == updatedpriority.getId()) {
                task.setPriority(updatedpriority);
            }
        });
    
        JSONHandler.writeData("Task.json", tasks);
    }
    
    /**
     * Updates tasks with a changed category.
     * 
     * <p>It is a static method</p>
     * @param updatedCategory the updated category
     */
    public static void updateTasksWithChangedCategory(Category updatedCategory) {
        List<Task> tasks = JSONHandler.readData("Task.json", Task.class);
    
        tasks.forEach(task -> {
            if (task.getCategory().getId() == updatedCategory.getId()) {
                task.setCategory(updatedCategory);
            }
        });
    
        JSONHandler.writeData("Task.json", tasks);
    }
    
    /**
     * Deletes a task by its ID.
     * 
     * @param id the ID of the task to delete
     */
    public void deleteTask(int id) {
        tasks.removeIf(t -> t.getId() == id);
        saveTasks();
    }

    /**
     * Deletes all tasks associated with a specified category.
     * 
     * <p>It is a static method</p>
     * @param category the category whose tasks to delete
     */
    public static void deleteTasksByCategory(Category category) {
        List<Task> tasks = JSONHandler.readData("Task.json", Task.class);
    
        tasks.removeIf(task -> task.getCategory().equals(category));
        JSONHandler.writeData("Task.json", tasks);
    }
    
    /**
     * Deletes a notification from a task by its ID.
     * 
     * @param taskId        the ID of the task
     * @param notificationId the ID of the notification to delete
     */
    public void deleteNotificationFromTask(int taskId, int notificationId) {
        Task task = getTaskByid(taskId); // ToDo: Problem if ids->1, 2 and delete 1
        task.deleteNotification(notificationId);
        // task.decNotId();
        task.setNotId(task.getNotId() - 1);
        saveTasks();
    }

    /**
     * Gets all tasks.
     * 
     * @return a list of all tasks
     */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Gets tasks with a specified status.
     * 
     * @param status the status to filter tasks by
     * @return a list of tasks with the specified status
     */
    public List<Task> getTasksByStatus(String status) {
        return tasks.stream()
            .filter(t -> t.getStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList());
    }

    /**
     * Gets a list of tasks that are due within the next 7 days.
     * 
     * @return a list of tasks due in the next 7 days
     */
    public List<Task> getTasksDueIn7Days() {
        return tasks.stream()
            .filter(t -> {
                LocalDate dueDate = t.getDueDate();
                LocalDate today = LocalDate.now();
                return (dueDate.isEqual(today) || (dueDate.isAfter(today) && dueDate.isBefore(today.plusDays(7))));
            })
            .collect(Collectors.toList());
    }

    /**
     * Gets a task by its title.
     * 
     * @param title the title of the task to retrieve
     * @return the task with the specified title
     * @throws IllegalArgumentException if the task is not found
     */
    public Task getTaskByTitle(String title) {
        return tasks.stream()
            .filter(t -> t.getTitle().equals(title))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Task not found."));
    }

    /**
     * Gets a task by its ID.
     * 
     * @param title the title of the task to retrieve
     * @return the task with the specified title
     * @throws IllegalArgumentException if the task is not found
     */
    public Task getTaskByid(int id) {
        return tasks.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Task not found."));
    }

    /**
     * Searches for tasks with titles containing the specified string.
     * 
     * @param title the string to search for in task titles
     * @return a list of tasks with titles containing the string
     * @throws IllegalArgumentException if the search string is null or empty
     */
    public List<Task> searchTasksByTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title for search cannot be null or empty.");
        }

        return tasks.stream()
            .filter(task -> task.getTitle().toLowerCase().contains(title.toLowerCase()))
            .collect(Collectors.toList());
    }

    /**
     * Saves the current list of Tasks to JSON storage.
     * <p>This method is private and intended for internal use only.</p>
     */
    private void saveTasks() {
        JSONHandler.writeData(TASKS_FILE, tasks);
    }
}

