package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

public class Task {
    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day5\\input.file";
    private Map<Integer, List<Integer>> rules;
    private List<String> data;

    private void loadFile() {
        try (Stream<String> linesFile = Files.lines(Paths.get(FILE_PATH))) {
            List<String> lines = linesFile.toList();
             rules = lines.stream()
                    .filter(line -> line.contains("|"))
                    .map(this::parseRule)
                    .collect(Collectors.groupingBy(
                            rule -> rule[0],
                            Collectors.mapping(rule -> rule[1], Collectors.toList())
                    ));
            data = lines.stream()
                    .filter(el -> !el.contains("|"))
                    .skip(1)
                    .toList();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private int[] parseRule(String s) {
        String[] split = s.split("\\|");
        return new int[]{Integer.parseInt(split[0]), Integer.parseInt(split[1])};
    }

    private void execute() {
        Map<Boolean, List<String>> result = data.stream()
                .collect(partitioningBy(this::matchesRules));

        int match = result.get(true).stream()
                .map(el -> el.split(","))
                .map(el -> el[el.length / 2])
                .mapToInt(Integer::parseInt)
                .reduce(0, Integer::sum);

        int noMatch = result.get(false).stream()
                .mapToInt(this::reOrder).sum();

        System.out.println("Match: " + match);
        System.out.println("No match: " + noMatch);
    }

    private int reOrder(String line) {
        List<Integer> numbers = Arrays.stream(line.split(","))
                .map(Integer::parseInt)
                .sorted((a, b) -> {
                    if (rules.containsKey(a) && rules.get(a).contains(b)) {
                        return -1;
                    } else if (rules.containsKey(b) && rules.get(b).contains(a)) {
                        return 1;
                    } else {
                        return 0;
                    }
                }).toList();

        return numbers.get(numbers.size() / 2);
    }

    private boolean matchesRules(String line) {
        List<Integer> numbers = Arrays.stream(line.split(","))
                .map(Integer::parseInt)
                .toList();

        for (int i = 0; i < numbers.size(); i++) {
            int current = numbers.get(i);

            if (rules.containsKey(current)) {
                List<Integer> expectedNext = rules.get(current);
                int firstIndex = numbers.indexOf(current);
                for (int second : expectedNext) {
                    int secondIndex = numbers.indexOf(second);
                    if (firstIndex != -1 && secondIndex != -1 && firstIndex > secondIndex) {
                        System.out.println("List " + line + " is not valid number " + current + " fail " + second);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        var task = new Task();
        task.loadFile();
        task.execute();
    }
}
