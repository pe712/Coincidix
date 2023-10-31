package test;

import main.Coordinate;

public class CoordinateTest {
    public static void main(String[] args) {
        System.out.print("CoordinateTest... ");
        assert new Coordinate("4,5,6").equals(new Coordinate(4, 5, 6));
        System.out.println(" [OK]");
    }
}
