package media_2024;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        TaskService taskService = new TaskService();

        try {
            // Προσθήκη εργασιών
            taskService.addTask("Complete project", "Finalize the project details", "Work", "High", LocalDate.of(2024, 12, 15));
            taskService.addTask("Buy groceries", "Milk, eggs, bread", "Personal", "Medium", LocalDate.of(2024, 12, 10));

            // Τροποποίηση εργασίας
            taskService.updateTask(1, "Complete project report", "Finalize and submit the report", "Work", "High", LocalDate.of(2024, 12, 20), Task.STATUS_IN_PROGRESS);

            // Διαγραφή εργασίας
            taskService.deleteTask(2);

            // Εκτύπωση εργασιών
            System.out.println("Tasks:");
            for (Task task : taskService.getTasks()) {
                System.out.println(task);
            }

            System.out.println("Tasks by Status:");
            for (Task task : taskService.getTasksByStatus("Open")) {
                System.out.println(task);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}


