package managers;

import common.general.User;
import dao.BDManager;
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
        try {
            ResultSet result = dao.executeQuery(sql, login, password);
            if (result.next()) {
                String userName = result.getString("userName");
                return "Добро пожаловать, " + userName + "!";
            }
            throw new AuthenticationException("Неверный логин или пароль");

        } catch (SQLException e) {
            throw new AuthenticationException("Ошибка при попытке входа");
        }
    }
    public String register(User user) throws AuthenticationException {
        String userName = user.getUserName();
        String login = user.getLogin();
        String password = user.getPassword();

        String sql = "INSERT INTO Users (collectionID, userName, login, password) VALUES (?, ?, ?, ?)";
        try {
            int collectionID = BDManager.addCollection(dao);
            dao.executeUpdate(sql, collectionID, userName, login, password);
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

