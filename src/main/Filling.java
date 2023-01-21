package main;

public enum Filling {
    PLAIN,
    HOLE;

    @Override
    public String toString() {
        if (this.equals(HOLE))
            return "O";
        else
            return "H";
    }

}
