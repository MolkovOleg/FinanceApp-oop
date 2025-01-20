package com.olmolkov.oopfm.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class FileRepository {
    protected final ObjectMapper objectMapper;

    public FileRepository() {
        objectMapper = new ObjectMapper(); // создаем экземпляр класса
        objectMapper.registerModule(new JavaTimeModule()); // модуль для преобразования дат
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // модуль для более читаемого вывода
    }

    // Метод сохранения данных в Json-файл
    protected <T> void saveDataToFile(String filePath, List<T> data) throws IOException {
        try {
            objectMapper.writeValue(new File(filePath), data);
            System.out.println("Данные успешно сохранены в: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении данных: " + e.getMessage());
            throw e;
        }
    }

    // Метод загрузки данных из Json-файла
    protected <T> List<T> loadDataFromFile(String filePath, Class<T> type) throws IOException {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }
        return objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    }
}
