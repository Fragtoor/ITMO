package managers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import commands.*;
import common.exceptions.InvalidInputException;
import common.models.MusicBand;

public class CollectionManager {

    private LinkedHashSet<MusicBand> collection;

    private final LocalDateTime time = LocalDateTime.now();

    private LinkedHashSet<MusicBand> backupCollection;

    private Stack<Command> commandsList = new Stack<>();

    public String back(int n) {
        if (n > commandsList.size()) {
            return "Было выполнено только " + commandsList.size() + " команд\n";
        }

        commandsList.stream()
                .limit(n)
                .forEach(command -> {
                    switch (command.getCommandName()) {
                        case "add", "clear", "remove_greater", "remove_by_id", "update", "add_if_min" -> command.undo();
                    }
                });

        for (int i = 0; i < n; i++) {
            commandsList.pop();
        }
        return "Были отклонены последние " + n + " команд\n";
    }

    public String history() {
        ArrayList<Command> listReverse = new ArrayList<>(commandsList);
        Collections.reverse(listReverse);
        StringBuilder result = new StringBuilder();;
        int cnt = listReverse.size();
        if (cnt == 0) return "История команд пуста\n";
        if (cnt <= 9) {
            result.append("Последние ").append(cnt).append(" команд:\n");
        } else {
            result.append("Последние 10 команд:\n");
        }

        AtomicInteger c = new AtomicInteger(1);
        String history = listReverse.stream()
                .limit(Math.max(listReverse.size(), 10))
                .map(elem -> c.getAndIncrement() + ") " + elem.getCommandName())
                .collect(Collectors.joining("\n", "", "\n"));
        result.append(history).append("\n");
        return result.toString();
    }

