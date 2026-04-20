DROP TABLE IF EXISTS Person CASCADE;
DROP TABLE IF EXISTS TypeConscience CASCADE;
DROP TABLE IF EXISTS Influence CASCADE;
DROP TABLE IF EXISTS Meeting CASCADE;
DROP TABLE IF EXISTS PersonMeeting CASCADE;
DROP TABLE IF EXISTS PersonState CASCADE;
DROP TABLE IF EXISTS StateType CASCADE;
DROP TABLE IF EXISTS InfluenceType CASCADE;
DROP TABLE IF EXISTS PlaceOfResidence CASCADE;

CREATE TABLE Person (
    personID SERIAL PRIMARY KEY,
    name varchar(40) NOT NULL,
    surname varchar(40),
    typeConscience int DEFAULT 1,
    sex varchar(3),
    height DOUBLE PRECISION,
    profession varchar(30),
    placeOfResidence int
);

CREATE TABLE TypeConscience (
    typeID SERIAL PRIMARY KEY,
    typeName varchar(40) NOT NULL,
    description varchar(100)
);

CREATE TABLE Influence (
    influenceID SERIAL PRIMARY KEY,
    level INTEGER CHECK (0 <= level and level <= 100) DEFAULT 0,
    description varchar(150),
    personSource int,
    personTarget int,
    influenceType int DEFAULT 1
);

CREATE TABLE Meeting (
    meetingID SERIAL PRIMARY KEY,
    dateTimeMeeting timestamp,
    coordinateX DOUBLE PRECISION,
    coordinateY DOUBLE PRECISION,
    description varchar(100),
    levelOfProductivity INTEGER CHECK (0 <= levelOfProductivity and levelOfProductivity <= 100) DEFAULT 0
);

CREATE TABLE PersonMeeting (
    personMeetingID SERIAL PRIMARY KEY,
    person int NOT NULL,
    meeting int NOT NULL
);

CREATE TABLE PersonState (
    personStateID SERIAL PRIMARY KEY,
    timeDate timestamp NOT NULL,
    level int CHECK (level >= 0 and level <= 100) DEFAULT 0,
    person int,
    stateType int
);

CREATE TABLE StateType (
    stateID SERIAL PRIMARY KEY,
    stateName varchar(40) NOT NULL,
    description varchar(100),
    levelOfHarm int CHECK (0 <= levelOfHarm and levelOfHarm <= 100) DEFAULT 0
);

CREATE TABLE PlaceOfResidence (
    placeOfResidenceID SERIAL PRIMARY KEY,
    coordinateX DOUBLE PRECISION NOT NULL,
    coordinateY DOUBLE PRECISION NOT NULL,
    country varchar(30),
    city VARCHAR(30),
    street VARCHAR(30),
    houseNumber int
);

CREATE TABLE InfluenceType (
    influenceTypeID SERIAL PRIMARY KEY,
    influenceName varchar(40) NOT NULL,
    description varchar(100)
);

INSERT INTO TypeConscience (typeName, description) VALUES
('Человек', 'Обитатели мира, обладающие эмоциями и самосознанием'),
('Центральный Компьютер', 'Высший ИИ, управляющий Диаспаром');

INSERT INTO PlaceOfResidence (coordinateX, coordinateY, country, city, street, houseNumber) VALUES
(48.8566, 2.3522, 'Диаспар', 'Центральный район', 'Проспект Бессмертия', 1),
(51.5074, -0.1278, 'Диаспар', 'Сады', 'Аллея Мудрости', 5),
(40.7128, -74.0060, 'Лис', 'Старый город', 'Улица Тайн', 10),
(35.6895, 139.6917, 'Диаспар', 'Залы Совета', 'Площадь Власти', 100),
(48.8584, 2.2945, 'Диаспар', 'Парящие террасы', 'Небесный бульвар', 15),
(51.5099, -0.1337, 'Диаспар', 'Библиотечный квартал', 'Улица Знаний', 7),
(40.7120, -74.0050, 'Лис', 'Новый город', 'Проспект Свободы', 25),
(35.6800, 139.7000, 'Диаспар', 'Технополис', 'Инженерная', 50),
(48.8600, 2.3200, 'Диаспар', 'Храмовый район', 'Священная аллея', 3),
(51.5000, -0.1000, 'Лис', 'Пригород', 'Тихая улица', 12);

INSERT INTO Person (name, surname, typeConscience, sex, height, profession, placeOfResidence) VALUES
('Олвин', NULL, 1, 'M', 185.5, 'Исследователь', 1),
('Хилвар', NULL, 1, 'M', 178.0, 'Спутник', 3),
('Хедрон', NULL, 1, 'M', 180.0, 'Шутник/Мудрец', 2),
('Центральный', 'Компьютер', 2, NULL, NULL, 'Управление Диаспаром', 4),
('Джезерак', NULL, 1, 'M', 175.0, 'Историк', 5),
('Алистра', NULL, 1, 'F', 165.0, 'Художница', 6),
('Криф', NULL, 2, NULL, 190.0, 'Техник', 8),
('Мерлин', NULL, 1, 'M', 170.0, 'Хранитель архивов', 7),
('Серанис', NULL, 1, 'F', 168.0, 'Правительница Лиса', 3),
('ВАЛ-7', 'Слуга', 1, NULL, 150.0, 'Домашний помощник', 2);

