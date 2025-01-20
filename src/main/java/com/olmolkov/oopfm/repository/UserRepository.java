package com.olmolkov.oopfm.repository;

import com.olmolkov.oopfm.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository extends FileRepository {
    private static final String FILE_PATH = "src/main/resources/data/users/users.json";

    public UserRepository() {
        ensureDirectoryExists();
        ensureFileExists();
    }

    // Метод проверки существования директории и его создания
    private void ensureDirectoryExists() {
        File directory = new File("src/main/resources/data/users");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    // Метод проверки существования Json-файла и его создания
    private void ensureFileExists() {
        File file = new File(FILE_PATH);
        try {
            if (!file.exists() || file.length() == 0) {
                file.createNewFile();
                saveUsers(new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла пользователей: " + e.getMessage());
        }
    }

    // Метод сохранения данных пользователей в Json-файл
    public void saveUsers(List<User> users) {
        try {
            saveDataToFile(FILE_PATH, users);
            System.out.println("Данные пользователй успешно сохранены");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении пользователей: " + e.getMessage());
        }
    }

    // Метод загрузки пользователей из Json-файла
    public List<User> loadUsers() {
        try {
            List<User> users = loadDataFromFile(FILE_PATH, User.class);
            if (users == null) {
                users = new ArrayList<>();
            }
            return users;
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке пользователей: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Метод поиска пользователя по логину
    public User findByUsername(String username) {
        return loadUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
}

