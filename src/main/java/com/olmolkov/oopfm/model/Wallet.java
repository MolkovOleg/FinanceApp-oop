package com.olmolkov.oopfm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Wallet {
    private String name;
    private String username;
    private double balance;
    private List<Transaction> transactions;

    @JsonCreator
    public Wallet(
            @JsonProperty("name") String name,
            @JsonProperty("username") String username,
            @JsonProperty("balance") double balance,
            @JsonProperty("transactions") List<Transaction> transactions) {
        this.name = name; // Имя кошелька
        this.username = username; // Идентификатор пользователя
        this.balance = balance != 0.0 ? balance : 0.0; // Значение баланса по умолчанию
        this.transactions = transactions != null ? transactions : new ArrayList<>(); // Инициализация списка транзакций
    }

    public Wallet(String name, String username, double balance) {
        this(name, username, balance, new ArrayList<>());
    }

    // Метод получения списка транзакций
    public List<Transaction> getAllTransactions() {
        return transactions;
    }

    // Метод добавления транзакции и обновления баланса
    public void addTransaction(Transaction newTransaction) {
        transactions.add(newTransaction);
        balance += newTransaction.getAmount();

    }

    // Метод поиска транзакции по идентификатору
    public Transaction findTransactionById(String transactionId) {
        return transactions.stream()
                .filter(t -> t.getId().equals(transactionId))
                .findFirst()
                .orElse(null);
    }
}

