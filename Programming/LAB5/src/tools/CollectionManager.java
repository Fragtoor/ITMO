package tools;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.*;

import exceptions.InvalidInputException;
import exceptions.RecursiveCallException;
import history_commands.*;
import main_classes.ApplicationContext;
import models.MusicBand;
import models.MusicBandCreate;
import reader_manager.CommandManager;
import reader_manager.InputManager;
import reader_manager.Reader;
/**
 * Менеджер коллекции - реализует все команды для работы с коллекцией MusicBand.
 */
public class CollectionManager {
    /**
     * Множество для отслеживания открытых файлов
     */
    private static HashSet<String> openedScripts = new HashSet<>();
    /**
     * Реализация команды {@code back}
     *
     * @param n количество команд, который нужно откатить
     */
    public static void back(int n) {
        if (n > ApplicationContext.commandsList.size()) {
            System.out.println("Было выполнено только " + ApplicationContext.commandsList.size() + " команд\n");
            return;
        }
        for (int i = 0; i < n; ++i) {
            switch (ApplicationContext.commandsList.peek().getCommandName()) {
                case ("add"): {
                    CommandManager.commands.get("add").undo();
                    break;
                }
                case ("add_if_min"): {
                    CommandManager.commands.get("add_if_min").undo();
                    break;
                }
                case ("update"): {
                    CommandManager.commands.get("update").undo();
                    break;
                }
                case ("remove_by_id"): {
                    CommandManager.commands.get("remove_by_id").undo();
                    break;
                }
                case ("clear"): {
                    CommandManager.commands.get("clear").undo();
                    break;
                }
                case ("remove_greater"): {
                    CommandManager.commands.get("remove_greater").undo();
                    break;
                }
                case ("save"): {
                    CommandManager.commands.get("save").undo();
                    break;
                }
            }
            ApplicationContext.commandsList.pop();
        }
        System.out.println("Были отклонены последние " + n + " команд\n");
    }

