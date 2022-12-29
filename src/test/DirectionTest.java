package test;

import main.Direction;

public class DirectionTest {
    public static void main(String[] args) {
        System.out.print("DirectionTest... ");
        Direction up = Direction.UP;
        assert up.flip().equals(Direction.DOWN);
        assert Direction.RIGHT.flip().equals(Direction.RIGHT);
        Direction.RIGHT.turn(Direction.RIGHT).equals(Direction.DOWN);
        Direction.RIGHT.turn(Direction.LEFT).equals(up);
        System.out.println("[OK]");
    }
}
