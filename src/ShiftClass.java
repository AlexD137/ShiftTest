import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShiftClass {
    private static Path filePath;
    public static void main(String[] args) {
        startProg(new String[]{"-o","C:\\Users\\dviri\\IdeaProjects\\main\\ShiftTest\\src", "in1.txt", "in2.txt"});
    }

    private static void startProg(String[] args) {


        List<String> strings = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (args[i].endsWith(".txt")) {
                strings.add(args[i]);
            } else {
                switch (args[i]) {
                    case "-s" -> System.out.println("параметры запуска S");
                    case "-p" -> System.out.println("параметры запуска P");
                    case "-o" -> filePath = setNewPath(args[i + 1]);
                    case "-f" -> System.out.println("параметры запуска F");
                    case "-a" -> System.out.println("параметры запуска A");
                }
            }
        }
        strings.parallelStream().forEach(x -> {
            try {
                readStrings(x);
            } catch (IOException e) {
                System.out.println("Файл не найден");

            }
        });
    }

    private static Path setNewPath(String path) {
        return Paths.get(path);
    }


    private static void readStrings(String arg) throws IOException {
        Path filePath = Paths.get(arg);
        Pattern integerPattern = Pattern.compile("^-?\\d+$");
        Pattern doublePattern = Pattern.compile("^-?\\d+\\.\\d+$|^-?\\d+\\.\\d+E[+-]?\\d+$");
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
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
                    String stringValue = line;
                    toStringFile(line);
                    System.out.println("Это тип стринг - " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void writeToFile(String fileName, String content) throws IOException {
        if (filePath == null) {
            filePath = Paths.get(fileName);
        }



        Files.write(filePath, (content + System.lineSeparator()).getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    private static void toStringFile(String s) throws IOException {
        writeToFile("string.txt", s);
    }

    private static void toDoubleFile(String d) throws IOException {
        writeToFile("double.txt", d);
    }

    private static void toIntegerFile(String i) throws IOException {
        writeToFile("integer.txt", i);
    }


}
