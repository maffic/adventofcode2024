package day14;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class Task {

    private static class Robot {
        int x;
        int y;
        int vx;
        int vy;
        public Robot (int x, int y, int vx, int vy) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
        }
    }

    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day14\\input.file";
    private List<Robot> robots;

    private void loadData() {
        try (var lines = Files.lines(Paths.get(FILE_PATH))) {
            System.out.println("Loading data...");
            robots = lines.map(line -> {
                        String[] parts = line.split(" ");
                        int[] position = Stream.of(parts[0].substring(2).split(","))
                                .mapToInt(Integer::parseInt)
                                .toArray();
                        int[] velocity = Stream.of(parts[1].substring(2).split(","))
                                .mapToInt(Integer::parseInt)
                                .toArray();
                        return new Robot(position[0], position[1], velocity[0], velocity[1]);
                    }).toList();

        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void part1() {
        int width = 101;
        int height = 103;

        int[] quads = {0, 0, 0, 0};

        for (Robot robot : robots) {
            int x = (robot.x + 100 * (robot.vx + width)) % width;
            int y = (robot.y + 100 * (robot.vy + height)) % height;

            if (x == width / 2 || y == height / 2) {
                continue;
            }

            int quadIdx = (x > width / 2 ? 1 : 0) + (y > height / 2 ? 2 : 0);
            quads[quadIdx]++;
        }
        System.out.println("Part 1 = " + (quads[0] * quads[1] * quads[2] * quads[3]));
    }

    private void part2() {
        int width = 101;
        int height = 103;

        int step = 0;

        while (true) {
            step++;
            boolean isEggs = true;
            Set<Point> points = new HashSet<>();
            for (Robot robot : robots) {
                int x = (robot.x + step * (robot.vx + width)) % width;
                int y = (robot.y + step * (robot.vy + height)) % height;

                if (points.contains(new Point(x, y))) {
                    isEggs = false;
                    break;
                }
                points.add(new Point(x, y));
            }
            if (isEggs) {
                System.out.println("Part 2 = " + step);
                break;
            }
        }
    }

    public static void main() {
        var task = new Task();
        task.loadData();
        task.part1();
        task.part2();
    }
}