    /**
     * Реализация команды {@code history}
     */
    public static void history() {
        ArrayList<HistoryCommand> listReverse = new ArrayList<>(ApplicationContext.commandsList);
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
            System.out.println(c + ") " + elem.getCommandName());
            c++;
        }
        System.out.println();
    }

    /**
     * Реализация команды {@code filter_contains_name}
     *
     * @param name подстрока, которую могут иметь объекты {@link models.MusicBand} в поле {@code name}
     */
    public static void filterContainsName(String name) {
        int cnt = 1;
        for (var elem: getList()) {
            if (elem.getName().toLowerCase().contains(name.toLowerCase())) {
                System.out.println(cnt + ") " + elem);
                cnt++;
            }
        }
        System.out.println("\n");
        if (cnt == 1) {
            System.out.println("Таких элементов не нашлось\n");
        }
    }
    /**
     * Реализация команды {@code average_of_number_of_participants}
     */
    public static void averageOfNumberOfParticipants() {
        long result = 0L;
        int cnt = 0;
        for (var elem: getList()) {
            result += elem.getNumberOfParticipants();
            cnt++;
        }
        System.out.printf("Среднее значение поля numberOfParticipants для всех элементов коллекции равно %.2f%n \n", result / (cnt * 1.0));
    }
    /**
     * Реализация команды {@code sum_of_number_of_participants}
     */
    public static void sumOfNumberOfParticipants() {
        long result = 0L;
        for (var elem: getList()) {
            result += elem.getNumberOfParticipants();
        }
        System.out.println("Сумма значений поля numberOfParticipants для всех элементов коллекции равна " + result + "\n");
    }
    /**
     * Реализация команды {@code remove_greater}
     *
     * @return Возвращает список удаленных объектов
     */
    public static LinkedHashSet<MusicBand> removeGreater() {
        MusicBand band = MusicBandCreate.create();
        if (band == null) throw new InvalidInputException("MusicBand был создан не до конца\n");
        LinkedHashSet<MusicBand> list2 = new LinkedHashSet<>();
        for (var elem: getList()) {
            if (elem.compareTo(band) <= 0) {
                list2.add(elem);
            }
        }
        ApplicationContext.collection = new LinkedHashSet<>(list2);
        System.out.println("Из коллекции были удалены элементы, меньшие заданного!\n");
        return list2;
    }
    /**
     * Реализация команды {@code add_if_min}
     *
     * @return Возвращает {@code true}, если объект {@link models.MusicBand} добавился в коллекцию, иначе {@code false}
     */
    public static boolean addIfMin() {
        MusicBand band = MusicBandCreate.create();
        if (band == null) throw new InvalidInputException("MusicBand был создан не до конца\n");
        boolean flag = true;
        for (var elem: getList()) {
            if (band.compareTo(elem) > 0) {
                flag = false;
                break;
            }
        }
        if (flag) {
            getList().add(band);
            System.out.println("Элемент добавлен в коллекцию!\n");
            return true;
        } else {
            System.out.println("Элемент не добавлен в коллекцию\n");
            return false;
        }
    }
    /**
     * Реализация команды {@code exit}
     */
    public static void exit() {
        System.out.println("Хорошо");
        System.exit(0);
    }
    /**
     * Реализация команды {@code execute_script}
     *
     * @param fileName имя файла, скрипт в котором нужно исполнить
     */
    public static void executeScript(String fileName) {
        String absolutePath;
        try {
            absolutePath = new File(fileName).getCanonicalPath();
        } catch (IOException e) {
            absolutePath = fileName;
        }
        TransactionManager tm = new TransactionManager();
        try {
            if (openedScripts.contains(absolutePath)) {
                throw new RecursiveCallException("Обнаружена рекурсия! Файл " + fileName + " уже выполняется.\n");
            }

            openedScripts.add(absolutePath);

            tm.beginTransaction();
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(isr);

            Scanner fileScanner = new Scanner(reader);
            InputManager.setFileInput(fileScanner);

            System.out.println("Выполнение скрипта: " + fileName);

            while (InputManager.consoleRead.hasNextLine()) {
                String line = fileScanner.nextLine();
                System.out.println("> " + line);
                Reader.getLine(line);

            }
            System.out.println("Команды из файла исполнены!\n");

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + fileName + "\n");
        } catch (Exception e) {
            tm.rollback();
            System.out.println("Произошла ошибка: " + e.getMessage());
            System.out.println("Все изменения были отменены\n");
        }
        finally {
            openedScripts.remove(absolutePath);
            if (openedScripts.isEmpty()) InputManager.restoreConsoleInput();
        }
    }
    /**
     * Реализация команды {@code save}
     */
    public static void save() {
        try {
            String content = Files.readString(Paths.get(ApplicationContext.FILE_NAME));
            ApplicationContext.historyFileContent.add(content);
            System.out.println(content);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        FileManager.saveCollection(ApplicationContext.FILE_NAME);
        System.out.println("Коллекция записана в файл " + ApplicationContext.FILE_NAME + "!\n");
    }
    /**
     * Реализация команды {@code help}
     */
    public static void help() {
        String helpMessage = """
                Справка по доступным командам:
                
                - help : получить справку по доступным командам
                - info : получить информацию о коллекции (тип, дата инициализации, количество элементов)
                - show : получить все элементы коллекции в строковом представлении
                - add : добавить новый элемент в коллекцию
                - update id : обновить значение элемента коллекции, id которого равен заданному
                - remove_by_id id : удалить элемент из коллекции по его id
                - clear : очистить коллекцию
                - save : сохранить коллекцию в файл
                - execute_script file_name : считать и исполнить скрипт из указанного файла
                - exit : завершить программу (без сохранения в файл)
                - add_if_min : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
                - remove_greater : удалить из коллекции все элементы, превышающие заданный
                - history : вывести последние 10 команд (без их аргументов)
                - sum_of_number_of_participants : вывести сумму значений поля numberOfParticipants для всех элементов коллекции
                - average_of_number_of_participants : вывести среднее значение поля numberOfParticipants для всех элементов коллекции
                - filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку
                - back n : отмена последних n команд
                """;
        System.out.println(helpMessage);
    }

    /**
     * Реализация команды {@code remove_by_id}
     *
     * @param id {@code id} объекта {@link models.MusicBand}, который нужно удалить
     *
     * @return Возвращает удаленный объект {@link models.MusicBand}
     */
    public static MusicBand removeById(Integer id) {
        MusicBand band = null;
        for (var elem: getList()) {
            if (elem.getId().equals(id)) {
                band = elem;
            }
        }

        if (band != null) {
            getList().remove(band);
            System.out.println("Элемент с id " + id + " удалён\n");
        } else {
            System.out.println("Элемента с id " + id + " не существует\n");
        }
        return band;

    }
    /**
     * Реализация команды {@code info}
     */
    public static void info() {
        System.out.println("Информация о коллекции:");
        System.out.println("Тип: LinkedHashSet");
        System.out.println("Дата инициализации: " + ApplicationContext.time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        System.out.println("Количество элементов: " + getList().size() + "\n");
    }
    /**
     * Реализация команды {@code add}
     */
    public static void add() {
        MusicBand band = MusicBandCreate.create();
        if (band == null) throw new InvalidInputException("MusicBand был создан не до конца\n");
        getList().add(band);
        System.out.println("Создание MusicBand завершено!\n");
    }
    /**
     * Реализация команды {@code clear}
     */
    public static void clear() {
        getList().clear();
        System.out.println("Коллекция очищена!\n");
    }
    /**
     * Реализация команды {@code show}
     */
    public static void show() {
        if (getList().isEmpty()) {
            System.out.println("Коллекция пуста\n");
        } else {
            System.out.println("Элементы коллекции");
            ArrayList<MusicBand> listSorted = new ArrayList<>(getList());
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
     * Реализация команды {@code update}
     *
     * @param id {@code id} объекта {@link models.MusicBand}, который нужно обновить
     *
     * @return Возвращает обновлённый объект {@link models.MusicBand}
     */
    public static MusicBand update(int id) {
        MusicBand currentBand = null;
        for (var elem: getList()) {
            if (elem.getId().equals(id)) {
                currentBand = elem;
                break;
            }
        }
        if (!(currentBand == null)) {
            MusicBand band = MusicBandCreate.create();
            if (band == null) throw new InvalidInputException("MusicBand был создан не до конца\n");
            MusicBand currentBandCopy = new MusicBand(currentBand);
            currentBand.setFields(band);
            System.out.println("Объект с id " + id + " был изменён.\n");
            return currentBandCopy;
        } else {
            System.out.println("Объект с указанным id не найден.\n");
            return null;
        }
    }

    /**
     * Нахождение максимального {@code id} в {@code collection}
     *
     * @return Возвращает максимальный {@code id} в коллекции
     */
    public static int getMaxId() {
        int id = 1;
        for (var elem: getList()) {
            if (elem.getId() > id) {
                id = elem.getId();
            }
        }
        return id;
    }
    /**
     * Получение актуального значения {@code collection}
     *
     * @return Возвращает актуальную ссылку на {@code collection}
     */
    private static LinkedHashSet<MusicBand> getList() {
        return ApplicationContext.collection;
    }
}
