package com.example.taskmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Provides services for managing categories.
 * 
 * <p>Allows adding, updating, deleting, and retrieving categories.</p>
 */
public class CategoryService implements Serializable {
    private List<Category> categories = new ArrayList<>();
    private static final String CATEGORIES_FILE = "Category.json";
    private int nextId = 1; // Keeps track of the next available ID

    /**
     * Constructs a new CategoryService instance and loads categories from the JSON storage.
     * Initializes the next ID based on the highest existing category ID.
     */
    public CategoryService() {
        categories = JSONHandler.readData(CATEGORIES_FILE, Category.class);
        // Initialize nextId based on existing ID
        categories.forEach(category -> nextId = Math.max(nextId, category.getId() + 1));
    }

    /**
     * Adds a new category with the specified name, if it does not already exist.
     * 
     * @param name the name of the new category
     * @throws IllegalArgumentException if a category with the same name already exists
     */
    public void addCategory(String name) {
        if (categories.stream().anyMatch(cat -> cat.getName().equals(name))) {
            throw new IllegalArgumentException("Category already exists.");
        }
        categories.add(new Category(nextId++, name));
        saveCategories();
    }

    /**
     * Updates the name of an existing category identified by its ID.
     * Also updates related tasks to reflect the new category name.
     * 
     * @param id the ID of the category to update
     * @param newName the new name for the category
     * @throws IllegalArgumentException if the category with the specified ID does not exist
     */
    public void updateCategory(int id, String newName) {
        Category category = getCategoryById(id);
        category.setName(newName);
        saveCategories();
        
        // Replace updated Category in related tasks
        TaskService.updateTasksWithChangedCategory(category);
    }

    /**
     * Deletes a category identified by its ID. 
     * Also deletes all tasks associated with the category.
     * 
     * @param id the ID of the category to delete
     * @throws IllegalArgumentException if the category with the specified ID does not exist
     */
    public void deleteCategory(int id) {
        Category category = getCategoryById(id);
        categories.remove(category);
        saveCategories();
    
        // Delete all Tasks in this Category
        TaskService.deleteTasksByCategory(category);
    }
    
    /**
     * Gets all categories.
     * 
     * @return a list of all categories
     */
    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    /**
     * Get a category by its ID.
     * 
     * @param id the ID of the category to get
     * @return the category with the specified ID
     * @throws IllegalArgumentException if the category with the specified ID does not exist
     */
    public Category getCategoryById(int id) {
        return categories.stream()
            .filter(cat -> cat.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Category not found."));
    }

    /**
     * Gets a category by its name.
     * 
     * @param name the name of the category to get
     * @return the category with the specified name
     * @throws IllegalArgumentException if the category with the specified name does not exist
     */
    public Category getCategoryByName(String name) {
        return categories.stream()
            .filter(cat -> cat.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Category not found."));
    }
    
    /**
     * Saves the current list of categories to JSON storage.
     * <p>This method is private and intended for internal use only.</p>
     */
    private void saveCategories() {
        JSONHandler.writeData(CATEGORIES_FILE, categories);
    }
}

