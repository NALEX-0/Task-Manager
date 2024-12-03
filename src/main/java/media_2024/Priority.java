package media_2024;

public class Priority {
    private int id;
    private String name;

    // Default priority
    public static final Priority DEFAULT = new Priority(1, "Default");

    // Constructor
    public Priority(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public int getId() { 
        return id; 
    }
    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    @Override
    public String toString() {
        return "Priority{id=" + id + ", name='" + name + "'}";
    }
}

