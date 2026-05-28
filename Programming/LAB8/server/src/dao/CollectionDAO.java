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
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class CollectionDAO {
    private final DAO dao;
    private final UserDAO userDAO;

    public CollectionDAO(DAO dao, UserDAO userDAO) {
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

    public int getGenreID(String name) throws SQLException {
        String sql = "SELECT genreID FROM MusicGenre WHERE genre = ?;";

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
            ResultSet resultSet = dao.executeQuery(sql, userDAO.getUserID(user), band.getName(), band.getCoordinates().getX(), band.getCoordinates().getY(),
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

            dao.executeUpdate(sql, id, userDAO.getUserID(user), band.getName(), band.getCoordinates().getX(), band.getCoordinates().getY(),
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

    public void deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM ItemsCollection WHERE id = ?;";
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
        WHERE id = ?;
        """;
        dao.executeUpdate(sql, band.getName(), band.getCoordinates().getX(),
                band.getCoordinates().getY(),
                band.getNumberOfParticipants(), band.getAlbumsCount(),
                band.getEstablishmentDate(), getGenreID(band.getGenre().toString()),
                band.getLabel().getSales(), id);
    }

    public void clearCollection(User user) throws SQLException {
        String sql = "DELETE FROM ItemsCollection WHERE userID = ?;";
        dao.executeUpdate(sql, userDAO.getUserID(user));
    }

    public void clearCollectionAll() throws SQLException {
        String sql = "DELETE FROM ItemsCollection;";
        dao.executeUpdate(sql);
    }
}
