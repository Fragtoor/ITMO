BEGIN;

CREATE TABLE IF NOT EXISTS Collection (
    collectionID SERIAL PRIMARY KEY,
    dateInitialization TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS Roles (
    roleid SERIAL PRIMARY KEY,
    roleName TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Permissions (
    permissionID SERIAL PRIMARY KEY,
    permissionName VARCHAR(50) NOT NULL UNIQUE,
    description TEXT
    );

CREATE TABLE IF NOT EXISTS Role_Permissions (
    roleID INTEGER REFERENCES Roles(roleID) ON DELETE CASCADE,
    permissionID INTEGER REFERENCES Permissions(permissionID) ON DELETE CASCADE,
    PRIMARY KEY (roleID, permissionID)
    );

CREATE TABLE IF NOT EXISTS Users (
    userID SERIAL PRIMARY KEY,
    userName VARCHAR(50) NOT NULL,
    login VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(32) NOT NULL,
    roleid INTEGER NOT NULL REFERENCES Roles(roleid) ON DELETE CASCADE,
    salt VARCHAR(7) NOT NULL
    );

CREATE TABLE IF NOT EXISTS MusicGenre (
    genreID SERIAL PRIMARY KEY,
    genre TEXT NOT NULL UNIQUE
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

CREATE TABLE IF NOT EXISTS UserCommand (
    id SERIAL PRIMARY KEY,
    userID INTEGER REFERENCES Users(userID) ON DELETE CASCADE,
    commandName VARCHAR(50) NOT NULL,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );


INSERT INTO MusicGenre (genre) VALUES
                                   ('JAZZ'),
                                   ('BLUES'),
                                   ('MATH_ROCK'),
                                   ('POST_ROCK'),
                                   ('PUNK_ROCK')
    ON CONFLICT (genre) DO NOTHING;


INSERT INTO Permissions (permissionName, description) VALUES
                                                          -- Read
                                                          ('READ_COLLECTION', 'Право видеть список элементов (show)'),
                                                          ('READ_INFO', 'Право видеть метаданные (info)'),
                                                          ('READ_STATS', 'Доступ к расчетным данным (average/sum of participants)'),
                                                          ('SEARCH', 'Использование фильтров (filter_contains_name)'),
                                                          -- Update
                                                          ('CREATE_OBJECT', 'Возможность добавлять новые элементы'),
                                                          ('UPDATE_OWN', 'Изменение объектов, созданных самим пользователем'),
                                                          ('UPDATE_ALL', 'Право изменять чужие объекты'),
                                                          ('DELETE_OWN', 'Удаление своих объектов'),
                                                          ('DELETE_ALL', 'Право удалять любые объекты в коллекции'),
                                                          ('CLEAR_OWN', 'Очистка только своих элементов'),
                                                          ('CLEAR_ALL', 'Полная очистка коллекции'),
                                                          -- System
                                                          ('VIEW_HISTORY', 'Доступ к истории команд'),
                                                          -- Admin
                                                          ('USER_VIEW', 'Просмотр списка всех пользователей и их ролей'),
                                                          ('ROLE_EDIT', 'Возможность изменить роль пользователя'),
                                                          ('PERMISSION_MANAGE', 'Добавление/удаление функциональностей из роли')
    ON CONFLICT (permissionName) DO NOTHING;


INSERT INTO Roles(roleName) VALUES
                                ('GUEST'),
                                ('USER'),
                                ('SUPERUSER'),
                                ('ADMIN')
    ON CONFLICT (roleName) DO NOTHING;

-- Права для GUEST
INSERT INTO Role_Permissions (roleID, permissionID)
SELECT r.roleID, p.permissionID FROM Roles r, Permissions p
WHERE r.roleName = 'GUEST'
  AND p.permissionName IN ('READ_COLLECTION', 'READ_INFO', 'READ_STATS', 'SEARCH')
    ON CONFLICT (roleID, permissionID) DO NOTHING;

-- Права для USER
INSERT INTO Role_Permissions (roleID, permissionID)
SELECT r.roleID, p.permissionID FROM Roles r, Permissions p
WHERE r.roleName = 'USER'
  AND p.permissionName IN (
                           'READ_COLLECTION', 'READ_INFO', 'READ_STATS', 'SEARCH',
                           'CREATE_OBJECT', 'UPDATE_OWN', 'DELETE_OWN', 'EXECUTE_SCRIPT', 'CLEAR_OWN', 'VIEW_HISTORY'
    )
    ON CONFLICT (roleID, permissionID) DO NOTHING;

-- Права для SUPERUSER
INSERT INTO Role_Permissions (roleID, permissionID)
SELECT r.roleID, p.permissionID FROM Roles r, Permissions p
WHERE r.roleName = 'SUPERUSER'
  AND p.permissionName IN (
                           'READ_COLLECTION', 'READ_INFO', 'READ_STATS', 'SEARCH',
                           'CREATE_OBJECT', 'UPDATE_OWN', 'DELETE_OWN', 'EXECUTE_SCRIPT', 'CLEAR_OWN',
                           'VIEW_HISTORY', 'UPDATE_ALL', 'DELETE_ALL', 'CLEAR_ALL'
    )
    ON CONFLICT (roleID, permissionID) DO NOTHING;

-- Права для ADMIN
INSERT INTO Role_Permissions (roleID, permissionID)
SELECT r.roleID, p.permissionID FROM Roles r, Permissions p
WHERE r.roleName = 'ADMIN'
  AND p.permissionName IN ('USER_VIEW', 'ROLE_EDIT', 'PERMISSION_MANAGE')
    ON CONFLICT (roleID, permissionID) DO NOTHING;



INSERT INTO Collection (dateInitialization)
SELECT CURRENT_TIMESTAMP
    WHERE NOT EXISTS (SELECT 1 FROM Collection LIMIT 1);

INSERT INTO Users (userName, login, password, roleid, salt)
VALUES (
           'Admin',
           'admin',
           md5('admin123AA' || '*63&^mVLC(#' || 'adm_slt'),
           (SELECT roleid FROM Roles WHERE roleName = 'ADMIN'),
           'adm_slt'
       )
    ON CONFLICT (login) DO NOTHING;

COMMIT;