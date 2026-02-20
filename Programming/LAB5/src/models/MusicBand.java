package models;

import main_classes.Main;
import tools.CollectionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Представляет музыкальную группу с полным описанием всех характеристик.
 *
 * @author alexSIV
 * @version 1.0
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
     * Сортировка объектов по ID
     */
    @Override
    public int compareTo(MusicBand other) {
        if (other == null) return 1;
        return this.id.compareTo(other.id);
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

    public void setIdMax() {
        id = CollectionManager.getMaxId();
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

}