package day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {
    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day12\\test1.file";
    private static final int[][] DIRECTIONS = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    private char[][] data;
    private int size;

    record Point(int x, int y) {}

    private void loadData() {
        try (var stream = Files.lines(Paths.get(FILE_PATH))) {
            List<String> lines = stream.toList();
            data = new char[lines.size()][lines.getFirst().length()];
            for (int i = 0; i < lines.size(); i++) {
                data[i] = lines.get(i).toCharArray();
            }
        } catch (IOException _) {

        }
    }

    private void part1() {
        int sum = 0;
        var visible = new boolean[data.length][data[0].length];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (visible[i][j]) {
                    continue;
                }
                size = 0;
                int result = dfs(visible, i, j, data[i][j]);
                int mult = size * result;
                System.out.println(data[i][j] + " = " + result + " mult " + mult);
                sum += mult;
            }
        }
        System.out.println("Part1: " + sum);
    }

    private void part2() {
        int sum = 0;
        var visible = new int[data.length][data[0].length];
        int current = 1;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                if (visible[i][j] != 0) {
                    continue;
                }
                size = 0;
                dfsPart2(visible, i, j, data[i][j], current++);
                int mult = size;
//                System.out.println(data[i][j] + " = " + result + " mult " + mult);
                sum += mult;
            }
        }


        Map<Integer, Integer> map = new HashMap<>();
//        sumSides(visible, false, map);
        sumSides(visible, true, map);

        System.out.println("Part1: " + sum);
    }

    private void sumSides(int[][] visible, boolean vertical, Map<Integer, Integer> map) {
        int current = visible[0][0];
        boolean found = false;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[0].length; j++) {
                int di = 0, dj = 0;
                if (vertical) {
                    di = j;
                    dj = i;
                } else {
                    di = i;
                    dj = j;
                }
                if (visible[di][dj] == current && !found) {
                    map.put(current, map.getOrDefault(current, 0) + 1);
                    found = true;
                }
                if (visible[di][dj] != current) {
                    current = visible[di][dj];
                    found = false;
                }
            }
        }
    }

    private void dfsPart2(int[][] visible, int i, int j, char search, int current) {
        if (outOfBounds(i, j)) {
            return;
        }

        if (data[i][j] != search || (visible[i][j] == current)) {
            return;
        }

        visible[i][j] = current;
        size++;

        for (int[] direction : DIRECTIONS) {
            int newI = i + direction[0], newJ = j + direction[1];
            dfsPart2(visible, newI, newJ, search, current);
        }
    }

    private int dfs(boolean[][] visible, int i, int j, char search) {
        if (outOfBounds(i, j)) {
            return 0;
        }

        if (data[i][j] != search || visible[i][j]) {
            return 0;
        }

        visible[i][j] = true;
        size++;
        int result = 0;

        for (int[] direction : DIRECTIONS) {
            int newI = i + direction[0], newJ = j + direction[1];

            result += countOfBorder(newI, newJ, search);
            result += dfs(visible, newI, newJ, search);
        }

        return result;
    }

    private int countOfBorder(int i, int j, char search) {
        if (outOfBounds(i, j) || data[i][j] != search) {
            return 1;
        }
        return 0;
    }

    private boolean outOfBounds(int i, int j) {
        return (i < 0 || j < 0 || i >= data.length || j >= data[0].length);
    }

    public static void main(String[] args) {
        var task = new Task();
        task.loadData();
//        task.part1();
        task.part2();
    }
}
