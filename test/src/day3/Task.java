package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {

    private static final Pattern PATTERN = Pattern.compile("mul\\((\\d+),(\\d+)\\)");
    private static final Pattern PATTERN_CONTENT = Pattern.compile("do\\(\\)(.*?)(?=don't\\(\\)|$)");
    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day3\\file3.task";
    public static final String DONT_T = "don't()";

    private List<long[]> matrix;

    private void loadFile() {
        matrix = new ArrayList<>();
        try {
            String content = Files.readString(Paths.get(FILE_PATH));
            //part 1
            //generateMatrixByString(content);
            //part 2
            int position = content.indexOf(DONT_T);
            generateMatrixByString(content.substring(0, position));
            content = content.substring(position + DONT_T.length());
            Matcher matcher = PATTERN_CONTENT.matcher(content);
            while (matcher.find()) {
                generateMatrixByString(matcher.group(1));
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void generateMatrixByString(String content) {
        Matcher matcher = PATTERN.matcher(content);
        while (matcher.find()) {
            long x = Integer.parseInt(matcher.group(1)); // Первое число
            long y = Integer.parseInt(matcher.group(2)); // Второе число
            matrix.add(new long[]{x, y});
        }
    }

    private Long getSum() {
        loadFile();
        return matrix.stream().mapToLong(x -> x[0] * x[1]).sum();
    }


    public static void main(String[] args) {
        var task3 = new Task();
        System.out.println(task3.getSum());
    }
}
