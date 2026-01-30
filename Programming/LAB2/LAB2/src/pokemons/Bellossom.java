package pokemons;
import ru.ifmo.se.pokemon.*;
import moves.*;


public final class Bellossom extends Gloom {
    public Bellossom(String name, int level) {
        super(name, level);
        setType(Type.GRASS);
        setStats(75, 80, 95, 90, 100, 50);
        addMove(new QuiverDance());
    }
}
