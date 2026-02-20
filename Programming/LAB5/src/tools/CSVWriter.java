package tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * Класс для записи коллекции MusicBand в CSV файл.
 *
 * @author alexSIV
 * @version 1.0
 */
public class CSVWriter {
    private static final String FILE_NAME = main_classes.Main.FILE_NAME;
    /**
     * Запись содержимого коллекции в csv файл
     */
    public static void write() {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            Path path = Paths.get(FILE_NAME);

            if (Files.exists(path)) {
                if (!Files.isWritable(path)) {
                    throw new AccessDeniedException("Отсутствуют права на запись в файл!\n");
                }
            } else throw new FileNotFoundException();

            fos = new FileOutputStream(FILE_NAME);
            bos = new BufferedOutputStream(fos);
            String head = "id,name,coordinates_x,coordinates_y,creationDate,numberOfParticipants,albumsCount,establishmentDate,genre,label_sales\n";
            byte[] bytesHead = head.getBytes(StandardCharsets.UTF_8);
            bos.write(bytesHead);
            for (var elem : main_classes.Main.collection) {
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
        } catch (AccessDeniedException e) {
                System.out.println(e.getMessage() + "\n");
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + FILE_NAME + "\n");
        } catch (IOException e) {
            System.out.println("Ошибка записи данных в файл!\n");
        }
    }
}