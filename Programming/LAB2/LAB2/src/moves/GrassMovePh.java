package moves;
import ru.ifmo.se.pokemon.*;

public class GrassMovePh extends PhysicalMove {
    public GrassMovePh(Type type, double pow, double acc) {
        super(type, pow, acc);
    }
    @Override
    protected void applyOppDamage(Pokemon defender, double damage) {
        double modifiedDamage = damage;

        // +70% урона против нормальных покемонов
        for (Type defenderType : defender.getTypes()) {
            if (defenderType == Type.NORMAL) {
                modifiedDamage *= 1.7;
                break;
            }
        }
        super.applyOppDamage(defender, modifiedDamage);
    }
}
