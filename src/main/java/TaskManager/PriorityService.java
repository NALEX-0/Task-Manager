package TaskManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PriorityService implements Serializable {
    private List<Priority> priorities = new ArrayList<>();
    private static final String PRIORITIES_FILE = "Priority.json";
    private static final Priority DEFAULT_PRIORITY  = new Priority (1, "Default");
    private int nextId = 2; // Keeps track of the next available ID, 1 is the Default

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

    // Adds new Priority if it doesn't already exist
    public void addPriority(String name) {
        if (priorities.stream().anyMatch(p -> p.getName().equals(name))) {
            throw new IllegalArgumentException("Priority already exists.");
        }
        priorities.add(new Priority(nextId++, name));
        savePriorities();
    }

    // Updates the name of the Priority with the specified id 
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

    // Deletes Priority with the specified id
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

    // Return List of Priorities
    public List<Priority> getPriorities() {
        return new ArrayList<>(priorities);
    }

    // Return Priority with specified id
    public Priority getPriorityById(int id) {
        return priorities.stream()
            .filter(priority -> priority.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Priority not found."));
    }

    // Return Priority with specified name
    public Priority getPriorityByName(String name) {
        return priorities.stream()
            .filter(priority -> priority.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Priority not found."));
    }
    
    // Save Priorities
    private void savePriorities() {
        JSONHandler.writeData(PRIORITIES_FILE, priorities);
    }
}
