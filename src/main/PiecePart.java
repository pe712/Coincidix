package main;

import java.util.HashMap;

public class PiecePart {
    public final Filling filling;
    public HashMap<Direction, PiecePart> neighbours = new HashMap<Direction, PiecePart>();

    public PiecePart(Filling filling) {
        this.filling = filling;
    }

    public PiecePart(String filling) throws ExceptionInInitializerError {
        this.filling = Filling.valueOf(filling);
    }

    public PiecePart moveTo(Direction dir) {
        assert neighbours.containsKey(dir);
        return neighbours.get(dir);
    }
}