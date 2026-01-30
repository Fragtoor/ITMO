package moves;
import ru.ifmo.se.pokemon.*;

public class PoisonMoveSp extends SpecialMove {
    public PoisonMoveSp(Type type, double pow, double acc) {
        super(type, pow, acc);
    }
    @Override
    protected void applyOppDamage(Pokemon defender, double damage) {
        double modifiedDamage = damage;

        // +75% урона против водяных покемонов
        for (Type defenderType : defender.getTypes()) {
            if (defenderType == Type.WATER) {
                modifiedDamage *= 1.75;
                break;
            }
        }
        super.applyOppDamage(defender, modifiedDamage);
    }
}
