package day13;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Task {
    private static final String FILE_PATH = "D:\\Project\\adventofcode2024\\test\\src\\day13\\input.file";

    private class Buttom {
        public int x;
        public int y;
        public int cnt = 0;

        public Buttom(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private class ClawMashine {
        public Buttom buttomA;
        public Buttom buttomB;
        public long resultX;
        public long resultY;

        public ClawMashine() {
        }
    }

    private List<ClawMashine> data = new ArrayList<>();

    private void loadData() {
        try (var stream = Files.lines(Paths.get(FILE_PATH))) {
            List<String> lines = stream.toList();
            Buttom buttomA = null, buttomB = null;
            ClawMashine clawMashine = new ClawMashine();
            for (String line : lines) {
                if (line.startsWith("Button A")) {
                    buttomA = parseButton(line);
                } else if (line.startsWith("Button B")) {
                    buttomB = parseButton(line);
                } else if (line.startsWith("Prize")) {
                    int[] prizeCoords = parsePrize(line);
                    clawMashine.buttomA = buttomA;
                    clawMashine.buttomB = buttomB;
                    clawMashine.resultX = prizeCoords[0];
                    clawMashine.resultY = prizeCoords[1];
                    data.add(clawMashine);
                } else if (line.isEmpty()) {
                    clawMashine = new ClawMashine();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private Buttom parseButton(String line) {
        String[] parts = line.split(":")[1].trim().split(",");
        int x = Integer.parseInt(parts[0].split("\\+")[1].trim());
        int y = Integer.parseInt(parts[1].split("\\+")[1].trim());
        return new Buttom(x, y);
    }

    private static int[] parsePrize(String line) {
        String[] parts = line.split(":")[1].trim().split(",");
        int x = Integer.parseInt(parts[0].split("=")[1].trim());
        int y = Integer.parseInt(parts[1].split("=")[1].trim());
        return new int[]{x, y};
    }

    private void part1() {
        int sumCoin = 0;

        for (ClawMashine clawMashine : data) {
            boolean found = false;
            int coin = 0;
            for (int nA = 0; nA <= 100; nA++) {
                for (int nB = 0; nB <= 100; nB++) {
                    coin = 0;
                    int currentX = nA * clawMashine.buttomA.x + nB * clawMashine.buttomB.x;
                    int currentY = nA * clawMashine.buttomA.y + nB * clawMashine.buttomB.y;

                    if (currentX == clawMashine.resultX && currentY == clawMashine.resultY) {
                        coin = nA * 3 + nB;
                        System.out.println("Current claw mashine: " + coin);
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                sumCoin += coin;
                System.out.println("Current claw mashine: " + coin);
            }
        }

        System.out.println("part1: " + sumCoin);
    }

    private void part1V2() {
        int sumCoin = 0;

        for (ClawMashine clawMashine : data) {
            boolean found = false;
            int coin = 0;

            double timesB = (double) (clawMashine.resultY * clawMashine.buttomA.x - clawMashine.resultX * clawMashine.buttomA.y) / (clawMashine.buttomB.y * clawMashine.buttomA.x - clawMashine.buttomB.x * clawMashine.buttomA.y);
            double timesA = (clawMashine.resultX - clawMashine.buttomB.x * timesB) / clawMashine.buttomA.x;

            if (timesA >= 0 && timesA <= 100 && timesB >= 0 && timesB <= 100 &&
                    timesA == Math.floor(timesA) && timesB == Math.floor(timesB)) {
                coin += (int) timesA * 3 + (int) timesB;
            }

            sumCoin += coin;
            System.out.println("Current claw mashine: " + coin);
        }

        System.out.println("part1: " + sumCoin);
    }

    private void part2V2() {
        long sumCoin = 0;

        for (ClawMashine clawMashine : data) {
            long coin = 0;
            clawMashine.resultY += 10000000000000L;
            clawMashine.resultX += 10000000000000L;
            long bUp = clawMashine.resultY * clawMashine.buttomA.x - clawMashine.resultX * clawMashine.buttomA.y;
            long bDown = (long) clawMashine.buttomB.y * clawMashine.buttomA.x - (long) clawMashine.buttomB.x * clawMashine.buttomA.y;

            if (bUp % bDown == 0) {
                long b = bUp / bDown;
                long aUp = clawMashine.resultX - clawMashine.buttomB.x * b;

                if (aUp % clawMashine.buttomA.x == 0) {
                    long a = aUp / clawMashine.buttomA.x;

                    coin = (a * 3 + b);
                }
            }

            sumCoin += coin;
            System.out.println("Current claw mashine: " + coin);
        }

        System.out.println("part1: " + sumCoin);
    }

    public static void main() {
        var task = new Task();
        task.loadData();
//        task.part1();
        task.part1V2();
        task.part2V2();
    }
}
