# FinanceApp

FinanceApp — это консольное приложение на Java для управления личными финансами. Оно позволяет пользователям регистрироваться, управлять кошельками, добавлять доходы и расходы, создавать категории с бюджетами и отслеживать финансовые операции. Данные хранятся в JSON-файлах, что обеспечивает простоту и переносимость.

## Основные возможности

- **Управление пользователями**:
  - Регистрация и аутентификация.
  - Смена имени пользователя и пароля.
- **Управление кошельками**:
  - Создание, удаление, переименование кошельков.
  - Обновление баланса и перевод средств между кошельками.
  - Просмотр списка кошельков и их бюджета.
- **Управление транзакциями**:
  - Добавление доходов и расходов с указанием категории.
  - Просмотр всех транзакций для конкретного кошелька.
- **Управление категориями и бюджетами**:
  - Создание и переименование категорий.
  - Установка и обновление лимитов бюджета для категорий.
  - Анализ расходов по категориям с уведомлениями о превышении лимитов.
- **Финансовый анализ**:
  - Подсчёт общих доходов и расходов.
  - Проверка превышения расходов над доходами.
  - Детальная информация о состоянии бюджета.

## Технологии

- **Язык программирования**: Java (JDK 11+).
- **Библиотеки**:
  - Jackson (`com.fasterxml.jackson`) — для работы с JSON.
  - Lombok — для упрощения создания моделей (геттеры, сеттеры).
- **Хранение данных**: JSON-файлы (`users.json`, `wallets.json`, `categories.json`).
- **Архитектура**: Многослойная (модели, репозитории, сервисы, контроллеры).

## Структура проекта

```
FinanceApp/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/olmolkov/oopfm/
│   │   │   │   ├── controller/    # Контроллеры для взаимодействия с пользователем
│   │   │   │   ├── model/         # Модели данных (User, Wallet, Transaction, Category)
│   │   │   │   ├── repository/    # Репозитории для работы с JSON-файлами
│   │   │   │   ├── service/       # Бизнес-логика приложения
│   │   │   │   ├── validator/     # Валидация данных
│   │   │   │   └── FinanceApp.java # Точка входа в приложение
│   │   ├── resources/
│   │   │   ├── data/
│   │   │   │   ├── users/         # JSON-файл для пользователей
│   │   │   │   ├── wallets/       # JSON-файл для кошельков
│   │   │   │   ├── categories/    # JSON-файл для категорий
```

### Основные компоненты

- **Модели** (`User`, `Wallet`, `Transaction`, `Category`): Представляют данные приложения.
- **Репозитории** (`UserRepository`, `WalletRepository`, `CategoryRepository`): Управляют чтением и записью данных в JSON-файлы.
- **Сервисы** (`UserService`, `WalletService`, `BudgetService`): Содержат бизнес-логику, включая валидацию и обработку данных.
- **Контроллеры** (`UserController`, `WalletController`, `TransactionController`, `BudgetController`): Реализуют консольный интерфейс для взаимодействия с пользователем.
- **Валидатор** (`DataValidator`): Проверяет корректность входных данных (логины, пароли, суммы и т.д.).

## Установка

1. **Требования**:
   - JDK 11 или выше.
   - Maven для управления зависимостями.
   - Git для клонирования репозитория.

2. **Клонирование репозитория**:
   ```bash
   git clone <repository-url>
   cd FinanceApp
   ```

3. **Установка зависимостей**:
   Убедитесь, что в файле `pom.xml` указаны следующие зависимости:
   ```xml
   <dependencies>
       <dependency>
           <groupId>com.fasterxml.jackson.core</groupId>
           <artifactId>jackson-databind</artifactId>
           <version>2.15.2</version>
       </dependency>
       <dependency>
           <groupId>com.fasterxml.jackson.datatype</groupId>
           <artifactId>jackson-datatype-jsr310</artifactId>
           <version>2.15.2</version>
       </dependency>
       <dependency>
           <groupId>org.projectlombok</groupId>
           <artifactId>lombok</artifactId>
           <version>1.18.30</version>
           <scope>provided</scope>
       </dependency>
   </dependencies>
   ```
   Затем выполните:
   ```bash
   mvn clean install
   ```

4. **Запуск приложения**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.olmolkov.oopfm.FinanceApp"
   ```

## Использование

1. **Запуск**:
   После запуска приложения появится главное меню:
   ```
   Добро пожаловать в FinanceApp!
   1. Войти в аккаунт
   2. Зарегистрироваться
   3. Выйти
   ```

2. **Регистрация**:
   - Выберите опцию `2` и введите имя пользователя и пароль.
   - Логин должен содержать только буквы, цифры и подчёркивания (4–20 символов).
   - Пароль должен быть не короче 6 символов.

3. **Вход**:
   - Выберите опцию `1` и введите свои учётные данные.
   - После входа откроется меню пользователя с доступом к управлению кошельками, транзакциями, категориями и аккаунтом.

4. **Пример работы**:
   - Создайте кошелёк: Меню → Управление кошельками → Добавить кошелёк.
   - Добавьте категорию: Меню → Управление категориями → Добавить категорию.
   - Запишите транзакцию: Меню → Управление транзакциями → Добавить доход/расход.
   - Проверьте бюджет: Меню → Управление категориями → Подсчёт бюджета.

## Текущие данные

Приложение поставляется с начальными JSON-файлами:
- `users.json`: Содержит одного пользователя (`olmolkov`).
- `wallets.json`: Содержит один кошелёк с балансом 100000.0, но с некорректным `username` (`visa` вместо `olmolkov`).
- `categories.json`: Пустой.

### Исправление данных
Для корректной работы рекомендуется обновить `wallets.json`, изменив `username` на `olmolkov`:
```json
[
  {
    "name": "olmolkov",
    "username": "olmolkov",
    "balance": 100000.0,
    "transactions": [],
    "allTransactions": []
  }
]
```

## Ограничения и возможные улучшения

### Ограничения
- **Консольный интерфейс**: Ограничивает удобство использования для широкой аудитории.
- **Хранение данных**: JSON-файлы не подходят для больших объёмов данных или многопользовательских систем.
- **Отсутствие тестов**: Нет юнит-тестов для проверки логики.
- **Ошибки в данных**: Несоответствие `username` в `wallets.json` требует ручного исправления.
- **Безопасность**: Пароли хранятся в открытом виде, что небезопасно.

### Рекомендации по улучшению
1. **Графический интерфейс**: Перейти на JavaFX или веб-интерфейс (Spring Boot + React).
2. **База данных**: Заменить JSON-файлы на реляционную БД (например, H2 или PostgreSQL).
3. **Шифрование паролей**: Использовать библиотеку BCrypt для хэширования паролей.
4. **Тестирование**: Добавить юнит-тесты с использованием JUnit и Mockito.
5. **Логирование**: Внедрить библиотеку SLF4J для отслеживания ошибок и событий.
6. **Международизация**: Поддержка нескольких языков в интерфейсе.
7. **Экспорт данных**: Добавить возможность экспорта транзакций в CSV или PDF.
