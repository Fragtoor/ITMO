package moves;
import ru.ifmo.se.pokemon.*;

public final class Bulldoze extends WaterMovePh {
    public Bulldoze() {
        super(Type.GROUND, 60, 1.0);
    }
    @Override protected String describe() { return "использует Снос"; }
    @Override protected void applyOppEffects(Pokemon p) {
        Effect e = new Effect().stat(Stat.SPEED, -1);
        p.addEffect(e);
    }
}
