package media_2024;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


public class JSONHandler implements Serializable {
    private static final String DATA_DIR = "media_2024/medialab/";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Initialise directory if it doesnt exist
    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
        objectMapper.registerModule(new JavaTimeModule());
    }

    // Read data from JSON file
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

    // Write data to JSON file
    public static <T> void writeData(String fileName, List<T> data) {
        File file = new File(DATA_DIR + fileName);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
        } catch (IOException e) {
            System.err.println("Error writing data to " + fileName + ": " + e.getMessage());
        }
    }

}

