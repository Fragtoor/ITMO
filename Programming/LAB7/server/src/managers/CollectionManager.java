package managers;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import commands.*;
import common.exceptions.InvalidInputException;
import common.models.MusicBand;
import common.ui.ConsoleColors;
import dao.DAO;

public class CollectionManager {

    private Set<MusicBand> collection;

    private LocalDateTime creationDate = LocalDateTime.now();

    private Stack<Command> commandsList = new Stack<>();

    public String back(int n, DAO dao) throws SQLException {
        if (n > commandsList.size()) {
            return "Было выполнено только " + commandsList.size() + " команд\n";
        }

        int count = 0;
        ListIterator<Command> iterator = commandsList.listIterator(commandsList.size());

        while (iterator.hasPrevious() && count < n) {
            Command command = iterator.previous();
            switch (command.getCommandName()) {
                case "add", "clear", "remove_greater", "update", "add_if_min", "remove_by_id": command.undo(this, dao);
            }
            count++;
        }
        return "Были отклонены последние " + n + " команд";
    }

    public String[] history() {
        ArrayList<Command> listReverse = new ArrayList<>(commandsList);
        Collections.reverse(listReverse);
        StringBuilder details = new StringBuilder();
        String message;
        int cnt = listReverse.size();
        if (cnt == 0) return new String[] {"История команд пуста\n", ""};
        if (cnt <= 9) {
            message = "Последние " + cnt + " команд:\n";
        } else {
            message = "Последние 10 команд:\n";
        }

        AtomicInteger c = new AtomicInteger(1);
        String history = listReverse.stream()
                .limit(Math.min(listReverse.size(), 10))
                .map(elem -> c.getAndIncrement() + ") " + elem.getCommandName())
                .collect(Collectors.joining("\n", "", "\n"));
        details.append(history);
        return new String[] {message, details.toString()};
    }

    public String[] filterContainsName(String name) {
        StringBuilder result = new StringBuilder();
        AtomicInteger cnt = new AtomicInteger(1);
        collection.stream()
                .filter(elem -> elem.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted(Comparator.comparing(MusicBand::getName))
                .forEach(elem -> {
                    if (elem.isOwner()) {
                        result.append(ConsoleColors.BG_WHITE).append(cnt.getAndIncrement()).append(") ").append(elem).append(ConsoleColors.RESET).append("\n");
                    }
                    else {
                        result.append(cnt.getAndIncrement()).append(") ").append(elem).append("\n");
                    }
                });
        if (cnt.get() == 1) {
            return new String[] {"Таких элементов не нашлось", ""};
        }
        return new String[] {"", result.toString()};
    }

    public String averageOfNumberOfParticipants() {
        long result = 0L;
        AtomicInteger count = new AtomicInteger(0);
        long totalParticipants = collection.stream()
                .peek(elem -> count.incrementAndGet())
                .mapToLong(MusicBand::getNumberOfParticipants)
                .sum();

        result += totalParticipants;
        return "Среднее значение поля numberOfParticipants: " + String.format("%.2f", result / (count.get() * 1.0));
    }

    public String sumOfNumberOfParticipants() {
        long result = collection.stream()
                .mapToLong(MusicBand::getNumberOfParticipants)
                .sum();
        return "Сумма значений поля numberOfParticipants для всех элементов коллекции равна " + result;
    }

    public Set<MusicBand> removeGreater(MusicBand band) {
        if (band == null) throw new InvalidInputException("MusicBand был создан не до конца");
        band.setCreationDate(LocalDateTime.now());
        band.setId(getMaxId() + 1);

        return collection.stream()
                .filter(elem -> elem.compareTo(band) > 0)
                .filter(MusicBand::isOwner)
                .collect(Collectors.toCollection(ConcurrentSkipListSet::new));
    }

    public MusicBand addIfMin(MusicBand band) {
        MusicBand minBand = collection.stream()
                .min(MusicBand::compareTo)
                .orElse(null);

        if (minBand == null || band.compareTo(minBand) <= 0) {
            return band;
        } else {
            return null;
        }
    }

    public String[] help() {
        String message = "Справка по доступным командам:\n";
        String helpMessage = """
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
                - register : зарегистрироваться""";
        return new String[] {message, helpMessage};
    }

    public void removeById(Integer id) {
        MusicBand band = getBand(id);
        if (band != null) collection.remove(band);
    }

    public String[] info() {
        String details = "";
        String message = "Информация о коллекции:\n";
        details += "Тип: ConcurrentSkipListSet\n";
        details += "Дата инициализации: " + creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n";
        details += "Количество элементов: " + collection.size();
        return new String[] {message, details};
    }

    public String add(MusicBand band) {
        collection.add(band);
        return "Создание MusicBand завершено!";
    }

    public String clear() {
        collection.removeAll(collection.stream().filter(MusicBand::isOwner).collect(Collectors.toCollection(ConcurrentSkipListSet::new)));
        return "Коллекция очищена!";
    }

    public String[] show() {
        if (collection.isEmpty()) {
            return new String[] {"Коллекция пуста", ""};
        } else {
            String message = "Элементы коллекции:\n";
            StringBuilder result = new StringBuilder();
            ArrayList<MusicBand> listSorted = new ArrayList<>(collection);
            listSorted.sort(Comparator.comparingInt(MusicBand::getId));
            AtomicInteger cnt = new AtomicInteger(1);
            listSorted.stream().sorted(Comparator.comparing(MusicBand::getName))
                    .forEach(elem -> {
                        if (elem.isOwner()) {
                            result.append(ConsoleColors.BG_WHITE).append(cnt.getAndIncrement()).append(") ").append(elem).append(ConsoleColors.RESET).append("\n");
                        }
                        else {
                            result.append(cnt.getAndIncrement()).append(") ").append(elem).append("\n");
                        }
                    });
            return new String[] {message, result.toString()};
        }
    }

    public MusicBand update(int id, MusicBand band) {
        MusicBand currentBand = collection.stream()
                .filter(elem -> elem.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (!(currentBand == null)) {
            MusicBand currentBandCopy = new MusicBand(currentBand);
            currentBand.setFields(band);
            return currentBandCopy;
        }
        return null;
    }

    public MusicBand getBand(int id) {
        return collection.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Нахождение максимального {@code id} в {@code collection}
     *
     * @return Возвращает максимальный {@code id} в коллекции
     */
    public int getMaxId() {
        return collection.stream().mapToInt(MusicBand::getId).max().orElse(1);
    }

    public void setCollection(Set<MusicBand> collection) {this.collection = collection;}

    public Set<MusicBand> getCollection() {return collection;}

    public Stack<Command> getCommandsList() {return commandsList;}

    public void setCommandsList(Stack<Command> stack) {
        this.commandsList = stack;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void addToCommandsList(Command command) {
        commandsList.add(command);
    }

}
