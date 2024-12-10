package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Task {

    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day8\\input.file";
    private List<String> map;


    private void loadData() {
        try (Stream<String> linesFile = Files.lines(Paths.get(FILE_PATH))) {
            map = linesFile.toList();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void countAntinodes() {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < map.size(); i++) {
            char[] chars = map.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] != '.') {
//                    searchAntinodes(set, chars[j], i, j);
                    String add = i + " " + j;
                    set.add(add);
                    searchAntinodesInfinit(set, chars[j], i, j);
                }
            }

        }

        printResult(set);

        System.out.println("Part 1 count : " + set.size());
    }

    private void printResult(Set<String> set) {
        for (int i = 0; i < map.size(); i++) {
            char[] chars = map.get(i).toCharArray();
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '.') {
                    if (set.contains(i + " " + j)) {
                        System.out.print("#");
                        continue;
                    }
                } else {
                    if (set.contains(i + " " + j)) {
                        System.out.print("*");
                        continue;
                    }
                }
                System.out.print(chars[j]);
            }
            System.out.println();
        }
    }

    private void searchAntinodes(Set<String> set, char searchChar, int i, int j) {
        for (int x = i; x < map.size(); x++) {
            String search = map.get(x);
            int y = search.indexOf(searchChar);
            while (y != -1) {
                if (x != i || y != j) {
                    int dx = x - i;
                    int dy = y - j;
                    int[] up = new int[] {i - dx, j - dy};
                    int[] down = new int[] {x + dx, y + dy};
                    checkOutOfBounds(up, set);
                    checkOutOfBounds(down, set);
                }
                y = search.indexOf(searchChar, y + 1);
            }
        }
    }

    private void searchAntinodesInfinit(Set<String> set, char searchChar, int i, int j) {
        for (int x = i; x < map.size(); x++) {
            String search = map.get(x);
            int y = search.indexOf(searchChar);
            while (y != -1) {
                if (x != i || y != j) {
                    int dx = x - i;
                    int dy = y - j;
                    int[] up = new int[] {i, j};
                    int[] down = new int[] {x, y};
                    do {
                        up[0] = up[0] - dx;
                        up[1] = up[1] - dy;
                        down[0] = down[0] + dx;
                        down[1] = down[1] + dy;
                        checkOutOfBounds(up, set);
                        checkOutOfBounds(down, set);
                    } while (outOfBounds(down) || outOfBounds(up));
                }
                y = search.indexOf(searchChar, y + 1);
            }
        }
    }

    private void checkOutOfBounds(int[] check, Set<String> set) {
        if (outOfBounds(check)) {
            String add = check[0] + " " + check[1];
            set.add(add);
        }
    }

    private boolean outOfBounds(int[] check) {
        return check[0] >= 0 && check[0] < map.size() && check[1] >= 0 && check[1] < map.get(check[0]).length();
    }

    public static void main(String[] args) {
        var task = new Task();
        task.loadData();
        task.countAntinodes();
    }
}
