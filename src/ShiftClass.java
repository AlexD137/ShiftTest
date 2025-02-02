import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShiftClass {
    private static Path filePath;
    private static String preffix = "";
    private static boolean deleteFileFlag = true;
    private static boolean shortStat = false;
    private static boolean fullStat = false;
    private static AtomicInteger integerCount = new AtomicInteger(0);
    private static AtomicInteger doubleCount = new AtomicInteger(0);
    private static AtomicInteger stringCount = new AtomicInteger(0);
    private static String integerFileName = preffix + "integer.txt";
    private static String stringFileName = preffix + "string.txt";
    private static String doubleFileName = preffix + "double.txt";
    private static int minLengthString = Integer.MAX_VALUE;
    private static int maxLengthString = 0;
    private static BigInteger sum = BigInteger.ZERO;
    private static BigInteger max = null;
    private static BigInteger min = null;
    private static BigInteger avg = BigInteger.ZERO;

    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].endsWith(".txt")) {
                strings.add(args[i]);
            } else {
                switch (args[i]) {
                    case "-s" -> shortStat = true;
                    case "-p" -> {
                        preffix = setPreffix(args[i + 1]);
                        updateFileNames();
                    }
                    case "-o" -> filePath = setNewPath(args[i + 1]);
                    case "-f" -> fullStat = true;
                    case "-a" -> deleteFileFlag = false;
                }
            }
        }

        if (filePath == null || !Files.exists(filePath)) {
            filePath = Paths.get(System.getProperty("user.dir"));
        }
        deleteFiles(deleteFileFlag);

        strings.parallelStream().forEach(file -> {
            try {
                readStrings(file);
            } catch (IOException e) {
                System.out.println("Ошибка при чтении файла " + file + ": " + e.getMessage());
            }
        });

        if (fullStat) {
            printFullStat();
            shortStat = false;
        }
        if (shortStat) {
            printShortStat();
        }

        if (!Files.exists(filePath) && (integerCount.get() > 0 || doubleCount.get() > 0 || stringCount.get() > 0)) {
            System.out.println("Путь указанный в параметрах запуска не существует, файлы созданы в папке по умолчанию!");
        }
    }

    private static void printFullStat() {
        printShortStat();

        if (integerCount.get() > 0) {
            System.out.printf(
                    "Максимальное значение - %d, минимальное значение - %d, среднее значение - %d, сумма - %d ",
                    max, min, sum.divide(BigInteger.valueOf(integerCount.get())), sum
            );
        }

        if (stringCount.get() > 0) {
            System.out.printf(
                    "Самая длинная строка - %d символа(ов), самая короткая строка - %d символа(ов)",
                    maxLengthString, minLengthString
            );
        }
    }

    private static String setPreffix(String arg) {
        return arg;
    }

    private static void updateFileNames() {
        integerFileName = preffix + "integer.txt";
        stringFileName = preffix + "string.txt";
        doubleFileName = preffix + "double.txt";
    }

    private static Path setNewPath(String path) {
        return Paths.get(path);
    }

    private static void readStrings(String arg) throws IOException {
        Path path = Paths.get(arg);
        Pattern integerPattern = Pattern.compile("^-?\\d+$");
        Pattern doublePattern = Pattern.compile("^-?\\d+\\.\\d+$|^-?\\d+\\.\\d+E[+-]?\\d+$");

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                Matcher integerMatcher = integerPattern.matcher(line);
                Matcher doubleMatcher = doublePattern.matcher(line);

                if (integerMatcher.matches()) {
                    toIntegerFile(line);
                } else if (doubleMatcher.matches()) {
                    toDoubleFile(line);
                } else {
                    toStringFile(line);
                }
            }
        } catch (IOException e) {
            System.out.printf("Файл %s не найден\n", arg);
        }
    }

    private synchronized static void writeToFile(String fileName, String content) throws IOException {
        Path fullPath = filePath.resolve(fileName);
        Files.write(fullPath, (content + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private static void toStringFile(String s) throws IOException {
        writeToFile(stringFileName, s);
        stringCount.incrementAndGet();

        synchronized (ShiftClass.class) {
            if (s.length() > maxLengthString) {
                maxLengthString = s.length();
            }
            if (s.length() < minLengthString) {
                minLengthString = s.length();
            }
        }
    }

    private static void toDoubleFile(String d) throws IOException {
        writeToFile(doubleFileName, d);
        doubleCount.incrementAndGet();
    }

    private static void toIntegerFile(String i) throws IOException {
        writeToFile(integerFileName, i);
        integerCount.incrementAndGet();

        BigInteger integer = new BigInteger(i);
        synchronized (ShiftClass.class) {
            sum = sum.add(integer);
            if (max == null || integer.compareTo(max) > 0) {
                max = integer;
            }
            if (min == null || integer.compareTo(min) < 0) {
                min = integer;
            }
        }
    }

    private static void deleteFiles(Boolean flag) {
        if (flag) {
            try {
                Files.deleteIfExists(filePath.resolve(integerFileName));
                Files.deleteIfExists(filePath.resolve(stringFileName));
                Files.deleteIfExists(filePath.resolve(doubleFileName));
            } catch (IOException e) {
                System.out.println("Ошибка при удалении файлов: " + e.getMessage());
            }
        }
    }

    private static void printShortStat() {
        if (integerCount.get() > 0) {
            System.out.printf("Было добавлено строк в файл %s - %d\n", integerFileName, integerCount.get());
        }
        if (doubleCount.get() > 0) {
            System.out.printf("Было добавлено строк в файл %s - %d\n", doubleFileName, doubleCount.get());
        }
        if (stringCount.get() > 0) {
            System.out.printf("Было добавлено строк в файл %s - %d\n", stringFileName, stringCount.get());
        }
    }
}