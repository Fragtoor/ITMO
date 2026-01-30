package moves;
import ru.ifmo.se.pokemon.*;


public final class ShadowBall extends WaterMoveSp {
    public ShadowBall() {
        super(Type.GHOST, 80, 1.0);
    }
    @Override protected String describe() { return "использует Теневой Шар"; }
    @Override protected void applyOppEffects(Pokemon p) {
        Effect e = new Effect().chance(0.2).stat(Stat.SPECIAL_DEFENSE, -1);
        p.addEffect(e);
        if (e.success()) {
            System.out.println("Специальная защита уменьшилась");
        }
    }
}
