package day2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class TTask2 {
    public static void main(String[] args) {
        List<String> reports = null;
        try (Stream<String> lines = Files.lines(Paths.get("D:\\Project\\adventofcode2024\\test\\src\\day2\\file2.task"), Charset.defaultCharset())) {
            reports = lines.toList();
        }
        catch (IOException exp) {
            System.out.println(exp.getMessage());
        }


        var safeReports = 0;
        for (var report : reports) {
            var levels = report.split(" ");
            if (isSafe(levels)) {
                safeReports++;
            }
        }
        System.out.println("[1] Safe reports = " + safeReports);

        var toleratedSafeReports = 0;
        for (var report : reports) {
            var levels = report.split(" ");
            if (isSafeWithOneRemoval(levels)) {
                toleratedSafeReports++;
            }
        }
        System.out.println("[2] Tolerated safe reports = " + toleratedSafeReports);
    }

    private static boolean isSafeWithOneRemoval(String[] levels) {
        if (isSafe(levels)) {
            return true;
        }
        for (int i = 0; i < levels.length; i++) {
            if (isSafe(removeLevel(levels, i))) {
                return true;
            }
        }
        return false;
    }

    private static String[] removeLevel(String[] levels, int index) {
        int n = levels.length;
        var attempt = new String[n - 1];
        for (int i = 0, j = 0; i < n; i++) {
            if (i != index) {
                attempt[j++] = levels[i];
            }
        }
        return attempt;
    }

    private static boolean isSafe(String[] levels) {
        var currAttempt = true;
        var inc = Integer.parseInt(levels[1]) > Integer.parseInt(levels[0]);
        for (int i = 1; i < levels.length && currAttempt; i++) {
            var curr = Integer.parseInt(levels[i]);
            var prev = Integer.parseInt(levels[i - 1]);
            int diff = curr - prev;
            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) {
                currAttempt = false;
            }
            if (inc && diff <= 0) {
                currAttempt = false;
            }
            if (!inc && diff >= 0) {
                currAttempt = false;
            }
        }
        return currAttempt;
    }
}
