// Test use case: Search Task/s

package TaskManager.Testers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import TaskManager.Category;
import TaskManager.CategoryService;
import TaskManager.Priority;
import TaskManager.PriorityService;
import TaskManager.TaskService;

public class SearchTasksTester {
    public static void main(String[] args) {
        System.out.println("---Test Task Search---");
        TaskService taskService = new TaskService();
        CategoryService categoryService = new CategoryService();
        PriorityService priorityService = new PriorityService();

        List<Integer> createdTaskIds = new ArrayList<>();
        List<Integer> createdCategoryIds = new ArrayList<>();
        List<Integer> createdPriorityIds = new ArrayList<>();

        try {
            // 1. Create Category and Priority
            categoryService.addCategory("TS-Work");
            priorityService.addPriority("TS-High");

            createdCategoryIds.add(categoryService.getCategoryByName("TS-Work").getId());
            createdPriorityIds.add(priorityService.getPriorityByName("TS-High").getId());

            Category workCategory = categoryService.getCategoryById(1);
            Priority highPriority = priorityService.getPriorityById(2);

            // 2. Add Tasks
            taskService.addTask("TT-Complete project", "Finalize project report", workCategory, highPriority, LocalDate.of(2024, 12, 15));
            taskService.addTask("TT-Prepare meeting", "Create agenda", workCategory, highPriority, LocalDate.of(2024, 12, 10));
            taskService.addTask("TT-Go home", "Enough work for today", workCategory, highPriority, LocalDate.of(2024, 12, 12));

            createdTaskIds.add(taskService.getTaskByTitle("TT-Complete project").getId());
            createdTaskIds.add(taskService.getTaskByTitle("TT-Prepare meeting").getId());
            createdTaskIds.add(taskService.getTaskByTitle("TT-Go home").getId());

            // 3. Search Task Go home
            System.out.println("\nSearch results:");
            taskService.searchTasksByTitle("Go h").forEach(System.out::println); 


        } catch (Exception e) {
            System.err.println("Error during testing Search: " + e.getMessage());
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
