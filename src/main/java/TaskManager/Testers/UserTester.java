// Test User and userService

package TaskManager.Testers;

import TaskManager.UserService;

public class UserTester {
    public static void main(String[] args) {
        UserService userService = new UserService();
        System.out.println("---Test Users---");
        try {
            // 1. Add users
            userService.addUser("nalex", "password123", "nikos", "alex", "nalex@example.com");
            userService.addUser("janedoe", "password456", "Jane", "Doe", "jane.doe@example.com");

            System.out.println("Users after addition:");
            userService.getAllUsers().forEach(System.out::println);

            System.out.println("\nGet User by username:");
            System.out.println(userService.getUserByUsername("nalex"));

            // 2. Update User
            userService.updateUser("nalex", "password4242", "nikos", "alex", "nalex@example.com");

            System.out.println("\nUsers after update:");
            userService.getAllUsers().forEach(System.out::println);

            // 3. Login
            boolean loginSuccess = userService.verifyLogin("nalex", "password4242");
            System.out.println("\nLogin success for nalex: " + loginSuccess);

            // 4. Delete User
            userService.deleteUser("janedoe");

            System.out.println("\nUsers after deletion:");
            userService.getAllUsers().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            // Delete testing data
            try {
                userService.deleteUser("nalex");
            } catch (Exception ignored) {
            }
        }
    }
}
