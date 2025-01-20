package com.olmolkov.oopfm.service;

import com.olmolkov.oopfm.model.Category;
import com.olmolkov.oopfm.model.User;
import com.olmolkov.oopfm.model.Wallet;
import com.olmolkov.oopfm.repository.CategoryRepository;
import com.olmolkov.oopfm.repository.UserRepository;
import com.olmolkov.oopfm.repository.WalletRepository;
import com.olmolkov.oopfm.validator.DataValidator;
import lombok.Getter;

import java.util.List;

@Getter
public class UserService {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;
    private User currentUser;

    public UserService(
            UserRepository userRepository,
            WalletRepository walletRepository,
            CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
    }

    // Метод регистрации нового пользователя
    public void register(String username, String password) {
        try {
            validateUsername(username);
            validatePassword(password);

            List<User> users = userRepository.loadUsers();

            if (users.stream()
                    .anyMatch(user -> user.getUsername().equals(username))) {
                throw new IllegalArgumentException("Пользователь с таким именем уже существует");
            }

            users.add(new User(username, password, null));
            userRepository.saveUsers(users);

            System.out.println("Пользователь успешно зарегистрирован");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при регистрации пользователя: " + e.getMessage());
        }
    }

    // Метод поиска пользователя по логину
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Метод аутентификации пользователя
    public boolean login(String username, String password) {
        try {
            validateUsername(username);
            validatePassword(password);

            User user = findUserByUsername(username);

            if (user == null) {
                System.out.println("Пользователь с таким {" + username + "} не найден");
                return false;
            }

            if (user.isValidPassword(password)) {
                currentUser = user;
                System.out.println("Пользователь {" + username + "} успешно аутентифицирован");
                return true;
            }

            System.out.println("Неверное имя пользователя или пароль");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при аутентификации пользователя: " + e.getMessage());
            return false;
        }
    }

    // Метод смены пароля пользователя
    public void changePassword(String oldPassword, String newPassword) {
        try {
            validatePassword(newPassword);

            if (currentUser == null || !currentUser.getPassword().equals(oldPassword)) {
                throw new IllegalArgumentException("Неверное старый пароль");
            }

            if (newPassword.equals(oldPassword)) {
                throw new IllegalArgumentException("Новый пароль должен отличаться от старого");
            }

            List<User> users = userRepository.loadUsers();

            users.stream()
                    .filter(user -> user.getUsername().equals(currentUser.getUsername()))
                    .forEach(user -> user.setPassword(newPassword));

            userRepository.saveUsers(users);
            currentUser.setPassword(newPassword);

            System.out.println("Пароль успешно изменен");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при изменении пароля: " + e.getMessage());
        }
    }

    // Метод смены логина пользователя
    public void changeUsername(String username) {
        try {
            validateUsername(username);

            if (currentUser == null) {
                throw new IllegalArgumentException("Пользователь не аутентифицирован");
            }

            if (findUserByUsername(username) != null) {
                throw new IllegalArgumentException("Пользователь с таким именем уже существует");
            }

            List<User> users = userRepository.loadUsers();

            users.stream()
                    .filter(user -> user.getUsername().equals(currentUser.getUsername()))
                    .forEach(user -> user.setUsername(username));

            userRepository.saveUsers(users);
            updateWalletUsername(currentUser.getUsername(), username);
            updateCategoriesUsername(currentUser.getUsername(), username);
            currentUser.setUsername(username);

            System.out.println("Имя пользователя успешно изменено");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при изменении имени пользователя: " + e.getMessage());
        }
    }

    // Метод обновления имени в кошельках
    private void updateWalletUsername(String oldUsername, String newUsername) {
        List<Wallet> allWallets = walletRepository.findWalletsByUserId(oldUsername);

        allWallets.forEach(wallet -> wallet.setUsername(newUsername));

        walletRepository.saveWallets(allWallets);
    }

    // Метод обновления имени в категориях
    private void updateCategoriesUsername(String oldUsername, String newUsername) {
        List<Category> allCategories = categoryRepository.findCategoriesByUserId(oldUsername);

        allCategories.forEach(category -> category.setUsername(newUsername));

        categoryRepository.saveCategories(allCategories);
    }

    // Метод проверки корректности логина
    private void validateUsername(String username) {
        if (!DataValidator.isNonEmptyString(username) || !DataValidator.isStringLenghtValid(username, 20)
                || !DataValidator.isValidLogin(username)) {
            throw new IllegalArgumentException("Некорректное имя пользователя");
        }
    }

    // Метод проверки корректности пароля
    private void validatePassword(String password) {
        if (!DataValidator.isNonEmptyString(password) || !DataValidator.isValidPassword(password)) {
            throw new IllegalArgumentException("Некорректный пароль");
        }
    }
}
