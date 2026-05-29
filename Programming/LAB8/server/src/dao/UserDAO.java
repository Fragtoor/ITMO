package dao;

import common.net.User;
import tools.PasswordHasher;
import javax.naming.AuthenticationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDAO {
    private final DAO dao;

    public UserDAO(DAO dao) {
        this.dao = dao;
    }

    public int getUserID(User user) throws SQLException {
        String sql = """
            SELECT userID FROM Users WHERE Users.login = ?;
            """;
        ResultSet result = dao.executeQuery(sql, user.getLogin());
        if (result.next()) {
            return result.getInt("userID");
        }
        throw new SQLException("Нет пользователя " + user.getLogin());
    }

    public String selectUser(User user) throws AuthenticationException {
        String login = user.getLogin();
        String password = user.getPassword();
        String sql = "SELECT userName, password, salt, is_banned FROM Users WHERE login = ?;";

        try {
            ResultSet result = dao.executeQuery(sql, login);
            if (result.next()) {
                String userName = result.getString("userName");
                String storedHash = result.getString("password");
                String salt = result.getString("salt");

                if (result.getBoolean("is_banned")) {
                    throw new AuthenticationException("server.auth.error.account_banned");
                }
                if (PasswordHasher.verifyPassword(password, salt, storedHash)) {
                    return "server.auth.success.login::" + userName;
                }
            }
            throw new AuthenticationException("server.auth.error.invalid_credentials");
        } catch (SQLException e) {
            throw new AuthenticationException("server.auth.error.unexpected_login");
        }
    }

    public String addUser(User user) throws AuthenticationException {
        String userName = user.getUserName();
        String login = user.getLogin();
        String password = user.getPassword();
        String salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.hashPassword(password, salt);

        String sql = "INSERT INTO Users (userName, login, password, roleid, salt) VALUES (?, ?, ?, ?, ?);";

        try {
            dao.executeUpdate(sql, userName, login, hashedPassword, getRoleId("USER"), salt);
            return "server.auth.success.register::" + userName;
        } catch (SQLException e) {
            throw new AuthenticationException("server.auth.error.user_exists");
        } catch (Exception e) {
            throw new AuthenticationException("server.auth.error.unexpected_register");
        }
    }

    public String getUserRole(String login) {
        String query = "SELECT roleName FROM users INNER JOIN Roles ON Roles.roleid = Users.roleid WHERE login = ?;";

        try (Connection connection = dao.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, login);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("roleName");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "GUEST";
    }

    public ArrayList<User> getAllUsersList() throws SQLException {
        java.util.ArrayList<User> users = new java.util.ArrayList<>();
        String sql = """
            SELECT u.userid, u.userName, u.login, r.roleName, u.is_banned
            FROM Users u
            JOIN Roles r ON u.roleid = r.roleid;
            """;
        ResultSet result = dao.executeQuery(sql);
        while (result.next()) {
            User u = new User(result.getString("userName"), result.getString("login"), "");
            u.setId(result.getInt("userid"));
            u.setRole(result.getString("roleName"));
            u.setBanned(result.getBoolean("is_banned"));
            users.add(u);
        }
        return users;
    }

    public void setUserBanned(int userId, boolean isBanned) throws SQLException {
        dao.executeUpdate("UPDATE Users SET is_banned = ? WHERE userid = ?;", isBanned, userId);
    }

    public int getRoleId(String role) throws SQLException {
        String sql = "SELECT roleid FROM Roles WHERE roleName = ?;";
        ResultSet result = dao.executeQuery(sql, role);
        if (result.next()) return result.getInt("roleid");
        return -1;
    }

    public boolean isUserBanned(int userId) throws SQLException {
        String sql = "SELECT is_banned FROM Users WHERE userid = ?;";
        ResultSet result = dao.executeQuery(sql, userId);
        if (result.next()) {
            return result.getBoolean("is_banned");
        }
        return false;
    }

    public void addFunctionsToRole(String role, Object... functions) throws SQLException {
        int[] functionsId = new int[functions.length];
        String sql = "SELECT permissionID FROM Permissions WHERE permissionName = ?;";

        int roleId = getRoleId(role);
        if (roleId == -1) throw new SQLException("Роли " + role + " нет!");

        int i = 0;
        for (Object function: functions) {
            ResultSet result = dao.executeQuery(sql, function);
            if (result.next()) {
                functionsId[i++] = result.getInt("permissionID");
            }
            else {
                throw new SQLException("Функциональности " + function + " нет!");
            }
        }
        sql = "INSERT INTO Role_Permissions (roleID, permissionID) VALUES (?, ?);";
        for (int funcId: functionsId) {
            dao.executeUpdate(sql, roleId, funcId);
        }
    }

    public void deleteFunctionsToRole(String role, Object... functions) throws SQLException {
        int[] functionsId = new int[functions.length];
        String sql = "SELECT permissionID FROM Permissions WHERE permissionName = ?;";

        int roleId = getRoleId(role);
        if (roleId == -1) throw new SQLException("Роли " + role + " нет!");
        int i = 0;
        for (Object function: functions) {
            ResultSet result = dao.executeQuery(sql, function);
            if (result.next()) {
                functionsId[i++] = result.getInt("permissionID");
            }
            else {
                throw new SQLException("Функциональности " + function + " нет!");
            }
        }
        sql = "DELETE FROM Role_Permissions WHERE roleID = ? AND permissionID = ?;";
        for (int funcId: functionsId) {
            dao.executeUpdate(sql, roleId, funcId);
        }
    }

    public void createRole(String roleName) throws SQLException {
        String sql = "INSERT INTO Roles (roleName) VALUES (?)";
        dao.executeUpdate(sql, roleName.toUpperCase());
    }

    public void deleteRole(String roleName) throws SQLException {
        String roleUpper = roleName.toUpperCase();
        if (roleUpper.equals("ADMIN") || roleUpper.equals("USER") ||
                roleUpper.equals("SUPERUSER") || roleUpper.equals("GUEST")) {
            throw new SQLException("Попытка удалить базовую роль!");
        }

        String sql = "DELETE FROM Roles WHERE roleName = ?";
        dao.executeUpdate(sql, roleUpper);
    }

    public void updateUserRole(int idUser, String role) throws SQLException {
        String sql = "SELECT roleid FROM Roles WHERE roleName = ?;";
        int id;

        ResultSet result = dao.executeQuery(sql, role);
        if (!result.next()) {
            throw new SQLException("Роли '" + role + "' не существует! Доступные роли: GUEST, USER, SUPERUSER, ADMIN");
        }
        id = result.getInt("roleid");

        sql = "SELECT * FROM Users WHERE userid = ?";
        result = dao.executeQuery(sql, idUser);
        if (!result.next()) throw new SQLException("Пользователя с id '" + idUser + "' не существует!");

        sql = """
    UPDATE Users SET
    roleid = ?
    WHERE userID = ?;
    """;
        dao.executeUpdate(sql, id, idUser);
    }
}