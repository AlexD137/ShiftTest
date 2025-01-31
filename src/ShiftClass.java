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
    public static void main(String[] args) {
        startProg(new String[]{"-s", "-a", "-p", "sample-", "in1.txt", "in2.txt"});
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
                    case "-o" -> System.out.println("параметры запуска O");
                    case "-f" -> System.out.println("параметры запуска F");
                    case "-a" -> System.out.println("параметры запуска A");
                }
            }
        }
        strings.stream().forEach(x -> {
            try {
                readStrings(x);
            } catch (IOException e) {
                System.out.println("Файл не найден");
                ;
            }
        });
    }


    private static void readStrings(String arg) throws IOException {
        Path filePath = Paths.get(arg);
        Pattern integerPattern = Pattern.compile("^-?\\d+$");
        Pattern doublePattern = Pattern.compile("^-?\\d+\\.\\d+$|^-?\\d+\\.\\d+E[+-]?\\d+$");
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher integerMatcher = integerPattern.matcher(line);
                Matcher doubleMatcher = doublePattern.matcher(line);
                if (integerMatcher.matches()) {
                    int intValue = Integer.parseInt(reader.readLine());
                    toIntegerFile(intValue);
                    System.out.println("Это тип инт - " + intValue);
                } else if (doubleMatcher.matches()) {
                    double doubleValue = Double.parseDouble(reader.readLine());
                    toDoubleFile(doubleValue);
                    System.out.println("Это тип дабл - " + doubleValue);
                } else {
                    String stringValue = reader.readLine();
                    toStringFile(stringValue);
                    System.out.println("Это тип стринг - " + stringValue);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void toStringFile(String s) throws IOException {
        Path path = Paths.get("string.txt");

        if (!Files.exists(path)) {
            Files.createFile(path);
            System.out.println("Создан файл: " + path);
        }

        Files.write(path, (s + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);


    }

    private static void toDoubleFile(Double d) throws IOException {
        Path path = Paths.get("double.txt");


        if (!Files.exists(path)) {
            Files.createFile(path);
            System.out.println("Создан файл: " + path);
        }

        Files.write(path, (Double.toString(d) + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

    }

    private static void toIntegerFile(Integer i) throws IOException {
        Path path = Paths.get("integer.txt");


        if (!Files.exists(path)) {
            Files.createFile(path);
            System.out.println("Создан файл: " + path);
        }

        Files.write(path, (Integer.toString(i) + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

    }


}
