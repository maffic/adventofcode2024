package day15;

import java.awt.*;

public enum Direction {
    UP('^', point -> point.x--),
    DOWN('v', point -> point.x++),
    LEFT('<', point -> point.y--),
    RIGHT('>', point -> point.y++);

    private final char symbol;
    private final Movement movement;
    private Direction forward;


    Direction(char symbol, Movement movement) {
        this.symbol = symbol;
        this.movement = movement;
    }

    static {
        UP.forward = DOWN;
        DOWN.forward = UP;
        LEFT.forward = RIGHT;
        RIGHT.forward = LEFT;
    }

    public void move(Point point) {
        movement.apply(point);
    }

    public void moveBox(Point point) {
        movement.apply(point);
        movement.apply(point);
    }

    public void moveback(Point point) {
        forward.move(point);
    }

    public static Direction fromSymbol(char symbol) {
        for (Direction direction : values()) {
            if (direction.symbol == symbol) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Unknown symbol: " + symbol);
    }

    @FunctionalInterface
    interface Movement {
        void apply(Point point);
    }
}
