package models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Представляет музыкальную группу с полным описанием всех характеристик.
 */
public class MusicBand implements Comparable<MusicBand> {
    /**
     * Уникальный идентификатор группы.
     * Поле не может быть null, значение должно быть больше 0.
     * Генерируется автоматически.
     */
    private Integer id;
    /**
     * Название группы.
     * Поле не может быть null, строка не может быть пустой.
     */
    private String name;
    /**
     * Координаты группы.
     * Поле не может быть null.
     */
    private Coordinates coordinates;
    /**
     * Дата и время создания записи о группе.
     * Поле не может быть null, генерируется автоматически.
     */
    private java.time.LocalDateTime creationDate;
    /**
     * Количество участников группы.
     * Значение поля должно быть больше 0.
     */
    private int numberOfParticipants;
    /**
     * Количество выпущенных альбомов.
     * Поле может быть null. Значение должно быть больше 0.
     */
    private Long albumsCount;
    /**
     * Дата основания группы.
     * Поле не может быть null.
     */
    private java.time.LocalDate establishmentDate;
    /**
     * Музыкальный жанр группы.
     * Поле не может быть null.
     */
    private MusicGenre genre;
    /**
     * Информация о лейбле группы.
     * Поле не может быть null.
     */
    private Label label;
    /**
     * Создание объекта {@link MusicBand} без значения без параметров
     */
    public MusicBand() {
        super();
    }
    /**
     * Создание объекта {@link MusicBand} со всеми параметрами
     *
     * @param id уникальный идентификатор группы
     * @param name название группы
     * @param coordinates координаты группы
     * @param creationDate дата и время создания записи о группе
     * @param numberOfParticipants количество участников группы
     * @param albumsCount количество выпущенных альбомов
     * @param establishmentDate дата основания группы
     * @param genre музыкальный жанр группы
     * @param label информация о лейбле группы
     */
    public MusicBand(Integer id, String name, Coordinates coordinates, LocalDateTime creationDate, int numberOfParticipants, Long albumsCount, LocalDate establishmentDate, MusicGenre genre, Label label) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.numberOfParticipants = numberOfParticipants;
        this.albumsCount = albumsCount;
        this.establishmentDate = establishmentDate;
        this.genre = genre;
        this.label = label;
    }
    /**
     * Создание объекта {@link MusicBand}, используя поля другого объекта {@link MusicBand}
     *
     * @param band другой объект {@link MusicBand}
     */
    public MusicBand(MusicBand band) {
        this.setId(band.getId());
        this.setName(band.getName());
        this.setCoordinates(band.getCoordinates());
        this.setCreationDate(band.getCreationDate());
        this.setNumberOfParticipants(band.getNumberOfParticipants());
        this.setAlbumsCount(band.getAlbumsCount());
        this.setEstablishmentDate(band.getEstablishmentDate());
        this.setGenre(band.getGenre());
        this.setLabel(band.getLabel());
    }

    /**
     * Сортировка объектов по {@code name}
     *
     * @param other другой объект {@link MusicBand}
     *
     * @return Возвращает результат сравнения
     */
    @Override
    public int compareTo(MusicBand other) {
        if (other == null) return 1;
        return this.getName().compareTo(other.getName());
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public Long getAlbumsCount() {
        return albumsCount;
    }

    public LocalDate getEstablishmentDate() {
        return establishmentDate;
    }

    public MusicGenre getGenre() {
        return genre;
    }

    public Label getLabel() {
        return label;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public void setAlbumsCount(Long albumsCount) {
        this.albumsCount = albumsCount;
    }

    public void setEstablishmentDate(LocalDate establishmentDate) {
        this.establishmentDate = establishmentDate;
    }

    public void setGenre(MusicGenre genre) {
        this.genre = genre;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public void setFields(MusicBand band) {
        this.setName(band.getName());
        this.setCoordinates(band.getCoordinates());
        this.setCreationDate(band.getCreationDate());
        this.setNumberOfParticipants(band.getNumberOfParticipants());
        this.setAlbumsCount(band.getAlbumsCount());
        this.setEstablishmentDate(band.getEstablishmentDate());
        this.setGenre(band.getGenre());
        this.setLabel(band.getLabel());
    }

    public String toString() {
        return "MusicBand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", numberOfParticipants=" + numberOfParticipants +
                ", albumsCount=" + albumsCount +
                ", establishmentDate=" + establishmentDate +
                ", genre=" + genre +
                ", label=" + label +
                '}';
    }
    /**
     * Сравнивание объектов по {@code id}
     *
     * @param o другой объект
     *
     * @return Возвращает результат сравнения
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicBand band = (MusicBand) o;
        return Objects.equals(id, band.id);
    }

    public int hashCode() {
        return Objects.hash(id);
    }

}