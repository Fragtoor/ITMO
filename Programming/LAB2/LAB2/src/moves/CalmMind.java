package moves;
import ru.ifmo.se.pokemon.*;

public final class CalmMind extends StatusMove {
    public CalmMind() {
        super(Type.PSYCHIC, 0, 1.0);
    }
    @Override protected String describe() { return "успокаивает разум"; }
    @Override protected void applySelfEffects(Pokemon p) {
        Effect e = new Effect().stat(Stat.SPECIAL_ATTACK, 1).stat(Stat.SPECIAL_DEFENSE, 1);
        p.addEffect(e);
    }
}
