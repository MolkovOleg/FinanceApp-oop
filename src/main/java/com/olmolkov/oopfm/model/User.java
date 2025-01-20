package com.olmolkov.oopfm.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private String username;
    private String password;
    private Wallet wallet;

    @JsonCreator
    public User(
            @JsonProperty("username") String username,
            @JsonProperty("password") String password,
            @JsonProperty("wallet") Wallet wallet) {
        this.username = username;
        this.password = password;
        this.wallet = wallet != null ? wallet : new Wallet(); // Инициализирует кошелек по умолчанию
    }

    // Метод проверки пароля
    public boolean isValidPassword(String inPassword) {
        return password.equals(inPassword);
    }
}
