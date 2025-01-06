package com.example.taskmanager;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * Represents a notification.
 * 
 * <p>A notification includes an ID, a reminder time, and a message.
 * It can be associated with a task.</p>
 */
public class Notification implements Serializable {
    private int id;                     // Unique identifier
    private LocalDate reminderDate;     // Notification time
    private String message;             // Notification message

    /**
     * Constructs a new notification with the specified attributes.
     * 
     * @param reminderDate the time when the notification should trigger
     * @param message      the message content of the notification
     */
    public Notification(int id, LocalDate reminderDate, String message) {
        this.id = id;
        this.reminderDate = reminderDate;
        this.message = message;
    }
    /**
     * Constructs a new notification without message.
     * 
     * @param reminderDate the time when the notification should trigger
     */
    public Notification(int id, LocalDate reminderDate) {
        this.id = id;
        this.reminderDate = reminderDate;
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
    public LocalDate getReminderDate() {
        return reminderDate;
    }
    public void setReminderDate(LocalDate reminderDate) {
        this.reminderDate = reminderDate;
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
        return "Notification{" + "id=" + id + ", reminderDate=" + reminderDate + ", message='" + message + '\'' + '}';
    }
}

