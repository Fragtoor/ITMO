package tools;

import main_classes.Main;
import models.MusicBand;
import models.Coordinates;
import models.Label;
import models.MusicGenre;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
/**
 * Класс для чтения данных из CSV файла и создания коллекции MusicBand.
 *
 * @author alexSIV
 * @version 1.0
 */
public class CSVReader {
    /**
     * Чтение данных из csv файла
     */
    public static LinkedHashSet<MusicBand> read() {
        LinkedHashSet<MusicBand> collection = new LinkedHashSet<>();
        String fileName = Main.FILE_NAME;

        try {
            Path path = Paths.get(fileName);
            if (Files.exists(path)) {
                if (!Files.isReadable(path)) {
                    throw new AccessDeniedException("Отсутствуют права на чтение файла!\n");
                }
            } else throw new FileNotFoundException();

            // Создаем InputStreamReader для чтения файла
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);

            String line;
            boolean isHeader = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Пропускаем заголовок
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    MusicBand band = parseLine(line, lineNumber - 1);
                    collection.add(band);
                } catch (Exception e) {
                    System.out.println("  Ошибка в строке " + lineNumber + ": " + e.getMessage());
                }
            }

            reader.close();
        } catch (AccessDeniedException e) {
            System.out.println(e.getMessage() + "\n");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + fileName + "\n");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage() + "\n");
        }
        return collection;
    }
    /**
     * Создание объекта {@link MusicBand} на основе данных строки csv файла
     */
    private static MusicBand parseLine(String line, int lineNumber) {
        String[] parts = line.split(",");

        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }

        MusicBand band = new MusicBand();

        band.setId(Integer.parseInt(parts[0]));

        band.setName(parts[1]);

        Coordinates coords = new Coordinates();
        coords.setX(Integer.parseInt(parts[2]));
        coords.setY(Long.parseLong(parts[3]));
        band.setCoordinates(coords);

        LocalDateTime date = LocalDateTime.parse(parts[4]);
        band.setCreationDate(date);

        band.setNumberOfParticipants(Integer.parseInt(parts[5]));

        band.setAlbumsCount(Long.parseLong(parts[6]));

        band.setEstablishmentDate(LocalDate.parse(parts[7]));

        band.setGenre(MusicGenre.valueOf(parts[8]));

        Label label = new Label();
        label.setSales(Double.parseDouble(parts[9]));
        band.setLabel(label);

        return band;
    }

}
