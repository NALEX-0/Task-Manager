package media_2024;

import java.io.IOException;
import java.io.Serializable;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


class User implements Serializable {
    private String Username;
    private String Password;
    private String firstName;
    private String lastName;
    private String email;

    // Constructor
    public User(String username, String password, String firstName, String lastName, String mail) {
        this.Username = username;
        this.Password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = mail;
    }

    // Username only constructor
    public User(String username) {
        this.Username = username;
    }

    // Getters
    public String getUsername() {
        return Username;
    }
    public String getPassword() {
        return Password;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }

    // Setters (for modifying the fields if need it)
    public void setPassword(String password) {
        this.Password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}


class Priority {
    
}


class Category {

}


class Jobs {

}





/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}