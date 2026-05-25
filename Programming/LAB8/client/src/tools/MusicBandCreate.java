package tools;

import common.models.Coordinates;
import common.models.Label;
import common.models.MusicBand;
import common.models.MusicGenre;
import reader_manager.InputManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Класс для создания нового объекта MusicBand в интерактивном режиме.
 */
public class MusicBandCreate {
    /**
     * Создание объекта {@link MusicBand} интерактивным способом
     *
     * @return Возвращает созданный объект {@link MusicBand}
     */
    public static MusicBand create(){
        MusicBand.Builder band = new MusicBand.Builder();
        boolean flag = true;
        do {
            String consoleRead = InputManager.readInput();

            if (consoleRead == null) return null; // Обрыв файла или Ctrl+D

            if (consoleRead.trim().isEmpty()) {
                continue;
            }
            band.name(consoleRead.trim());
            flag = false;
        } while(flag);

        flag = true;
        int X = 0;
        do {
            String consoleRead = InputManager.readInput();

            if (consoleRead == null) return null;
            if (consoleRead.trim().isEmpty()) break;

            long xCheck = Long.parseLong(consoleRead.trim());
            if (consoleRead.trim().length() > 11 || xCheck > Integer.MAX_VALUE || xCheck < Integer.MIN_VALUE) {
                continue;
            }
            X = Integer.parseInt(consoleRead.trim());
            flag = false;
        } while(flag);

        flag = true;
        do {
            String consoleRead = InputManager.readInput();

            if (consoleRead == null) return null;
            if (consoleRead.trim().isEmpty()) continue;

            BigInteger yCheck = new BigInteger(consoleRead.trim());
            if (consoleRead.trim().length() > 28 || yCheck.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 || yCheck.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
                continue;
            }
            Long Y = Long.parseLong(consoleRead.trim());
            band.coordinates(new Coordinates(X, Y));
            flag = false;

        } while(flag);

        flag = true;
        do {
            String consoleRead = InputManager.readInput();

            if (consoleRead == null) return null;
            if (consoleRead.trim().isEmpty()) continue;

            long numberOfParticipantsCheck = Long.parseLong(consoleRead.trim());
            if (consoleRead.trim().length() > 11 || numberOfParticipantsCheck > Integer.MAX_VALUE || numberOfParticipantsCheck < Integer.MIN_VALUE) {
                continue;
            }
            int numberOfParticipants = Integer.parseInt(consoleRead.trim());
            if (numberOfParticipants <= 0) {
                continue;
            }
            band.numberOfParticipants(numberOfParticipants);
            flag = false;

        } while(flag);

        flag = true;
        do {
            String consoleRead = InputManager.readInput();

            if (consoleRead == null) return null;
            if (consoleRead.trim().isEmpty()) break;

            long albumsCount = Long.parseLong(consoleRead.trim());
            if (albumsCount <= 0) {
                continue;
            }
            BigInteger albumsCountCheck = new BigInteger(consoleRead.trim());
            if (consoleRead.trim().length() > 28 || albumsCountCheck.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0 || albumsCountCheck.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
                continue;
            }
            band.albumsCount(albumsCount);
            flag = false;
        } while(flag);

        flag = true;
        do {
            String consoleRead = InputManager.readInput();
            if (consoleRead == null) return null;
            if (consoleRead.trim().isEmpty()) {
                continue;
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate date = LocalDate.parse(consoleRead.trim(), formatter);
            band.establishmentDate(date);
            flag = false;
        } while(flag);

        flag = true;
        do {
            String consoleRead = InputManager.readInput();

            if (consoleRead == null) return null;
            if (consoleRead.trim().isEmpty()) continue;

            band.genre(MusicGenre.valueOf(consoleRead.trim().toUpperCase()));
            flag = false;
        } while(flag);

        flag = true;
        do {
            String consoleRead = InputManager.readInput();

            if (consoleRead == null) return null;
            if (consoleRead.trim().isEmpty()) continue;

            BigDecimal labelCheck = new BigDecimal(consoleRead.trim());
            if (consoleRead.trim().length() > 310 || labelCheck.compareTo(BigDecimal.valueOf(Double.MAX_VALUE)) > 0 || labelCheck.compareTo(BigDecimal.valueOf(Double.MIN_VALUE)) < 0) {
                continue;
            }

            double sales = Double.parseDouble(consoleRead.trim());

            if (sales <= 0) {
                continue;
            }
            band.label(new Label(sales));
            flag = false;
        } while(flag);

        return band.build();
    }
}