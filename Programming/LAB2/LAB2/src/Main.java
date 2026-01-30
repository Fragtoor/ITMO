import ru.ifmo.se.pokemon.*;
import pokemons.*;

public class Main {
    public static void main(String[] args){
        Battle b = new Battle();
        Pokemon p1 = new Alomomola("Alomomola", 50);
        Pokemon p2 = new Bidoof("Bidoof", 50);
        Pokemon p3 = new Bibarel("Bibarel", 50);
        Pokemon p4 = new Oddish("Oddish", 51);
        Pokemon p5 = new Gloom("Gloom", 52);
        Pokemon p6 = new Bellossom("Bellossom", 53);
        b.addAlly(p1);
        b.addAlly(p2);
        b.addAlly(p3);
        b.addFoe(p4);
        b.addFoe(p5);
        b.addFoe(p6);
        b.go();
    }
}
