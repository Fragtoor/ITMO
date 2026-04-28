package managers;

import common.general.User;
import dao.BDManager;
import dao.DAO;
import tools.PasswordHasher;

import javax.naming.AuthenticationException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
    DAO dao;
    public String login(User user) throws AuthenticationException {
        String login = user.getLogin();
        String password = user.getPassword();
        String sql = "SELECT userName, password, salt FROM Users WHERE login = ?";
        try {
            ResultSet result = dao.executeQuery(sql, login);
            if (result.next()) {
                String userName = result.getString("userName");
                String storedHash = result.getString("password");
                String salt = result.getString("salt");
                if (PasswordHasher.verifyPassword(password, salt, storedHash)) {
                    return "Добро пожаловать, " + userName + "!";
                }
            }
            throw new AuthenticationException("Неверный логин или пароль");

        } catch (SQLException e) {
            throw new AuthenticationException(e.getMessage() + "Непредвиденная ошибка при попытке входа");
        }
    }
    public String register(User user) throws AuthenticationException {
        String userName = user.getUserName();
        String login = user.getLogin();
        String password = user.getPassword();

        String salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.hashPassword(password, salt);

        String sql = "INSERT INTO Users (collectionID, userName, login, password, salt) VALUES (?, ?, ?, ?, ?)";
        try {
            int collectionID = BDManager.addCollection(dao);
            dao.executeUpdate(sql, collectionID, userName, login, hashedPassword, salt);
            return userName + ", Вы зарегистрировались!";
        } catch (SQLException e) {
            throw new AuthenticationException("Пользователь с таким логином уже существует\n");
        } catch (Exception e) {
            throw new AuthenticationException("Непредвиденная ошибка при попытке регистрации\n");
        }
    }

    public void setDAO(DAO dao) {
        this.dao = dao;
    }
}

