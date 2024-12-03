package media_2024;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskService {
    private List<Task> tasks = new ArrayList<>();
    private static final String TASKS_FILE = "Task.ser";
    private int nextId = 1; // Keeps track of the next available ID

    public TaskService() {
        tasks = JSONHandler.readData(TASKS_FILE, Task.class);
        // Initialize nextId based on existing IDs
        tasks.forEach(task -> nextId = Math.max(nextId, task.getId() + 1));
        // Update statuses for delayed tasks
        tasks.forEach(Task::checkAndUpdateStatus);
    }

    public static void updateTasksWithDeletedPriority(int deletedPriorityId, String defaultPriorityName) {
        List<Task> tasks = JSONHandler.readData(TASKS_FILE, Task.class);

        tasks = tasks.stream()
                .peek(task -> {
                    if (task.getPriority().equalsIgnoreCase(String.valueOf(deletedPriorityId))) {
                        task.setPriority(defaultPriorityName);
                    }
                })
                .collect(Collectors.toList());

        JSONHandler.writeData(TASKS_FILE, tasks);
    }

    // Προσθήκη εργασίας
    public void addTask(String title, String description, String category, String priority, LocalDate dueDate) {
        tasks.add(new Task(nextId++, title, description, category, priority, dueDate));
        saveTasks();
    }

    // Τροποποίηση εργασίας
    public void updateTask(int id, String title, String description, String category, String priority, LocalDate dueDate, String status) {
        Task task = tasks.stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Task not found."));
        task.setTitle(title);
        task.setDescription(description);
        task.setCategory(category);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setStatus(status);
        saveTasks();
    }

    // Διαγραφή εργασίας
    public void deleteTask(int id) {
        tasks.removeIf(t -> t.getId() == id);
        saveTasks();
    }

    // Επιστροφή λίστας εργασιών
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    // Επιστροφή λίστας εργασιών για συγκεκριμένη κατάσταση
    public List<Task> getTasksByStatus(String status) {
        return tasks.stream()
                    .filter(t -> t.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
    }

    // Αποθήκευση εργασιών
    private void saveTasks() {
        JSONHandler.writeData(TASKS_FILE, tasks);
    }
}

