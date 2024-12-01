package day1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Task1 {

    List<Integer> listA = new ArrayList<Integer>();
    List<Integer> listB = new ArrayList<Integer>();

    private void loadFile() {
        try (Stream<String> lines = Files.lines(Paths.get("D:\\Project\\adventofcode2024\\test\\src\\day1\\file1.task"), Charset.defaultCharset())) {
            lines.map(line -> line.split("   ")).forEach(el -> {
                listA.add(Integer.parseInt(el[0]));
                listB.add(Integer.parseInt(el[1]));
            });
        }
        catch (IOException exp) {
            System.out.println(exp.getMessage());
        }
        listA.sort(Integer::compareTo);
        listB.sort(Integer::compareTo);
    }

    private int executePart1() {
        return IntStream.range(0, listA.size()).map(i -> Math.abs(listA.get(i) - listB.get(i))).sum();
    }

    private int executePart2() {
        Map<Integer, Long> frequencyMap = listB.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        return listA.stream()
                .mapToInt(key -> Math.toIntExact(frequencyMap.getOrDefault(key, 0L) * key))
                .sum();
    }

    public static void main(String[] args) {
        var task = new Task1();
        task.loadFile();
        System.out.println(task.executePart1());
        System.out.println(task.executePart2());
    }
}
