package pokemons;
import ru.ifmo.se.pokemon.*;
import moves.*;

public final class Alomomola extends Pokemon {
    public Alomomola(String name, int level) {
        super(name, level);
        setType(Type.WATER);
        setStats(165, 75, 80, 40, 45, 65);
        setMove(new ShadowBall(), new Refresh(), new WakeUpSlap(),new CalmMind());
    }
}