INSERT INTO InfluenceType (influenceName, description) VALUES
('Растворение', 'Потеря собственной идентичности, "тонет в личности" другого'),
('Подчинение', 'Добровольное следование за лидером'),
('Восхищение', 'Сильное уважение, граничащее с преклонением'),
('Сомнение', 'Влияние, вызывающее неуверенность'),
('Интеллектуальное', 'Влияние через знания и мудрость'),
('Эмоциональное', 'Влияние через чувства и переживания'),
('Манипуляция', 'Скрытое управление поведением'),
('Вдохновение', 'Побуждение к действиям и творчеству'),
('Страх', 'Влияние через угрозу или опасение'),
('Дружеское', 'Мягкое влияние через доверие');

INSERT INTO Influence (level, description, personSource, personTarget, influenceType) VALUES
(90, 'Я тонет в личности Олвина', 1, 2, 1), (50, 'С меньшим основанием', 1, 3, 4),
(40, 'Уважение к лидеру', 1, 2, 3), (85, 'Подчиняется воле Олвина', 1, 5, 2),
(60, 'Ищет одобрения', 1, 6, 8), (95, 'Абсолютное доверие', 4, 1, 5),
(75, 'Советуется по важным вопросам', 4, 5, 5),
(30, 'Легкое восхищение', 3, 1, 3),
(45, 'Сомнения в решениях', 2, 3, 4),
(70, 'Интеллектуальное превосходство', 4, 8, 5),
(55, 'Эмоциональная зависимость', 1, 6, 6),
(25, 'Скрытое недоверие', 7, 1, 7),
(80, 'Страх перед мощью', 9, 4, 9),
(65, 'Дружеское влияние', 9, 1, 10),
(35, 'Легкое подчинение', 10, 2, 2);

INSERT INTO StateType (stateName, description, levelOfHarm) VALUES
('Сомнение', 'Внутреннее колебание, неуверенность', 30),
('Отпечаток на сознании', 'Глубокий след после встречи с ЦентрКом', 70),
('Растворение Я', 'Потеря собственной идентичности', 80),
('Водоворот эмоций', 'Сильное эмоциональное смятение', 60),
('Замешательство', 'Легкое недоумение', 20),
('Просветление', 'Внезапное понимание истины', 10),
('Спокойствие', 'Умиротворенное состояние', 5),
('Тревога', 'Беспокойство без причины', 45),
('Эйфория', 'Безудержная радость', 15),
('Апатия', 'Полное безразличие', 55),
('Любопытство', 'Желание узнать новое', 10),
('Страх', 'Боязнь неизвестности', 65);

INSERT INTO Meeting (dateTimeMeeting, coordinateX, coordinateY, description, levelOfProductivity) VALUES
('2450-03-15 10:00:00', 48.8566, 2.3522, 'Встреча Олвина с Центральным Компьютером', 100),
('2450-03-14 18:00:00', 51.5074, -0.1278, 'Разговор Олвина с Хедроном', 60),
('2450-03-13 15:00:00', 48.8566, 2.3522, 'Встреча Олвина, Хилвара и Хедрона', 80),
('2450-03-12 12:00:00', 48.8566, 2.3522, 'Олвин изучает архивы с Хилваром', 70),
('2450-03-10 14:30:00', 40.7128, -74.0060, 'Совет старейшин Лиса', 90),
('2450-03-08 09:00:00', 35.6895, 139.6917, 'Техническое обслуживание', 50),
('2450-03-05 20:00:00', 48.8584, 2.2945, 'Вечер воспоминаний', 75),
('2450-02-28 11:00:00', 51.5099, -0.1337, 'Поиск древних знаний', 85),
('2450-02-20 16:00:00', 40.7120, -74.0050, 'Обсуждение будущего Лиса', 95),
('2450-02-15 13:00:00', 35.6800, 139.7000, 'Экскурсия по Технополису', 65);

INSERT INTO PersonMeeting (person, meeting) VALUES
(1, 1), (4, 1),
(1, 2), (3, 2),
(1, 3), (2, 3), (3, 3),
(1, 4), (2, 4),
(9, 5), (3, 5), (1, 5),
(4, 6), (7, 6), (10, 6),
(1, 7), (2, 7), (3, 7), (5, 7),
(1, 8), (5, 8), (8, 8),
(9, 9), (2, 9), (6, 9),
(1, 10), (2, 10), (7, 10), (8, 10);

INSERT INTO PersonState (timeDate, level, person, stateType) VALUES
('2450-03-14 10:00:00', 60, 2, 1),
('2450-03-15 11:30:00', 90, 1, 2),
('2450-03-15 16:00:00', 85, 2, 3),
('2450-03-15 16:30:00', 75, 2, 4),
('2450-03-14 18:30:00', 40, 3, 5),
('2450-03-13 20:00:00', 50, 3, 1),
('2450-03-10 16:00:00', 80, 9, 2),
('2450-03-08 12:00:00', 30, 7, 11),
('2450-03-05 22:00:00', 70, 1, 6),
('2450-02-28 15:00:00', 55, 5, 12),
('2450-02-25 09:00:00', 45, 6, 8),
('2450-02-20 18:00:00', 90, 2, 3),
('2450-02-15 14:00:00', 65, 8, 12),
('2450-02-10 11:00:00', 20, 10, 7),
('2450-02-05 17:00:00', 85, 1, 4),
('2450-02-01 13:00:00', 35, 3, 1),
('2450-01-28 10:00:00', 95, 4, 6),
('2450-01-20 19:00:00', 50, 2, 8),
('2450-01-15 12:00:00', 40, 9, 11),
('2450-01-10 16:00:00', 25, 5, 10);
