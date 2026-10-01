import java.io.IOException;
import java.util.List;

public class Main {

    private static final String INPUT_FILE_1 = "input/vedomost1.txt";
    private static final String INPUT_FILE_2 = "input/vedomost2.txt";
    private static final String OUTPUT_FILE_1 = "output/excellent.txt";
    private static final String OUTPUT_FILE_2 = "output/report.txt";

    public static void main(String[] args) {

        try {
            List<GradeBook> books = FileReader.readGradeBooks(
                    INPUT_FILE_1, INPUT_FILE_2);

            System.out.println("Загружено студентов: " + books.size());

            for (GradeBook book : books) {
                System.out.println(book);
                for (GradeBook.Session session : book.getSessions()) {
                    System.out.println("  " + session);
                    System.out.println("  Средний балл: "
                            + String.format("%.2f", session.getAverageGrade()));
                }
                System.out.println("  Отличник: " + book.isExcellentStudent());
            }

            FileWriter.writeExcellentStudents(books, OUTPUT_FILE_1);
            FileWriter.writeReport(books, OUTPUT_FILE_2);

            System.out.println("Файл 1: " + OUTPUT_FILE_1);
            System.out.println("Файл 2: " + OUTPUT_FILE_2);

        } catch (IOException e) {
            System.err.println("Ошибка ввода/вывода: " + e.getMessage());
        }
    }
}