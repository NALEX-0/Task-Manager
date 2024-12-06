package TaskManager;

import java.io.Serializable;

public class User implements Serializable {
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

    // Getters and Setters
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

    @Override
    public String toString() {
        return "User{" + "Username='" + Username + '\'' + ", Password='" + Password + '\'' + ", FirstName='" + firstName + '\'' + ", LastName='" + lastName + '\'' + ", Email='" + email + '\'' + '}';
    }

}
