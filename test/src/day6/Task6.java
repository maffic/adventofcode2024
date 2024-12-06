package day6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Task6 {
    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day6\\input.file";
    private final List<char[]> matrix = new ArrayList<>();
    private static final int[][] DIRECTIONS = new int[][]{{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    private void loadFile() {
        try (Stream<String> linesFile = Files.lines(Paths.get(FILE_PATH))) {
            linesFile.forEach(line -> matrix.add(line.toCharArray()));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private int findPath(int[] start) {
        int count = 0;
        int i = start[0], j = start[1];
        int currentDirection = 0;

        while (notGoOut(i, j)) {
            if (matrix.get(i)[j] == '#') {
                i -= DIRECTIONS[currentDirection][0];
                j -= DIRECTIONS[currentDirection][1];
                currentDirection = turnRight(currentDirection);
            } else {
                if (matrix.get(i)[j] != '*') {
                    count++;
                }
                matrix.get(i)[j] = '*';
            }

            i += DIRECTIONS[currentDirection][0];
            j += DIRECTIONS[currentDirection][1];
        }

        return count;
    }

    private int findCycle(int[] start) {
        int cycleCount = 0;

        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                if (matrix.get(i)[j] == '*') {
                    matrix.get(i)[j] = '#';
                    if (checkCycle(start[0], start[1], 0)) {
                        cycleCount++;
                    }
                    matrix.get(i)[j] = '*';
                }
            }
        }

        return cycleCount;
    }

    private boolean checkCycle(int inputI, int inputJ, int direction) {
        int currentDirection = direction;
        int currI = inputI, currJ = inputJ;

        Set<String> set = new HashSet<>();
        while (notGoOut(currI, currJ)) {
            String key = currI + "," + currJ + "," + currentDirection;
            if (set.contains(key)) {
                return true;
            }
            set.add(key);
            if (matrix.get(currI)[currJ] == '#') {
                currI -= DIRECTIONS[currentDirection][0];
                currJ -= DIRECTIONS[currentDirection][1];
                currentDirection = turnRight(currentDirection);
            }

            currI += DIRECTIONS[currentDirection][0];
            currJ += DIRECTIONS[currentDirection][1];
        }

        return false;
    }

    private int turnRight(int currentDirection) {
        return (currentDirection + 1) % 4;
    }

    private boolean notGoOut(int i, int j) {
        if (i < 0 || j < 0 || i >= matrix.size() || j >= matrix.get(i).length) {
            return false;
        }
        return true;
    }

    private int[] findStart() {
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                if (matrix.get(i)[j] == '^') {
                    return new int[]{i, j};
                }
            }
        }
        return new int[] {0, 0};
    }

    public static void main(String[] args) {
        var task = new Task6();
        task.loadFile();
        int[] start = task.findStart();
        System.out.println("Start = " + start[0] + " " + start[1]);
        int count = task.findPath(start);
        System.out.println("Count = " + count);
        count = task.findCycle(start);
        System.out.println("Cycle = " + count);
    }

}
