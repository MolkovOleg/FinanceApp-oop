package com.olmolkov.oopfm.controller;

import com.olmolkov.oopfm.model.User;
import com.olmolkov.oopfm.service.UserService;
import com.olmolkov.oopfm.service.WalletService;
import com.olmolkov.oopfm.validator.DataValidator;

import java.util.Scanner;
import java.util.SortedMap;

public class WalletController {
    private final WalletService walletService;
    private final UserService userService;
    private final User user;
    private final Scanner scanner;

    public WalletController(WalletService walletService, UserService userService, User user, Scanner scanner) {
        this.walletService = walletService;
        this.userService = userService;
        this.user = user;
        this.scanner = scanner;
    }

    // Меню управления кошельками
    public void menu() {
        System.out.println("Меню управления кошельками:");
        while (true) {
            System.out.println("1. Добавить кошелек");
            System.out.println("2. Удалить кошелек");
            System.out.println("3. Переименовать кошелек");
            System.out.println("4. Обновить баланс кошелек");
            System.out.println("5. Перевод средств на другой кошелек");
            System.out.println("6. Показать список кошельков");
            System.out.println("7. Показать доходы и расходы");
            System.out.println("8. Показать данные по бюджету кошельков");
            System.out.println("9. В главное меню");

            try {
                String operation = scanner.nextLine();
                switch (operation) {
                    case "1" -> addWallet();
                    case "2" -> removeWallet();
                    case "3" -> renameWallet();
                    case "4" -> updateWalletBalance();
                    case "5" -> transferFunds();
                    case "6" -> showAllWallets();
                    case "7" -> displayIncomeExpenses();
                    case "8" -> budgetData();
                    case "9" -> {
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

    private void addWallet() {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();

            System.out.print("Введите начальную сумму баланса: ");
            double balance = Double.parseDouble(scanner.nextLine());

            walletService.addWallet(user, walletName, balance);
            System.out.println("Кошелек успешно добавлен");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении кошелька: " + e.getMessage());
        }
    }

    private void removeWallet() {
        try {
            System.out.print("Введите название кошелька для удаления: ");
            String walletName = scanner.nextLine();

            walletService.removeWallet(user, walletName);
            System.out.println("Кошелек успешно удален");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении кошелька: " + e.getMessage());
        }

    }

    private void renameWallet() {
        try {
            System.out.print("Введите текущее название кошелька: ");
            String oldWalletName = scanner.nextLine();

            System.out.print("Введите новое название кошелька: ");
            String newWalletName = scanner.nextLine();

            walletService.renameWallet(user, oldWalletName, newWalletName);
            System.out.println("Название кошелька успешно изменено");
        } catch (Exception e) {
            System.out.println("Ошибка при переименовании кошелька: " + e.getMessage());
        }
    }

    private void updateWalletBalance() {
        try {
            System.out.print("Введите название кошелька: ");
            String walletName = scanner.nextLine();

            System.out.print("Введите новую сумму баланса: ");
            double newBalance = Double.parseDouble(scanner.nextLine());

            walletService.updateBalance(user, walletName, newBalance);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Введите корректное число");
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении баланса кошелька: " + e.getMessage());
        }

    }

    private void transferFunds() {
        try {
            System.out.print("Введите название кошелька для отправки: ");
            String senderWallet = scanner.nextLine();

            System.out.print("Введите имя пользователя получателя: ");
            String recieverUsername = scanner.nextLine();

            System.out.print("Введите название кошелька получателя: ");
            String recieverWallet = scanner.nextLine();

            System.out.print("Введите сумму перевода: ");
            String amountIn = scanner.nextLine();

            if (!DataValidator.isNumeric(amountIn) || !DataValidator.isPositiveNumber(amountIn)) {
                System.out.println("Ошибка: Сумма перевода должна быть положительной");
                return;
            }

            double amount = Double.parseDouble(amountIn);

            User recieverUser = userService.findUserByUsername(recieverUsername);
            if (recieverUser == null) {
                System.out.println("Ошибка: Такого получателя не существует");
                return;
            }

            walletService.transferFunds(user, senderWallet, recieverUser, recieverWallet, amount);
        } catch (Exception e) {
            System.out.println("Ошибка при переводе средств: " + e.getMessage());
        }
    }

    private void showAllWallets() {
        try {
            walletService.printAllWallets(user);
        } catch (Exception e) {
            System.out.println("Ошибка при получении списка кошельков: " + e.getMessage());
        }
    }

    private void displayIncomeExpenses() {
        try {
            walletService.financeCalculation(user);
        } catch (Exception e) {
            System.out.println("Ошибка при подсчете финансов: " + e.getMessage());
        }
    }

    private void budgetData() {
        try {
            walletService.displayBudgetInfo(user);
        } catch (Exception e) {
            System.out.println("Ошибка при отображении данных по бюджету: " + e.getMessage());
        }
    }
}
