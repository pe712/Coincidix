package main;

public enum Direction {
    RIGHT(1),
    LEFT(3),
    UP(0),
    DOWN(2);

    private final int value;

    private Direction(int value) {
        this.value = value;
    }

    public Direction flip() {
        if (this.equals(Direction.DOWN))
            return Direction.UP;
        else if (this.equals(Direction.UP))
            return Direction.DOWN;
        else
            return this;
    }

    public Direction turn(Direction dir) {
        int sum = (this.value + dir.value) % 4;
        switch (sum) {
            case 0:
                return Direction.UP;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.DOWN;
            default:
                return Direction.LEFT;
        }
    }

}
