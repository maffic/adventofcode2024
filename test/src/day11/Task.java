package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Task {
    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day11\\input.file";
    private List<String> data;

    private void loadData() {
        try {
            data = Arrays.stream(Files.readString(Paths.get(FILE_PATH)).split(" ")).collect(Collectors.toList());
        } catch (IOException e) {

        }
    }

    /*Если на камне выгравирован номер 0, его заменяют камнем с выгравированным номером 1.

      Если на камне выгравировано число, имеющее четное количество цифр, его заменяют двумя камнями .
      Левая половина цифр гравируется на новом левом камне, а правая половина цифр гравируется на новом правом камне.
      (Новые числа не содержат дополнительных ведущих нулей: 1000станут камнями 10и 0.)

      Если ни одно из других правил не применимо, камень заменяется новым;
      на новом камне гравируется номер старого камня , умноженный на 2024 .
    * */

    private void counting(int step) {
        System.out.println("Start: "  + data.toString());

        Map<String, Long> last = IntStream.rangeClosed(0, data.size() - 1)
                .boxed() // Преобразуем int в Integer
                .parallel()
                .collect(Collectors.groupingBy(
                        pos -> data.get(pos), // Ключ — значение из строки, преобразованное в Long
                        Collectors.counting() // Значение — список индексов
                ));

        for (int i = 0; i < step; i++) {
            Map<String, Long> currentMap = new HashMap<>();
            for (Map.Entry<String, Long> entry : last.entrySet()) {
                String current = entry.getKey();
                long count = entry.getValue();
                if ("0".equals(current)) { //rule 1
                    currentMap.merge("1", count, Long::sum);
                } else { //rule 2
                    if (current.length() % 2 == 0) {
                        int midlle = current.length() / 2;
                        currentMap.merge(current.substring(0, midlle), count, Long::sum);
                        long temp = Long.parseLong(current.substring(current.length() / 2));
                        currentMap.merge(String.valueOf(temp), count, Long::sum);
                    } else {
                        currentMap.merge(String.valueOf(Long.parseLong(current) * 2024), count, Long::sum);
                    }
                }
//            }
//            System.out.println("Step " + i + ": " + data.toString());
            }
            last = currentMap;
        }

        System.out.println("Stone count: " + last.values().stream().reduce(0L, Long::sum) + " in step " + step);
    }

    public static void main(String[] args) {
        var task = new Task();
        task.loadData();
        task.counting(25);
        task.counting(75);
    }
}
