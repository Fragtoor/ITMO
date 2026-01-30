package moves;
import ru.ifmo.se.pokemon.*;

public final class Rest extends StatusMove {
    public Rest() {
        super(Type.PSYCHIC, 0, 1.0);
    }
    @Override protected String describe() { return "ложится отдыхать"; }
    @Override protected void applySelfEffects(Pokemon p) {
        Effect sleepEffect = new Effect().turns(2).condition(Status.SLEEP).stat(Stat.HP, -(int)p.getStat(Stat.HP));
        p.setCondition(sleepEffect);
    }
}
