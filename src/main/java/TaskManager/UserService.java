package TaskManager;

import java.util.List;


/**
 * Provides services for user operations.
 * 
 * <p>Allows adding, updating, deleting, and retrieving users.
 * It also provides functionality to verify user login credentials.</p>
 */
public class UserService {
    private List<User> users;
    private static final String USERS_FILE = "Users.json";

    /**
     * Constructs a new UserService and loads users from JSON storage.
     */
    public UserService() {
        this.users = JSONHandler.readData(USERS_FILE, User.class);
    }

    /**
     * Adds a new user with the specified attributes.
     * 
     * @param username  the username of the user
     * @param password  the password of the user
     * @param firstName the first name of the user
     * @param lastName  the last name of the user
     * @param email     the email address of the user
     * @throws IllegalArgumentException if the username already exists
     */
    public void addUser(String username, String password, String firstName, String lastName, String email) {
        if (getUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }
        User user = new User(username, password, firstName, lastName, email);
        users.add(user);
        saveUsers();
    }

    /**
     * Updates the details of an existing user.
     * 
     * @param username  the username of the user to update
     * @param password  the new password for the user
     * @param firstName the new first name for the user
     * @param lastName  the new last name for the user
     * @param email     the new email address for the user
     * @throws IllegalArgumentException if the user does not exist
     */
    public void updateUser(String username, String password, String firstName, String lastName, String email) {
        User user = getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        saveUsers();
    }

    /**
     * Deletes a user identified by its username.
     * 
     * @param username the username of the user to delete
     */
    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
        saveUsers();
    }

    /**
     * Gets a user by username.
     * 
     * @param username the username of the user to retrieve
     * @return the user with the specified username, or null if no such user exists
     */
    public User getUserByUsername(String username) {
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

    /**
     * Gets all users.
     * 
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return this.users;
    }

    /**
     * Verifies login credentials for a user.
     * 
     * @param username the username of the user
     * @param password the password of the user
     * @return true if the credentials are valid, false otherwise
     */
    public boolean verifyLogin(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * Saves the current list of users to JSON storage.
     * <p>This method is private and intended for internal use only.</p>
     */
    private void saveUsers() {
        JSONHandler.writeData(USERS_FILE, users);
    }
}
