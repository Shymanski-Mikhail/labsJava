import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    private static final String INPUT_FILE = "input.txt";
    private static final String OUTPUT_FILE = "output.txt";

    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));

        try {
            int width = readWidth(br);

            FileManager fileManager = new FileManager(INPUT_FILE, OUTPUT_FILE);

            List<String> lines = fileManager.readLines();

            TextAligner aligner = new TextAligner(width);
            List<String> aligned = aligner.align(lines);

            fileManager.writeLines(aligned);

            System.out.println("Текст выровнен. Результат в " + OUTPUT_FILE);

            System.out.println();
            System.out.println("Результат:");
            for (String line : aligned) {
                System.out.println(line);
            }

        } catch (IOException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private static int readWidth(BufferedReader br) throws IOException {
        while (true) {
            System.out.print("Введите ширину текста: ");
            String line = br.readLine();

            if (line == null) {
                throw new IOException("Поток ввода закрыт");
            }

            line = line.trim();

            if (line.isEmpty()) {
                System.out.println("Ввод не может быть пустым");
                continue;
            }

            try {
                int width = Integer.parseInt(line);
                if (width < 10) {
                    System.out.println("Ширина должна быть минимум 10");
                    continue;
                }
                return width;
            } catch (NumberFormatException e) {
                System.out.println("Не целое число");
            }
        }
    }
}