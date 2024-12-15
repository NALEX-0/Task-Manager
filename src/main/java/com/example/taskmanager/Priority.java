package com.example.taskmanager;

import java.io.Serializable;


/**
 * Represents a priority with an ID and a name.
 */
public class Priority implements Serializable {
    private int id;
    private String name;

    /**
     * Constructs a new priority
     * 
     * @param id the unique ID of the priority
     * @param name the name of the priority
     */
    public Priority(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Default constructor required for Jackson.
     */
    public Priority() {}

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the priority.
     * 
     * @return a string representation of the priority
     */
    @Override
    public String toString() {
        return "Priority{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
