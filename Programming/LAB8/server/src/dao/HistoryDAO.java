package dao;

import common.models.Coordinates;
import common.models.Label;
import common.models.MusicBand;
import common.models.MusicGenre;
import common.net.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class HistoryDAO {
    private final DAO dao;
    private final UserDAO userDAO;

    public HistoryDAO(DAO dao, UserDAO userDAO) {
        this.dao = dao;
        this.userDAO = userDAO;
    }

    public Set<MusicBand> getAllDataCollection() throws Exception {
        String sql = """
    SELECT * FROM ItemsCollection it
    JOIN Users USING(userID)
    JOIN MusicGenre USING (genreID);
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
            int ownerId = result.getInt("userid");

            MusicBand band = new MusicBand.Builder()
                    .id(id)
                    .name(name)
                    .coordinates(coords)
                    .creationDate(creationDate)
                    .numberOfParticipants(numberOfParticipants)
                    .albumsCount(albumsCount)
                    .establishmentDate(establishmentDate)
                    .genre(genre)
                    .label(label)
                    .build();

            band.setOwnerId(ownerId);
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

    public void saveHistoryCommand(User user, String commandName) throws SQLException {
        String sql = "INSERT INTO UserCommand (userID, commandName) VALUES (?, ?);";

        System.out.println("Попытка сохранить команду: " + commandName + " для юзера: " + user.getLogin());
        dao.executeUpdate(sql, userDAO.getUserID(user), commandName);
        System.out.println("Команда сохранена успешно.");
    }

    public List<String> getLastCommands(User user, int limit) throws SQLException {
        List<String> commands = new ArrayList<>();
        String sql = "SELECT commandName FROM UserCommand WHERE userID = ? ORDER BY id DESC LIMIT ?;";

        ResultSet result = dao.executeQuery(sql, userDAO.getUserID(user), limit);
        while (result.next()) {
            commands.add(result.getString("commandName"));
        }

        return commands;
    }
}
