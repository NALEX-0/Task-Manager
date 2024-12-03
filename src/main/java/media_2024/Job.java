package media_2024;
import java.time.LocalDate;

public class Job {
    private int id;
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private LocalDate dueDate;
    private JobStatus status;

    // Constructor
    public Job(int id, String title, String description, Category category, Priority priority, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = JobStatus.OPEN;  // Default status "Open"
    }

    // Test Constructor
    // public Job() {
    //     this.id = "42";
    //     this.title = "Test Title";
    //     this.description = "Test Description";
    //     // this.category = "Test Category";
    //     // this.priority = "Test Priority";
    //     // this.dueDate = "Test DueDate";
    //     this.status = JobStatus.OPEN;  // Default status "Open"
    // }

    // Check if the task is overdue
    public boolean isOverdue() {
        return dueDate.isBefore(LocalDate.now()) && status != JobStatus.COMPLETED;
    }

    // Getters and Setters
    public int getId() {
        return id;
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
    public JobStatus getStatus() { 
        return status; 
    }
    public void setStatus(JobStatus status) { 
        this.status = status; 
    }

    


    @Override
    public String toString() {
        return "Job{id=" + id + ", title='" + title + "', dueDate=" + dueDate + ", status=" + status + "}";
    }
}
