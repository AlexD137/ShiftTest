//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if(args[i].endsWith(".txt")){
                System.out.println(args[i]);
            }
                else {
                switch (args[i]) {
                    case "-s" -> System.out.println("параметры запуска S");
                    case "-p" -> System.out.println("параметры запуска P");
                    case "-o" -> System.out.println("параметры запуска O");
                    case "-f" -> System.out.println("параметры запуска F");

                }
            }
        }
    }
}