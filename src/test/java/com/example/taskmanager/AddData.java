package com.example.taskmanager;

import java.time.LocalDate;

/**
 * Adds data to Category, Priority and Task
 */
public class AddData {
    public static void main(String[] args) {
        TaskService taskService = new TaskService();
        CategoryService categoryService = new CategoryService();
        PriorityService priorityService = new PriorityService();

        try {
            // 1. Add categories (if not exist)
            Category Work;
            try {
                Work = categoryService.getCategoryByName("Work");
            } catch (Exception e) {
                categoryService.addCategory("Work");
                Work = categoryService.getCategoryByName("Work");
            }
            Category Personal;
            try {
                Personal = categoryService.getCategoryByName("Personal");
            } catch (Exception e) {
                categoryService.addCategory("Personal");
                Personal = categoryService.getCategoryByName("Personal");
            }
            Category Family;
            try {
                Family = categoryService.getCategoryByName("Family");
            } catch (Exception e) {
                categoryService.addCategory("Family");
                Family = categoryService.getCategoryByName("Family");
            }

            // 2. Add priorities (if not exist)
            Priority High;
            try {
                High = priorityService.getPriorityByName("High");
            } catch (Exception e) {
                priorityService.addPriority("High");
                High = priorityService.getPriorityByName("High");
            }
            Priority Medium;
            try {
                Medium = priorityService.getPriorityByName("Medium");
            } catch (Exception e) {
                priorityService.addPriority("Medium");
                Medium = priorityService.getPriorityByName("Medium");
            }
            Priority Low;
            try {
                Low = priorityService.getPriorityByName("Low");
            } catch (Exception e) {
                priorityService.addPriority("Low");
                Low = priorityService.getPriorityByName("Low");
            }

            // 3. Add tasks
            taskService.addTask("Complete project", "Finalize project report", Work, High, LocalDate.of(2025, 4, 20), "Open");
            taskService.addTask("Code reviews", "Do code reviews", Work, Medium, LocalDate.of(2025, 6, 30), "In Progress");
            taskService.addTask("Haircut", "Book a haircut appointment", Personal, Low, LocalDate.of(2024, 12, 10), "Completed");
            taskService.addTask("Supermarket", "Go to the supermarket", Family, Medium, LocalDate.of(2024, 11, 1), "Open"); // Should be updated to Delayed automatically
            taskService.addTask("Bank", "Go to the bank", Personal, Medium, LocalDate.now().plusDays(6), "Open"); // Due in the next 6 days

            taskService.addNotificationToTask(taskService.getTaskByTitle("Complete project").getId(), LocalDate.of(2025, 1, 11));
            taskService.addNotificationToTask(taskService.getTaskByTitle("Code reviews").getId(), LocalDate.of(2025, 1, 13), "Take the laptop with me");

            System.out.println("\nCreated Tasks:");
            taskService.getTasks().forEach(System.out::println);
        
        } catch (Exception e) {
            System.err.println("Error during data addition: " + e.getMessage());
        }

    }
}
