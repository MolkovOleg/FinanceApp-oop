package com.olmolkov.oopfm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class Transaction {
    private final String id;
    private double amount;
    private Category category;
    private LocalDate date;

    @JsonCreator
    public Transaction(
            @JsonProperty("id") String id,
            @JsonProperty("amount") double amount,
            @JsonProperty("category") Category category,
            @JsonProperty("date") LocalDate date) {
        this.id = id != null ? id : UUID.randomUUID().toString(); // Генерация уникального идентификатора
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Transaction(double amount, Category category, LocalDate date) {
        this(UUID.randomUUID().toString(), amount, category, date);
    }
}
