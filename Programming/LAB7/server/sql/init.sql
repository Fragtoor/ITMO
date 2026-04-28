DROP TABLE IF EXISTS UserCommand;
DROP TABLE IF EXISTS ItemsCollection;
DROP TABLE IF EXISTS MusicGenre;
DROP TABLE IF EXISTS Users;
DROP TABLE IF EXISTS Collection;


CREATE TABLE IF NOT EXISTS Collection (
    collectionID SERIAL PRIMARY KEY,
    dateInitialization TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS Users (
    userID SERIAL PRIMARY KEY,
    userName VARCHAR(50) NOT NULL,
    login VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    salt VARCHAR(7) NOT NULL
);

CREATE TABLE IF NOT EXISTS ItemsCollection (
    id SERIAL PRIMARY KEY,
    userID INTEGER NOT NULL REFERENCES Users (userID) ON DELETE CASCADE,
    name TEXT NOT NULL CHECK (trim(name) <> ''),
    coordinateX INTEGER,
    coordinateY BIGINT NOT NULL,
    creationDate TIMESTAMP NOT NULL,
    numberOfParticipants INTEGER NOT NULL CHECK (numberOfParticipants > 0),
    albumsCount BIGINT CHECK (albumsCount > 0),
    establishmentDate DATE NOT NULL,
    genreID INTEGER NOT NULL REFERENCES MusicGenre (genreID),
    labelSales DOUBLE PRECISION NOT NULL CHECK (labelSales > 0)
);

CREATE TABLE IF NOT EXISTS MusicGenre (
    genreID SERIAL PRIMARY KEY,
    genre TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS UserCommand (
    id SERIAL PRIMARY KEY,
    userID INTEGER REFERENCES Users(userID),
    commandName VARCHAR(50) NOT NULL,
    commandObject BYTEA,
    createdAt TIMESTAMP
);

INSERT INTO MusicGenre (genre) VALUES
                                   ('JAZZ'),
                                   ('BLUES'),
                                   ('MATH_ROCK'),
                                   ('POST_ROCK'),
                                   ('PUNK_ROCK')

INSERT INTO Collection (dateInitialization) VALUES (CURRENT_TIMESTAMP);
