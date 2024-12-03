package day2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.Math.abs;

public class Task2 {

    private int count;

    private void loadFile() {
        try (Stream<String> lines = Files.lines(Paths.get("D:\\Project\\adventofcode2024\\test\\src\\day2\\file2.task"), Charset.defaultCharset())) {
            List<List<Integer>> list = lines.map(line -> Arrays.stream(line.trim().split("\\s+"))  // Разделение строки по пробелам
                    .map(Integer::parseInt).toList()).toList();
            list.forEach(this::checkSafetyRetest);
        }
        catch (IOException exp) {
            System.out.println(exp.getMessage());
        }
    }

    private void checkSafetyRetest(List<Integer> list) {
        if (checkSafety(list)) {
            return;
        }

        for (int i = 0; i < list.size() - 1; i++) {
            if (buildAndCheckSafety(list, i)) {
                return;
            }
        }
    }

    private boolean buildAndCheckSafety(List<Integer> list, int i) {
        List<Integer> newList = new ArrayList<>(list);
        newList.remove(i);
        return checkSafety(newList);
    }

    private boolean checkSafety(List<Integer> list) {
        int direction = -1;
        if (list.get(1) - list.get(0) > 0) {
            direction = 1;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            int result = list.get(i + 1) - list.get(i);
            if (abs(result) == 0 || abs(result) > 3) {
                return false;
            }
            if (result > 0 && direction == -1) {
                return false;
            }
            if (result < 0 && direction == 1) {
                return false;
            }
        }
        count++;
        return true;
    }

    public static void main(String[] args) {
        var task2 = new Task2();
        task2.loadFile();
        System.out.println(task2.count);
    }
}
