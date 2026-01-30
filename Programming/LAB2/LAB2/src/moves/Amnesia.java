package moves;
import ru.ifmo.se.pokemon.*;

public final class Amnesia extends StatusMove {
    public Amnesia() {
        super(Type.PSYCHIC, 0, 1.0);
    }
    @Override protected String describe() { return "впадает в амнезию"; }
    @Override protected void applySelfEffects(Pokemon p) {
        Effect e = new Effect().stat(Stat.SPECIAL_DEFENSE, 2);
        p.addEffect(e);
    }
}