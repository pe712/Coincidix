package main;

import java.util.ArrayList;

public class Coordinate {
    public final int i;
    public final int j;
    private final int n;

    public Coordinate(int i, int j, int n) {
        assert (i >= 0 && i < n && j >= 0 && j < n);
        this.i = i;
        this.j = j;
        this.n = n;
    }

    public Coordinate(String strCoord) {
        String[] splitted = strCoord.split(",");
        this.i = Integer.valueOf(splitted[0]);
        this.j = Integer.valueOf(splitted[1]);
        this.n = Integer.valueOf(splitted[2]);
    }

    public boolean isBorder() {
        return (i == 0 || j == 0 || i == n - 1 || j == n - 1);
    }

    public ArrayList<Coordinate> neighbours() {
        ArrayList<Coordinate> neigh = new ArrayList<Coordinate>();
        assert (i >= 0 && i < n && j >= 0 && j < n);
        if (i != 0)
            neigh.add(new Coordinate(i - 1, j, n));
        if (i != n - 1)
            neigh.add(new Coordinate(i + 1, j, n));
        if (j != 0)
            neigh.add(new Coordinate(i, j - 1, n));
        if (j != n - 1)
            neigh.add(new Coordinate(i, j + 1, n));
        return neigh;
    }

    @Override
    public String toString() {
        return "(" + i + ", " + j + ")";
    }

    @Override
    public boolean equals(Object obj) {
        Coordinate coord = (Coordinate) obj;
        return this.i == coord.i && this.j == coord.j && this.n == coord.n;
    }

    @Override
    public int hashCode() {
        return 17 * i + j;
    }

    public Coordinate moveTo(Direction dir) {
        if (dir.equals(Direction.RIGHT))
            return new Coordinate(i, j + 1, n);
        else if (dir.equals(Direction.LEFT))
            return new Coordinate(i, j - 1, n);
        else if (dir.equals(Direction.UP))
            return new Coordinate(i - 1, j, n);
        else
            return new Coordinate(i + 1, j, n);
    }
}
