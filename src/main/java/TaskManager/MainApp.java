package TaskManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    private TaskService taskService = new TaskService();

    private Label totalTasksLabel = new Label("Total Tasks: 0");
    private Label completedTasksLabel = new Label("Completed Tasks: 0");
    private Label delayedTasksLabel = new Label("Delayed Tasks: 0");
    private Label tasksDueIn7DaysLabel = new Label("Tasks Due in 7 Days: 0");

    @Override
    public void start(Stage primaryStage) {
        // Ορισμός τίτλου παραθύρου
        primaryStage.setTitle("MediaLab Assistant");

        // Ορισμός εικονιδίου παραθύρου
        Image icon = new Image(getClass().getResourceAsStream("/icons/java.png"));
        primaryStage.getIcons().add(icon);
        
        // Δημιουργία του επάνω μέρους για συγκεντρωτικές πληροφορίες
        HBox topPane = new HBox(20); // 20 είναι το spacing μεταξύ των στοιχείων
        topPane.getChildren().addAll(totalTasksLabel, completedTasksLabel, delayedTasksLabel, tasksDueIn7DaysLabel);
        topPane.setStyle("-fx-padding: 10; -fx-alignment: center;");
        totalTasksLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;");
        completedTasksLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;");
        delayedTasksLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;");
        tasksDueIn7DaysLabel.setStyle("-fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 10; -fx-background-radius: 10;");
        totalTasksLabel.getStyleClass().addAll("lbl", "lbl-default");
        completedTasksLabel.getStyleClass().addAll("lbl", "lbl-success");
        delayedTasksLabel.getStyleClass().addAll("lbl", "lbl-danger");
        tasksDueIn7DaysLabel.getStyleClass().addAll("lbl", "lbl-warning");


        // Δημιουργία κουμπιού για παράδειγμα (κάτω μέρος)
        Button refreshButton = new Button("Refresh Information");
        refreshButton.setOnAction(e -> updateSummaryInfo());

        VBox bottomPane = new VBox(10);
        bottomPane.getChildren().add(refreshButton);

        // Ρύθμιση διάταξης σε BorderPane
        BorderPane root = new BorderPane();
        root.setTop(topPane);
        root.setBottom(bottomPane);

        // Δημιουργία σκηνής με διαστάσεις 800x600
        Scene scene = new Scene(root, 1000, 800);           // Default: 800, 600
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        // Ορισμός της σκηνής στο παράθυρο
        primaryStage.setScene(scene);

        // Ενημέρωση αρχικών δεδομένων
        updateSummaryInfo();

        // Εμφάνιση του παραθύρου
        primaryStage.show();
    }

    private void updateSummaryInfo() {
        // Υπολογισμός πληροφοριών
        int totalTasks = taskService.getTasks().size();
        int completedTasks = taskService.getTasksByStatus(Task.STATUS_COMPLETED).size();
        int delayedTasks = taskService.getTasksByStatus(Task.STATUS_DELAYED).size();
        int tasksDueIn7Days = taskService.getTasksDueIn7Days().size();

        // Ενημέρωση Labels
        totalTasksLabel.setText("Total Tasks: " + totalTasks);
        completedTasksLabel.setText("Completed Tasks: " + completedTasks);
        delayedTasksLabel.setText("Delayed Tasks: " + delayedTasks);
        tasksDueIn7DaysLabel.setText("Tasks Due in 7 Days: " + tasksDueIn7Days);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
