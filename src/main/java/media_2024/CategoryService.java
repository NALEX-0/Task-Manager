package media_2024;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryService implements Serializable {
    private List<Category> categories = new ArrayList<>();
    private static final String CATEGORIES_FILE = "Category.ser";
    private int nextId = 1; // Keeps track of the next available ID

    public CategoryService() {
        categories = JSONHandler.readData(CATEGORIES_FILE, Category.class);
        // Initialize nextId based on existing ID
        categories.forEach(category -> nextId = Math.max(nextId, category.getId() + 1));
    }

    // Adds new Category if it doesn't already exist
    public void addCategory(String name) {
        if (categories.stream().anyMatch(cat -> cat.getName().equals(name))) {
            throw new IllegalArgumentException("Category already exists.");
        }
        categories.add(new Category(nextId++, name));
        saveCategories();
    }

    // Updates the name of the Category with the specified id 
    public void updateCategory(int id, String newName) {
        Category category = getCategoryById(id);
        category.setName(newName);
        saveCategories();
        
        // Replace updated Category in related tasks
        TaskService.updateTasksWithChangedCategory(category);
    }

    // Deletes Category with the specified id, also deletes all Tasks in this Category
    public void deleteCategory(int id) {
        Category category = getCategoryById(id);
        categories.remove(category);
        saveCategories();
    
        // Delete all Tasks in this Category
        TaskService.deleteTasksByCategory(category);
    }
    
    // Return List of Categories
    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    // Return Category with specified id
    public Category getCategoryById(int id) {
        return categories.stream()
            .filter(cat -> cat.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Category not found."));
    }
    // Return Category with specified name
    public Category getCategoryByName(String name) {
        return categories.stream()
            .filter(cat -> cat.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Category not found."));
    }
    
    // Save Categories
    private void saveCategories() {
        JSONHandler.writeData(CATEGORIES_FILE, categories);
    }
}

