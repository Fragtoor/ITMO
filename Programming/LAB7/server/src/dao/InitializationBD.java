package dao;

import java.sql.SQLException;

public class InitializationBD {
    private final DAO dao;
    public InitializationBD (DAO dao) {
        this.dao = dao;
    }
    public void run() throws SQLException {
        dao.executeUpdate("DROP TABLE IF EXISTS UserCommand, ItemsCollection, Users, Collection, MusicGenre CASCADE;");

        createMusicGenre();
        createCollection();
        createUser();
        createItemsCollection();
        createUserCommands();
    }

    private void createCollection() throws SQLException {
        String sqlCollection = """
                CREATE TABLE IF NOT EXISTS Collection (
                    collectionID SERIAL PRIMARY KEY,
                    type VARCHAR(30) NOT NULL,
                    dateInitialization TIMESTAMP NOT NULL
                )
                """;
        dao.executeUpdate(sqlCollection);
    }

    private void createUser() throws SQLException {
        String sqlCollection = """
                CREATE TABLE IF NOT EXISTS Users (
                    userID SERIAL PRIMARY KEY,
                    collectionID INTEGER NOT NULL REFERENCES Collection (collectionID),
                    userName VARCHAR(50) NOT NULL,
                    login VARCHAR(20) NOT NULL UNIQUE,
                    password VARCHAR(50) NOT NULL
                )
                """;
        dao.executeUpdate(sqlCollection);
    }

    private void createItemsCollection() throws SQLException {
        String sqlCollection = """
                CREATE TABLE IF NOT EXISTS ItemsCollection (
                    id SERIAL PRIMARY KEY,
                    userID INTEGER NOT NULL REFERENCES Users (userID),
                    name TEXT NOT NULL CHECK (trim(name) <> ''),
                    coordinateX INTEGER,
                    coordinateY BIGINT NOT NULL,
                    creationDate TIMESTAMP NOT NULL,
                    numberOfParticipants INTEGER NOT NULL CHECK (numberOfParticipants > 0),
                    albumsCount BIGINT CHECK (albumsCount > 0),
                    establishmentDate DATE NOT NULL,
                    genreID INTEGER NOT NULL REFERENCES MusicGenre (genreID),
                    labelSales DOUBLE PRECISION NOT NULL CHECK (labelSales > 0)
                )
                """;
        dao.executeUpdate(sqlCollection);
    }

    private void createMusicGenre() throws SQLException {
        String sqlCollection = """
                CREATE TABLE IF NOT EXISTS MusicGenre (
                    genreID SERIAL PRIMARY KEY,
                    genre TEXT NOT NULL
                )
                """;
        dao.executeUpdate(sqlCollection);
    }

    private void createUserCommands() throws SQLException {
        String sqlCollection = """
                CREATE TABLE IF NOT EXISTS UserCommand (
                    id SERIAL PRIMARY KEY,
                    userID INTEGER REFERENCES Users(userID),
                    commandName VARCHAR(50) NOT NULL,
                    commandObject BYTEA,
                    createdAt TIMESTAMP
                )
                """;
        dao.executeUpdate(sqlCollection);
    }

}
