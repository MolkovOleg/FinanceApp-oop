package com.olmolkov.oopfm;

import com.olmolkov.oopfm.controller.BudgetController;
import com.olmolkov.oopfm.controller.TransactionController;
import com.olmolkov.oopfm.controller.UserController;
import com.olmolkov.oopfm.controller.WalletController;
import com.olmolkov.oopfm.model.User;
import com.olmolkov.oopfm.repository.CategoryRepository;
import com.olmolkov.oopfm.repository.UserRepository;
import com.olmolkov.oopfm.repository.WalletRepository;
import com.olmolkov.oopfm.service.BudgetService;
import com.olmolkov.oopfm.service.UserService;
import com.olmolkov.oopfm.service.WalletService;

import java.util.Scanner;

public class FinanceApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        // Инициализация репозиториев
        UserRepository userRepository = new UserRepository();
        WalletRepository walletRepository = new WalletRepository();
        CategoryRepository categoryRepository = new CategoryRepository();

        // Инициализация сервисов
        UserService userService = new UserService(userRepository, walletRepository, categoryRepository);
        WalletService walletService = new WalletService(walletRepository, categoryRepository);
        BudgetService budgetService = new BudgetService(walletRepository, categoryRepository);

        // Инициализация контроллеров
        UserController userController = new UserController(userService, scanner);

        // Запуск главного меню
        mainMenu(scanner, userController, userService, walletService, budgetService);

    }

    private static void mainMenu(Scanner scanner, UserController userController, UserService userService,
                                 WalletService walletService, BudgetService budgetService) {
        while (true) {
            System.out.println("Добро пожаловать в FinanceApp!");
            System.out.println("1. Войти в аккаунт");
            System.out.println("2. Зарегистрироваться");
            System.out.println("3. Выйти");

            try {
                String operation = scanner.nextLine();
                switch (operation) {
                    case "1" -> {
                        User currentUser = login(scanner, userService);
                        if (currentUser != null) {
                            System.out.println("Вы успешно вошли в аккаунт.");
                            manageUserMenu(scanner, currentUser, userController, walletService, budgetService, userService);
                        } else {
                            System.out.println("Неправильный логин или пароль.");
                        }
                    }
                    case "2" -> register(scanner, userService);
                    case "3" -> {
                        System.out.println("До свидания!");
                        return;
                    }
                    default -> System.out.println("Неправильная операция.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private static User login(Scanner scanner, UserService userService) {
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        if (userService.login(username, password)) {
            return userService.getCurrentUser();
        }
        return null;
    }

    private static void register(Scanner scanner, UserService userService) {
        try {
            System.out.print("Введите имя пользователя: ");
            String username = scanner.nextLine();

            System.out.print("Введите пароль: ");
            String password = scanner.nextLine();

            userService.register(username, password);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static void manageUserMenu(Scanner scanner, User currentUser, UserController userController,
                                       WalletService walletService, BudgetService budgetService, UserService userService) {
        WalletController walletController = new WalletController(walletService, userService, currentUser, scanner);
        BudgetController budgetController = new BudgetController(budgetService, currentUser, scanner);
        TransactionController transactionController = new TransactionController(walletService, budgetService, currentUser, scanner);

        while (true) {
            System.out.println("Меню пользователя:");
            System.out.println("1. Управление кошельками");
            System.out.println("2. Управление категориями и бюджетами");
            System.out.println("3. Управление транзакциями");
            System.out.println("4. Управление аккаунтом");
            System.out.println("5. Выход");

            try {
                String operation = scanner.nextLine();
                switch (operation) {
                    case "1" -> walletController.menu();
                    case "2" -> budgetController.menu();
                    case "3" -> transactionController.menu();
                    case "4" -> userController.menu();
                    case "5" -> {
                        System.out.println("Выход из аккаунта...");
                        return;
                    }
                    default -> System.out.println("Неправильная операция.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}
