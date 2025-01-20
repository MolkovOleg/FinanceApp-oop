package com.olmolkov.oopfm.controller;

import com.olmolkov.oopfm.model.User;
import com.olmolkov.oopfm.service.BudgetService;
import com.olmolkov.oopfm.service.WalletService;

import java.util.List;
import java.util.Scanner;

public class TransactionController {
    private final WalletService walletService;
    private final BudgetService budgetService;
    private final User user;
    private final Scanner scanner;

    public TransactionController(WalletService walletService, BudgetService budgetService, User user, Scanner scanner) {
        this.walletService = walletService;
        this.budgetService = budgetService;
        this.user = user;
        this.scanner = scanner;
    }

    // Меню управления транзакциями
    public void menu() {
        System.out.println("Меню управления транзакциями:");
        while (true) {
            System.out.println("1. Добавить доход");
            System.out.println("2. Добавить расход");
            System.out.println("3. Посмотреть все транзакции");
            System.out.println("4. В главное меню");

            try {
                String operation = scanner.nextLine();
                switch (operation) {
                    case "1" -> addTransaction(true);
                    case "2" -> addTransaction(false);
                    case "3" -> displayTransactions();
                    case "4" -> {
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

    private void addTransaction(boolean isIncome) {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();

            System.out.print("Введите сумму транзакции: ");
            double amount = Double.parseDouble(scanner.nextLine());

            System.out.print("Введите категорию транзакции: ");
            String category = scanner.nextLine();

            walletService.addTransaction(user, walletName, amount, category, isIncome);

            List<String> warnings = budgetService.checkBudget(user);
            if (!warnings.isEmpty()) {
                System.out.println("Предупреждения:");
                warnings.forEach(System.out::println);
            }

            String expenseWarning = walletService.checkExpenseExceedsIncome(user);
            if (!expenseWarning.isEmpty()) {
                System.out.println(expenseWarning);
            }

            System.out.println("Транзакция успешно добавлена");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении транзакции: " + e.getMessage());
        }
    }

    private void displayTransactions() {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();

            walletService.displayTransactions(user, walletName);
        } catch (Exception e) {
            System.out.println("Ошибка при выводе транзакций: " + e.getMessage());
        }
    }


}
