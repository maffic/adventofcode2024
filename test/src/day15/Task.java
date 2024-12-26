package day15;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Task {

    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day15\\test3.file";
    private char[][] map;
    private char[] steps;
    private Point start;

    private void loadData() {
        try (var lines = Files.lines(Paths.get(FILE_PATH))) {
            System.out.println("Loading data...");
            List<String> linesList = lines.toList();
            int size = 0;
            while (!linesList.get(size).isEmpty()) {
                size++;
            }

            map = new char[linesList.getFirst().length()][size];
            for (int i = 0; i < size; i++) {
                String line = linesList.get(i);
                for (int j = 0; j < line.length(); j++) {
                    map[i][j] = line.charAt(j);
                }
            }

            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    if (map[i][j] == '@') {
                        start = new Point(i, j);
                        break;
                    }
                }
                if (start != null) {
                    break;
                }
            }

            steps = linesList.stream().skip(size)
                    .flatMapToInt(String::chars)
                    .mapToObj(c -> (char) c)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString()
                    .toCharArray();

        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void part1() {
        for (int i = 0; i < steps.length; i++) {
            Direction direction = Direction.fromSymbol(steps[i]);
            int oldX = start.x, oldY = start.y;
            direction.move(start);
            if (map[start.x][start.y] == '#') {
                direction.moveback(start);
            } else if (map[start.x][start.y] == 'O') {
                if (!moveBox(start.x, start.y, direction)) {
                    direction.moveback(start);
                }
            }
            map[oldX][oldY] = '.';
            map[start.x][start.y] = '@';
        }

        int sum = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'O') {
                    sum += 100 * i + j;
                }
            }
        }

        System.out.println("Finish: " + sum);
        Arrays.stream(map)
                .map(String::valueOf)
                .forEach(System.out::println);
    }

    private boolean moveBox(int x, int y, Direction direction) {
        Point next = new Point(x, y);
        direction.move(next);

        if (map[next.x][next.y] == '#') {
            return false;
        }

        boolean result = true;

        if (map[next.x][next.y] == 'O') {
            result = moveBox(next.x, next.y, direction);
        }

        if (result) {
            map[next.x][next.y] = 'O';
            map[x][y] = '.';
        }

        return result;
    }

    private void part2() {

        char[][] newMap = new char[map.length][map[0].length*2];
        for (int i = 0; i < map.length; i++) {
            int k = 0;
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'O') {
                    newMap[i][k] = '[';
                    newMap[i][++k] = ']';
                } else if (map[i][j] == '@') {
                    newMap[i][k] = '@';
                    newMap[i][++k] = '.';
                } else {
                    newMap[i][k] = map[i][j];
                    newMap[i][++k] = map[i][j];
                }
                k++;
            }
        }

        start = null;
        for (int i = 0; i < newMap.length; i++) {
            for (int j = 0; j < newMap[i].length; j++) {
                if (newMap[i][j] == '@') {
                    start = new Point(i, j);
                    break;
                }
            }
            if (start != null) {
                break;
            }
        }

        for (int i = 0; i < steps.length; i++) {
            Direction direction = Direction.fromSymbol(steps[i]);
            int oldX = start.x, oldY = start.y;
            direction.move(start);
            if (newMap[start.x][start.y] == '#') {
                direction.moveback(start);
            } else if (newMap[start.x][start.y] == '[' || newMap[start.x][start.y] == ']') {
                if (!moveBox2(newMap, start.x, start.y, direction, newMap[start.x][start.y])) {
                    direction.moveback(start);
                }
            }
            newMap[oldX][oldY] = '.';
            newMap[start.x][start.y] = '@';
        }

        int sum = 0;

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (newMap[i][j] == '[') {
                    sum += 100 * i + j;
                }
            }
        }

        System.out.println("Finish: " + sum);
        Arrays.stream(newMap)
                .map(String::valueOf)
                .forEach(System.out::println);
    }

    private boolean moveBox2(char[][] newMap, int x, int y, Direction direction, char boxStart) {
        Point next = new Point(x, y);
        boolean b = direction.equals(Direction.UP) || direction.equals(Direction.DOWN);
        if (b) {
            direction.move(next);
        } else {
            direction.moveBox(next);
        }

        if (newMap[next.x][next.y] == '#') {
            return false;
        }

        boolean result = true;

        if (newMap[next.x][next.y] == '[' || newMap[next.x][next.y] == ']') {
            result = moveBox2(newMap, next.x, next.y, direction, newMap[next.x][next.y]);
        }

        if (result) {
            if (b) {
                newMap[x][y] = '.';
            } else {
                newMap[x][y] = '.';
                Point old = new Point(x, y);
                direction.move(old);
                newMap[old.x][old.y] = boxStart;
                direction.move(old);
                newMap[old.x][old.y] = boxStart == ']' ? '[' : ']';
            }
        }

        return result;
    }

    public static void main() {
        var task = new Task();
        task.loadData();
//        task.part1();
        task.part2();
    }
}
