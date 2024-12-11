package TaskManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

public class MainCli {
    static Scanner scanner = new Scanner(System.in);
    static TaskService taskService = new TaskService();
    static PriorityService priorityService = new PriorityService();
    static CategoryService categoryService = new CategoryService();

    public static void main(String[] args) {
        System.out.println("Welcome to Task Manager CLI!");
        System.out.println("For options type -h");

        showStats();

        while (true) {
            System.out.print("\nEnter a command: ");
            String command = scanner.nextLine().trim();

            switch (command) {
                case "addCategory":
                    System.out.println("\n--- Add Category ---");
                    System.out.println("Type -back if you want to go back");
                    addC();
                    break;
                    
                case "addPriority":
                    System.out.println("\n--- Add Priority ---");
                    System.out.println("Type -back if you want to go back");
                    addP();
                    break;

                case "addTask":
                    System.out.println("\n--- Add Task ---");
                    System.out.println("Type -back if you want to go back");
                    addT();
                    break;

                case "updateTask":
                    System.out.println("\n--- Update Task ---");
                    System.out.println("Type -back if you want to go back");
                    updateT();
                    break;

                case "updateCategory":
                    System.out.println("\n--- Update Category ---");
                    System.out.println("Type -back if you want to go back");
                    updateC();
                    break;

                case "updatePriority":
                    System.out.println("\n--- Update Priority ---");
                    System.out.println("Type -back if you want to go back");
                    updateP();
                    break;

                case "deleteTask":
                    System.out.println("\n--- Delete Task ---");
                    System.out.println("Type -back if you want to go back");
                    deleteT();
                    break;

                case "deleteCategory":
                    System.out.println("\n--- Delete Category ---");
                    System.out.println("Type -back if you want to go back");
                    deleteC();
                    break;

                case "deletePriority":
                    System.out.println("\n--- Delete Priority ---");
                    System.out.println("Type -back if you want to go back");
                    deleteP();
                    break;

                case "showStats":
                    showStats();
                    break;

                case "showCategories":
                    System.out.println("\n--- Available Categories ---");
                    categoryService.getCategories().forEach(System.out::println);
                    break;

                case "showPriorities":
                    System.out.println("\n--- Available Priorities ---");
                    priorityService.getPriorities().forEach(System.out::println);
                    break;

                case "showTasks":
                    System.out.println("\n--- Available Tasks ---");
                    showTasksByStatus();
                    break;

                case "searchTask":
                    System.out.println("\n--- Search Task/s ---");
                    searchTasks();
                    break;

                case "-h":
                    printHelp();
                    break;

                case "exit":
                    break;
            
                default:
                    System.out.println("Invalid command, please try again or type -h for help");
                    break;
            }

            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Exiting Task Manager. Goodbye!");
                break;
            } 
            
        }

