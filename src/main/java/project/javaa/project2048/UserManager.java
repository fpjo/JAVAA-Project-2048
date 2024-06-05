package project.javaa.project2048;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private Map<String, String> userData = new HashMap<>();
    private Path path;

    public UserManager() {
        loadUserDataFromFile();
    }

    private void loadUserDataFromFile() {
        path = Paths.get("").resolve("src/main/resources/project/javaa/project2048/data.dat");

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileInputStream fis = new FileInputStream(path.toAbsolutePath().toString());
            ObjectInputStream ois = new ObjectInputStream(fis);
            userData = (Map<String, String>) ois.readObject();
            ois.close();
            fis.close();
        } catch (EOFException e) {
            register("Administrator", "admin");
            saveUserDataToFile();
            loadUserDataFromFile();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveUserDataToFile() {
        try {
            FileOutputStream fos = new FileOutputStream(path.toAbsolutePath().toString());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(userData);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean register(String username, String password) {
        for (Map.Entry<String, String> entry : userData.entrySet()) {
            if (entry.getKey().equals(username)) {
                System.out.println(username+" "+password+" "+entry.getKey()+" "+entry.getValue());
                return false;
            }
        }

        userData.put(username, password);
        saveUserDataToFile();

        return true;
    }
    //如果当前注册的用户名与已有用户重复则返回false，否则注册成功返回true

    public boolean login(String username, String password) {
        for (Map.Entry<String, String> entry : userData.entrySet()) {
            if (entry.getKey().equals(username) && entry.getValue().equals(password)) {
                return true;
            }
        }

        return false;
    }
    //登录成功（账号密码匹配）返回true，否则返回false
}
