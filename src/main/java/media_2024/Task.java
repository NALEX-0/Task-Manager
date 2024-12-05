package media_2024;

import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private int id;                   // Unique identifier
    private String title;             // Title of the task
    private String description;       // Description of the task
    private Category category;          // Category of the task
    private Priority priority;          // Priority level
    private LocalDate dueDate;        // Deadline (e.g., 2024-12-10)
    private String status;            // Status (e.g., Open, In Progress, etc.)

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
        this.dueDate = dueDate;
        this.status = STATUS_OPEN;
    }

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

    public void checkAndUpdateStatus() {
        if (!STATUS_COMPLETED.equals(this.status) && this.dueDate.isBefore(LocalDate.now())) {
            this.status = STATUS_DELAYED;
        }
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", category='" + category.getName() + '\'' 
        + ", priority='" + priority.getName() + '\'' + ", dueDate=" + dueDate + ", status='" + status + '\'' + '}';
    }
}

