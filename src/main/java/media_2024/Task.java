package media_2024;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task implements Serializable {
    private int id;                             // Unique identifier
    private String title;                       // Title of the task
    private String description;                 // Description of the task
    private Category category;                  // Category of the task
    private Priority priority;                  // Priority level
    private List<Notification> notifications;   // Notifications
    private LocalDate dueDate;                  // Deadline (e.g., 2024-12-10)
    private String status;                      // Status (e.g., Open, In Progress, etc.)

    public static final String STATUS_OPEN = "Open";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_POSTPONED = "Postponed";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_DELAYED = "Delayed";

    // Constructor
    public Task(int id, String title, String description, Category category, Priority priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.notifications = new ArrayList<>();
        this.dueDate = dueDate;
        this.status = STATUS_OPEN;
    }

    // Default constructor required for Jackson
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

    // Set, Delete, Get for Notifications
    public void setNotification(Notification notification) {
        if (STATUS_COMPLETED.equals(this.status)) {
            throw new IllegalStateException("Cannot add a notification to a completed task.");
        }

        if (notification.getReminderTime().isAfter(this.dueDate.atStartOfDay())) {
            throw new IllegalArgumentException("Reminder time cannot be after the task's due date.");
        }
        this.notifications.add(notification);
    }
    public void deleteNotification(int id) {
        this.notifications.removeIf(notification -> notification.getId() == id);
    }
    public List<Notification> getNotifications() {
        return new ArrayList<>(this.notifications);
    }

    // Update Status on delayed Tasks
    public void checkAndUpdateStatus() {
        if (!STATUS_COMPLETED.equals(this.status) && this.dueDate.isBefore(LocalDate.now())) {
            this.status = STATUS_DELAYED;
        }
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", category='" + category.getName() + '\'' 
        + ", priority='" + priority.getName() + '\'' + ", notifications:" + notifications + ", dueDate=" + dueDate + ", status='" + status + '\'' + '}';
    }
}

