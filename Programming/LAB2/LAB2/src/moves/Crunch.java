package moves;
import ru.ifmo.se.pokemon.*;

public final class Crunch extends PhysicalMove {
    public Crunch() {
        super(Type.DARK, 80, 1.0);
    }
    @Override protected String describe() { return "использует Хруст"; }
    @Override protected void applyOppEffects(Pokemon p) {
        Effect e = new Effect().chance(0.2).stat(Stat.DEFENSE, -1);
        p.addEffect(e);
        if (e.success()) {
            System.out.println("ХРУСТИМ!");
        }
    }
}
