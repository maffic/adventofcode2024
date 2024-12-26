package day16;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class Task {

    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day16\\test.file";
    private char[][] map;

    public static void main(String[] args) {
        var task = new Task();
        task.loadData();
        task.part1();
        task.part2();
    }

    private void loadData() {
        try (var lines = Files.lines(Paths.get(FILE_PATH))) {
            System.out.println("Loading data...");
            List<String> linesList = lines.toList();

            map = new char[linesList.get(0).length()][linesList.get(0).length()];
            for (int i = 0; i < linesList.size(); i++) {
                for (int j = 0; j < linesList.get(i).length(); j++) {
                    map[i][j] = linesList.get(i).charAt(j);
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void part2() {

    }

    private void part1() {
        boolean[][] visited = new boolean[map.length][map[0].length];
        long result = solveMaze(map);

        System.out.println("Part 1 " + result);
    }

    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private static final char START = 'S';
    private static final char END = 'E';
    private static final char WALL = '#';
    private static final char PATH = '.';

    static class State implements Comparable<State> {
        int x, y, direction, cost, steps;

        public State(int x, int y, int direction, int cost, int steps) {
            this.x = x;
            this.y = y;
            this.direction = direction;
            this.cost = cost;
            this.steps = steps;
        }

        @Override
        public int compareTo(State other) {
            return Integer.compare(this.cost, other.cost);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return x == state.x && y == state.y && direction == state.direction;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, direction);
        }
    }

    public static int solveMaze(char[][] maze) {
        int rows = maze.length;
        int cols = maze[0].length;

        int startX = 0, startY = 0;
        int endX = 0, endY = 0;

        // Находим начальную и конечную точки
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (maze[i][j] == START) {
                    startX = i;
                    startY = j;
                } else if (maze[i][j] == END) {
                    endX = i;
                    endY = j;
                }
            }
        }

        // Приоритетная очередь для Дейкстры
        PriorityQueue<State> pq = new PriorityQueue<>();
        Map<String, Integer> visited = new HashMap<>();

        // Начальное состояние: смотрим на восток (direction = 1)
        State startState = new State(startX, startY, 1, 0, 0);
        pq.add(startState);
        visited.put(encodeState(startState), 0);

        State bestState = null;

        // Дейкстра
        while (!pq.isEmpty()) {
            State current = pq.poll();

            // Если достигли конца
            if (maze[current.x][current.y] == END) {
                bestState = current;
                break;
            }

            // 1. Повороты
            for (int turn = -1; turn <= 1; turn++) { // -1: влево, 0: прямо, 1: вправо
                int newDir = (current.direction + turn + 4) % 4;
                int turnCost = (turn == 0) ? 0 : 1000; // Стоимость поворота
                State newState = new State(current.x, current.y, newDir, current.cost + turnCost, current.steps);

                if (updateVisited(visited, newState)) {
                    pq.add(newState);
                }
            }

            // 2. Движение вперед
            int newX = current.x + DIRECTIONS[current.direction][0];
            int newY = current.y + DIRECTIONS[current.direction][1];

            if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && maze[newX][newY] != WALL) {
                State newState = new State(newX, newY, current.direction, current.cost + 1, current.steps + 1);

                if (updateVisited(visited, newState)) {
                    pq.add(newState);
                }
            }
        }

        if (bestState != null) {
            System.out.println("Минимальная стоимость пути: " + bestState.cost);
            System.out.println("Общее количество шагов: " + bestState.steps);
            return bestState.cost;
        }

        // Если путь не найден
        System.out.println("Путь не найден.");
        return -1;
    }

    // Метод для кодирования состояния
    private static String encodeState(State state) {
        return state.x + "," + state.y + "," + state.direction;
    }

    // Метод для обновления таблицы посещенных
    private static boolean updateVisited(Map<String, Integer> visited, State state) {
        String key = encodeState(state);
        if (!visited.containsKey(key) || visited.get(key) > state.cost) {
            visited.put(key, state.cost);
            return true;
        }
        return false;
    }

}