        scanner.close();
    }

    /**
     * Adds a Category
     */
    private static void addC() {
        try {
            String categoryName = promptInput("Give Category name: ");
            categoryService.addCategory(categoryName);
            System.out.println("Category created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    /**
     * Adds a Priority
     */
    private static void addP() {
        try {
            String priorityName = promptInput("Give Priority name: ");
            priorityService.addPriority(priorityName);
            System.out.println("Priority created successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    /**
     * Adds a Task
     */
    private static void addT() {
        try {
            String title = promptInput("Give Task title: ");
            String description = promptInput("Give Task description: ");
            
            System.out.println("Here are the available categories:");
            categoryService.getCategories().forEach(System.out::println);
            String category = promptInput("Give Task category name: ");
            
            System.out.println("Here are the available priorities:");
            priorityService.getPriorities().forEach(System.out::println);
            String priority = promptInput("Give Task priority name: ");
            
            String dueDateInput = promptInput("Give Task due date (YYYY-MM-DD): ");
            LocalDate dueDate = LocalDate.parse(dueDateInput);

            taskService.addTask(title, description, 
                                categoryService.getCategoryByName(category),
                                priorityService.getPriorityByName(priority),
                                dueDate);
    
            System.out.println("Task added successfully!");
    
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    /**
     * Adds a notification to a task.
     * 
     * @param task the task to add the notification to
     */
    private static void addN(Task task) {
        try {
            LocalDateTime reminderTime = LocalDateTime.parse(promptInput("Enter Notification date and time (YYYY-MM-DDTHH:MM): "));

            String message = promptInput("Enter Notification message: ");
            if (message.isEmpty()) {
                taskService.addNotificationToTask(task.getId(), reminderTime);
            }
            else {
                // taskService.addNotificationToTask(task.getId(), reminderTime,);
            }

            System.out.println("Notification added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    private static void updateT() {
        try {
            System.out.println("Available Tasks:");
            taskService.getTasks().forEach(System.out::println);
            int taskId = Integer.parseInt(promptInput("Enter Task ID to update: "));
            Task task = taskService.getTaskByid(taskId);
            
            String title = promptInput("Enter new Task title: ");
            String description = promptInput("Enter new Task description: ");
    
            System.out.println("Available Categories:");
            categoryService.getCategories().forEach(System.out::println);
            String categoryName = promptInput("Enter new Task category name: ");
    
            System.out.println("Available Priorities:");
            priorityService.getPriorities().forEach(System.out::println);
            String priorityName = promptInput("Enter new Task priority name: ");
    
            String date = promptInput("Enter new Task due date (YYYY-MM-DD): ");

            String status = promptInput("Enter new Task status: ");

            System.out.println("Edit notification (press Enter for skip)");
            editNotification(task);
     
            taskService.updateTask(taskId,
                title.isEmpty() ? task.getTitle() : title,
                description.isEmpty() ? task.getDescription() : description,
                categoryName.isEmpty() ? task.getCategory() : categoryService.getCategoryByName(categoryName),
                priorityName.isEmpty() ? task.getPriority() : priorityService.getPriorityByName(priorityName),
                date.isEmpty() ? task.getDueDate() : LocalDate.parse(date), 
                status.isEmpty() ? task.getStatus() : status
            );
            System.out.println("Task updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    private static void updateC() {
        try {
            System.out.println("Available Categories:");
            categoryService.getCategories().forEach(System.out::println);
            int categoryId = Integer.parseInt(promptInput("Enter Category ID to update: "));
            String newName = promptInput("Enter new Category name: ");
    
            categoryService.updateCategory(categoryId, newName);
            System.out.println("Category updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    private static void updateP() {
        try {
            System.out.println("Available Priorities:");
            priorityService.getPriorities().forEach(System.out::println);
            int priorityId = Integer.parseInt(promptInput("Enter Priority ID to update: "));
            String newName = promptInput("Enter new Priority name: ");
    
            priorityService.updatePriority(priorityId, newName);
            System.out.println("Priority updated successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    private static void deleteT() {
        try {
            System.out.println("Available Tasks:");
            taskService.getTasks().forEach(System.out::println);
            int taskId = Integer.parseInt(promptInput("Enter Task ID to delete: "));
            taskService.deleteTask(taskId);
            System.out.println("Task deleted successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }

    }

    private static void deleteC() {
        try {
            System.out.println("Available Categories:");
            categoryService.getCategories().forEach(System.out::println);
            int categoryId = Integer.parseInt(promptInput("Enter Category ID to delete: "));
            categoryService.deleteCategory(categoryId);
            System.out.println("Category deleted successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    private static void deleteP() {
        try {
            System.out.println("Available Priorities:");
            priorityService.getPriorities().forEach(System.out::println);
            int priorityId = Integer.parseInt(promptInput("Enter Priority ID to delete: "));
            priorityService.deletePriority(priorityId);
            System.out.println("Priority deleted successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    /**
     * Deletes a notification from a task.
     * 
     * @param task the task whose notification is to be deleted
     */
    private static void deleteN(Task task) {
        try {
            task.getNotifications().forEach(System.out::println);
            int notificationId = Integer.parseInt(promptInput("Enter Notification ID to delete: "));
            task.deleteNotification(notificationId);
            System.out.println("Notification deleted successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during editing notifications: " + e.getMessage());
        }
    }

    private static void showStats() {
        System.out.println("\n--- Current Statistics ---");
        System.out.println("1. Total Tasks: " + taskService.getTasks().size());
        System.out.println("2. Completed Tasks: " + taskService.getTasksByStatus(Task.STATUS_COMPLETED).size());
        System.out.println("3. Delayed Tasks: " + taskService.getTasksByStatus(Task.STATUS_DELAYED).size());
        System.out.println("4. Tasks Due in 7 Days: " + taskService.getTasksDueIn7Days().size());
    }

    private static void showTasksByStatus() {
        try {
            System.out.println("Options: Total, Open, InProgress, Postponed, Completed, Delayed, DueIn7");
            String status = promptInput("Give Task status: ");
            switch (status) {
                case "Total":
                    taskService.getTasks().forEach(System.out::println);
                    break;
                
                case "Open":
                    taskService.getTasksByStatus(Task.STATUS_OPEN).forEach(System.out::println);
                    break;
                
                case "InProgress":
                    taskService.getTasksByStatus(Task.STATUS_IN_PROGRESS).forEach(System.out::println);
                    break;
                
                case "Postponed":
                    taskService.getTasksByStatus(Task.STATUS_POSTPONED).forEach(System.out::println);
                    break;
                
                case "Completed":
                    taskService.getTasksByStatus(Task.STATUS_COMPLETED).forEach(System.out::println);
                    break;
                    
                case "Delayed":
                    taskService.getTasksByStatus(Task.STATUS_DELAYED).forEach(System.out::println);
                    break;
                
                case "DueIn7":
                    taskService.getTasksDueIn7Days().forEach(System.out::println);
                    break;

                default:
                    System.out.println("Invalid input, please try again");
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    /**
     * Search tasks with title
     */
    private static void searchTasks() {
        try {
            String title = promptInput("Give title to search: ");
            taskService.searchTasksByTitle(title).forEach(System.out::println);

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Invalid input. Please try again");
        }
    }

    /**
     * Allows the user to edit task notifications (Add, Update, Delete).
     * 
     * @param task the task whose notifications are to be edited
     */
    private static void editNotification(Task task) {
        try {
            String input = promptInput("Press 1 to Add, 2 to Delete: ");

            if (input.isEmpty()) {
                return; // User skipped editing notifications
            }

            switch (input) {
                case "1": // Add Notification
                    addN(task);
                    break;

                case "2": // Update Notification
                    deleteN(task);
                    break;

                default:
                    System.out.println("Invalid option. Skipping notification editing.");
            }
        }catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error during editing notifications: " + e.getMessage());
        }
    }

    /**
     * Prompts the user for input and checks for the "-back" command.
     * 
     * @param prompt the message to display to the user
     * @return the user input
     * @throws IllegalArgumentException if the user types "-back"
     */
    private static String promptInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("-back")) {
            throw new IllegalArgumentException("Operation cancelled by user");
        }
        return input;
    }

    /**
     * Prints the help menu with available commands.
     */
    private static void printHelp() {
        System.out.println("\n--- Available Commands ---");
        System.out.println("If you are inside an action type -back to leave");
        // Add
        System.out.println("addCategory - Add a new category");
        System.out.println("addPriority - Add a new priority");
        System.out.println("addTask - Add a new task");
        // Update
        System.out.println("updateTask - Update an existing task");
        System.out.println("updateCategory - Update an existing Category");
        System.out.println("updatePriority - Update an existing Priority");
        // Delete
        System.out.println("deleteTask - Delete a task");
        System.out.println("deleteCategory - Delete a category");
        System.out.println("deletePriority - Delete a priority");
        // Show
        System.out.println("showCategories - Show all Categories");
        System.out.println("showPriorities - Show all Priorities");
        System.out.println("showTasks - Show all tasks or by status");
        // Other
        System.out.println("searchTask - Search a task by title");
        System.out.println("showStats - Show Statistics");
        System.out.println("-h - Help");
        System.out.println("exit - Exit the program");
    }
}
