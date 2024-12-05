package media_2024.Testers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import media_2024.Category;
import media_2024.CategoryService;
import media_2024.Priority;
import media_2024.PriorityService;
import media_2024.Task;
import media_2024.TaskService;

public class TaskTester {
    public static void main(String[] args) {
        // Test Tasks
        System.out.println("---Test Tasks---");
        TaskService taskService = new TaskService();
        CategoryService categoryService = new CategoryService();
        PriorityService priorityService = new PriorityService();

        List<Integer> createdTaskIds = new ArrayList<>();
        List<Integer> createdCategoryIds = new ArrayList<>();
        List<Integer> createdPriorityIds = new ArrayList<>();

        try {
            // 1. Δημιουργία Κατηγοριών και Προτεραιοτήτων
            categoryService.addCategory("TT-Work");
            priorityService.addPriority("TT-High");

            createdCategoryIds.add(categoryService.getCategoryByName("TT-Work").getId());
            createdPriorityIds.add(priorityService.getPriorityByName("TT-High").getId());

            Category workCategory = categoryService.getCategoryById(1);
            Priority highPriority = priorityService.getPriorityById(2);

            // 2. Προσθήκη Εργασιών
            taskService.addTask("TT-Complete project", "Finalize project report", workCategory, highPriority, LocalDate.of(2024, 12, 15));
            taskService.addTask("TT-Prepare meeting", "Create agenda", workCategory, highPriority, LocalDate.of(2024, 12, 10));

            createdTaskIds.add(taskService.getTaskByTitle("TT-Complete project").getId());
            createdTaskIds.add(taskService.getTaskByTitle("TT-Prepare meeting").getId());

            System.out.println("Tasks after addition:");
            taskService.getTasks().forEach(System.out::println);

            // 3. Ενημέρωση Εργασίας
            taskService.updateTask(
                createdTaskIds.get(0),
                "TT-Complete final report",
                "Update and finalize the report",
                workCategory,
                highPriority,
                LocalDate.of(2024, 12, 20),
                Task.STATUS_IN_PROGRESS
            );

            System.out.println("\nTasks after update:");
            taskService.getTasks().forEach(System.out::println);

            // 4. Διαγραφή Εργασίας
            taskService.deleteTask(createdTaskIds.get(1));

            System.out.println("\nTasks after deletion:");
            taskService.getTasks().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error during testing tasks: " + e.getMessage());
        } finally {
            // Delete testing data
            for (int id : createdCategoryIds) {
                try {
                    categoryService.deleteCategory(id);
                } catch (Exception ignored) {
                }
            }
            for (int id : createdPriorityIds) {
                try {
                    priorityService.deletePriority(id);
                } catch (Exception ignored) {
                }
            }
            for (int id : createdTaskIds) {
                try {
                    taskService.deleteTask(id);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
