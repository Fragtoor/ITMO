package moves;
import ru.ifmo.se.pokemon.*;

public final class WakeUpSlap extends WaterMovePh {
    public WakeUpSlap() {
        super(Type.FIGHTING, 70, 1.0);
    }
    @Override protected String describe() {
        return "использует Звонкую Пощечину";
    }
}
