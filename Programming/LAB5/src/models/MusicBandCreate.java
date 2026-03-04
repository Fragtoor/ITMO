package models;

import exceptions.InvalidInputException;
import reader_manager.InputManager;
import tools.CollectionManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Класс для создания нового объекта MusicBand в интерактивном режиме.
 */
public class MusicBandCreate {
    /**
     * Создание объекта {@link MusicBand} интерактивным способом
     *
     * @return Возвращает созданный объект {@link models.MusicBand}
     */
    public static MusicBand create(){
        MusicBand band = new MusicBand();

        boolean flag = true;
        do {
            try {
                System.out.print("Введите название группы name: ");
                String consoleRead = InputManager.readInput();
                if (InputManager.isEndOfFile()) {
                    return null;
                }
                if (consoleRead == null || consoleRead.isEmpty()) {
                    throw new InvalidInputException("Поле name должно быть отличным от null и пустой строки!");
                }
                band.setName(consoleRead);
                flag = false;
            } catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }
        } while(flag);

        flag = true;
        int X = 0;
        do {
            try {
                System.out.print("Введите координату X: ");
                String consoleRead = InputManager.readInput();
                if (consoleRead == null || consoleRead.trim().isEmpty()) break;

                long xCheck = Long.parseLong(consoleRead);
                if (consoleRead.trim().length() > 11 || xCheck > Integer.MAX_VALUE || xCheck < Integer.MIN_VALUE) {
                    throw new InvalidInputException("");
                }
                X = Integer.parseInt(consoleRead);

                flag = false;
                if (InputManager.isEndOfFile()) {
                    throw new InvalidInputException("Не до конца выполнилось создание объекта");
                }
            } catch (InvalidInputException | NumberFormatException e) {
                System.out.println("Поле X должно быть числом типа int от -2^31 до 2^31 - 1");
            }
        } while(flag);

        flag = true;
        do {
            try {
                System.out.print("Введите координату Y: ");
                String consoleRead = InputManager.readInput();
                if (InputManager.isEndOfFile()) {
                    return null;
                }
                if (consoleRead == null || consoleRead.trim().isEmpty()) throw new InvalidInputException("");
                BigInteger yCheck = new BigInteger(consoleRead);
                if (consoleRead.trim().length() > 28 || yCheck.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 || yCheck.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
                    throw new InvalidInputException("");
                }
                Long Y = Long.parseLong(Objects.requireNonNull(consoleRead));
                flag = false;
                band.setCoordinates(new Coordinates(X, Y));
            } catch (InvalidInputException | NumberFormatException e) {
                System.out.println("Поле Y должно быть числом типа long от -2^63 до 2^63 - 1 и не может быть null!");
            }
        } while(flag);

        flag = true;
        do {
            try {
                System.out.print("Введите значение numberOfParticipants: ");
                String consoleRead = InputManager.readInput();
                if (InputManager.isEndOfFile()) {
                    return null;
                }
                if (consoleRead == null || consoleRead.trim().isEmpty()) throw new InvalidInputException("");
                long numberOfParticipantsCheck = Long.parseLong(consoleRead);
                if (consoleRead.trim().length() > 11 || numberOfParticipantsCheck > Integer.MAX_VALUE || numberOfParticipantsCheck < Integer.MIN_VALUE) {
                    throw new InvalidInputException("");
                }
                int numberOfParticipants = Integer.parseInt(Objects.requireNonNull(consoleRead));
                if (numberOfParticipants <= 0) {
                    throw new InvalidInputException("");
                }
                band.setNumberOfParticipants(numberOfParticipants);
                flag = false;
            } catch (InvalidInputException | NumberFormatException e) {
                System.out.println("Поле numberOfParticipants должно быть положительным целым числом int от 1 до 2^31 - 1!");
            }
        } while(flag);

        flag = true;
        do {
            try {
                long albumsCount;
                System.out.print("Введите значение albumsCount: ");
                String consoleRead = InputManager.readInput();
                if (InputManager.isEndOfFile()) {
                    return null;
                }
                if (consoleRead == null || consoleRead.trim().isEmpty()) throw new InvalidInputException("");
                albumsCount = Long.parseLong(consoleRead);
                if (albumsCount <= 0) {
                    throw new InvalidInputException("");
                }
                BigInteger albumsCountCheck = new BigInteger(consoleRead);
                if (consoleRead.trim().length() > 28 || albumsCountCheck.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 || albumsCountCheck.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
                    throw new InvalidInputException("");
                }
                band.setAlbumsCount(albumsCount);
                flag = false;
            } catch (InvalidInputException | NumberFormatException e) {
                System.out.println("Значение поля должно быть положительным числом типа long от 1 до 2^63 - 1!");
            }
        } while(flag);

        flag = true;
        do {
            try {
                System.out.print("Введите значение establishmentDate в формате YYYY-MM-DD: ");
                String consoleRead = InputManager.readInput();
                if (InputManager.isEndOfFile()) {
                    return null;
                }
                if (consoleRead == null || consoleRead.isEmpty()) {
                    throw new InvalidInputException("");
                }
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(consoleRead.trim(), formatter);
                band.setEstablishmentDate(date);
                flag = false;
            } catch (InvalidInputException | NumberFormatException | DateTimeParseException e) {
                System.out.println("Введите корректную дату в формате YYYY-MM-DD!");
            }
        } while(flag);

        flag = true;
        do {
            try {
                System.out.print("Введите значение genre альбома (JAZZ, BLUES, MATH_ROCK, POST_ROCK, PUNK_ROCK): ");
                String consoleRead = InputManager.readInput();
                if (InputManager.isEndOfFile()) {
                    return null;
                }
                if (consoleRead == null || consoleRead.trim().isEmpty()) throw new InvalidInputException("");
                band.setGenre(MusicGenre.valueOf(consoleRead));

                flag = false;
            } catch (InvalidInputException | IllegalArgumentException e) {
                System.out.println("Такого жанра нет!");
            }
        } while(flag);

        flag = true;
        do {
            try {
                System.out.print("Введите значение поля label: ");
                String consoleRead = InputManager.readInput();
                if (InputManager.isEndOfFile()) {
                    return null;
                }
                if (consoleRead == null || consoleRead.trim().isEmpty()) throw new InvalidInputException("");
                BigDecimal labelCheck = new BigDecimal(consoleRead);
                if (consoleRead.trim().length() > 310 || labelCheck.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) > 0 || labelCheck.compareTo(BigDecimal.valueOf(Double.MIN_VALUE)) < 0) {
                    throw new InvalidInputException("");
                }

                double sales = Double.parseDouble(Objects.requireNonNull(consoleRead));

                if (sales <= 0) {
                    throw new InvalidInputException("");
                }
                band.setLabel(new Label(sales));
                flag = false;
            } catch (InvalidInputException | NumberFormatException e) {
                System.out.println("Поле label должно быть положительным числом типа double от -4.9e-324 до 1.8e+308 и не может быть null!");
            }
        } while(flag);
        band.setCreationDate(LocalDateTime.now());
        band.setId(CollectionManager.getMaxId() + 1);
        return band;
    }
}
