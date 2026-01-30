package creatures;

import java.util.ArrayList;
import java.util.List;
import items.Item;
import env.Audible;
import env.SoundType;
import exceptions.InventoryFullException;

public final class Character extends Creature implements Audible {
    private CharacterState state = CharacterState.NORMAL;
    private final int capacity = 5;
    private ArrayList<Item> inventory = new ArrayList<>();
    private final double stressTolerance;

    public Character(String name, boolean isFrozen, double stressTolerance) {
        super(name, isFrozen);
        if (isFrozen) this.state = CharacterState.DEATH;
        this.stressTolerance = stressTolerance;
    }

    public Character(String name, boolean isFrozen, double stressTolerance, Wound... wounds) {
        super(name, isFrozen, wounds);
        if (isFrozen) this.state = CharacterState.DEATH;
        this.stressTolerance = stressTolerance;
    }

    public CharacterState getState() {
        return state;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public double getStressTolerance() {
        return stressTolerance;
    }

    public void changeState(CharacterState state) {
        this.state = state;
    }

    @Override
    public SoundType makeSound() {
        if (List.of(CharacterState.NORMAL, CharacterState.DETERMINED).contains(getState())) {
            return SoundType.LAUGHTER;
        } else if (List.of(CharacterState.SHOCKED, CharacterState.PANIC, CharacterState.PARANOID).contains(getState())) {
            return SoundType.SCREAM;
        }
        return SoundType.SILENCE;
    }

    public void addToInventory(Item... items) throws InventoryFullException {
        if (inventory.size() + items.length <= capacity) {
            inventory.addAll(List.of(items));
        } else {
            throw new InventoryFullException("Рюкзак переполнен");
        }
    }

    public void hearSound(SoundType sound){
        String result = "";

        switch(sound) {
            case FAMILIAR_EARTHLY:
                result += "\033[3m - Хм, очень земные и хорошо знакомые мне звуки. Я очень поражён...\033[0m";
                changeState(CharacterState.SHOCKED);
                break;
            case INCREDIBLE_NOISE:
                result += "\033[3m - Невероятные шумы... но мы были к ним готовы.\033[0m";
                changeState(CharacterState.PARANOID);
                break;
            case UNSETTLING_WHISPER:
                result += "\033[3m - Тревожный шепот леденит душу...\033[0m";
                changeState(CharacterState.SHOCKED);
                break;
            case WIND:
                result += "\033[3m - Просто ветер... мы почти успокоились.\033[0m";
                changeState(CharacterState.THOUGHTFUL);
                break;
            case SILENCE:
                result += "\033[3m - Эта тишина меня успокаивает.\033[0m";
                changeState(CharacterState.NORMAL);
                break;
            case SCREAM:
                result += "\033[3m - Крик! Что происходит?\033[0m";
                changeState(CharacterState.PANIC);
                break;
            case BARKING:
                result += "\033[3m - Собачий лай... может, Джэк?\033[0m";
                changeState(CharacterState.THOUGHTFUL);
                break;
        }
        System.out.println(result);
    }

    public void think(String thought) {
        System.out.println(getName() + " думает: " + thought);
    }
}
