package tools;

import common.exceptions.InvalidInputException;
import common.general.User;
import common.tools.Validator;
import reader_manager.InputManager;

public class AuthService {
    private static String getLogin() {
        String login;
        do {
            System.out.print("Введите логин: ");
            login = InputManager.readInput();
            if (login != null && !login.isBlank()) {
                return login;
            } else {
                System.out.println("Логин не должен быть пустой строкой");
            }
        } while (true);

    }

    private static String createPassword() {
        String password;
        System.out.println("\nТребования для пароля:");
        System.out.println("- Пароль должен содержать не менее 8 символов.");
        System.out.println("- В пароле должна быть хотя бы одна цифра");
        System.out.println("- Должна присутствовать хотя бы одна строчная буква (a-z).");
        System.out.println("- Должна присутствовать хотя бы одна заглавная буква (A-Z).");
        do {
            System.out.print("Введите пароль: ");
            password = InputManager.readInput();
            try {
                Validator.validatePassword(password);
                return password;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    private static String getPassword() {
        String password;
        do {
            System.out.print("Введите пароль: ");
            password = InputManager.readInput();
            if (password != null && !password.isBlank()) {
                return password;
            } else {
                System.out.println("Пароль не должен быть пустой строкой");
            }
        } while (true);
    }

    private static String getUserName() {
        String userName;
        do {
            System.out.print("Введите своё имя: ");
            userName = InputManager.readInput();
            if (userName != null && !userName.isBlank()) {
                return userName;
            } else {
                System.out.println("Имя не должно быть пустой строкой");
            }
        } while (true);

    }
    public static User register() {
        String userName = getUserName();
        String login = getLogin();
        String password = createPassword();
        return new User(userName, login, password);
    }

    public static User login() {
        String login = getLogin();
        String password = getPassword();
        return new User(login, password);
    }
}
