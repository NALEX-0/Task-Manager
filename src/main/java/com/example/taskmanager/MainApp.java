package com.example.taskmanager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * Main application class for the MediaLab Assistant application.
 */
public class MainApp extends Application {
    private TaskService taskService = new TaskService();
    private CategoryService categoryService = new CategoryService();
    private PriorityService priorityService = new PriorityService();

    private TableView<Task> taskTableView = new TableView<>();
    private TableView<Category> categoryTableView = new TableView<>();
    private TableView<Priority> priorityTableView = new TableView<>();

    /**
     * Entry point for the application.
     *
     * @param primaryStage the primary stage for the application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("MediaLab Assistant");
        Image icon = new Image(getClass().getResourceAsStream("/icons/java.png"));
        primaryStage.getIcons().add(icon);

        // Create tab pane
        TabPane tabPane = new TabPane();

        // Tasks Tab
        Tab tasksTab = new Tab("Tasks");
        tasksTab.setClosable(false);

        // Buttons for Task Actions
        Button totalTasksButton = new Button("Total Tasks");
        Button completedTasksButton = new Button("Completed Tasks");
        Button delayedTasksButton = new Button("Delayed Tasks");
        Button tasksDueIn7DaysButton = new Button("Tasks Due in 7 Days");
        Button addTaskButton = new Button("Add Task");

        // Style for buttons
        totalTasksButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        completedTasksButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        delayedTasksButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        tasksDueIn7DaysButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        addTaskButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        totalTasksButton.getStyleClass().addAll("btn", "btn-info");
        completedTasksButton.getStyleClass().addAll("btn", "btn-success");
        delayedTasksButton.getStyleClass().addAll("btn", "btn-danger");
        tasksDueIn7DaysButton.getStyleClass().addAll("btn", "btn-warning");
        addTaskButton.getStyleClass().addAll("btn", "btn-primary");

        // Set button actions
        totalTasksButton.setOnAction(e -> updateTaskTable(taskService.getTasks()));
        completedTasksButton.setOnAction(e -> updateTaskTable(taskService.getTasksByStatus("Completed")));
        delayedTasksButton.setOnAction(e -> updateTaskTable(taskService.getTasksByStatus("Delayed")));
        tasksDueIn7DaysButton.setOnAction(e -> updateTaskTable(taskService.getTasksDueIn7Days()));
        addTaskButton.setOnAction(e -> showAddTaskDialog());

        // Search Field
        TextField searchField = new TextField();
        searchField.setPromptText("Search tasks...");
        searchField.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateTaskTable(taskService.searchTasks(newValue));
        });
        VBox searchBox = new VBox(searchField);
        searchBox.setStyle("-fx-padding: 0 0 20 0;");

        // Top pane
        HBox centerButtons = new HBox(20); // Spacing between buttons
        centerButtons.getChildren().addAll(totalTasksButton, completedTasksButton, delayedTasksButton, tasksDueIn7DaysButton);
        centerButtons.setStyle("-fx-alignment: center;");
        BorderPane topPane = new BorderPane();
        topPane.setRight(addTaskButton);
        topPane.setCenter(centerButtons);
        topPane.setTop(searchBox);
        topPane.setStyle("-fx-padding: 10;");

        // Set up Task Table
        setupTaskTable();

        // Combine Top Pane and TableView
        VBox tasksContent = new VBox();
        tasksContent.getChildren().addAll(topPane, taskTableView);
        tasksTab.setContent(tasksContent);


        // Priority Tab
        Tab priorityTab = new Tab("Priorities");
        priorityTab.setClosable(false);
        
        // Pane for Add Priority button
        BorderPane btnPaneP = new BorderPane();
        Button addPriorityButton = new Button("Add Priority");
        addPriorityButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        addPriorityButton.getStyleClass().addAll("btn", "btn-primary");
        addPriorityButton.setOnAction(e -> showAddPriorityDialog());
        btnPaneP.setRight(addPriorityButton);
        btnPaneP.setStyle("-fx-padding: 10;");
        
        // Set up Priority Table
        setupPriorityTable();
        updatePriorityTable(priorityService.getPriorities());
        
        // Combine Pane and TableView
        VBox priorityContent = new VBox();
        // priorityContent.setStyle("-fx-alignment: center; -fx-padding: 20;");
        priorityContent.getChildren().addAll(btnPaneP, priorityTableView);
        priorityTab.setContent(priorityContent);


        // Category Tab
        Tab categoryTab = new Tab("Categories");
        categoryTab.setClosable(false);

        // Pane for Add Category button
        BorderPane btnPaneC = new BorderPane();
        Button addCategoryButton = new Button("Add Category");
        addCategoryButton.setStyle("-fx-font-size: 14px; -fx-padding: 10;");
        addCategoryButton.getStyleClass().addAll("btn", "btn-primary");
        addCategoryButton.setOnAction(e -> showAddCategoryDialog());
        btnPaneC.setRight(addCategoryButton);
        btnPaneC.setStyle("-fx-padding: 10;");

        // Set up Category Table
        setupCategoryTable();
        updateCategoryTable(categoryService.getCategories());

        // Combine Pane and TableView
        VBox categoryContent = new VBox();
        categoryContent.getChildren().addAll(btnPaneC, categoryTableView);
        categoryTab.setContent(categoryContent);


        // Add tabs to TabPane
        tabPane.getTabs().addAll(tasksTab, priorityTab, categoryTab);

        // Settup main window 
        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        // Window size
        Scene scene = new Scene(root, 1200, 600);

        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        primaryStage.setScene(scene);

        primaryStage.show();

        // Load initial data into table
        updateTaskTable(taskService.getTasks());

        // Show delayed tasks popup
        showDelayedTasksPopup();
    }

    /**
     * Configures the Tasks table with fields and controls.
     */
    private void setupTaskTable() {
        // Column: Title
        TableColumn<Task, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        
        // Column: Description
        TableColumn<Task, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Column: Category
        TableColumn<Task, String> categoryColumn = new TableColumn<>("Category");
        categoryColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory().getName())
        );

        // Column: Priority
        TableColumn<Task, String> priorityColumn = new TableColumn<>("Priority");
        priorityColumn.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPriority().getName())
        );

        // Column: Notifications
        TableColumn<Task, String> notificationsColumn = new TableColumn<>("Notifications");
        notificationsColumn.setCellValueFactory(cellData -> {
            List<Notification> notifications = cellData.getValue().getNotifications();
            String formattedNotifications = notifications.stream()
                .map(notification -> {
                    LocalDate today = LocalDate.now();
                    LocalDate reminderDate = notification.getReminderDate();

                    long daysBetween = ChronoUnit.DAYS.between(today, reminderDate);

                    if (daysBetween == 0) {
                        return "Today";
                    } else if (daysBetween == 1) {
                        return "Tomorrow";
                    } else if (daysBetween > 1 && daysBetween <= 7) {
                        return "In " + daysBetween + " days";
                    } else {
                        return "In " + daysBetween + " days";
                    }
                })
                .collect(Collectors.joining("\n"));

            return new javafx.beans.property.SimpleStringProperty(formattedNotifications);
        });

        // Column: DueDate
        TableColumn<Task, LocalDate> dueDateColumn = new TableColumn<>("Due Date");
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));

        // Column: Status
        TableColumn<Task, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

    
        // Actions Column for Delete and Update
        TableColumn<Task, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<Task, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");
    
            {
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px;");
                updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px;");

                deleteButton.setOnAction(e -> {
                    Task task = getTableView().getItems().get(getIndex());
                    taskService.deleteTask(task.getId());
                    updateTaskTable(taskService.getTasks());
                });
    
                updateButton.setOnAction(e -> {
                    Task task = getTableView().getItems().get(getIndex());
                    showUpdateTaskDialog(task);
                });
            }
    
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, deleteButton, updateButton);
                    setGraphic(buttons);
                }
            }
        });

        // Add columns to the TableView
        taskTableView.getColumns().addAll(titleColumn, descriptionColumn, categoryColumn, priorityColumn, notificationsColumn, dueDateColumn, statusColumn, actionsColumn);

        // Resize columns
        taskTableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        taskTableView.getColumns().forEach(column -> {
            column.setPrefWidth(140); 
            column.setResizable(true);
        });

        // Placeholder for empty TableView
        Label placeholder = new Label("No tasks available");
        placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        taskTableView.setPlaceholder(placeholder);

        taskTableView.setPrefHeight(600);
    }

    /**
     * Configures the Priorities table with fields and controls.
     */
    private void setupPriorityTable() {
        TableColumn<Priority, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(100);

        // Actions Column for Delete and Update
        TableColumn<Priority, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<Priority, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");

            {
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px;");
                updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px;");

                deleteButton.setOnAction(e -> {
                    Priority priority = getTableView().getItems().get(getIndex());
                    if (priority.getName().equals("Default")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Cant delete Default priority!");
                        alert.show();
                    }
                    else {
                        priorityService.deletePriority(priority.getId());
                        updatePriorityTable(priorityService.getPriorities());
                    }
                });
    
                updateButton.setOnAction(e -> {
                    Priority priority = getTableView().getItems().get(getIndex());
                    showUpdatePriorityDialog(priority);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                // No button for Default priority
                if (empty || getTableView().getItems().get(getIndex()).getName().equalsIgnoreCase("Default")) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, deleteButton, updateButton);
                    setGraphic(buttons);
                }
            }
        });

        // Add columns to the TableView
        priorityTableView.getColumns().addAll(nameColumn, actionsColumn);

        // Placeholder for empty TableView
        Label placeholder = new Label("No categories available");
        placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        priorityTableView.setPlaceholder(placeholder);
    }
    
    /**
     * Configures the Categories table with fields and controls.
     */
    private void setupCategoryTable() {
        // Category Name Column
        TableColumn<Category, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(100);

        // Actions Column for Delete and Update
        TableColumn<Category, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(column -> new TableCell<Category, Void>() {
            private final Button deleteButton = new Button("Delete");
            private final Button updateButton = new Button("Update");

            {
                deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px;");
                updateButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 12px;");

                deleteButton.setOnAction(e -> {
                    Category category = getTableView().getItems().get(getIndex());
                    categoryService.deleteCategory(category.getId());
                    updateCategoryTable(categoryService.getCategories());
                });
    
                updateButton.setOnAction(e -> {
                    Category category = getTableView().getItems().get(getIndex());
                    showUpdateCategoryDialog(category);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(10, deleteButton, updateButton);
                    setGraphic(buttons);
                }
            }
        });

        // Add columns to the TableView
        categoryTableView.getColumns().addAll(nameColumn, actionsColumn);

        // Placeholder for empty TableView
        Label placeholder = new Label("No categories available");
        placeholder.setStyle("-fx-font-size: 14px; -fx-text-fill: gray;");
        categoryTableView.setPlaceholder(placeholder);

    }

    /**
     * Displays the add task dialog for adding a task.
     */
    private void showAddTaskDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add Task");
        VBox dialogVBox = new VBox(10);
        dialogVBox.setStyle("-fx-padding: 20;");
    
        // Title Field
        TextField titleField = new TextField();
        titleField.setPromptText("Enter task title");
    
        // Description Field
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Enter task description");
    
        // Category Dropdown
        ComboBox<Category> categoryDropdown = new ComboBox<>();
        categoryDropdown.setPromptText("Select category");
        categoryDropdown.getItems().addAll(categoryService.getCategories());
        categoryDropdown.setCellFactory(cell -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });
        categoryDropdown.setButtonCell(new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });
    
        // Priority Dropdown
        ComboBox<Priority> priorityDropdown = new ComboBox<>();
        priorityDropdown.setPromptText("Select priority");
        priorityDropdown.getItems().addAll(priorityService.getPriorities());
        priorityDropdown.setCellFactory(cell -> new ListCell<Priority>() {
            @Override
            protected void updateItem(Priority item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });
        priorityDropdown.setButtonCell(new ListCell<Priority>() {
            @Override
            protected void updateItem(Priority item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });
    
        // Due Date Picker
        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Select due date");
    
        // Status Dropdown
        ComboBox<String> statusDropdown = new ComboBox<>();
        statusDropdown.setPromptText("Select status");
        statusDropdown.getItems().addAll("Open", "In Progress", "Postponed", "Completed", "Delayed");
    
        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String title = titleField.getText();
            String description = descriptionField.getText();
            Category category = categoryDropdown.getValue();
            Priority priority = priorityDropdown.getValue();
            LocalDate dueDate = dueDatePicker.getValue();
            String status = statusDropdown.getValue();
    
            if (title.isEmpty() || category == null|| dueDate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Title, category and due date are required!");
                alert.show();
            } else {
                if (priority == null) {
                    priority = priorityService.getPriorityByName("Default");
                }
                if (status == null) {
                    status = "Open";
                }
                try {
                    taskService.addTask(title, description, category, priority, dueDate, status);
                    taskTableView.getItems().add(taskService.getLastTask());
                    dialog.close();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add task: " + ex.getMessage());
                    alert.show();
                }
            }
        });
    
        dialogVBox.getChildren().addAll(
            new Label("Title:"), titleField,
            new Label("Description:"), descriptionField,
            new Label("Category:"), categoryDropdown,
            new Label("Priority:"), priorityDropdown,
            new Label("Due Date:"), dueDatePicker,
            new Label("Status:"), statusDropdown,
            saveButton
        );
    
        Scene dialogScene = new Scene(dialogVBox, 400, 500);
        dialog.setScene(dialogScene);
        dialog.show();
    }    

    /**
     * Displays the add category dialog for adding a category.
     */
    private void showAddCategoryDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add Category");
        VBox dialogVBox = new VBox(10);
        dialogVBox.setStyle("-fx-padding: 20;");
    
        // Name Field
        TextField nameField = new TextField();
        nameField.setPromptText("Enter category name");
    
        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Category name cannot be empty!");
                alert.show();
            } else {
                try {
                    categoryService.addCategory(name);
                    categoryTableView.getItems().add(categoryService.getLastCategory());
                    dialog.close();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add category: " + ex.getMessage());
                    alert.show();
                }
            }
        });
    
        dialogVBox.getChildren().addAll(
            new Label("Category Name:"), nameField,
            saveButton
        );
    
        Scene dialogScene = new Scene(dialogVBox, 250,150);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Displays the add priority dialog for adding a priority.
     */
    private void showAddPriorityDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add Priority");
        VBox dialogVBox = new VBox(10);
        dialogVBox.setStyle("-fx-padding: 20;");
    
        // Name Field
        TextField nameField = new TextField();
        nameField.setPromptText("Enter priority name");
    
        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Priority name cannot be empty!");
                alert.show();
            } else {
                try {
                    priorityService.addPriority(name);
                    priorityTableView.getItems().add(priorityService.getLastPriority());
                    dialog.close();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add priority: " + ex.getMessage());
                    alert.show();
                }
            }
        });
    
        dialogVBox.getChildren().addAll(
            new Label("Priority Name:"), nameField,
            saveButton
        );
    
        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

   /**
     * Displays the update category dialog for updating a category.
     * 
     * @param task the task to be updated
     */
    private void showUpdateTaskDialog(Task task) {
        Stage dialog = new Stage();
        dialog.setTitle("Update Task");
        VBox dialogVBox = new VBox(10);
        dialogVBox.setStyle("-fx-padding: 20;");

        // Title Field
        TextField titleField = new TextField(task.getTitle());
        titleField.setPromptText("Enter task title");

        // Description Field
        TextField descriptionField = new TextField(task.getDescription());
        descriptionField.setPromptText("Enter task description");

        // Category Dropdown
        ComboBox<Category> categoryDropdown = new ComboBox<>();
        categoryDropdown.setPromptText("Select category");
        categoryDropdown.getItems().addAll(categoryService.getCategories());
        Category selectedCategory = categoryService.getCategories().stream()
            .filter(category -> category.getName().equals(task.getCategory().getName()))
            .findFirst()
            .orElse(null);
        categoryDropdown.setValue(selectedCategory);
        categoryDropdown.setCellFactory(cell -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });
        categoryDropdown.setButtonCell(new ListCell<Category>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });

        // Priority Dropdown
        ComboBox<Priority> priorityDropdown = new ComboBox<>();
        priorityDropdown.setPromptText("Select category");
        priorityDropdown.getItems().addAll(priorityService.getPriorities());
        Priority selectedPriority = priorityService.getPriorities().stream()
            .filter(priority -> priority.getName().equals(task.getPriority().getName()))
            .findFirst()
            .orElse(null);
        priorityDropdown.setValue(selectedPriority);
        priorityDropdown.setCellFactory(cell -> new ListCell<Priority>() {
            @Override
            protected void updateItem(Priority item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });
        priorityDropdown.setButtonCell(new ListCell<Priority>() {
            @Override
            protected void updateItem(Priority item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? null : item.getName());
            }
        });

        // Due Date Picker
        DatePicker dueDatePicker = new DatePicker(task.getDueDate());

        // Status Dropdown
        ComboBox<String> statusDropdown = new ComboBox<>();
        statusDropdown.setPromptText("Select status");
        statusDropdown.getItems().addAll("Open", "In Progress", "Postponed", "Completed", "Delayed");
        statusDropdown.setValue(task.getStatus());

        // Notifications Section
        VBox notificationsBox = new VBox(10);
        notificationsBox.setStyle("-fx-padding: 10; -fx-border-color: #ccc; -fx-border-width: 1;");
        for (Notification notification : task.getNotifications()) {
            HBox notificationRow = new HBox(10);
            notificationRow.setStyle("-fx-padding: 5; -fx-border-color: lightgrey; -fx-border-width: 0 0 1 0;");
            LocalDate today = LocalDate.now();
            LocalDate reminderDate = notification.getReminderDate();
            long daysBetween = ChronoUnit.DAYS.between(today, reminderDate);

            String displayMessage;
            if (daysBetween == 0) {
                displayMessage = "Today";
            } else if (daysBetween == 1) {
                displayMessage = "Tomorrow";
            } else if (daysBetween > 1 && daysBetween <= 7) {
                displayMessage = "In " + daysBetween + " days";
            } else {
                displayMessage = "In " + daysBetween + " days";
            }

            Label reminderLabel = new Label(displayMessage);
            Label messageLabel = new Label(notification.getMessage());

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 12px;");
            deleteButton.setOnAction(e -> {
                taskService.deleteNotificationFromTask(task.getId(), notification.getId());
                notificationsBox.getChildren().remove(notificationRow);
            });

            notificationRow.getChildren().addAll(reminderLabel, messageLabel, deleteButton);
            notificationsBox.getChildren().add(notificationRow);
        }

        // Add Notification Button
        Button addNotificationButton = new Button("Add Notification");
        addNotificationButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 12px;");
        addNotificationButton.setOnAction(e -> {
            Stage notificationDialog = new Stage();
            notificationDialog.setTitle("Add Notification");

            VBox notificationVBox = new VBox(10);
            notificationVBox.setStyle("-fx-padding: 20;");

            DatePicker notificationDatePicker = new DatePicker();
            notificationDatePicker.setPromptText("Select Reminder Date");

            TextField notificationMessageField = new TextField();
            notificationMessageField.setPromptText("Enter Reminder Message");

            Button saveNotificationButton = new Button("Save Notification");
            saveNotificationButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            saveNotificationButton.setOnAction(ev -> {
                LocalDate reminderDate = notificationDatePicker.getValue();
                String message = notificationMessageField.getText();

                if (reminderDate == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Date is required for a notification!");
                    alert.show();
                } else {
                    if (message.isEmpty()) {
                        taskService.addNotificationToTask(task.getId(), reminderDate);
                        notificationDialog.close();
                    }
                    else {
                        taskService.addNotificationToTask(task.getId(), reminderDate, message);
                        notificationDialog.close();
                    }

                    // Add new notification row
                    HBox newNotificationRow = new HBox(10);
                    newNotificationRow.setStyle("-fx-padding: 5; -fx-border-color: lightgrey; -fx-border-width: 0 0 1 0;");

                    Label newReminderLabel = new Label("In " + ChronoUnit.DAYS.between(LocalDate.now(), reminderDate) + " days (Reminder Time):");
                    Label newMessageLabel = new Label(message);

                    Button newDeleteButton = new Button("Delete");
                    newDeleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 12px;");
                    newDeleteButton.setOnAction(ev2 -> {
                        Notification newNotification = task.getLastNotification();
                        taskService.deleteNotificationFromTask(task.getId(), newNotification.getId());
                        notificationsBox.getChildren().remove(newNotificationRow);
                    });

                    newNotificationRow.getChildren().addAll(newReminderLabel, newMessageLabel, newDeleteButton);
                    notificationsBox.getChildren().add(newNotificationRow);

                    notificationDialog.close();
                }
            });

            notificationVBox.getChildren().addAll(
                new Label("Reminder Date:"), notificationDatePicker,
                new Label("Message:"), notificationMessageField,
                saveNotificationButton
            );

            Scene notificationScene = new Scene(notificationVBox, 300, 200);
            notificationDialog.setScene(notificationScene);
            notificationDialog.show();
        });


        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String newTitle = titleField.getText();
            String newDescription = descriptionField.getText();
            LocalDate newDueDate = dueDatePicker.getValue();
            Category newCategory = categoryDropdown.getValue();
            Priority newPriority = priorityDropdown.getValue();
            String selectedStatus = statusDropdown.getValue();

            if (newTitle.isEmpty() || newDueDate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Title and due date are required!");
                alert.show();
            } else {
                taskService.updateTask(
                    task.getId(),
                    newTitle,
                    newDescription,
                    newCategory,
                    newPriority,
                    newDueDate,
                    selectedStatus
                );
                updateTaskTable(taskService.getTasks());
                dialog.close();
            }
        });

        dialogVBox.getChildren().addAll(
            new Label("Title:"), titleField,
            new Label("Description:"), descriptionField,
            new Label("Category:"), categoryDropdown,
            new Label("Priority:"), priorityDropdown,
            new Label("Notifications:"), notificationsBox, addNotificationButton,
            new Label("Due Date:"), dueDatePicker,
            new Label("Status:"), statusDropdown,
            saveButton
        );

        ScrollPane scrollPane = new ScrollPane(dialogVBox);
        scrollPane.setFitToWidth(true);
        Scene dialogScene = new Scene(scrollPane, 400, 600);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Displays the update category dialog for updating a category.
     * 
     * @param category the category to be updated
     */
    private void showUpdateCategoryDialog(Category category) {
        Stage dialog = new Stage();
        dialog.setTitle("Update Category");
        VBox dialogVBox = new VBox(10);
        dialogVBox.setStyle("-fx-padding: 20;");

        // Name Field
        TextField nameField = new TextField(category.getName());
        nameField.setPromptText("Enter category name");

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String newName = nameField.getText();
            if (newName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Name is required!");
                alert.show();
            }
            else if (category.getName().equals(newName)) {
                dialog.close();
            }
            else {
                try {
                    categoryService.updateCategory(category.getId(), newName);
                    updateCategoryTable(categoryService.getCategories());
                    dialog.close();
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update category: " + ex.getMessage());
                    alert.show();
                }
            }
        });

        dialogVBox.getChildren().addAll(
            new Label("Name"), nameField,
            saveButton
        );

        ScrollPane scrollPane = new ScrollPane(dialogVBox);
        scrollPane.setFitToWidth(true);
        Scene dialogScene = new Scene(scrollPane, 250, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Displays the update priority dialog for updating a priority.
     * 
     * @param priority the priority to be updated
     */
    private void showUpdatePriorityDialog(Priority priority) {
        Stage dialog = new Stage();
        dialog.setTitle("Update Category");
        VBox dialogVBox = new VBox(10);
        dialogVBox.setStyle("-fx-padding: 20;");

        // Name Field
        TextField nameField = new TextField(priority.getName());
        nameField.setPromptText("Enter priority name");

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String newName = nameField.getText();
            if (newName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Name is required!");
                alert.show();
            }
            else if(priority.getName().equals(newName)) {
                dialog.close(); 
            }
            else {
                try {
                    priorityService.updatePriority(priority.getId(), newName);
                    updatePriorityTable(priorityService.getPriorities());
                    dialog.close(); 
                } catch (Exception ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update priority: " + ex.getMessage());
                    alert.show();
                }
            }
        });
        dialogVBox.getChildren().addAll(
            new Label("Name"), nameField,
            saveButton
        );

        ScrollPane scrollPane = new ScrollPane(dialogVBox);
        scrollPane.setFitToWidth(true);
        Scene dialogScene = new Scene(scrollPane, 250, 150);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Updates the task table with a given list of tasks.
     * This method refreshes the table to display the latest info.
     *
     * @param tasks the list of tasks to be displayed in the task table
     */
    private void updateTaskTable(List<Task> tasks) {
        ObservableList<Task> taskList = FXCollections.observableArrayList(tasks);
        taskTableView.setItems(taskList);
        taskTableView.refresh();
    }

    /**
     * Updates the priority table with a given list of priorities.
     * This method refreshes the table to display the latest info.
     *
     * @param priorities the list of priorities to be displayed in the priority table
     */
    private void updatePriorityTable(List<Priority> priorities) {
        ObservableList<Priority> priorityList = FXCollections.observableArrayList(priorities);
        priorityTableView.setItems(priorityList);
        priorityTableView.refresh();
    }

    /**
     * Updates the category table with a given list of categories.
     * This method refreshes the table to display the latest info.
     *
     * @param categories the list of categories to be displayed in the category table
     */
    private void updateCategoryTable(List<Category> categories) {
        ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);
        categoryTableView.setItems(categoryList);
        categoryTableView.refresh();
    }

    /**
     * Displays a popup notification if there are delayed tasks, during the application startup.
     * <p>The popup contains the number of delayed tasks.</p>
     */
    private void showDelayedTasksPopup() {
        List<Task> delayedTasks = taskService.getTasksByStatus(Task.STATUS_DELAYED);
    
        if (!delayedTasks.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Delayed Tasks Notification");
            alert.setHeaderText("You have delayed tasks!");
            alert.setContentText("There are " + delayedTasks.size() + " delayed task(s) in your task list.");
            alert.showAndWait();
        }
    }

    /**
     * Main entry point for the application.
     * Launches the JavaFX application.
     *
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
