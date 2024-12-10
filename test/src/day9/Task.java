package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Task {

//    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day9\\test.file";
    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day9\\input.file";
    private List<Integer> map = new ArrayList<>();
    private List<int[]> empty = new ArrayList<>();


    private void loadData() {
        int fileId = 0;
        try (Stream<String> linesFile = Files.lines(Paths.get(FILE_PATH))) {
            String data = linesFile.findFirst().get();
            int position = 0;
            while (position < data.length()) {
                int fillData = 0, emptyData = 0;
                if (position + 2 >= data.length()) {
                    String file = data.substring(position, position + 1);
                    fillData = Integer.parseInt(file.substring(0, 1));
                } else {
                    String file = data.substring(position, position + 2);
                    fillData = Integer.parseInt(file.substring(0, 1));
                    emptyData = Integer.parseInt(file.substring(1, 2));
                }
                empty.add(new int[]{fillData, emptyData, 0, map.size()});
                for (int i = 0; i < fillData; i++) {
                    map.add(fileId);
                }
                for (int i = 0; i < emptyData; i++) {
                    map.add(-1);
                }
                fileId++;
                position += 2;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void defragmentationDataPart1() {
        long crc = 0;

        int left = 0, right = map.size() - 1;

        while (left <= right) {

            while (map.get(left) != -1) {
                left++;
            }

            while (map.get(right) == -1) {
                right--;
            }

            if (left <= right) {
              map.set(left, map.get(right));
              map.set(right, -1);
            }

            left++;
        }

        for (int i = 0; i < map.size(); i++) {
            if (map.get(i) == -1) {
                break;
            }
            crc += i * map.get(i);
        }

        map.forEach(el -> System.out.print(el + " "));
        System.out.println();
        System.out.println("CRC part 1 = " + crc);
    }

    private void defragmentationDataPart2() {
        long crc = 0;

        int right = empty.size() - 1;

        while (right > 0) {

            int size = empty.get(right)[0];
            int left = 0, positionMapLeft = 0;
            while (empty.get(left)[1] < size && left < empty.size() - 1) {
                positionMapLeft += empty.get(left)[0] + empty.get(left)[1] + empty.get(left)[2];
                left++;
            }

            if (left < right) {
                int[] leftEl = empty.get(left);

                positionMapLeft += leftEl[0] + leftEl[2];

                empty.get(left)[1] -= size;
                empty.get(left)[2] += size;

                for (int i = positionMapLeft; i < positionMapLeft + size; i++) {
                    map.set(i, right);
                }

                for (int i = empty.get(right)[3]; i < empty.get(right)[3] + size; i++) {
                    map.set(i, -1);
                }
            }

            right--;
        }

        //CRC
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i) == -1) {
                continue;
            }
            crc += i * map.get(i);
        }

        map.forEach(el -> System.out.print(el + " "));
        System.out.println();
        System.out.println("CRC part 2 = " + crc);
    }

    public static void main(String[] args) {
        var task = new Task();
        task.loadData();
//        task.defragmentationDataPart1();
        task.defragmentationDataPart2();
    }
}
