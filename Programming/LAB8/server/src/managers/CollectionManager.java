package managers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import common.exceptions.InvalidInputException;
import common.models.MusicBand;

public class CollectionManager {
    private Set<MusicBand> collection;
    private LocalDateTime creationDate;

    public CollectionManager(Set<MusicBand> collection) {
        this.collection = collection;
    }

    public ArrayList<MusicBand> filterContainsName(String name, int userId) {
        ArrayList<MusicBand> result = collection.stream()
                .filter(elem -> elem.getName().toLowerCase().contains(name.toLowerCase()))
                .sorted(Comparator.comparing(MusicBand::getName))
                .collect(Collectors.toCollection(ArrayList::new));;
        return result;
    }

    public synchronized String averageOfNumberOfParticipants() {
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

    public synchronized Set<MusicBand> removeGreater(MusicBand band) {
        if (band == null) throw new InvalidInputException("MusicBand был создан не до конца");
        band.setCreationDate(LocalDateTime.now());
        band.setId(getMaxId() + 1);

        return collection.stream()
                .filter(elem -> elem.compareTo(band) > 0)
                .collect(Collectors.toCollection(ConcurrentSkipListSet::new));
    }

    public synchronized MusicBand addIfMin(MusicBand band) {
        MusicBand minBand = collection.stream()
                .min(MusicBand::compareTo)
                .orElse(null);

        if (minBand == null || band.compareTo(minBand) <= 0) {
            return band;
        } else {
            return null;
        }
    }

    public String help() {
        return """
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
                - register : зарегистрироваться""";
    }

    public void removeById(Integer id) {
        collection.removeIf(b -> Objects.equals(b.getId(), id));
    }

    public String info() {
        String message = "Информация о коллекции:\n";
        message += "Тип: ConcurrentSkipListSet\n";
        message += "Дата инициализации: " + creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "\n";
        message += "Количество элементов: " + collection.size();
        return message;
    }

    public synchronized String add(MusicBand band) {
        collection.add(band);
        return "Создание MusicBand завершено!";
    }

    public String clear(int userId) {
        collection.removeIf(band -> band.getOwnerId() == userId);
        return "Из коллекции удалены собственные элементы!";
    }

    public String clearAll() {
        collection.clear();
        return "Коллекция очищена!";
    }

    public ArrayList<MusicBand> show(int userId) {
        ArrayList<MusicBand> col = new ArrayList<>(collection).stream().peek(band -> band.setIsOwner(band.getOwnerId() == userId)).collect(Collectors.toCollection(ArrayList::new));;
        return col;
    }

    public synchronized void update(int id, MusicBand band) {
        Optional<MusicBand> b2 = collection.stream().filter(b -> b.getId() == id).findFirst();
        if (b2.isPresent()) {
            collection.remove(b2.get());
            band.setId(id);
            collection.add(band);
        }
    }

    public MusicBand getBand(int id) {
        return collection.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void removeAll(Set<MusicBand> list) {
        collection.removeAll(list);
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

    public void setCreationDate(LocalDateTime date) {
        this.creationDate = date;
    }
}
