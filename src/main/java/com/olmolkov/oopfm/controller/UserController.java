package com.olmolkov.oopfm.controller;

import com.olmolkov.oopfm.service.UserService;

import java.util.Scanner;

public class UserController {
    private final UserService userService;
    private final Scanner scanner;

    public UserController(UserService userService, Scanner scanner) {
        this.userService = userService;
        this.scanner = scanner;
    }


    // Меню управления пользователем
    public void menu() {
        System.out.println("Меню управления аккаунтом:");
        while (true) {
            System.out.println("1. Изменить имя пользователя");
            System.out.println("2. Изменить пароль");
            System.out.println("3. В главное меню");

            try {
                String operation = scanner.nextLine();
                switch (operation) {
                    case "1" -> changeUsername();
                    case "2" -> changePassword();
                    case "3" -> {
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

    private void changeUsername() {
        System.out.print("Введите новое имя пользователя: ");
        String newUsername = scanner.nextLine();

        userService.changeUsername(newUsername);
    }

    private void changePassword() {
        System.out.print("Введите новый пароль: ");
        String newPassword = scanner.nextLine();

        System.out.print("Введите старый пароль: ");
        String oldPassword = scanner.nextLine();

        userService.changePassword(oldPassword, newPassword);
    }
}
