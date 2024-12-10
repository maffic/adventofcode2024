package day10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Task {
    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day10\\input.file";
    private int[][] map;

    private void loadData() {
        try (Stream<String> linesFile = Files.lines(Paths.get(FILE_PATH))) {
            List<String> temp = linesFile.toList();
            map = new int[temp.size()][temp.get(0).length()];
            for (int i = 0; i < temp.size(); i++) {
                String line = temp.get(i);
                for (int j = 0; j < temp.get(i).length(); j++) {
                    map[i][j] = line.charAt(j) == '.' ? -1 : Integer.parseInt("" + line.charAt(j));
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void searchPathPart1() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 0) {
                    boolean visited[][] = new boolean[map.length][map[i].length];
                    result.add(dfs(-1, i, j, new ArrayList<String>(), visited));
                }
            }
        }

        System.out.println("All result = " + result);
        System.out.println("Sum = " + result.stream().mapToInt(Integer::intValue).sum());
    }

    private void searchPathPart2() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 0) {
                    result.add(dfsPart2(-1, i, j, new ArrayList<String>()));
                }
            }
        }

        System.out.println("All result part2 = " + result);
        System.out.println("Sum part 2 = " + result.stream().mapToInt(Integer::intValue).sum());
    }

    private Integer dfs(int lastStep, int i, int j, List<String> path, boolean[][] visited) {
        if (outOfBounds(i, j) || map[i][j] == -1) {
            return 0;
        }

        if (lastStep + 1 != map[i][j]) {
            return 0;
        }

        if (map[i][j] == 9) {
            if (visited[i][j]) {
                return 0;
            }
            visited[i][j] = true;
            path.add("(" + i + ", " + j + ") " + map[i][j]);
            System.out.println(path);
            path.remove(path.size() - 1);
            return 1;
        }

        path.add("(" + i + ", " + j + ") " + map[i][j]);

        int result = dfs(map[i][j], i + 1, j, path, visited) + dfs(map[i][j], i, j + 1, path, visited) + dfs(map[i][j], i - 1, j, path, visited) + dfs(map[i][j], i, j - 1, path, visited);
        path.remove(path.size() - 1);

        return result;
    }

    private Integer dfsPart2(int lastStep, int i, int j, List<String> path) {
        if (outOfBounds(i, j) || map[i][j] == -1) {
            return 0;
        }

        if (lastStep + 1 != map[i][j]) {
            return 0;
        }

        if (map[i][j] == 9) {
            path.add("(" + i + ", " + j + ") " + map[i][j]);
            System.out.println(path);
            path.remove(path.size() - 1);
            return 1;
        }

        path.add("(" + i + ", " + j + ") " + map[i][j]);

        int result = dfsPart2(map[i][j], i + 1, j, path) + dfsPart2(map[i][j], i, j + 1, path) + dfsPart2(map[i][j], i - 1, j, path) + dfsPart2(map[i][j], i, j - 1, path);
        path.remove(path.size() - 1);

        return result;
    }

    private boolean outOfBounds(int i, int j) {
        return (i < 0 || j < 0 || i >= map.length || j >= map[i].length);
    }

    public static void main(String[] args) {
        var task = new Task();
        task.loadData();
        task.searchPathPart1();
        task.searchPathPart2();
    }
}


