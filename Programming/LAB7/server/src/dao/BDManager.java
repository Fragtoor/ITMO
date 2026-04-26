package dao;

import commands.Command;
import common.general.User;
import common.models.*;
import managers.CollectionManager;

import java.io.*;
import java.sql.*;
import java.sql.ResultSet;
import java.util.LinkedHashSet;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Stack;


public class BDManager {
    public static void setDataCollection(DAO dao, User user, CollectionManager cm) throws Exception{
        String sql = """
        SELECT * FROM ItemsCollection it JOIN Users USING(userID) JOIN MusicGenre USING (genreID) WHERE Users.login = ?
        """;
        LinkedHashSet<MusicBand> collection = new LinkedHashSet<>();

        try (ResultSet result = dao.select(sql, user.getLogin())) {
            while (result.next()) {
                int id = result.getInt("id");
                String name = result.getString("name");
                int numberOfParticipants = result.getInt("numberOfParticipants");
                long albumsCount = result.getLong("albumsCount");

                LocalDateTime creationDate = result.getObject("creationDate", LocalDateTime.class);
                LocalDate establishmentDate = result.getObject("establishmentDate", LocalDate.class);

                MusicGenre genre = MusicGenre.valueOf(result.getString("genre"));

                Coordinates coords = new Coordinates(
                        result.getInt("coordinateX"),
                        result.getLong("coordinateY")
                );
                Label label = new Label(result.getDouble("labelSales"));

                MusicBand band = new MusicBand(id, name, coords, creationDate, numberOfParticipants, albumsCount, establishmentDate, genre, label);
                collection.add(band);
            }
        }
        cm.setCollection(collection);
    }

    public static void setCreationDateCollection(DAO dao, User user, CollectionManager cm) throws Exception{
        String sql = """
            SELECT dateInitialization FROM Users JOIN Collection USING(collectionID) WHERE Users.login = ?
            """;

        try (ResultSet result = dao.select(sql, user.getLogin())) {
            LocalDateTime creationDate = result.getObject("dateInitialization", LocalDateTime.class);
            cm.setCreationDate(creationDate);
        }
    }

    public static void setCommandsList(DAO dao, User user, CollectionManager cm) throws Exception {
        Stack<Command> stack = new Stack<>();
        String sql = """
            SELECT * FROM UserCommand WHERE userID = ? ORDER BY id
            """;
        try (ResultSet result = dao.select(sql, getUserID(dao, user))) {
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
            cm.setCommandsList(stack);
        }
    }

    public static int getUserID(DAO dao, User user) throws SQLException {
        String sql = """
            SELECT userID FROM User WHERE Users.login = ?
            """;
        try (ResultSet result = dao.select(sql, user.getLogin())) {
            return result.getInt("userID");
        }
    }

    public static int getGenreID(DAO dao, User user, String name) throws SQLException {
        String sql = """
                    SELECT genreID FROM MusicGenre WHERE genre = ?
                """;
        try (ResultSet result = dao.select(sql, name)) {
            return result.getInt("genreID");
        }
    }

    public static void addItem(DAO dao, User user, MusicBand band) throws SQLException {
        int genreID = getGenreID(dao, user, band.getName());
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
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);""";
        dao.executeUpdate(sql, BDManager.getUserID(dao, user), band.getName(), band.getCoordinates().getX(), band.getCoordinates().getY(),
                band.getCreationDate(), band.getNumberOfParticipants(), band.getAlbumsCount(),
                band.getEstablishmentDate(), genreID, band.getLabel().getSales()
        );
    }

    public static void addItems(DAO dao, User user, LinkedHashSet<MusicBand> bands) throws SQLException {
        for (var band: bands) addItem(dao, user, band);
    }

    public static void saveHistoryCommand(DAO dao, User user, Command command) throws SQLException {
        String sql = """
        INSERT INTO UserCommand (userID, commandName, commandObject, createdAt) VALUES (?, ?, ?, ?)
        """;
        dao.executeUpdate(sql, getUserID(dao, user), command.getCommandName(), commandToBytes(command), java.sql.Timestamp.valueOf(LocalDateTime.now()));

    }

    public static byte[] commandToBytes(Command command) throws SQLException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            oos.writeObject(command);
            oos.flush();

            return baos.toByteArray();

        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public static void deleteItem(DAO dao, User user, int id) throws SQLException {
        String sql = "DELETE FROM ItemsCollection WHERE userID = ? AND id = ?";
        dao.executeUpdate(sql, getUserID(dao, user), id);
    }

    public static void deleteItems(DAO dao, User user, LinkedHashSet<MusicBand> bands) throws SQLException {
        for (var band: bands) deleteItem(dao, user, band.getId());
    }

    public static void updateItem(DAO dao, User user, MusicBand band, int id) throws SQLException {
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
        dao.executeUpdate(sql, band.getName(), band.getCoordinates().getX(), band.getCoordinates().getY(),
                band.getCoordinates().getY(), band.getNumberOfParticipants(), band.getAlbumsCount(),
                band.getEstablishmentDate(), getGenreID(dao, user, band.getGenre().toString()), band.getLabel().getSales(), id, getUserID(dao, user));
    }

    public static void deleteHistoryCommand(DAO dao, User user) throws SQLException {
        String sql = """
                DELETE FROM UserCommand
                WHERE id = (
                    SELECT id FROM UserCommand
                    WHERE userID = ?
                    ORDER BY id DESC
                    LIMIT 1
                );""";
        dao.executeUpdate(sql, getUserID(dao, user));
    }

    public static void clearCollection(DAO dao, User user) throws SQLException {
        String sql = "DELETE FROM ItemsCollection WHERE userID = ?;";
        dao.executeUpdate(sql, getUserID(dao, user));
    }
}
