package com.example.taskmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Provides services for managing priorities.
 * 
 * <p>Allows adding, updating, deleting, and retrieving priorities. 
 * It ensures that a default priority always exists.</p>
 */
public class PriorityService implements Serializable {
    private List<Priority> priorities = new ArrayList<>();
    private static final String PRIORITIES_FILE = "Priority.json";
    private static final Priority DEFAULT_PRIORITY  = new Priority (1, "Default");
    private int nextId = 2; // Keeps track of the next available ID, 1 is the Default

    /**
     * Constructs a new PriorityService instance and loads priorities from the JSON storage.
     * Ensures the default priority exists and initializes the next available ID.
     * Because 'nextId' is 2 when Priorities are loaded from JSON.
     */
    public PriorityService() {
        priorities = JSONHandler.readData(PRIORITIES_FILE, Priority.class);

        // Ensure the "Default" priority exists
        if (priorities.stream().noneMatch(p -> p.getId() == DEFAULT_PRIORITY.getId())) {
            priorities.add(DEFAULT_PRIORITY);
        }

        // Initialize nextId based on existing ID
        priorities.forEach(priority -> nextId = Math.max(nextId, priority.getId() + 1));
        savePriorities();
    }

    /**
     * Adds a new priority with the specified name, if it does not already exist.
     * 
     * @param name the name of the new priority
     * @throws IllegalArgumentException if a priority with the same name already exists
     */
    public void addPriority(String name) {
        if (priorities.stream().anyMatch(p -> p.getName().equals(name))) {
            throw new IllegalArgumentException("Priority already exists.");
        }
        priorities.add(new Priority(nextId++, name));
        savePriorities();
    }

    /**
     * Updates the name of an existing priority identified by its ID.
     * Also updates related tasks to reflect the new priority name.
     * 
     * @param id      the ID of the priority to update
     * @param newName the new name for the priority
     * @throws IllegalArgumentException if the priority is the default priority or does not exist
     */
    public void updatePriority(int id, String newName) {
        Priority priority = getPriorityById(id);
        if (priority.equals(DEFAULT_PRIORITY)) {
            throw new IllegalArgumentException("Cannot modify the default priority.");
        }
        priority.setName(newName);
        savePriorities();

        // Replace updated Priority in related tasks
        TaskService.updateTasksWithChangedPriority(priority);
    }

    /**
     * Deletes a priority identified by its ID.
     * Also replace all tasks associated with the deleted priority with default priority.
     * 
     * @param id the ID of the priority to delete
     * @throws IllegalArgumentException if the priority is the default priority or does not exist
     */
    public void deletePriority(int id) {
        Priority priority = getPriorityById(id);
        if (priority.equals(DEFAULT_PRIORITY)) {
            throw new IllegalArgumentException("Cannot delete the default priority.");
        }

        priorities.remove(priority);
        savePriorities();

        // Replace deleted Priority with "Default" in related tasks
        TaskService.updateTasksWithChangedPriority(DEFAULT_PRIORITY);
    }

    /**
     * Getts a list of all priorities.
     * 
     * @return a list of all priorities
     */
    public List<Priority> getPriorities() {
        return new ArrayList<>(priorities);
    }

    /**
     * Gets a priority by its ID.
     * 
     * @param id the ID of the priority to retrieve
     * @return the priority with the specified ID
     * @throws IllegalArgumentException if no priority with the specified ID exists
     */
    public Priority getPriorityById(int id) {
        return priorities.stream()
            .filter(priority -> priority.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Priority not found."));
    }

    /**
     * Gets a priority by its name.
     * 
     * @param name the name of the priority to retrieve
     * @return the priority with the specified name
     * @throws IllegalArgumentException if no priority with the specified name exists
     */
    public Priority getPriorityByName(String name) {
        return priorities.stream()
            .filter(priority -> priority.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Priority not found."));
    }
    
    /**
     * Saves the current list of priorities to JSON storage.
     * <p>This method is private and intended for internal use only.</p>
     */
    private void savePriorities() {
        JSONHandler.writeData(PRIORITIES_FILE, priorities);
    }
}
