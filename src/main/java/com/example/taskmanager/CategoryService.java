package com.example.taskmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Provides services for managing categories.
 * 
 * <p>Allows adding, updating, deleting, and retrieving categories.</p>
 */
public class CategoryService implements Serializable {
    TaskService taskService = new TaskService();

    private List<Category> categories = new ArrayList<>();
    private static final String CATEGORIES_FILE = "Category.json";
    private int nextId = 1; // Keeps track of the next available ID

    /**
     * Constructs a new CategoryService instance and loads categories from the JSON storage.
     * Initializes the next ID based on the highest existing category ID.
     * Because 'nextId' is 1 when Categories are loaded from JSON. 
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
     * @throws IllegalArgumentException if the category does not exist, or if a category
     *                                  with the new name already exists                       
     */
    public void updateCategory(int id, String newName) {
        Category category = getCategoryById(id);
        if (categories.stream().anyMatch(cat -> cat.getName().equals(newName))) {
            throw new IllegalArgumentException("Category already exists.");
        }

        category.setName(newName);
        saveCategories();
        
        List<Task> tasksToUpdate = taskService.getTasks().stream()
        .filter(task -> task.getCategory().getId() == id)
        .collect(Collectors.toList());

        for (Task task : tasksToUpdate) {
            taskService.updateTask(task.getId(), task.getTitle(), task.getDescription(),
                            category, task.getPriority(), task.getDueDate(), task.getStatus());
        }
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

        List<Task> tasksToDelete = taskService.getTasks().stream()
        .filter(task -> task.getCategory().getId() == id)
        .collect(Collectors.toList());

        for (Task task : tasksToDelete) {
            taskService.deleteTask(task.getId());
        }
        
        categories.remove(category);
        saveCategories();
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
     * Gets the last added category
     * <p>Used in MainApp (GUI)</p>
     * 
     * @return the last category
     */
    public Category getLastCategory() {
        return getCategoryById(nextId - 1);
    }
    
    /**
     * Saves the current list of categories to JSON storage.
     * <p>This method is private and intended for internal use only.</p>
     */
    private void saveCategories() {
        JSONHandler.writeData(CATEGORIES_FILE, categories);
    }
}

