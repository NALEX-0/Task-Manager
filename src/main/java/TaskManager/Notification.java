package TaskManager;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Represents a notification.
 * 
 * <p>A notification includes an ID, a reminder time, and a message.
 * It can be associated with a task.</p>
 */
public class Notification implements Serializable {
    private int id;                     // Unique identifier
    private LocalDateTime reminderTime; // Notification time
    private String message;             // Notification message

    /**
     * Constructs a new notification with the specified attributes.
     * 
     * @param reminderTime the time when the notification should trigger
     * @param message      the message content of the notification
     */
    public Notification(int id, LocalDateTime reminderTime, String message) {
        this.id = id;
        this.reminderTime = reminderTime;
        this.message = message;
    }
    /**
     * Constructs a new notification without message.
     * 
     * @param reminderTime the time when the notification should trigger
     */
    public Notification(int id, LocalDateTime reminderTime) {
        this.id = id;
        this.reminderTime = reminderTime;
    }

    /**
     * Default constructor required for Jackson.
     */
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

    /**
     * Returns a string representation of the notification.
     * 
     * @return a string representation of the notification
     */
    @Override
    public String toString() {
        return "Notification{" + "id=" + id + ", reminderTime=" + reminderTime + ", message='" + message + '\'' + '}';
    }
}

