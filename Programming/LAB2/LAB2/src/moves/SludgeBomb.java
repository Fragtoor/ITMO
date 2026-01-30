package moves;
import ru.ifmo.se.pokemon.*;
import java.util.Arrays;

public final class SludgeBomb extends PoisonMovePh {
    public SludgeBomb() {
        super(Type.POISON, 90, 1.0);
    }
    @Override protected String describe() { return "использует Грязевую Бомбу"; }
    @Override protected void applyOppEffects(Pokemon p) {
        Effect e = new Effect().chance(0.3);
        if (e.success()) Effect.poison(p);
    }
}
