package com.olmolkov.oopfm.service;

import com.olmolkov.oopfm.model.Category;
import com.olmolkov.oopfm.model.Transaction;
import com.olmolkov.oopfm.model.User;
import com.olmolkov.oopfm.repository.CategoryRepository;
import com.olmolkov.oopfm.repository.WalletRepository;
import com.olmolkov.oopfm.validator.DataValidator;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BudgetService {
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;

    public BudgetService(WalletRepository walletRepository, CategoryRepository categoryRepository) {
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
    }

    // Метод добавления новой категории
    public void addCategory(User user, String categoryName, double budget) {
        validateCategoryName(categoryName);
        validateBudget(budget);

        Category existingCategory = categoryRepository.findCategoryByName(user.getUsername(), categoryName);
        if (existingCategory != null) {
            throw new IllegalArgumentException("Категория с таким названием уже существует");
        }

        Category newCategory = new Category(user.getUsername(), categoryName, budget);
        List<Category> categories = categoryRepository.loadCategories();
        categories.add(newCategory);
        categoryRepository.saveCategories(categories);

        System.out.println("Категория успешно добавлена");
    }

    // Метод изменения имени категории
    public void changeCategoryName(User user, String oldCategoryName, String newCategoryName) {
        try {
            validateCategoryName(newCategoryName);

            Category userCategory = categoryRepository.findCategoryByName(user.getUsername(), oldCategoryName);
            if (userCategory == null) {
                throw new IllegalArgumentException("Категория с таким именем не найдена");
            }

            userCategory.setName(newCategoryName);
            categoryRepository.saveCategories(categoryRepository.loadCategories());

            System.out.println("Имя категории успешно изменено");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при изменении имени категории: " + e.getMessage());
        }
    }

    // Метод для обновления бюджета категории
    public void updateCategoryBudget(User user, String categoryName, double newBudget) {
        try {
            validateBudget(newBudget);

            List<Category> userCategories = categoryRepository.loadCategories();
            Category userCategory = categoryRepository.findCategoryByName(user.getUsername(), categoryName);
            if (userCategory == null) {
                throw new IllegalArgumentException("Категория с таким именем не найдена");
            }

            userCategory.setBudget(newBudget);
            categoryRepository.saveCategories(userCategories);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при обновлении бюджета категории: " + e.getMessage());
        }
    }

    // Метод вывода списка всех категорий пользователя
    public void printAllCategories(User user) {
        try {
            List<Category> categories = categoryRepository.findCategoriesByUserId(user.getUsername());
            if (categories.isEmpty()) {
                System.out.println("У пользователя нет категорий");
                return;
            }

            System.out.println("Список категорий пользователя:");
            categories.forEach(category -> {
                System.out.println("Имя категории: " + category.getName());
                System.out.println("Бюджет категории: " + category.getBudget());
                System.out.println("----------------------------");
            });
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при выводе списка категорий: " + e.getMessage());
        }
    }

    // Метод подсчета бюджета по категории
    public void calculateBudget(User user) {
        Map<String, Double> expensesByCategory = walletRepository.findWalletsByUserId(user.getUsername()).stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        List<Category> userCategories = categoryRepository.findCategoriesByUserId(user.getUsername());

        System.out.println("Состояние бюджета по категориям:");
        userCategories.forEach(category -> {
            double expenses = Math.abs(expensesByCategory.getOrDefault(category.getName(), 0.0));
            double remainingBudget = category.getBudget() - expenses;

            System.out.printf("- %s: Лимит: %.2f, Расходы: %.2f, Остаток: %.2f%n",
                    category.getName(), category.getBudget(), expenses, remainingBudget);
        });
    }

    // Метод проверки превышения бюджета для категорий пользователя
    public List<String> checkBudget(User user) {
        Map<String, Double> expensesByCategory = calculateExpensesByCategory(user);
        List<Category> userCategories = categoryRepository.findCategoriesByUserId(user.getUsername());

        return userCategories.stream()
                .filter(category -> {
                    double expenses = Math.abs(expensesByCategory.getOrDefault(category.getName(), 0.0));
                    return expenses > category.getBudget();
                })
                .map(category -> "Лимит превышен для категории: " + category.getName())
                .toList();
    }

    // Метод для подсчета расходов по категориям пользователя
    private Map<String, Double> calculateExpensesByCategory(User user) {
        return walletRepository.findWalletsByUserId(user.getUsername()).stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .filter(transaction -> transaction.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCategory().getName(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));
    }

    // Метод проверки корректности имени категории
    private void validateCategoryName(String categoryName) {
        if (!DataValidator.isNonEmptyString(categoryName)
                || !DataValidator.isStringLenghtValid(categoryName, 20)) {
            throw new IllegalArgumentException("Некорректное имя категории");
        }
    }


    // Метод проверки корректности бюджета
    private void validateBudget(double budget) {
        if (!DataValidator.isPositiveNumber(String.valueOf(budget))
                || !DataValidator.isValidRange(budget, 1, 1000000)) {
            throw new IllegalArgumentException("Некорректная сумма бюджета");
        }
    }
}
