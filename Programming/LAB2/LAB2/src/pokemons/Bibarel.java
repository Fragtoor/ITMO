package pokemons;
import ru.ifmo.se.pokemon.*;
import moves.*;
public  class Bibarel extends Bidoof {
    public Bibarel(String name, int level) {
        super(name, level);
        addType(Type.WATER);
        setStats(79, 85, 60, 55, 60, 71);
        addMove(new Bulldoze());
    }
}