package project.javaa.project2048;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, String> userData = new HashMap<>();
    private String path;

    public UserManager() {
        loadUserDataFromFile();
    }

    private void loadUserDataFromFile() {
        if (!Files.exists(Paths.get("").getParent().getParent().resolve("resources/project.javaa.project2048/data.dat"))) {
            try {
                Files.createFile(Paths.get("").getParent().getParent().resolve("resources/project.javaa.project2048/data.dat"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        path = Paths.get("").getParent().getParent().resolve("resources/project.javaa.project2048/data.dat").toString();

        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            userData = (Map<String, String>) ois.readObject();
            ois.close();
            fis.close();

            System.out.println(userData);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveUserDataToFile() {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userData);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(String username, String password) {
        userData.put(username, password);
        saveUserDataToFile();
    }

    public boolean login(String username, String password) {
        for (Map.Entry<String, String> entry : userData.entrySet()) {
            if (entry.getKey().equals(username) && entry.getValue().equals(password)) {
                return true;
            }
        }

        return false;
    }
}