    public String filterContainsName(String name) {
        StringBuilder result = new StringBuilder();
        AtomicInteger cnt = new AtomicInteger(1);
        collection.stream()
                .filter(elem -> elem.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted(Comparator.comparing(MusicBand::getName))
                .forEach(elem -> {
                    result.append(cnt.getAndIncrement()).append(") ").append(elem).append("\n");
                });
        if (cnt.get() == 1) {
            result.append("Таких элементов не нашлось\n");
        }
        return result.toString();
    }

    public String averageOfNumberOfParticipants() {
        long result = 0L;
        AtomicInteger count = new AtomicInteger(0);
        long totalParticipants = collection.stream()
                .peek(elem -> count.incrementAndGet())
                .mapToLong(MusicBand::getNumberOfParticipants)
                .sum();

        result += totalParticipants;
        return String.format("%.2f", result / (count.get() * 1.0));
    }

    public String sumOfNumberOfParticipants() {
        long result = collection.stream()
                .mapToLong(elem -> elem.getNumberOfParticipants())
                .sum();
        return "Сумма значений поля numberOfParticipants для всех элементов коллекции равна " + result + "\n";
    }

    public LinkedHashSet<MusicBand> removeGreater(MusicBand band) {
        if (band == null) throw new InvalidInputException("MusicBand был создан не до конца\n");
        band.setCreationDate(LocalDateTime.now());
        band.setId(getMaxId() + 1);

        LinkedHashSet<MusicBand> list2 = collection.stream()
                .filter(elem -> elem.compareTo(band) > 0)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        collection = collection.stream()
                .filter(elem -> elem.compareTo(band) <= 0)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return list2;
    }
    /**
     * Реализация команды {@code add_if_min}
     *
     * @return Возвращает {@code true}, если объект {@link MusicBand} добавился в коллекцию, иначе {@code false}
     */
    public boolean addIfMin(MusicBand band) {
        band.setCreationDate(LocalDateTime.now());
        band.setId(getMaxId() + 1);
        AtomicInteger count = new AtomicInteger(0);
        collection.stream().filter(elem -> elem.compareTo(elem) > 0)
                .forEach(elem -> count.incrementAndGet());

        if (count.get() == 0) {
            collection.add(band);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Реализация команды {@code help}
     */
    public String help() {
        String helpMessage = """
                Справка по доступным командам:
                
                - help : получить справку по доступным командам
                - info : получить информацию о коллекции (тип, дата инициализации, количество элементов)
                - show : получить все элементы коллекции в строковом представлении
                - add : добавить новый элемент в коллекцию
                - update id : обновить значение элемента коллекции, id которого равен заданному
                - remove_by_id id : удалить элемент из коллекции по его id
                - clear : очистить коллекцию
                - execute_script file_name : считать и исполнить скрипт из указанного файла
                - exit : завершить программу (без сохранения в файл)
                - add_if_min : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
                - remove_greater : удалить из коллекции все элементы, превышающие заданный
                - history : вывести последние 10 команд (без их аргументов)
                - sum_of_number_of_participants : вывести сумму значений поля numberOfParticipants для всех элементов коллекции
                - average_of_number_of_participants : вывести среднее значение поля numberOfParticipants для всех элементов коллекции
                - filter_contains_name name : вывести элементы, значение поля name которых содержит заданную подстроку
                - back n : отмена последних n команд
                - login : войти в аккаунт
                - register : зарегистрироваться
                """;
        return helpMessage;
    }

    /**
     * Реализация команды {@code remove_by_id}
     *
     * @param id {@code id} объекта {@link MusicBand}, который нужно удалить
     *
     * @return Возвращает удаленный объект {@link MusicBand}
     */
    public MusicBand removeById(Integer id) {
        MusicBand band = collection.stream()
                .filter(elem -> elem.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (band != null) collection.remove(band);
        return band;
    }
    /**
     * Реализация команды {@code info}
     */
    public String info() {
        String result = "";
        result += "Информация о коллекции:\n";
        result += "Тип: LinkedHashSet\n";
        result += "Дата инициализации: " + time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n";
        result += "Количество элементов: " + collection.size() + "\n";
        return result;
    }
    /**
     * Реализация команды {@code add}
     */
    public String add(MusicBand band) {
        band.setCreationDate(LocalDateTime.now());
        band.setId(getMaxId() + 1);
        collection.add(band);
        return "Создание MusicBand завершено!\n";
    }
    /**
     * Реализация команды {@code clear}
     */
    public String clear() {
        collection.clear();
        return "Коллекция очищена!\n";
    }
    /**
     * Реализация команды {@code show}
     */
    public String show() {
        if (collection.isEmpty()) {
            return "Коллекция пуста\n";
        } else {
            StringBuilder result = new StringBuilder("Элементы коллекции:\n");
            ArrayList<MusicBand> listSorted = new ArrayList<>(collection);
            listSorted.sort(Comparator.comparingInt(MusicBand::getId));
            AtomicInteger i = new AtomicInteger(1);
            listSorted.stream().sorted(Comparator.comparing(MusicBand::getName)).forEach(elem -> {
                result.append(i.getAndIncrement()).append(") ").append(elem).append("\n");});
            result.append("\n");
            return result.toString();
        }
    }

    /**
     * Реализация команды {@code update}
     *
     * @param id {@code id} объекта {@link MusicBand}, который нужно обновить
     *
     * @return Возвращает обновлённый объект {@link MusicBand}
     */
    public MusicBand update(int id, MusicBand band) {
        MusicBand currentBand = collection.stream()
                .filter(elem -> elem.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (!(currentBand == null)) {
            band.setCreationDate(LocalDateTime.now());
            band.setId(getMaxId() + 1);
            MusicBand currentBandCopy = new MusicBand(currentBand);
            currentBand.setFields(band);
            return currentBandCopy;
        } else return null;
    }

    /**
     * Нахождение максимального {@code id} в {@code collection}
     *
     * @return Возвращает максимальный {@code id} в коллекции
     */
    public int getMaxId() {
        return collection.stream().mapToInt(MusicBand::getId).max().orElse(1);
    }

    public void setCollection(LinkedHashSet<MusicBand> collection) {this.collection = collection;}

    public LinkedHashSet<MusicBand> getCollection() {return collection;}

    public LinkedHashSet<MusicBand> getBackupCollection() {
        return backupCollection;
    }

    public Stack<Command> getCommandsList() {
        return commandsList;
    }

    public void setBackupCollection(LinkedHashSet<MusicBand> backupCollection) {
        this.backupCollection = backupCollection;
    }

    public void addToCommandsList(Command command) {
        commandsList.add(command);
    }
}
