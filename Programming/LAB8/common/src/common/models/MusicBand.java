package common.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class MusicBand implements Comparable<MusicBand>, Serializable {
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private int numberOfParticipants;
    private Long albumsCount;
    private LocalDate establishmentDate;
    private MusicGenre genre;
    private Label label;
    private int ownerId;
    private boolean isOwner;

    private MusicBand(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.coordinates = builder.coordinates;
        this.creationDate = builder.creationDate;
        this.numberOfParticipants = builder.numberOfParticipants;
        this.albumsCount = builder.albumsCount;
        this.establishmentDate = builder.establishmentDate;
        this.genre = builder.genre;
        this.label = builder.label;
        this.ownerId = builder.ownerId;
        this.isOwner = builder.isOwner;
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Coordinates coordinates;
        private LocalDateTime creationDate;
        private int numberOfParticipants;
        private Long albumsCount;
        private LocalDate establishmentDate;
        private MusicGenre genre;
        private Label label;
        private int ownerId;
        private boolean isOwner;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder coordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
            return this;
        }

        public Builder creationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder numberOfParticipants(int numberOfParticipants) {
            this.numberOfParticipants = numberOfParticipants;
            return this;
        }

        public Builder albumsCount(Long albumsCount) {
            this.albumsCount = albumsCount;
            return this;
        }

        public Builder establishmentDate(LocalDate establishmentDate) {
            this.establishmentDate = establishmentDate;
            return this;
        }

        public Builder genre(MusicGenre genre) {
            this.genre = genre;
            return this;
        }

        public Builder label(Label label) {
            this.label = label;
            return this;
        }

        public Builder ownerId(int ownerId) {
            this.ownerId = ownerId;
            return this;
        }

        public Builder isOwner(boolean isOwner) {
            this.isOwner = isOwner;
            return this;
        }

        public MusicBand build() {
            return new MusicBand(this);
        }
    }

    @Override
    public int compareTo(MusicBand other) {
        if (other == null) return 1;
        int nameCompare = this.getName().compareTo(other.getName());
        if (nameCompare != 0) {
            return nameCompare;
        }
        return this.getId().compareTo(other.getId());
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Coordinates getCoordinates() { return coordinates; }
    public void setCoordinates(Coordinates coordinates) { this.coordinates = coordinates; }

    public LocalDateTime getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate; }

    public int getNumberOfParticipants() { return numberOfParticipants; }
    public void setNumberOfParticipants(int numberOfParticipants) { this.numberOfParticipants = numberOfParticipants; }

    public Long getAlbumsCount() { return albumsCount; }
    public void setAlbumsCount(Long albumsCount) { this.albumsCount = albumsCount; }

    public LocalDate getEstablishmentDate() { return establishmentDate; }
    public void setEstablishmentDate(LocalDate establishmentDate) { this.establishmentDate = establishmentDate; }

    public MusicGenre getGenre() { return genre; }
    public void setGenre(MusicGenre genre) { this.genre = genre; }

    public Label getLabel() { return label; }
    public void setLabel(Label label) { this.label = label; }

    public int getOwnerId() { return ownerId; }
    public void setOwnerId(int ownerId) { this.ownerId = ownerId; }

    public boolean isOwner() { return isOwner; }
    public void setIsOwner(boolean isOwner) { this.isOwner = isOwner; }

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

    @Override
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicBand band = (MusicBand) o;
        return Objects.equals(id, band.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean validate() {
        if (name == null || name.trim().isEmpty()) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        if (numberOfParticipants <= 0) return false;
        if ((albumsCount != null) && albumsCount <= 0) return false;
        if (establishmentDate == null) return false;
        if (genre == null) return false;
        return label != null && label.validate();
    }
}