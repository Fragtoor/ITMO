package dao;

import commands.Command;
import common.net.User;
import common.models.*;
import tools.PasswordHasher;

import javax.naming.AuthenticationException;
import java.io.*;
import java.sql.*;
import java.sql.ResultSet;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentSkipListSet;

public class DBManager {
    private final DAO dao;
    public DBManager(DAO dao) {
        this.dao = dao;
    }

    public Set<MusicBand> getAllDataCollection(User user) throws Exception {
        String sql = """
    SELECT * FROM ItemsCollection it
    JOIN Users USING(userID)
    JOIN MusicGenre USING (genreID)
    """;
        Set<MusicBand> collection = new ConcurrentSkipListSet<>();

        int currentUserId = -1;
        try {
            if (user != null) {
                currentUserId = getUserID(user);
            }
        } catch (SQLException ignored) {}

        ResultSet result = dao.executeQuery(sql);
        while (result.next()) {
            int id = result.getInt("id");
            String name = result.getString("name");
            int numberOfParticipants = result.getInt("numberOfParticipants");
            long albumsCount = result.getLong("albumsCount");

            java.sql.Timestamp timestamp = result.getTimestamp("creationDate");
            java.sql.Date date = result.getDate("establishmentDate");
            LocalDateTime creationDate = timestamp.toLocalDateTime();
            LocalDate establishmentDate = date.toLocalDate();

            MusicGenre genre = MusicGenre.valueOf(result.getString("genre"));

            Coordinates coords = new Coordinates(
                    result.getInt("coordinateX"),
                    result.getLong("coordinateY")
            );
            Label label = new Label(result.getDouble("labelSales"));
            int ownerId = result.getInt("userID");

            MusicBand band = new MusicBand(id, name, coords, creationDate,
                    numberOfParticipants, albumsCount,
                    establishmentDate, genre, label);

            band.setOwner(ownerId == currentUserId);
            band.setOwnerId(ownerId);
            collection.add(band);
        }
        return collection;
    }

    public void restoreItems(Set<MusicBand> bands) throws SQLException {
        String sql = """
            INSERT INTO ItemsCollection (
            id, userID, name, coordinateX, coordinateY, creationDate,
            numberOfParticipants, albumsCount, establishmentDate, genreID, labelSales)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);""";

        for (MusicBand band : bands) {
            dao.executeUpdate(sql, band.getId(), band.getOwnerId(), band.getName(),
                    band.getCoordinates().getX(), band.getCoordinates().getY(),
                    band.getCreationDate(), band.getNumberOfParticipants(), band.getAlbumsCount(),
                    band.getEstablishmentDate(), getGenreID(band.getGenre().toString()),
                    band.getLabel().getSales());
        }
        dao.executeQuery("SELECT setval('itemscollection_id_seq', (SELECT MAX(id) FROM ItemsCollection));");
    }

    public LocalDateTime getCreationDateCollection() throws Exception{
        String sql = """
            SELECT dateInitialization FROM Collection;
            """;
        ResultSet result = dao.executeQuery(sql);

        if (result.next()) {
            java.sql.Timestamp timestamp = result.getTimestamp("dateInitialization");
            return timestamp.toLocalDateTime();
        }
        return null;
    }

