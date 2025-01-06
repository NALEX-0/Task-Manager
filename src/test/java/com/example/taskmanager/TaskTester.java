package com.example.taskmanager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Test Task and TaskService
 */
public class TaskTester {
    public static void main(String[] args) {
        System.out.println("---Test Tasks---");
        TaskService taskService = new TaskService();
        CategoryService categoryService = new CategoryService();
        PriorityService priorityService = new PriorityService();

        List<Integer> createdTaskIds = new ArrayList<>();
        List<Integer> createdCategoryIds = new ArrayList<>();
        List<Integer> createdPriorityIds = new ArrayList<>();

        try {
            // 1. Create Category and Priority
            categoryService.addCategory("TT-Work");
            priorityService.addPriority("TT-High");

            createdCategoryIds.add(categoryService.getCategoryByName("TT-Work").getId());
            createdPriorityIds.add(priorityService.getPriorityByName("TT-High").getId());

            Category workCategory = categoryService.getCategoryById(1);
            Priority highPriority = priorityService.getPriorityById(2);

            // 2. Add Tasks
            taskService.addTask("TT-Complete project", "Finalize project report", workCategory, highPriority, LocalDate.of(2025, 6, 15));
            taskService.addTask("TT-Prepare meeting", "Create agenda", workCategory, highPriority, LocalDate.of(2025, 7, 10), "Delayed");

            createdTaskIds.add(taskService.getTaskByTitle("TT-Complete project").getId());
            createdTaskIds.add(taskService.getTaskByTitle("TT-Prepare meeting").getId());

            System.out.println("Tasks after addition:");
            taskService.getTasks().forEach(System.out::println);

            // 3. Add Notifications to Task
            taskService.addNotificationToTask(createdTaskIds.get(0), LocalDate.of(2025, 3, 14));
            taskService.addNotificationToTask(createdTaskIds.get(0), LocalDate.of(2025, 3, 11));

            System.out.println("\nNotifications for TT-Complete project:");
            taskService.getTasks().get(0).getNotifications().forEach(System.out::println);

            // 4. Delete Notification from Task
            taskService.deleteNotificationFromTask(createdTaskIds.get(0), 1);

            System.out.println("\nNotifications for TT-Complete project after deletion:");
            taskService.getTasks().get(0).getNotifications().forEach(System.out::println);

            // 5. Update Tasks
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

            // 6. Delete Tasks
            taskService.deleteTask(createdTaskIds.get(1));

            System.out.println("\nTasks after deletion:");
            taskService.getTasks().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error during testing tasks: " + e.getMessage());
        } 
        finally {
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
