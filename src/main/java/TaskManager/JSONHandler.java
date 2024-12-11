package TaskManager;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


/**
 * Handles JSON file operations.
 * 
 * <p>This class provides functionality to read and write data to JSON files, such as 
 * Category, Priority, Task, and User.</p>
 */
public class JSONHandler implements Serializable {
    // private static final String DATA_DIR = "task_manager/medialab/";
    private static final String DATA_DIR = "C:/dev/code/Java/multimedia_project/task_manager/src/main/resources/medialab/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Initialise directory if it doesnt exist
    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Read data from JSON file
     *  
     * @param <T> the type of object: Category, Priority, Task, User
     * @param fileName the name of the JSON file to read the data from
     * @param clazz the object class: Category, Priority, Task, User
     * @return a List with type T
     */
    public static <T> List<T> readData(String fileName, Class<T> clazz) {
        File file = new File(DATA_DIR + fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
            return objectMapper.readValue(file, listType);
        } catch (IOException e) {
            System.err.println("Error reading data from " + fileName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Write a list of objects to JSON file
     *  
     * @param <T> the type of objects in the list: Category, Priority, Task, User
     * @param fileName the name of the JSON file to write the data to
     * @param data the list of objects of type T to be written to the file 
     */
    public static <T> void writeData(String fileName, List<T> data) {
        File file = new File(DATA_DIR + fileName);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.err.println("Error writing data to " + fileName + ": " + e.getMessage());
        }
    }

}

