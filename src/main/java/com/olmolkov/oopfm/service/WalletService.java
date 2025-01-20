package com.olmolkov.oopfm.service;

import com.olmolkov.oopfm.model.Category;
import com.olmolkov.oopfm.model.User;
import com.olmolkov.oopfm.model.Wallet;
import com.olmolkov.oopfm.model.Transaction;
import com.olmolkov.oopfm.repository.CategoryRepository;
import com.olmolkov.oopfm.repository.WalletRepository;
import com.olmolkov.oopfm.validator.DataValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class WalletService {
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;

    public WalletService(WalletRepository walletRepository, CategoryRepository categoryRepository) {
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
    }

    // Метод добавления кошелька
    public void addWallet(User user, String walletName, double initialBalance) {
        try {
            validateWalletName(walletName);
            validateBalance(initialBalance);

            Wallet newWallet = new Wallet(user.getUsername(), walletName, initialBalance);
            walletRepository.saveWallet(newWallet);

            System.out.println("Кошелек успешно добавлен");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении кошелька: " + e.getMessage());
        }
    }

    // Метод удаления кошелька
    public void removeWallet(User user, String walletName) {
        try {
            validateWalletName(walletName);

            List<Wallet> userWallets = walletRepository.findWalletsByUserId(user.getUsername());

            Optional<Wallet> walletToRemove = userWallets.stream()
                    .filter(wallet -> wallet.getName().equals(walletName))
                    .findFirst();

            if (walletToRemove.isPresent()) {
                userWallets.remove(walletToRemove.get());
            } else {
                throw new IllegalArgumentException("Кошелек c таким именем не найден");
            }

            walletRepository.saveWallets(userWallets);

            System.out.println("Кошелек успешно удален");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при удалении кошелька: " + e.getMessage());
        }
    }

    // Метод переименования кошелька
    public void renameWallet(User user, String oldWalletName, String newWalletName) {
        try {
            validateWalletName(newWalletName);

            List<Wallet> userWallets = walletRepository.findWalletsByUserId(user.getUsername());

            Optional<Wallet> walletToRename = userWallets.stream()
                    .filter(wallet -> wallet.getName().equals(oldWalletName))
                    .findFirst();

            if (walletToRename.isPresent()) {
                walletToRename.get().setName(newWalletName);
            } else {
                throw new IllegalArgumentException("Кошелек c таким именем не найден");
            }

            walletRepository.saveWallets(userWallets);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при переименовании кошелька: " + e.getMessage());
        }
    }

    // Метод обновления баланса кошелька
    public void updateBalance(User user, String walletName, double newBalance) {
        try {
            validateBalance(newBalance);

            List<Wallet> userWallets = walletRepository.findWalletsByUserId(user.getUsername());

            Optional<Wallet> walletToUpdate = userWallets.stream()
                    .filter(wallet -> wallet.getName().equals(walletName))
                    .findFirst();

            if (walletToUpdate.isPresent()) {
                walletToUpdate.get().setBalance(newBalance);
            } else {
                throw new IllegalArgumentException("Кошелек c таким именем не найден");
            }

            walletRepository.saveWallets(userWallets);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при обновлении баланса кошелька: " + e.getMessage());
        }
    }

    // Метод перевода средств между кошельками
    public void transferFunds(User senderUser, String senderWallet, User recieverUser, String recieverWallet, double amount) {
        if (!DataValidator.isPositiveNumber(String.valueOf(amount))) {
            throw new IllegalArgumentException("Сумма перевода должна быть положительной");
        }

        List<Wallet> senderWallets = walletRepository.findWalletsByUserId(senderUser.getUsername());
        Optional<Wallet> senderWalletOpt = senderWallets.stream()
                .filter(wallet -> wallet.getName().equals(senderWallet))
                .findFirst();

        List<Wallet> reciverWallets = walletRepository.findWalletsByUserId(recieverUser.getUsername());
        Optional<Wallet> recieverWalletOpt = reciverWallets.stream()
                .filter(wallet -> wallet.getName().equals(recieverWallet))
                .findFirst();

        if (senderWalletOpt.isPresent()) {
            senderWalletOpt.get().setBalance(senderWalletOpt.get().getBalance() - amount);
        } else {
            throw new IllegalArgumentException("Кошелек отправителя не найден");
        }

        if (recieverWalletOpt.isPresent()) {
            recieverWalletOpt.get().setBalance(recieverWalletOpt.get().getBalance() + amount);
        } else {
            throw new IllegalArgumentException("Кошелек получателя не найден");
        }

        walletRepository.saveWallet(senderWalletOpt.get());
        walletRepository.saveWallet(recieverWalletOpt.get());

        System.out.println("Перевод между кошельками успешно выполнен");
    }

    // Метод вывода всех кошельков пользователя
    public void printAllWallets(User user) {
        try {
            List<Wallet> userWallets = walletRepository.findWalletsByUserId(user.getUsername());

            if (userWallets.isEmpty()) {
                throw new IllegalArgumentException("У пользователя нет кошельков");
            }

            System.out.println("Ваши кошельки:");

            userWallets.forEach(wallet -> {
                System.out.println("Имя кошелька: " + wallet.getName());
                System.out.println("Баланс кошелька: " + wallet.getBalance());
                System.out.println("----------------------------");
            });
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при выводе кошельков: " + e.getMessage());
        }
    }

    // Метод подсчета общих доходов и расходов по всем кошелькам
    public void financeCalculation(User user) {
        List<Wallet> userWallets = walletRepository.findWalletsByUserId(user.getUsername());

        double totalIncome = userWallets.stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .filter(transaction -> transaction.getAmount() > 0)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpenses = userWallets.stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .filter(transaction -> transaction.getAmount() < 0)
                .mapToDouble(Transaction::getAmount)
                .sum();

        System.out.println("Общие доходы: " + totalIncome);
        System.out.println("Общие расходы: " + Math.abs(totalExpenses));
    }

    // Метод вывода данных по бюджету каждого кошелька
    public void displayBudgetInfo(User user) {
        List<Category> userCategories = categoryRepository.findCategoriesByUserId(user.getUsername());
        List<Wallet> userWallets = walletRepository.findWalletsByUserId(user.getUsername());

        userWallets.forEach(wallet -> {
            System.out.println("Кошелек: " + wallet.getName());
            System.out.printf("Баланс: %.2f\n", wallet.getBalance());
            System.out.println("Транзакции:");

            wallet.getTransactions().stream()
                    .map(transaction -> {
                        String categoryName = transaction.getCategory().getName();
                        String transactionCategory = userCategories.stream()
                                .anyMatch(category -> category.getName().equals(categoryName))
                                ? categoryName : "Нет категории";
                        return String.format("  - Дата: %s, Сумма: %.2f, Категория: %s",
                                transaction.getDate(), transaction.getAmount(), transactionCategory);
                    })
                    .forEach(System.out::println);

            System.out.println("----------------------------");
        });
    }

    // Метод проверки превышения расходов над доходами
    public String checkExpenseExceedsIncome(User user) {
        List<Wallet> userWallets = walletRepository.findWalletsByUserId(user.getUsername());

        double totalIncome = userWallets.stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .filter(transaction -> transaction.getAmount() > 0)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpenses = userWallets.stream()
                .flatMap(wallet -> wallet.getTransactions().stream())
                .filter(transaction -> transaction.getAmount() < 0)
                .mapToDouble(Transaction::getAmount)
                .sum();

        if (Math.abs(totalExpenses) > totalIncome) {
            return "Расходы превышают доходы";
        }
        return "";
    }

    // Метод добавления транзакции в кошелек
    public void addTransaction(User user, String walletName, double amount, String categoryName, boolean isIncome) {
        try {
            List<Wallet> userWallets = walletRepository.findWalletsByUserId(user.getUsername());
            Optional<Wallet> userWallet = userWallets.stream()
                    .filter(wallet -> wallet.getName().equals(walletName))
                    .findFirst();

            if (userWallet.isEmpty()) {
                throw new IllegalArgumentException("Кошелек с таким именем не найден");
            }

            Category userCategory = categoryRepository.findCategoryByName(user.getUsername(), categoryName);
            if (userCategory == null) {
                throw new IllegalArgumentException("Категория с таким именем не найдена");
            }

            double signOfAmount = isIncome ? amount : -amount;
            Transaction newTransaction = new Transaction(signOfAmount, userCategory, LocalDate.now());
            userWallet.get().addTransaction(newTransaction);

            walletRepository.saveWallet(userWallet.get());
            System.out.println("Транзакция успешно добавлена");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при добавлении транзакции: " + e.getMessage());
        }
    }

    // Метод вывода всех транзакций кошелька
    public void displayTransactions(User user, String walletName) {
        try {
            List<Wallet> userWallets = walletRepository.findWalletsByUserId(user.getUsername());

            userWallets.forEach(wallet -> {
                if (wallet.getName().equals(walletName)) {
                    System.out.println("Кошелек: " + wallet.getName());
                    System.out.println("Транзакции:");

                    wallet.getTransactions().forEach(transaction -> {
                        System.out.println("  - ID: " + transaction.getId());
                        System.out.println("  - Дата: " + transaction.getDate());
                        System.out.println("    Сумма: " + transaction.getAmount());
                        System.out.println("    Категория: " + transaction.getCategory().getName());
                        System.out.println("----------------------------");
                    });
                } else {
                    throw new IllegalArgumentException("Кошелек с таким именем не найден");
                }
            });
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при выводе транзакций: " + e.getMessage());
        }
    }

    // Метод проверки имени кошелька
    private void validateWalletName(String walletName) {
        if (!DataValidator.isNonEmptyString(walletName) || !DataValidator.isStringLenghtValid(walletName, 20)) {
            throw new IllegalArgumentException("Некорректное имя кошелька");
        }
    }

    // Метод проверки корректности баланса
    private void validateBalance(double initialBalance) {
        if (!DataValidator.isPositiveNumber(String.valueOf(initialBalance))
                || !DataValidator.isValidRange(initialBalance, 1, 1000000)) {
            throw new IllegalArgumentException("Некорректная сумма баланса");
        }
    }
}
