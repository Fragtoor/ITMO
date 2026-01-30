package moves;
import ru.ifmo.se.pokemon.*;

public final class PoisonPowder extends StatusMove {
    public PoisonPowder() {
        super(Type.POISON, 0, 0.75);
    }
    @Override protected String describe() { return "распыляет ядовитый порошок"; }
    @Override protected void applyOppEffects(Pokemon p) {
        Effect.poison(p);
    }
}