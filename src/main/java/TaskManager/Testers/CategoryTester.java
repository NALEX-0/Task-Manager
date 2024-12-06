package TaskManager.Testers;

import java.util.ArrayList;
import java.util.List;

import TaskManager.CategoryService;

public class CategoryTester {
    public static void main(String[] args) {
        // Test Categories
        CategoryService categoryService = new CategoryService();
        List<Integer> createdCategoryIds = new ArrayList<>();
        System.out.println("---Test Categories---");
        try {
            // 1. Αdd Categories
            categoryService.addCategory("TC-Work");
            categoryService.addCategory("TC-Personal");
            createdCategoryIds.add(categoryService.getCategoryByName("TC-Work").getId());
            createdCategoryIds.add(categoryService.getCategoryByName("TC-Personal").getId());

            System.out.println("Categories after addition:");
            categoryService.getCategories().forEach(System.out::println);

            // 2. Update Categories
            categoryService.updateCategory(createdCategoryIds.get(0), "TC-Office");

            System.out.println("\nCategories after update:");
            categoryService.getCategories().forEach(System.out::println);

            // 3. Delete Categories
            categoryService.deleteCategory(createdCategoryIds.get(1));

            System.out.println("\nCategories after deletion:");
            categoryService.getCategories().forEach(System.out::println);

        } catch (Exception e) {
            System.err.println("Error during testing categories: " + e.getMessage());
        } finally {
            // Delete testing data
            for (int id : createdCategoryIds) {
                try {
                    categoryService.deleteCategory(id);
                } catch (Exception ignored) {
                }
            }
        }
    }
}
