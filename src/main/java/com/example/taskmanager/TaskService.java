package com.example.taskmanager;

import java.io.Serializable;
import java.time.LocalDate;
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
     * Initializes the next task ID, because 'nextId' is 0 when Tasks are loaded from JSON,
     * and updates the status of delayed tasks.
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
    public void addNotificationToTask(int taskId, LocalDate reminderTime, String message) {
        Task task = getTaskByid(taskId);
        int notId = task.getNextNotId() + 1;
        Notification not = new Notification(notId, reminderTime, message);
        task.setNextNotId(notId);
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
    public void addNotificationToTask(int taskId, LocalDate reminderTime) {
        Task task = getTaskByid(taskId);
        int notId = task.getNextNotId() + 1; 
        Notification not = new Notification(notId, reminderTime);
        task.setNextNotId(notId);
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
     * Deletes a task by its ID.
     * 
     * @param id the ID of the task to delete
     */
    public void deleteTask(int id) {
        Task task = getTaskByid(id);
        tasks.remove(task);
        saveTasks();
    }
    
    /**
     * Deletes a notification from a task by its ID.
     * 
     * @param taskId        the ID of the task
     * @param notificationId the ID of the notification to delete
     */
    public void deleteNotificationFromTask(int taskId, int notificationId) {
        Task task = getTaskByid(taskId);
        task.deleteNotification(notificationId);
        saveTasks();
    }

    /**
     * Gets all tasks.
     * 
     * @return a list of all tasks
     */
    public List<Task> getTasks() {
        refreshTasks();
        return new ArrayList<>(tasks);
    }

    /**
     * Gets tasks with a specified status.
     * 
     * @param status the status to filter tasks by
     * @return a list of tasks with the specified status
     */
    public List<Task> getTasksByStatus(String status) {
        refreshTasks();
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
        refreshTasks();
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
        refreshTasks();
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
        refreshTasks();
        return tasks.stream()
            .filter(t -> t.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Task not found."));
    }

    /**
     * Gets the last added task
     * <p>Used in MainApp (GUI)</p>
     * 
     * @return the last task
     */
    public Task getLastTask() {
        refreshTasks();
        return getTaskByid(nextId - 1);
    }

    /**
     * Searches for tasks containing the specified string.
     * <p>Searches on Title, Description, Category, Priority and DueDate</p>
     * 
     * @param title the string to search for in task titles
     * @return a list of tasks with titles containing the string
     * @throws IllegalArgumentException if the search string is null or empty
     */
    public List<Task> searchTasks(String query) {
        refreshTasks();
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query cannot be null or empty.");
        }
    
        String lowerCaseQuery = query.toLowerCase();
    
        return tasks.stream()
            .filter(task -> 
                task.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                task.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                task.getCategory().getName().toLowerCase().contains(lowerCaseQuery) ||
                task.getPriority().getName().toLowerCase().contains(lowerCaseQuery) ||
                task.getDueDate().toString().contains(lowerCaseQuery))
            .collect(Collectors.toList());
    }

    public void refreshTasks() {
        tasks = JSONHandler.readData(TASKS_FILE, Task.class);
    }

    /**
     * Saves the current list of Tasks to JSON storage.
     * <p>This method is private and intended for internal use only.</p>
     */
    private void saveTasks() {
        JSONHandler.writeData(TASKS_FILE, tasks);
    }
}

