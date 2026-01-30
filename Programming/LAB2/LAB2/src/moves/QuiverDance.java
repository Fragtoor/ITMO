package moves;
import ru.ifmo.se.pokemon.*;

public final class QuiverDance extends GrassMoveSp {
    public QuiverDance() {
        super(Type.BUG, 0, 1.0);
    }
    @Override protected String describe() { return "исполняет танец стрекозы"; }
    @Override protected void applySelfEffects(Pokemon p) {
        Effect e = new Effect().stat(Stat.SPECIAL_ATTACK, 1).stat(Stat.SPECIAL_DEFENSE, 1).stat(Stat.SPEED, 1);
        p.addEffect(e);
    }
}
