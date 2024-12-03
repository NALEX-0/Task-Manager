package media_2024;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PriorityService implements Serializable {
    private List<Priority> priorities = new ArrayList<>();
    private static final String PRIORITIES_FILE = "Priority.ser";
    private int nextId = 1; // Keeps track of the next available ID
    private static final String DEFAULT_PRIORITY_NAME = "Default";

    public PriorityService() {
        priorities = JSONHandler.readData(PRIORITIES_FILE, Priority.class);
        // Initialize nextId based on existing IDs
        priorities.forEach(priority -> nextId = Math.max(nextId, priority.getId() + 1));

        // Ensure the "Default" priority exists
        if (priorities.stream().noneMatch(p -> DEFAULT_PRIORITY_NAME.equals(p.getName()))) {
            priorities.add(new Priority(nextId++, DEFAULT_PRIORITY_NAME));
            savePriorities();
        }
    }

    // Προσθήκη προτεραιότητας
    public void addPriority(String name) {
        if (priorities.stream().anyMatch(p -> p.getName().equalsIgnoreCase(name))) {
            throw new IllegalArgumentException("Priority already exists.");
        }
        priorities.add(new Priority(nextId++, name));
        savePriorities();
    }

    // Τροποποίηση προτεραιότητας
    public void updatePriority(int id, String newName) {
        Priority priority = priorities.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Priority not found."));
        if (DEFAULT_PRIORITY_NAME.equals(priority.getName())) {
            throw new IllegalArgumentException("Cannot modify the default priority.");
        }
        priority.setName(newName);
        savePriorities();
    }

    // Διαγραφή προτεραιότητας
    public void deletePriority(int id) {
        Priority priority = priorities.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Priority not found."));
        if (DEFAULT_PRIORITY_NAME.equals(priority.getName())) {
            throw new IllegalArgumentException("Cannot delete the default priority.");
        }
        priorities.remove(priority);
        savePriorities();

        // Αντικατάσταση της διαγραμμένης προτεραιότητας με την "Default" στις σχετικές εργασίες
        TaskService.updateTasksWithDeletedPriority(id, DEFAULT_PRIORITY_NAME);
    }

    // Επιστροφή λίστας προτεραιοτήτων
    public List<Priority> getPriorities() {
        return new ArrayList<>(priorities);
    }

    // Αποθήκευση προτεραιοτήτων
    private void savePriorities() {
        JSONHandler.writeData(PRIORITIES_FILE, priorities);
    }
}
