package TaskManager;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notification implements Serializable {
    private int id;                     // Unique identifier
    private LocalDateTime reminderTime; // Notification time
    private String message;             // Notification message

    // Constructor
    public Notification(int id, LocalDateTime reminderTime, String message) {
        this.id = id;
        this.reminderTime = reminderTime;
        this.message = message;
    }

    // Default constructor required for Jackson
    public Notification() {}

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public LocalDateTime getReminderTime() {
        return reminderTime;
    }
    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Notification{" + "id=" + id + ", reminderTime=" + reminderTime + ", message='" + message + '\'' + '}';
    }
}

