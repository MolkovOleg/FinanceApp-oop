package com.olmolkov.oopfm.validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DataValidator {

    // Проверка строки на пустоту
    public static boolean isNonEmptyString(String string) {
        return string != null && !string.isBlank();
    }

    // Проверка является ли строка числом
    public static boolean isNumeric(String string) {
        if (!isNonEmptyString(string)) {
            return false;
        }
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Проверка на положительное число
    public static boolean isPositiveNumber(String string) {
        return isNumeric(string) && Double.parseDouble(string) > 0;
    }

    // Проверка на корректную дату
    public static boolean isValidDate(String date, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // Проверка на корректные диапазон
    public static boolean isValidRange(double number, double min, double max) {
        return number >= min && number <= max;
    }

    // Проверка на превышение длины строки
    public static boolean isStringLenghtValid(String string, int maxLength) {
        return isNonEmptyString(string) && string.length() <= maxLength;
    }

    // Проверка на доступность логина
    public static boolean isValidLogin(String login) {
        return isNonEmptyString(login) && login.matches("^[a-zA-Z0-9_]{4,20}$");
    }

    // Проверка на доступность пароля
    public static boolean isValidPassword(String password) {
        return isNonEmptyString(password) && password.length() >= 6;
    }
}
