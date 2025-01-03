package day18;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Stream;

public class TTask {
    public static void main(String[] args) throws Exception {
        new Puzzle().solve();
    }
}

class Puzzle {
    final Grid grid;

    Puzzle() throws Exception {
        try (var input = Objects.requireNonNull(getClass().getResourceAsStream("input.file"))) {
            var reader = new BufferedReader(new InputStreamReader(input));
            grid = Grid.from(reader.lines());
        }
    }

    void solve() {
        System.out.println(grid.walk(Grid.BYTES));
        int i = Grid.BYTES + 1;
        while (grid.walk(i) != -1) {
            ++i;
        }
        System.out.println(grid.badCell(i - 1));
    }
}

record Coordinate(int x, int y) implements Comparable<Coordinate> {
    static Coordinate from(String s) {
        var split = s.split(",");
        return new Coordinate(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
    }

    Collection<Coordinate> neighbours() {
        return Set.of(
                new Coordinate(x, y - 1),
                new Coordinate(x + 1, y),
                new Coordinate(x, y + 1),
                new Coordinate(x - 1, y)
        );
    }

    @Override
    public int compareTo(Coordinate o) {
        return y == o.y ? Integer.compare(x, o.x) : Integer.compare(y, o.y);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}

record ND(Coordinate node, int distance) implements Comparable<ND> {
    @Override
    public int compareTo(ND o) {
        return distance == o.distance ? node.compareTo(o.node) : Integer.compare(distance, o.distance);
    }
}

record Grid(Map<Coordinate, Integer> badCells) {
    static final int SIZE = 71; // width & height
    static final int BYTES = 1024;
    static final Coordinate START = new Coordinate(0, 0);
    static final Coordinate FINISH = new Coordinate(SIZE - 1, SIZE - 1);

    static Grid from(Stream<String> lines) {
        Map<Coordinate, Integer> badCells = new HashMap<>();
        int i = 0;
        for (String line : lines.toList()) {
            badCells.put(Coordinate.from(line), i);
            ++i;
        }
        return new Grid(badCells);
    }

    boolean isBadAt(Coordinate cell, int n) {
        return Optional.ofNullable(badCells.get(cell)).map(i -> i < n).orElse(false);
    }

    int walk(int n) {
        var queue = new PriorityQueue<ND>();
        Set<Coordinate> visited = new HashSet<>();
        queue.add(new ND(START, 0));
        while (!queue.isEmpty()) {
            var current = queue.remove();
            Coordinate coordinate = current.node();
            if (coordinate.equals(FINISH)) {
                return current.distance();
            }
            visited.add(coordinate);
            for (var neighbour : coordinate.neighbours()) {
                if (neighbour.x() < 0 || neighbour.x() >= SIZE || neighbour.y() < 0 || neighbour.y() >= SIZE) {
                    continue;
                }
                if (isBadAt(neighbour, n)) {
                    continue;
                }
                if (!visited.contains(neighbour)) {
                    ND next = new ND(neighbour, current.distance() + 1);
                    if (!queue.contains(next)) {
                        queue.add(next);
                    }
                }
            }
        }
        return -1;
    }

    Coordinate badCell(int i) {
        return badCells.entrySet().stream().filter(e -> e.getValue() == i).map(Map.Entry::getKey).findFirst().orElseThrow();
    }
}
