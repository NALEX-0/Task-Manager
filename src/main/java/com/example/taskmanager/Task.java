package com.example.taskmanager;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * Represents a task.
 * 
 * <p>A task includes attributes such as title, description, category, priority, due date, 
 * status, and associated notifications.</p>
 */
public class Task implements Serializable {
    private int id;                             // Unique identifier
    private String title;                       // Title of the task
    private String description;                 // Description of the task
    private Category category;                  // Category of the task
    private Priority priority;                  // Priority level
    private List<Notification> notifications;   // Notifications
    private LocalDate dueDate;                  // Deadline (e.g., 2024-12-10)
    private String status;                      // Status (e.g., Open, In Progress, etc.)

    private int nextNotId;                          // Notification next identifier

    // Status constants
    public static final String STATUS_OPEN = "Open";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_POSTPONED = "Postponed";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_DELAYED = "Delayed";

    /**
     * Constructs a new task with the mandatory attributes.
     * 
     * @param id          the unique ID of the task
     * @param title       the title of the task
     * @param description the description of the task
     * @param category    the category of the task
     * @param priority    the priority of the task
     * @param dueDate     the due date of the task
     */
    public Task(int id, String title, String description, Category category, Priority priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.notifications = new ArrayList<>();
        this.dueDate = dueDate;
        this.status = STATUS_OPEN;
        this.nextNotId = 0;
    }

    /**
     * Constructs a new task with the mandatory attributes and a specific status.
     * 
     * @param id          the unique ID of the task
     * @param title       the title of the task
     * @param description the description of the task
     * @param category    the category of the task
     * @param priority    the priority of the task
     * @param dueDate     the due date of the task
     * @param status      the status of the task (must be a valid status constant)
     * @throws IllegalArgumentException if the status is not valid
     */
    public Task(int id, String title, String description, Category category, Priority priority, LocalDate dueDate, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.notifications = new ArrayList<>();
        this.dueDate = dueDate;
        this.nextNotId = 0;
        if (status.equals("Open") || status.equals("In Progress") || status.equals("Postponed") || status.equals("Completed") || status.equals("Delayed")) {
            this.status = status;
        }
        else {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    /**
     * Default constructor required for Jackson.
     */
    public Task() {}

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getNextNotId() {
        return this.nextNotId;
    }
    public void setNextNotId(int notId) {
        this.nextNotId = notId;
    }

    /**
     * Adds a notification to the task.
     * 
     * @param notification the notification to add
     * @throws IllegalStateException if the task is completed
     * @throws IllegalArgumentException if the notification reminder time is after the due date or is before today
     */
    public void setNotification(Notification notification) {
        if (STATUS_COMPLETED.equals(this.status)) {
            throw new IllegalStateException("Cannot add a notification to a completed task.");
        }

        if (notification.getReminderDate().isAfter(this.dueDate)) {
            throw new IllegalArgumentException("Reminder time cannot be after the task due date.");
        }

        if (notification.getReminderDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Reminder time cannot be before today.");
        }
        this.notifications.add(notification);
    }

    /**
     * Deletes a notification by its ID.
     * 
     * @param id the ID of the notification to delete
     */
    public void deleteNotification(int id) {
        this.notifications.removeIf(notification -> notification.getId() == id);
    }

    /**
     * Gets a list of notifications for the task.
     * 
     * @return a list of notifications
     */
    public List<Notification> getNotifications() {
        return new ArrayList<>(this.notifications);
    }

    /**
     * Gets the last added notification
     * 
     * @return the last notification
     */
    @JsonIgnore
    public Notification getLastNotification() {
        return notifications.get(notifications.size() - 1);
    }

    /**
     * Checks if the task is delayed and updates its status if necessary.
     */
    public void checkAndUpdateStatus() {
        if (!STATUS_COMPLETED.equals(this.status) && this.dueDate.isBefore(LocalDate.now())) {
            this.status = STATUS_DELAYED;
        }
    }

    /**
     * Returns a string representation of the task.
     * 
     * @return a string representation of the task
     */
    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", category='" + category.getName() + '\'' 
        + ", priority='" + priority.getName() + '\'' + ", notifications:" + notifications + ", dueDate=" + dueDate + ", status='" + status + '\'' + ", nextNotId=" + nextNotId + '}';
    }
}

