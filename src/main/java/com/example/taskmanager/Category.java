package com.example.taskmanager;

import java.io.Serializable;


/**
 * Represents a category with an ID and a name.
 */
public class Category implements Serializable {
    private int id;
    private String name;

    /**
     * Constructs a new category
     * 
     * @param id the unique ID of the category
     * @param name the name of the category
     */
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Default constructor required for Jackson.
     */
    public Category() {}

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
     * Returns a string representation of the category.
     * 
     * @return a string representation of the category
     */
    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "'}";
    }
}
