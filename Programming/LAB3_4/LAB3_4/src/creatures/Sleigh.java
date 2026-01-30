package creatures;

import java.util.ArrayList;

public class Sleigh {
    private ArrayList<Creature> content;
    public Sleigh(ArrayList<Creature> content) {
        this.content = content;
    }

    public ArrayList<Creature> getContent() {return content;}

    public void delCreature(Creature creature) {
        if (creature == null) {
            throw new IllegalArgumentException("Существо не может быть null");
        }
        if (!content.contains(creature)) {
            throw new IllegalArgumentException("Существо " + creature.getName() + " не найдено в санях");
        }
        content.remove(creature);
    }
}
