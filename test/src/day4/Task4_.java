package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Task4_ {
    //
    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day4\\input.file";
    private static final char[] FORWARD = new char[] {'X', 'M', 'A', 'S'};
    private static final char[] BACKWARD = new char[] {'S', 'A', 'M', 'X'};
    private static final int[][] STEP = new int[][] {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};

    private static final char[] FORWARD_X = new char[] {'M', 'A', 'S'};
    private static final char[] BACKWARD_X = new char[] {'S', 'A', 'M'};
    private static final int[][] STEP_X = new int[][] {{1, 1}, {-1, 1}};
    private static final int[] POSITION = new int[] {0, 2};

    private List<char[]> matrix = new ArrayList<>();

    private void loadFile() {
        try (Stream<String> lines = Files.lines(Paths.get(FILE_PATH))) {
            matrix.addAll(lines.map(String::toCharArray).toList());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private int execute() {
        int count = 0;
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                for(int[]  direction : STEP) {
                    if (matrix.get(i)[j] == FORWARD[0]) {
                        count += bfs(i, j, FORWARD, 0, direction) ? 1 : 0;
                    } else if (matrix.get(i)[j] == BACKWARD[0]) {
                        count += bfs(i, j, BACKWARD, 0, direction) ? 1 : 0;
                    }
                }
            }
        }
        return count / 2; // т.к. нет проверки на то, что комбинация была уже учтена
    }

    private boolean bfs(int i, int j, char[] mask, int current, int[] step) {
        if (current == mask.length) {
            return true;
        }

        if (i < 0 || j < 0 || i >= matrix.size() || j >= matrix.get(i).length || matrix.get(i)[j] != mask[current]) {
            return false;
        }

        boolean result = bfs(i + step[0], j + step[1], mask, current + 1, step);

        return result;
    }

    private int executeX() {
        int count = 0;
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).length; j++) {
                boolean result = (bfs(i, j, FORWARD_X, 0, STEP_X[0]) && bfs(i + 2, j, FORWARD_X, 0, STEP_X[1])) ||
                        (bfs(i, j, FORWARD_X, 0, STEP_X[0]) && bfs(i + 2, j, BACKWARD_X, 0, STEP_X[1])) ||
                        (bfs(i, j, BACKWARD_X, 0, STEP_X[0]) && bfs(i + 2, j, BACKWARD_X, 0, STEP_X[1])) ||
                        (bfs(i, j, BACKWARD_X, 0, STEP_X[0]) && bfs(i + 2, j, FORWARD_X, 0, STEP_X[1]));

                if (result) {
                    count++;
                }

//                for(int t = 0; t < POSITION.length; i++) {
//                    int i_position = i + POSITION[t];
//                    char[] mask = null;
//                    if (matrix.get(i_position)[j] == FORWARD_X[0]) {
//                        mask = FORWARD_X;
//                    } else if (matrix.get(i_position)[j] == BACKWARD_X[0]) {
//                        mask = BACKWARD_X;
//                    }
//
//                    if (mask == null || !bfs(i_position, j, mask, 0, STEP_X[t])) {
//                        count--;
//                        break;
//                    }
//                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        var task4 = new Task4_();
        task4.loadFile();
        System.out.println("XMAS + MASX %d = " + task4.execute());
        System.out.println("X-MAS %d = " + task4.executeX());
    }
}
