package moves;
import ru.ifmo.se.pokemon.*;

public final class Refresh extends StatusMove {
    public Refresh() {
        super(Type.NORMAL, 0, 1.0);
    }
    @Override protected String describe() { return "очищается от статусов"; }
    @Override protected void applySelfEffects(Pokemon p) {
        p.restore();
    }
}
