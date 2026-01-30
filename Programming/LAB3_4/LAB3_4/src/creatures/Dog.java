package creatures;

import env.SoundType;

public final class Dog extends Creature {
    private final String breed;

    public Dog(String name, boolean isFrozen, String breed) {
        super(name, isFrozen);
        this.breed = breed;
    }

    public Dog(String name, boolean isFrozen, String breed, Wound... wounds) {
        super(name, isFrozen, wounds);
        this.breed = breed;
    }

    @Override
    public SoundType makeSound() {
        return SoundType.BARKING;
    }
    
    public String getBreed() {
        return breed;
    }
}
