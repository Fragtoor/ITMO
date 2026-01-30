package items;

import java.util.Objects;

abstract public class Item {
    private final String title;

    public Item (String title) {
        this.title = title;
    }

    public String getItemTitle() {return title;}

    abstract String use();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        // Приведение типа
        Item other = (Item) obj;

        if (!Objects.equals(title, other.title)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getItemTitle());
    }

    @Override
    public String toString() {
        return String.format("Предмет %s ", getItemTitle());
    }
}
