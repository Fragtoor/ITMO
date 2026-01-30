package moves;
import ru.ifmo.se.pokemon.*;

public class WaterMoveSp extends SpecialMove {
    public WaterMoveSp(Type type, double pow, double acc) {
        super(type, pow, acc);
    }
    @Override
    protected void applyOppDamage(Pokemon defender, double damage) {
        double modifiedDamage = damage;

        // +50% урона против травяных покемонов
        for (Type defenderType : defender.getTypes()) {
            if (defenderType == Type.GRASS) {
                modifiedDamage *= 1.5;
                break;
            }
        }
        super.applyOppDamage(defender, modifiedDamage);
    }
}
