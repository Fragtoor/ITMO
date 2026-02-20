package tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.*;

import main_classes.Main;
import models.MusicBand;
import models.MusicBandCreate;
import reader_manager.InputManager;
import reader_manager.Reader;
/**
 * Менеджер коллекции - реализует все команды для работы с коллекцией MusicBand.
 *
 * @author alexSIV
 * @version 1.0
 */
public class CollectionManager {
    private static LinkedHashSet<MusicBand> list = main_classes.Main.collection;
    // Множество для отслеживания открытых файлов
    private static HashSet<String> openedScripts = new HashSet<>();
    /**
     * Реализация команды history
     */
    public static void history() {
        ArrayList<String> listReverse = new ArrayList<>(Main.commandsList);
        Collections.reverse(listReverse);
        int cnt = listReverse.size();
        if (cnt <= 9) {
            System.out.println("Последние " + cnt + " команд:");
        } else {
            System.out.println("Последние 10 команд:");
        }
        int c = 1;
        for (var elem: listReverse) {
            if (c == 11) break;
            System.out.println(c + ") " + elem);
            c++;
        }
        System.out.println();
    }
    /**
     * Реализация команды filter_contains_name
     */
    public static void filterContainsName(String name) {
        int cnt = 1;
        for (var elem: list) {
            if (elem.getName().toLowerCase().contains(name.toLowerCase())) {
                System.out.println(cnt + ") " + elem);
                cnt++;
            }
        }
        if (cnt == 1) {
            System.out.println("Таких элементов не нашлось\n");
        }
    }
    /**
     * Реализация команды average_of_number_of_participants
     */
    public static void averageOfNumberOfParticipants() {
        long result = 0L;
        int cnt = 0;
        for (var elem: list) {
            result += elem.getNumberOfParticipants();
            cnt++;
        }
        System.out.printf("Среднее значение поля numberOfParticipants для всех элементов коллекции равно %.2f%n", result / (cnt * 1.0));
    }
    /**
     * Реализация команды sum_of_number_of_participants
     */
    public static void sumOfNumberOfParticipants() {
        long result = 0L;
        for (var elem: list) {
            result += elem.getNumberOfParticipants();
        }
        System.out.println("Сумма значений поля numberOfParticipants для всех элементов коллекции равна " + result + "\n");
    }
    /**
     * Реализация команды remove_greater
     */
    public static void removeGreater(MusicBand band) {
        LinkedHashSet<MusicBand> list2 = new LinkedHashSet<>();
        for (var elem: list) {
            if (elem.compareTo(band) <= 0) {
                list2.add(elem);
            }
        }
        list = list2;
        System.out.println("Из коллекции были удалены элементы, меньшие заданного!\n");
    }
    /**
     * Реализация команды add_if_min
     */
    public static void addIfMin(MusicBand band) {
        boolean flag = true;
        for (var elem: list) {
            if (band.compareTo(elem) > 0) {
                flag = false;
                break;
            }
        }
        if (flag) {
            list.add(band);
            System.out.println("Элемент добавлен в коллекцию!\n");
        } else {
            System.out.println("Элемент не добавлен в коллекцию\n");
        }
    }
    /**
     * Реализация команды exit
     */
    public static void exit() {
        System.out.println("Хорошо");
        System.exit(0);
    }
    /**
     * Реализация команды execute_script
     */
    public static void executeScript(String fileName) {
        String absolutePath;
        try {
            absolutePath = new File(fileName).getCanonicalPath();
        } catch (IOException e) {
            absolutePath = fileName;
        }

        if (openedScripts.contains(absolutePath)) {
            System.out.println("Обнаружена рекурсия! Файл " + fileName + " уже выполняется.\n");
            return;
        }

        openedScripts.add(absolutePath);

        try {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);

            Scanner fileScanner = new Scanner(reader);
            InputManager.setFileInput(fileScanner);

            System.out.println("Выполнение скрипта: " + fileName);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                System.out.println("> " + line);
                Reader.getLine(line);
            }

            System.out.println("Команды из файла исполнены!\n");

        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден: " + fileName);
        } finally {
            InputManager.restoreConsoleInput();
            openedScripts.remove(absolutePath);
        }
    }
    /**
     * Реализация команды save
     */
    public static void save() {
        CSVWriter.write();
        System.out.println("Коллекция записана в файл " + Main.FILE_NAME + "!\n");
    }
    /**
     * Реализация команды remove_by_id
     */
    public static void removeById(Integer id) {
        boolean flag = false;
        for (var elem: list) {
            if (elem.getId().equals(id)) {
                flag = true;
            }
        }
        if (flag) {
            list.removeIf((band) -> band.getId().equals(id));
            System.out.println("Элемент с id " + id + " удалён\n");
        } else {
            System.out.println("Элемента с id " + id + " не существует\n");
        }

    }
    /**
     * Реализация команды info
     */
    public static void info() {
        System.out.println("Информация о коллекции:");
        System.out.println("Тип: LinkedHashSet");
        System.out.println("Дата инициализации: " + main_classes.Main.time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("Количество элементов: " + list.size() + "\n");
    }
    /**
     * Реализация команды add
     */
    public static void add() {
        MusicBand result = MusicBandCreate.create();
        list.add(result);
        System.out.println("Создание MusicBand завершено!\n");
    }
    /**
     * Реализация команды clear
     */
    public static void clear() {
        list.clear();
        System.out.println("Коллекция очищена!\n");
    }
    /**
     * Реализация команды show
     */
    public static void show() {
        if (list.isEmpty()) {
            System.out.println("Коллекция пуста\n");
        } else {
            System.out.println("Элементы коллекции");
            ArrayList<MusicBand> listSorted = new ArrayList<>(list);
            Collections.sort(listSorted);
            int i = 1;
            for (var elem: listSorted) {
                System.out.print(i + ") ");
                System.out.println(elem);
                i += 1;
            }
            System.out.println();
        }
    }
    /**
     * Реализация команды update
     */
    public static void update(String id_elem) {
        Integer id = Integer.parseInt(id_elem);
        MusicBand currentBand = null;
        for (var elem: list) {
            if (elem.getId().equals(id)) {
                currentBand = elem;
                break;
            }
        }
        if (!(currentBand == null)) {
            MusicBand band = MusicBandCreate.create();
            list.remove(currentBand);
            band.setId(id);
            list.add(band);
            System.out.println("Объект с id " + id + " был изменён.\n");
        } else {
            System.out.println("Объект с указанным id не найден.\n");
        }
    }

    /**
     * Нахождение максимального id в {@code collection}
     */
    public static int getMaxId() {
        int id = 1;
        for (var elem: list) {
            if (elem.getId() > id) {
                id = elem.getId();
            }
        }
        return id;
    }
}
