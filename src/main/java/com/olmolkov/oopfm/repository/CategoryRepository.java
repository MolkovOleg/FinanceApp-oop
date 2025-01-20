package com.olmolkov.oopfm.repository;

import com.olmolkov.oopfm.model.Category;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CategoryRepository extends FileRepository {
    private static final String FILE_PATH = "src/main/resources/data/categories/categories.json";

    public CategoryRepository() {
        ensureDirectoryExists();
        ensureFileExists();
    }

    // Метод проверки существования директории и его создания
    private void ensureDirectoryExists() {
        File directory = new File("src/main/resources/data/categories");
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
                saveCategories(new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла категорий: " + e.getMessage());
        }
    }

    // Метод сохранения категорий в Json-файл
    public void saveCategories(List<Category> categories) {
        try {
            saveDataToFile(FILE_PATH, categories);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении категорий: " + e.getMessage());
        }
    }

    // Метод загрузки категорий из Json-файла
    public List<Category> loadCategories() {
        try {
            return loadDataFromFile(FILE_PATH, Category.class);
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке категорий: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Метод поиска категорий для указанного пользователя
    public List<Category> findCategoriesByUserId(String username) {
        List<Category> allCategories = loadCategories();

        return allCategories.stream()
                .filter(category -> category.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    // Метод поиска по названию и username
    public Category findCategoryByName(String username, String categoryName) {
        List<Category> allCategories = loadCategories();
        Optional<Category> category = allCategories.stream()
                .filter(c -> c.getUsername().equals(username) && c.getName().equals(categoryName))
                .findFirst();
        return category.orElse(null);
    }

}
