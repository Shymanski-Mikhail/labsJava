import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static final String JSON_FILE = "resources/gradebook.json";
    private static final String BROKEN_FILE = "resources/gradebook-broken.json";

    public static void main(String[] args) {
        try {
            GradeBook book = createSampleBook();

            JsonSerializer serializer = new JsonSerializer();
            serializer.saveToFile(book, JSON_FILE);
            System.out.println("JSON сохранён: " + JSON_FILE);

            JsonValidator validator = new JsonValidator();

            System.out.println();
            System.out.println("Проверка " + JSON_FILE + ":");
            JSONObject json1 = readJson(JSON_FILE);
            if (validator.validate(json1)) {
                System.out.println("Файл корректен");
                serializer.printWithDescriptions(JSON_FILE);
            } else {
                System.out.println("Файл НЕкорректен: " + validator.getErrorMessage());
            }

            System.out.println();
            System.out.println("Проверка " + BROKEN_FILE + ":");
            JSONObject json2 = readJson(BROKEN_FILE);
            if (validator.validate(json2)) {
                System.out.println("Файл корректен");
            } else {
                System.out.println("Файл НЕкорректен: " + validator.getErrorMessage());
            }

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }

    private static JSONObject readJson(String path) throws Exception {
        String content = new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
        return new JSONObject(content);
    }

    private static GradeBook createSampleBook() {
        GradeBook book = new GradeBook("Иванов Иван Иванович", 1, "101");

        GradeBook.Session s1 = book.getOrCreateSession(1);
        s1.addSubject(new Subject("Математика", "Экзамен", "9"));
        s1.addSubject(new Subject("Физика", "Экзамен", "10"));
        s1.addSubject(new Subject("История", "Зачет", "Зачет"));

        GradeBook.Session s2 = book.getOrCreateSession(2);
        s2.addSubject(new Subject("Программирование", "Экзамен", "10"));
        s2.addSubject(new Subject("Базы данных", "Экзамен", "9"));

        return book;
    }
}