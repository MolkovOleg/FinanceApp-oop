package com.olmolkov.oopfm.repository;

import com.olmolkov.oopfm.model.Wallet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WalletRepository extends FileRepository {
    private static final String FILE_PATH = "src/main/resources/data/wallets/wallets.json";

    public WalletRepository() {
        ensureDirectoryExists();
        ensureFileExists();
    }

    // Метод проверки существования директории и его создания
    private void ensureDirectoryExists() {
        File directory = new File("src/main/resources/data/wallets");
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
                saveWallets(new ArrayList<>());
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла кошельков: " + e.getMessage());
        }
    }

    // Метод сохранения кошельков в Json-файл
    public void saveWallets(List<Wallet> wallets) {
        try {
            saveDataToFile(FILE_PATH, wallets);
            System.out.println("Данные кошельков успешно сохранены");
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении файла кошельков: " + e.getMessage());
        }
    }

    // Метод загрузки кошельково из Json-файла
    public List<Wallet> loadWallets() {
        try {
            List<Wallet> allWallets = loadDataFromFile(FILE_PATH, Wallet.class);
            if (allWallets == null) {
                allWallets = new ArrayList<>();
            }
            return allWallets;
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке кошельков: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Метод загрузки кошельков одного пользователя из Json-файла
    public List<Wallet> findWalletsByUserId(String username) {
        List<Wallet> allWallets = loadWallets();

        return allWallets.stream()
                .filter(w -> w.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    // Метод сохранения или обновления кошелька
    public void saveWallet(Wallet newWallet) {
        try {
            List<Wallet> allWallets = loadWallets();

            Optional<Wallet> existWallet = allWallets.stream()
                    .filter(wallet -> wallet.getUsername().equals(newWallet.getUsername())
                            && wallet.getName().equals(newWallet.getName()))
                    .findFirst();

            if (existWallet.isPresent()) {
                allWallets.set(allWallets.indexOf(existWallet.get()), newWallet);
            } else {
                allWallets.add(newWallet);
            }

            saveWallets(allWallets);
        } catch (Exception e) {
            System.err.println("Ошибка при сохранении кошелька: " + e.getMessage());
        }
    }
}