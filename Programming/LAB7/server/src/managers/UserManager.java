package managers;

import common.general.User;
import dao.DAO;

import javax.naming.AuthenticationException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
    DAO dao;
    public String login(User user) throws AuthenticationException {
        String login = user.getLogin();
        String password = user.getPassword();
        String sql = "SELECT userName FROM Users WHERE login = ? AND password = ?";
        try (ResultSet result = dao.select(sql, login, password)) {
            if (result.next()) {
                String userName = result.getString("userName");
                return "Добро пожаловать, " + userName + "!\n";
            }
            throw new AuthenticationException("Неверный логин или пароль\n");

        } catch (SQLException e) {
            throw new AuthenticationException("Ошибка при попытке входа\n");
        }
    }
    public String register(User user) throws AuthenticationException {
        String userName = user.getUserName();
        String login = user.getLogin();
        String password = user.getPassword();
        String sql = "INSERT INTO Users (userName, login, password) VALUES (?, ?, ?)";
        try {
            dao.executeUpdate(sql, userName, login, password);
            return userName + ", Вы зарегистрировались!\n";
        } catch (SQLException e) {
            throw new AuthenticationException("Непредвиденная ошибка при попытке регистрации\n");
        } catch (Exception e) {
            throw new AuthenticationException("Пользователь с таким логином уже существует\n");
        }
    }

    public void setDAO(DAO dao) {
        this.dao = dao;
    }
}

