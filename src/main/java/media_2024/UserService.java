package media_2024;

import java.util.List;

public class UserService {
    private List<User> users;
    private static final String USERS_FILE = "Users.json";

    public UserService() {
        this.users = JSONHandler.readData(USERS_FILE, User.class);
    }

    // Add User
    public void addUser(String username, String password, String firstName, String lastName, String email) {
        if (getUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }
        User user = new User(username, password, firstName, lastName, email);
        users.add(user);
        saveUsers();
    }

    // Update User
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

    // Delete User
    public void deleteUser(String username) {
        users.removeIf(user -> user.getUsername().equals(username));
        saveUsers();
    }

    // Return User by username
    public User getUserByUsername(String username) {
        return users.stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }

    // Return all Users
    public List<User> getAllUsers() {
        return this.users;
    }

    // Varify login
    public boolean verifyLogin(String username, String password) {
        User user = getUserByUsername(username);
        return user != null && user.getPassword().equals(password);
    }

    // Save Users
    private void saveUsers() {
        JSONHandler.writeData(USERS_FILE, users);
    }
}
