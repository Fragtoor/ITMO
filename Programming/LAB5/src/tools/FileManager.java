package tools;

import exceptions.InvalidFileExtensionException;
import exceptions.InvalidInputException;
import main_classes.ApplicationContext;
import models.Coordinates;
import models.Label;
import models.MusicBand;
import models.MusicGenre;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
/**
 * Менеджер файлов - реализует все команды для работы с файлами.
 */
public class FileManager {
    /**
     * Записать текст {@code content} в указанный файл {@code fileName}
     *
     * @param fileName файл, в который нужно записать контент
     * @param content само содержимое
     */
    public static void writeToFile (String fileName, String content) {
        try {
            if (!FileManager.fileExists(fileName)) {
                throw new FileNotFoundException("Укажите правильный путь к файлу!\n");
            }
            if (!FileManager.hasRightToWrite(fileName)) {
                throw new FileNotFoundException("Нет прав на запись в файл!\n");
            }
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            byte[] bytesContent = content.getBytes(StandardCharsets.UTF_8);
            bos.write(bytesContent);

            bos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Сохранить коллекцию {@code collection} в указанный файл {@code fileName}
     *
     * @param fileName файл, в который нужно сохранить коллекцию
     */
    public static void saveCollection(String fileName) {
        try {
            if (!FileManager.fileExists(fileName)) {
                throw new FileNotFoundException("Укажите правильный путь к файлу!\n");
            }
            if (!FileManager.hasRightToWrite(fileName)) {
                throw new FileNotFoundException("Нет прав на запись в файл!\n");
            }
            if (!FileManager.hasExtension(fileName, "csv")) {
                throw new InvalidFileExtensionException("Файл должен быть в формате csv!\n");
            }
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            String head = "id,name,coordinates_x,coordinates_y,creationDate,numberOfParticipants,albumsCount," +
                    "establishmentDate,genre,label_sales\n";
            byte[] bytesHead = head.getBytes(StandardCharsets.UTF_8);
            bos.write(bytesHead);
            for (var elem : main_classes.ApplicationContext.collection) {
                byte[] bytesID = (elem.getId() + ",").getBytes(StandardCharsets.UTF_8);
                byte[] bytesName = (elem.getName() + ",").getBytes(StandardCharsets.UTF_8);
                byte[] bytesX = (elem.getCoordinates().getX() + ",").getBytes(StandardCharsets.UTF_8);
                byte[] bytesY = (elem.getCoordinates().getY() + ",").getBytes(StandardCharsets.UTF_8);
                byte[] bytesCreationDate = (elem.getCreationDate() + ",").getBytes(StandardCharsets.UTF_8);
                byte[] bytesNumberOfParticipants = (elem.getNumberOfParticipants() + ",").getBytes(StandardCharsets.UTF_8);
                byte[] bytesAlbumsCount = (elem.getAlbumsCount() + ",").getBytes(StandardCharsets.UTF_8);
                byte[] bytesEstablishmentDate = (elem.getEstablishmentDate() + ",").getBytes(StandardCharsets.UTF_8);
                byte[] bytesGenre = (elem.getGenre() + ",").getBytes(StandardCharsets.UTF_8);
                byte[] bytesLabelSales = (elem.getLabel().getSales() + "\n").getBytes(StandardCharsets.UTF_8);
                bos.write(bytesID);
                bos.write(bytesName);
                bos.write(bytesX);
                bos.write(bytesY);
                bos.write(bytesCreationDate);
                bos.write(bytesNumberOfParticipants);
                bos.write(bytesAlbumsCount);
                bos.write(bytesEstablishmentDate);
                bos.write(bytesGenre);
                bos.write(bytesLabelSales);
            }
            bos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Прочитать коллекцию из указанного файла {@code FILE_NAME}
     *
     * @return Возвращает коллекцию, прочитанную из файла
     */
    public static LinkedHashSet<MusicBand> readCollectionFromFile(){
        LinkedHashSet<MusicBand> collection = new LinkedHashSet<>();
        String fileName = ApplicationContext.FILE_NAME;
        try {
            if (!FileManager.fileExists(fileName)) {
                throw new FileNotFoundException("Укажите правильный путь к файлу!\n");
            }
            if (!FileManager.hasRighToRead(fileName)) {
                throw new FileNotFoundException("Нет прав на чтение файла!\n");
            }
            if (!FileManager.hasExtension(fileName, "csv")) {
                throw new InvalidFileExtensionException("Файл должен быть в формате csv!\n");
            }

            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);

            String line;
            boolean isHeader = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    MusicBand band = parseLine(line);
                    collection.add(band);
                } catch (Exception e) {
                    throw new InvalidInputException("  Ошибка в строке " + lineNumber + ": " + e.getMessage());
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return collection;
    }
    /**
     * Интерпретирует каждую строку
     *
     * @param line строка csv файла, в которой есть данные для создания объекта {@link models.MusicBand}
     *
     * @return Возвращает прочитанный объект {@link models.MusicBand}
     */
    private static MusicBand parseLine(String line) {
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
    /**
     * Проверяет, есть ли права для чтения из файла {@code fileName}
     *
     * @param fileName имя файла
     *
     * @return Возвращает {@code true}, если есть права для прочтения файла, иначе {@code false}
     */
    public static boolean hasRighToRead(String fileName) {
        Path path = Paths.get(fileName);
        return Files.isReadable(path);
    }
    /**
     * Проверяет, есть ли права для записи в файл {@code fileName}
     *
     * @param fileName имя файла
     *
     * @return Возвращает {@code true}, если есть права для записи в файл, иначе {@code false}
     */
    public static boolean hasRightToWrite(String fileName) {
        Path path = Paths.get(fileName);
        return Files.isWritable(path);
    }
    /**
     * Проверяет, существует ли указанный файл {@code fileName}
     *
     * @param fileName имя файла
     *
     * @return Возвращает {@code true}, если файл существует, иначе {@code false}
     */
    public static boolean fileExists(String fileName) {
        Path path = Paths.get(fileName);
        return Files.exists(path);
    }
    /**
     * Проверяет, соответствие расширения файла {@code fileName} к расширению {@code extension}
     *
     * @param fileName имя файла
     *
     * @return Возвращает {@code true}, если расширение файла соответствует расширению {@code extension}, иначе {@code false}
     */
    public static boolean hasExtension(String fileName, String extension) {
        return fileName.toLowerCase().endsWith("." + extension.toLowerCase());
    }

}