    public Stack<Command> getCommandsList(User user) throws Exception {
        Stack<Command> stack = new Stack<>();
        String sql = """
            SELECT * FROM UserCommand WHERE userID = ? ORDER BY id
            """;
        ResultSet result = dao.executeQuery(sql, getUserID(user));

        while (result.next()) {
            byte[] bytes = result.getBytes("commandObject");

            try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                 ObjectInputStream ois = new ObjectInputStream(bais)) {
                Command command = (Command) ois.readObject();
                stack.add(command);
            } catch (IOException | ClassNotFoundException e) {
                throw new SQLException(e);
            }
        }
        return stack;
    }

    public boolean isOwner(User user, int itemId) throws SQLException {
        String sql = "SELECT userID FROM ItemsCollection WHERE id = ?";
        ResultSet result = dao.executeQuery(sql, itemId);
        if (result.next()) {
            int ownerId = result.getInt("userID");
            return ownerId == getUserID(user);
        }
        return false;
    }

    public int getUserID(User user) throws SQLException {
        String sql = """
            SELECT userID FROM Users WHERE Users.login = ?
            """;
        ResultSet result = dao.executeQuery(sql, user.getLogin());
        if (result.next()) {
            return result.getInt("userID");
        }
        throw new SQLException("Нет пользователя " + user.getLogin());
    }

    public int getGenreID(String name) throws SQLException {
        String sql = "SELECT genreID FROM MusicGenre WHERE genre = ?";

        ResultSet result = dao.executeQuery(sql, name);
        if (result.next()) {
            return result.getInt("genreID");
        }
        throw new SQLException("Нет жанра " + name);
    }

    public int addItem(User user, MusicBand band, int id) throws SQLException {
        int genreID = getGenreID(band.getGenre().toString());
        if (id == -1) {
            String sql = """
            INSERT INTO ItemsCollection (
            userID,
            name,
            coordinateX,
            coordinateY,
            creationDate,
            numberOfParticipants,
            albumsCount,
            establishmentDate,
            genreID,
            labelSales)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id;""";
            ResultSet resultSet = dao.executeQuery(sql, getUserID(user), band.getName(), band.getCoordinates().getX(), band.getCoordinates().getY(),
                    band.getCreationDate(), band.getNumberOfParticipants(), band.getAlbumsCount(),
                    band.getEstablishmentDate(), genreID, band.getLabel().getSales());
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
            throw new SQLException("Создание элемента коллекции провалилось, ID не получен.");
        }
        else {
            String sql = """
                        INSERT INTO ItemsCollection (
                        id,
                        userID,
                        name,
                        coordinateX,
                        coordinateY,
                        creationDate,
                        numberOfParticipants,
                        albumsCount,
                        establishmentDate,
                        genreID,
                        labelSales)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);""";

            dao.executeUpdate(sql, id, getUserID(user), band.getName(), band.getCoordinates().getX(), band.getCoordinates().getY(),
                    band.getCreationDate(), band.getNumberOfParticipants(), band.getAlbumsCount(),
                    band.getEstablishmentDate(), genreID, band.getLabel().getSales());
            dao.executeQuery("SELECT setval('itemscollection_id_seq', (SELECT MAX(id) FROM ItemsCollection));");
            return id;
        }

    }

    public void addItems(User user, Set<MusicBand> bands) throws SQLException {
        for (var band: bands) {
            addItem(user, band, -1);
        }
    }

    public void saveHistoryCommand(User user, Command command) throws SQLException {
        String sql = """
        INSERT INTO UserCommand (userID, commandName, commandObject, createdAt) VALUES (?, ?, ?, ?)
        """;
        dao.executeUpdate(sql, getUserID(user), command.getCommandName(), commandToBytes(command), java.sql.Timestamp.valueOf(LocalDateTime.now()));

    }

    public byte[] commandToBytes(Command command) throws SQLException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(command);
            oos.flush();

            return baos.toByteArray();

        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public void deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM ItemsCollection WHERE id = ?";
        dao.executeUpdate(sql, id);
    }

    public void deleteItems(Set<MusicBand> bands) throws SQLException {
        for (var band: bands) deleteItem(band.getId());
    }

    public void updateItem(MusicBand band, int id) throws SQLException {
        String sql = """
        UPDATE ItemsCollection SET
        name = ?,
        coordinateX = ?,
        coordinateY = ?,
        numberOfParticipants = ?,
        albumsCount = ?,
        establishmentDate = ?,
        genreID = ?,
        labelSales = ?
        WHERE id = ?
        """;
        dao.executeUpdate(sql, band.getName(), band.getCoordinates().getX(),
                band.getCoordinates().getY(),
                band.getNumberOfParticipants(), band.getAlbumsCount(),
                band.getEstablishmentDate(), getGenreID(band.getGenre().toString()),
                band.getLabel().getSales(), id);
    }

    public void deleteHistoryCommand(User user) throws SQLException {
        String sql = """
                DELETE FROM UserCommand
                WHERE id = (
                    SELECT id FROM UserCommand
                    WHERE userID = ?
                    ORDER BY id DESC
                    LIMIT 1
                );""";
        dao.executeUpdate(sql, getUserID(user));
    }

    public void clearCollection(User user) throws SQLException {
        String sql = "DELETE FROM ItemsCollection WHERE userID = ?;";
        dao.executeUpdate(sql, getUserID(user));
    }

    public void clearCollectionAll() throws SQLException {
        String sql = "DELETE FROM ItemsCollection;";
        dao.executeUpdate(sql);
    }

    public String selectUser(User user) throws AuthenticationException {
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

    public String addUser(User user) throws AuthenticationException {
        String userName = user.getUserName();
        String login = user.getLogin();
        String password = user.getPassword();

        String salt = PasswordHasher.generateSalt();
        String hashedPassword = PasswordHasher.hashPassword(password, salt);

        String sql = "INSERT INTO Users (userName, login, password, roleid, salt) VALUES (?, ?, ?, ?, ?)";
        try {
            dao.executeUpdate(sql, userName, login, hashedPassword, getRoleId("GUEST"), salt);
            return userName + ", Вы зарегистрировались!";
        } catch (SQLException e) {
            throw new AuthenticationException("Пользователь с таким логином уже существует");
        } catch (Exception e) {
            throw new AuthenticationException("Непредвиденная ошибка при попытке регистрации");
        }
    }

    public Set<String> getUserPermissions(User user) throws SQLException {
        Set<String> permissions = new HashSet<>();
        String sql = """
        SELECT p.permissionName
        FROM Permissions p
        JOIN Role_Permissions rp ON p.permissionID = rp.permissionID
        JOIN Users u ON u.roleid = rp.roleID
        WHERE u.login = ?
        """;

        ResultSet result = dao.executeQuery(sql, user.getLogin());
        while (result.next()) {
            permissions.add(result.getString("permissionName"));
        }
        return permissions;
    }

    public HashMap<String, String[]> getUsersAndPermissions() throws SQLException {
        HashMap<String, String[]> permissions = new HashMap<>();

        String sql = """
        SELECT u.userid, u.login, r.roleName
        FROM Users u
        JOIN Roles r ON u.roleid = r.roleid
        """;

        ResultSet result = dao.executeQuery(sql);

        while (result.next()) {
            String userId = String.valueOf(result.getInt("userid"));
            String login = result.getString("login");
            String roleName = result.getString("roleName");
            String[] data = new String[] {login, roleName};
            permissions.put(userId, data);
        }

        return permissions;
    }

    public int getRoleId(String role) throws SQLException {
        String sql = "SELECT roleid FROM Roles WHERE roleName = ?";
        ResultSet result = dao.executeQuery(sql, role);
        if (result.next()) return result.getInt("roleid");
        return -1;
    }

    public void addFunctionsToRole(String role, Object... functions) throws SQLException {
        int[] functionsId = new int[functions.length];
        String sql = "SELECT permissionID FROM Permissions WHERE permissionName = ?";

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
        sql = "INSERT INTO Role_Permissions (roleID, permissionID) VALUES (?, ?)";
        for (int funcId: functionsId) {
            dao.executeUpdate(sql, roleId, funcId);
        }
    }

    public void deleteFunctionsToRole(String role, Object... functions) throws SQLException {
        int[] functionsId = new int[functions.length];
        String sql = "SELECT permissionID FROM Permissions WHERE permissionName = ?";

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
        sql = "DELETE FROM Role_Permissions WHERE roleID = ? AND permissionID = ?";
        for (int funcId: functionsId) {
            dao.executeUpdate(sql, roleId, funcId);
        }
    }

    public void updateUserRole(int idUser, String role) throws SQLException {
        String sql = "SELECT roleid FROM Roles WHERE roleName = ?";
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
    WHERE userID = ?
    """;
        dao.executeUpdate(sql, id, idUser);
    }

    public void ddlUpdate(String sql) throws SQLException {
        dao.executeUpdate(sql);
    }
}
