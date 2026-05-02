package dao;

import commands.Command;
import common.general.User;
import common.models.*;
import tools.PasswordHasher;

import javax.naming.AuthenticationException;
import java.io.*;
import java.sql.*;
import java.sql.ResultSet;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentSkipListSet;

public class DBManager {
    DAO dao;
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

            band.setOwner(isOwner(user, ownerId));
            collection.add(band);
        }
        return collection;
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

    public void deleteItem(User user, int id) throws SQLException {
        String sql = "DELETE FROM ItemsCollection WHERE userID = ? AND id = ?";
        dao.executeUpdate(sql, getUserID(user), id);
    }

    public void deleteItems(User user, Set<MusicBand> bands) throws SQLException {
        for (var band: bands) deleteItem(user, band.getId());
    }

    public void updateItem(User user, MusicBand band, int id) throws SQLException { //static synhronyze???
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
            WHERE id = ? AND userID = ?
            """;
        dao.executeUpdate(sql, band.getName(), band.getCoordinates().getX(),
                band.getCoordinates().getY(),
                band.getNumberOfParticipants(), band.getAlbumsCount(),
                band.getEstablishmentDate(), getGenreID(band.getGenre().toString()),
                band.getLabel().getSales(), id, getUserID(user));
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

        String sql = "INSERT INTO Users (userName, login, password, salt) VALUES (?, ?, ?, ?)";
        try {
            dao.executeUpdate(sql, userName, login, hashedPassword, salt);
            return userName + ", Вы зарегистрировались!";
        } catch (SQLException e) {
            throw new AuthenticationException("Пользователь с таким логином уже существует\n");
        } catch (Exception e) {
            throw new AuthenticationException("Непредвиденная ошибка при попытке регистрации\n");
        }
    }

    public void ddlUpdate(String sql) throws SQLException {
        dao.executeUpdate(sql);
    }
}
