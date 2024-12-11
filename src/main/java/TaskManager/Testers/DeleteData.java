// Delete data to Task, Category, Priority

package TaskManager.Testers;

import java.util.List;

import TaskManager.Category;
import TaskManager.CategoryService;
import TaskManager.Priority;
import TaskManager.PriorityService;
import TaskManager.Task;
import TaskManager.TaskService;

public class DeleteData { 
    public static void main(String[] args) {
        TaskService taskService = new TaskService();
        CategoryService categoryService = new CategoryService();
        PriorityService priorityService = new PriorityService();

        try {
            // Delete Categories
            List<Category> categories = categoryService.getCategories();
            for (Category category : categories) {
                categoryService.deleteCategory(category.getId());
            } 

            // Delete Priorities
            List<Priority> priorities = priorityService.getPriorities();
            for (Priority priority : priorities) {
                priorityService.deletePriority(priority.getId());
            } 
            
            // Delete Tasks
            List<Task> tasks = taskService.getTasks();
            for (Task task : tasks) {
                taskService.deleteTask(task.getId());
            } 

            System.out.println("\nSuccessfull delete");
        
        } catch (Exception e) {
            System.err.println("Error during data deletion: " + e.getMessage());
        }

    }
}
