package creatures;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Arrays;

import env.Audible;
import env.SoundType;


abstract public class Creature implements Audible {
    private final String name;
    private boolean isFrozen;
    private ArrayList<Wound> wounds;
    
    public Creature(String name, boolean isFrozen) {
        this.name = name;
        this.isFrozen = isFrozen;
        this.wounds = new ArrayList<>();
    }
    
    public Creature(String name, boolean isFrozen, Wound... wounds) {
        this.name = name;
        this.isFrozen = isFrozen;
        this.wounds = new ArrayList<>(Arrays.asList(wounds));
    }
    
    public String getName() {return name;}
    
    public boolean getIsFrozen() {return isFrozen;}
    
    public ArrayList<Wound> getWounds() {return wounds;}

    public abstract SoundType makeSound();
    
    public void addWound(Wound wound) {
        wounds.add(wound);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        // либо instanceof попробуй
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        // Приведение типа
        Creature other = (Creature) obj;
        
        if (!Objects.equals(name, other.name)) {
            return false;
        }
        
        if (!Objects.equals(isFrozen, other.isFrozen)) {
            return false;
        }
        
        if (!Objects.equals(wounds, other.wounds)) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getIsFrozen(), getWounds());
    }

    @Override
    public String toString() {
        return String.format("Существо %s (замёрз: %s)",
                getName(), getIsFrozen());
    }
}
