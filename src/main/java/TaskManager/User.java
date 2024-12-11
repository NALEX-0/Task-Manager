package TaskManager;

import java.io.Serializable;


/**
 * Represents a user.
 * 
 * <p>A user has a username, password, first name, last name, and email address. 
 */
public class User implements Serializable {
    private String Username;
    private String Password;
    private String firstName;
    private String lastName;
    private String email;

    /**
     * Constructs a new User with all attributes.
     * 
     * @param username  the username of the user
     * @param password  the password of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email address of the user
     */
    public User(String username, String password, String firstName, String lastName, String email) {
        this.Username = username;
        this.Password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * Constructs a new User with only a username.
     * 
     * @param username the username of the user
     */
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

    /**
     * Returns a string representation of the user.
     * 
     * @return a string representation of the user
     */
    @Override
    public String toString() {
        return "User{" + "Username='" + Username + '\'' + ", Password='" + Password + '\'' + ", FirstName='" + firstName + '\'' + ", LastName='" + lastName + '\'' + ", Email='" + email + '\'' + '}';
    }

}
