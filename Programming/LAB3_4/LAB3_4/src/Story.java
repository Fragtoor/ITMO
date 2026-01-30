import creatures.*;
import creatures.Character;
import env.Environment;
import env.SoundType;
import exceptions.InventoryFullException;
import items.EquipmentItem;
import items.Item;
import items.MedicalItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Story {
    private ArrayList<Creature> determineSleighContents(Environment env, creatures.Character person1) {
        double random = Math.random();

        String[] names = {"Гедни", "Кейси", "Джонатан"};
        int index = (int)(Math.random() * names.length);
        String name = names[index];

        creatures.Character person3 = new creatures.Character(name, true, 0.3);
        Dog dog = new Dog("Джэк", true, "Хаски");

        if (random < 0.7) {
            System.out.println(
                    "\033[3m - В санях два свежезамороженных экземпляра... " +
                    person3.getName() + " и пропавшая собака " + dog.getName() +
                    ", видимо, породы " + dog.getBreed() + " - сказал " + person1.getName() + ".\n\033[0m"
            );
            env.addCreatures(person3, dog);
            return new ArrayList<>(Arrays.asList(person3, dog));
        } else {
            System.out.println(
                    "\033[3m - В санях только тело " + person3.getName() +
                    "... - сказал " + person1.getName() + ".\n\033[0m"
            );
            env.addCreatures(person3);
            return new ArrayList<>(Arrays.asList(person3));
        }
    }

    private void woundsCharacters(List<Creature> creatures) {
        for (Creature creature: creatures) {
            if (Math.random() < 0.7) {
                Wound wound = new Wound("Шея", true);
                creature.addWound(wound);
                if (Math.random() < 0.5) {
                    Wound wound2 = new Wound("Голова", false);
                    if (Math.random() < 0.5) wound2 = wound2.applyPlaster();
                    creature.addWound(wound2);
                }
            } else {
                Wound wound = new Wound("Нога", false);
                if (Math.random() < 0.3) wound = wound.applyPlaster();
                creature.addWound(wound);
            }
        }
    }

    private CharacterState determineReaction(creatures.Character character, ArrayList<Creature> findings) {
        double stressTolerance = Math.random();
        if (stressTolerance < 0.3) {
            // ПАНИКА
            System.out.println("- " + character.getName() + " впадает в панику!");
            return CharacterState.PANIC;
        } else if (stressTolerance < 0.7) {
            // ШОК
            System.out.println("- " + character.getName() + " шокирован находкой.");
            return CharacterState.SHOCKED;
        } else {
            // РЕШИМОСТЬ
            System.out.println("- " + character.getName() + " сохраняет хладнокровие.");
            return CharacterState.DETERMINED;
        }
    }

    private void handleTunnelThoughts(creatures.Character character) {
        double obsessionLevel = Math.random();
        if (character.getState() == CharacterState.PANIC) {
            character.think("\033[3mнужно бежать! Туннель не важен!\033[0m");
        } else if (obsessionLevel < 0.8) {
            character.think("\033[3mнесмотря ни на что, нужно добраться до северного туннеля...\033[0m");
        } else {
            character.think("\033[3mэта находка меняет всё... туннель может подождать.\033[0m");
        }
    }

    public void handleClimacticSound(Environment environment, creatures.Character character) {
        SoundType sound = environment.makeSound();
        System.out.println("   Внезапно появляется " + sound.getDescription() + ", и исследователи говорят:");
        character.hearSound(sound);
    }

    public void handleInventory(creatures.Character character) {
        Item[] possibleItems = {
                new MedicalItem("пластырь"),
                new EquipmentItem("фонарь"),
                new MedicalItem("аптечка"),
                new EquipmentItem("рация"),
                new MedicalItem("болеутоляющее")
        };
        int index = (int) (Math.random() * possibleItems.length);

        String result = "";
        for (Item item : possibleItems) {
            try {
                if (item != possibleItems[index]) {
                    character.addToInventory(item);
                    result += item.getItemTitle() + ", ";
                }
            } catch (InventoryFullException e) {
                System.out.println(e.getMessage());
                break;
            }
        }
        System.out.println(
                "Снаряжение исследователя " + character.getName() + ": " +
                        "\033[3m" + result.substring(0, result.length()-2) + "\033[0m");
    }

    public void runStory() {
        Environment env = new Environment("Горная местность");
        creatures.Character person1 = new creatures.Character("Лейк", false, 0.8);
        creatures.Character person2 = new Character("Кейси", false, 0.2);

        env.addCreatures(person1, person2);

        env.addSoundSource(
                SoundType.WIND, SoundType.FAMILIAR_EARTHLY,
                SoundType.INCREDIBLE_NOISE, SoundType.SILENCE,
                SoundType.SCREAM, SoundType.UNSETTLING_WHISPER
        );
        System.out.println(
                "\n\n   После всего предыдущего исследователи не очень удивились находке, \n" +
                "скажу больше -- они были почти к ней готовы. Однако когда, склонившись над санями, \n" +
                "развязали брезентовый тюк, они обнаружили нечто неожиданное... \n"
        );

        // Наполняем содержимое саней обмороженными существами
        Sleigh sleigh = new Sleigh(determineSleighContents(env, person1));

        // Даём им различные раны
        woundsCharacters(sleigh.getContent());

        // Реакция исследователей на содержимое саней
        System.out.println("   После увиденного:");
        person1.changeState(determineReaction(person1, sleigh.getContent()));
        person2.changeState(determineReaction(person2, sleigh.getContent()));

        // Мышление о туннеле
        System.out.println("");
        System.out.println(
                "   Немного придя в себя после этого неприятного зрелища,\n" +
                "исследователи начали размышлять о туннеле:"
        );
        handleTunnelThoughts(person1);
        handleTunnelThoughts(person2);
        System.out.println("");

        // НЕОЖИДАННЫЙ ЗВУК
        handleClimacticSound(env, person1);

        ArrayList<CharacterState> states = new ArrayList<>(List.of(
                CharacterState.NORMAL,
                CharacterState.THOUGHTFUL,
                CharacterState.DETERMINED
        ));
        if (states.contains(person1.getState()) & states.contains(person2.getState())) {
            System.out.println(
                    "\n   После шокирующей находки исследователи решили обновить снаряжение для продолжения похода."
            );
            handleInventory(person1);
            handleInventory(person2);
            System.out.println("Также герои захотели освободить сани, поэтому убрали из них замёрзшие тела.");
            try {
                sleigh.delCreature(sleigh.getContent().get(0));
                if (!sleigh.getContent().isEmpty()) {
                    sleigh.delCreature(sleigh.getContent().get(0));
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("\n   Пожелаем удачного похода нашим исследователям!");

        } else {
            System.out.println("\n   Исследователи решили не продолжать поход и пойти домой.");
        }
    }
}
