package pokemons;
import ru.ifmo.se.pokemon.*;
import moves.*;

public class Bidoof extends Pokemon {
    public Bidoof(String name, int level) {
        super(name, level);
        setType(Type.NORMAL);
        setStats(59, 45, 40, 35, 40, 31);
        setMove(new Amnesia(), new Rest(), new Crunch());
    }
}