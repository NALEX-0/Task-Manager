package media_2024;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JSONHandler implements Serializable {
    // private static final String DATA_DIR = "C:/dev/code/Java/multimedia_project/media_2024/src/main/java/media_2024/medialab/";
    private static final String DATA_DIR = "media_2024/medialab/";

    // Αρχικοποίηση φακέλου medialab
    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    // Ανάγνωση δεδομένων από αρχείο .ser
    public static <T> List<T> readData(String fileName, Class<T> clazz) {
        File file = new File(DATA_DIR + fileName);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading data from " + fileName + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Εγγραφή δεδομένων σε αρχείο .ser
    public static <T> void writeData(String fileName, List<T> data) {
        File file = new File(DATA_DIR + fileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (IOException e) {
            System.err.println("Error writing data to " + fileName + ": " + e.getMessage());
        }
    }
}

