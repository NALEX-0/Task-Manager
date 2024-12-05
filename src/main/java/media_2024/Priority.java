package media_2024;

import java.io.Serializable;

public class Priority implements Serializable {
    private int id;             // Unique identifier
    private String name;        // Priority name (e.g., High, Medium)

    // Constructor
    public Priority(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Priority{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
