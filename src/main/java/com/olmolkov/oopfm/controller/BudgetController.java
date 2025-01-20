package com.olmolkov.oopfm.controller;

import com.olmolkov.oopfm.model.User;
import com.olmolkov.oopfm.service.BudgetService;

import javax.swing.*;
import java.util.Scanner;

public class BudgetController {
    private final BudgetService budgetService;
    private final User user;
    private final Scanner scanner;

    public BudgetController(BudgetService budgetService, User user, Scanner scanner) {
        this.budgetService = budgetService;
        this.user = user;
        this.scanner = scanner;
    }

    // Меню управления категориями и бюджетами
    public void menu() {
        System.out.println("Меню управления категориями и бюджетами:");
        while (true) {
            System.out.println("1. Добавить категорию");
            System.out.println("2. Переименовать категорию");
            System.out.println("3. Обновить лимит категории");
            System.out.println("4. Вывести все категории");
            System.out.println("5. Подсчет бюджета по категориям");
            System.out.println("6. В главное меню");

            try {
                String operation = scanner.nextLine();
                switch (operation) {
                    case "1" -> addCategory();
                    case "2" -> renameCategory();
                    case "3" -> updateLimit();
                    case "4" -> showAllCategories();
                    case "5" -> calculateBudgetOnCategory();
                    case "6" -> {
                        System.out.println("Выход в главное меню...");
                        return;
                    }
                    default -> System.out.println("Неверная операция. Попробуйте ещё раз.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void addCategory() {
        try {
            System.out.print("Введите название категории: ");
            String categoryName = scanner.nextLine();

            System.out.print("Введите лимит категории: ");
            double limit = Double.parseDouble(scanner.nextLine());

            budgetService.addCategory(user, categoryName, limit);
            System.out.println("Категория успешно добавлена.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении категории: " + e.getMessage());
        }
    }

    private void renameCategory() {
        try {
            System.out.print("Введите название категории: ");
            String categoryName = scanner.nextLine();

            System.out.print("Введите новое название категории: ");
            String newCategoryName = scanner.nextLine();

            budgetService.changeCategoryName(user, categoryName, newCategoryName);
            System.out.println("Категория успешно переименована.");
        } catch (Exception e) {
            System.out.println("Ошибка при переименовании категории: " + e.getMessage());
        }
    }

    private void updateLimit() {
        try {
            System.out.print("Введите название категории: ");
            String categoryName = scanner.nextLine();

            System.out.print("Введите новый лимит: ");
            double newLimit = Double.parseDouble(scanner.nextLine());

            budgetService.updateCategoryBudget(user, categoryName, newLimit);
            System.out.println("Лимит категории успешно обновлен.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число.");
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении лимита категории: " + e.getMessage());
        }
    }

    private void showAllCategories() {
        try {
            budgetService.printAllCategories(user);
        } catch (Exception e) {
            System.out.println("Ошибка при выводе всех категорий: " + e.getMessage());
        }
    }

    private void calculateBudgetOnCategory() {
        try {
            budgetService.calculateBudget(user);
        } catch (Exception e) {
            System.out.println("Ошибка при подсчете бюджета по категориям: " + e.getMessage());
        }
    }
}
