package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

public class Task {

    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day7\\input.file";
    private List<String> lines;

    private void loadFile() {
        try (Stream<String> linesFile = Files.lines(Paths.get(FILE_PATH))) {
            lines = linesFile.toList();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findResult() {
        long sum = 0;
        long sumConcated = 0;
        int number = 0;

        for (String line : lines) {
            String[] split = line.split(":");

            long target = Long.parseLong(split[0]);
            long[] values = stream(split[1].trim().split(" ")).mapToLong(Long::parseLong).toArray();
            if (dfs(number, target, values, 1, values[0], String.valueOf(values[0]))) {
                sum += target;
            } else {
                if (dfsConcate(target, values, 1, values[0], String.valueOf(values[0]))) {
                    sumConcated += target;
                } else {
                    System.out.println(number + ": Не найдено выражение для " + target + " в строке " + line);
                }
            }
            number++;
        }

        System.out.println("Result part1 = " + sum);
        System.out.println("Result part2 = " + sumConcated);
        System.out.printf("Result = %d%n", sumConcated + sum);
    }

    private boolean dfs(int number, Long target, long[] value, int index, long currentSum, String expression) {
        if (index == value.length) {
            if (currentSum == target) {
                System.out.println(number + ": Найдено выражение: " + expression + " = " + target + " (" + currentSum + ")");
                return true;
            }
            return false;
        }

        return dfs(number, target, value, index + 1, currentSum + value[index], expression + " + " + value[index])
                || dfs(number, target, value, index + 1, currentSum * value[index], expression + " * " + value[index]);
    }

    private boolean dfsConcate(long target, long[] numbers, int index, long currentResult, String expression) {
        if (index == numbers.length) {
            if (currentResult == target) {
                return true;
            }
            return false;
        }

        long nextNumber = numbers[index];

        // +
        if (dfsConcate(target, numbers, index + 1, currentResult + nextNumber,
                expression + " + " + nextNumber)) {
            return true;
        }

        // *
        if (dfsConcate(target, numbers, index + 1, currentResult * nextNumber,
                expression + " * " + nextNumber)) {
            return true;
        }

        // текущего результата и следующее число
        String concatenatedStr = expression + nextNumber;
        long concatenatedResult = Long.parseLong(concatenatedStr);
        if (dfsConcate(target, numbers, index + 1, concatenatedResult, concatenatedStr)) {
            return true;
        }


        // результат и результат
        String combinedConcat = currentResult + String.valueOf(nextNumber);
        long combinedConcatResult = Long.parseLong(combinedConcat);
        String combinedConcatExpression = expression + nextNumber;
        if (dfsConcate(target, numbers, index + 1, combinedConcatResult, combinedConcatExpression)) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        var task = new Task();
        task.loadFile();
        task.findResult();
    }

}
