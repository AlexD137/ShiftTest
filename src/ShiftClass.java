import java.io.BufferedReader;
import java.io.IOException;
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
    private static boolean deleteFlag = true;
    private static boolean shortStat = false;
    private static AtomicInteger integerCount = new AtomicInteger(0);
    private static AtomicInteger doubleCount = new AtomicInteger(0);
    private static AtomicInteger stringCount = new AtomicInteger(0);
    private static String integerFileName;

    public static void main(String[] args) {
        startProg(new String[]{"-p", "preffix", "-s","in1.txt", "in2.txt"});
    }

    private static void startProg(String[] args) {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].endsWith(".txt")) {
                strings.add(args[i]);
            } else {
                switch (args[i]) {
                    case "-s" -> shortStat = true;
                    case "-p" ->  preffix = setPreffix(args[i + 1]);
                    case "-o" ->  filePath = setNewPath(args[i + 1]);
                    case "-f" -> System.out.println("параметры запуска F");
                    case "-a" -> deleteFlag = false;
                }
            }
         }
        if (filePath == null) {
            filePath = Paths.get(System.getProperty("user.dir"));
        }
        deleteFiles(deleteFlag);
        strings.parallelStream().forEach(x -> {
            try {
                readStrings(x);
            } catch (IOException e) {
                System.out.println("Файл не найден");
            }
        });
        if(shortStat){
            printShortStat();
        }
    }



    private static String setPreffix(String arg) {

        return arg;
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
                Matcher integerMatcher = integerPattern.matcher(line);
                Matcher doubleMatcher = doublePattern.matcher(line);
                if (integerMatcher.matches()) {
                    toIntegerFile(line);
                    System.out.println("Это тип инт - " + line);
                } else if (doubleMatcher.matches()) {
                    toDoubleFile(line);
                    System.out.println("Это тип дабл - " + line);
                } else {
                    toStringFile(line);
                    System.out.println("Это тип стринг - " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized static void writeToFile(String fileName, String content) throws IOException {

        Path fullPath = filePath.resolve(fileName);


            Files.write(fullPath, (content + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

    }

    private static void toStringFile(String s) throws IOException {
        writeToFile(preffix + "string.txt", s);
        stringCount.incrementAndGet();
    }

    private static void toDoubleFile(String d) throws IOException {
        writeToFile(preffix +"double.txt", d);
        doubleCount.incrementAndGet();
    }

    private static void toIntegerFile(String i) throws IOException {
        writeToFile(preffix +"integer.txt", i);
        integerCount.incrementAndGet();
    }
    private static void deleteFiles(Boolean flag) {
        if(flag) {
            try {
                Files.deleteIfExists(filePath.resolve(preffix + "string.txt"));
                Files.deleteIfExists(filePath.resolve(preffix + "double.txt"));
                Files.deleteIfExists(filePath.resolve(preffix + "integer.txt"));
            } catch (IOException e) {
                System.out.println("Ошибка при удалении файлов: " + e.getMessage());
            }
        }
    }
    private static void printShortStat() {
        System.out.printf("Было добавлено строк в файл integer.txt - %d, double.txt - %d, string.txt - %d%n",
                integerCount.get(), doubleCount.get(), stringCount.get());
    }
}
