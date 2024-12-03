package media_2024;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryService implements Serializable {
    private List<Category> categories = new ArrayList<>();
    private static final String CATEGORIES_FILE = "Category.ser";
    private int nextId = 1;

    public CategoryService() {
        categories = JSONHandler.readData(CATEGORIES_FILE, Category.class);
        categories.forEach(category -> nextId = Math.max(nextId, category.getId() + 1));
    }

    public void addCategory(String name) {
        if (categories.stream().anyMatch(cat -> cat.getName().equalsIgnoreCase(name))) {
            throw new IllegalArgumentException("Category already exists.");
        }
        categories.add(new Category(nextId++, name));
        saveCategories();
    }

    public void updateCategory(int id, String newName) {
        Category category = categories.stream()
                .filter(cat -> cat.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found."));
        category.setName(newName);
        saveCategories();
    }

    public void deleteCategory(int id) {
        categories.removeIf(cat -> cat.getId() == id);
        saveCategories();
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    private void saveCategories() {
        JSONHandler.writeData(CATEGORIES_FILE, categories);
    }
}

