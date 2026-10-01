import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public static List<GradeBook> readGradeBooks(String file1, String file2)
            throws IOException {
        List<GradeBook> books = new ArrayList<GradeBook>();
        readFile(file1, 1, books);
        readFile(file2, 2, books);
        return books;
    }

    private static void readFile(String filePath, int sessionNumber,
                                 List<GradeBook> books) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

        try {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(";");
                if (parts.length != 6) {
                    continue;
                }

                String fullName = parts[0].trim();
                int course = Integer.parseInt(parts[1].trim());
                String group = parts[2].trim();
                String subjectName = parts[3].trim();
                String subjectType = parts[4].trim();
                String grade = parts[5].trim();

                GradeBook book = findOrCreateBook(books, fullName, course, group);
                GradeBook.Session session = book.getOrCreateSession(sessionNumber);
                session.addSubject(new Subject(subjectName, subjectType, grade));
            }
        } finally {
            br.close();
        }
    }

    private static GradeBook findOrCreateBook(List<GradeBook> books,
                                              String fullName, int course,
                                              String group) {
        for (GradeBook book : books) {
            if (book.getFullName().equals(fullName)) {
                return book;
            }
        }
        GradeBook newBook = new GradeBook(fullName, course, group);
        books.add(newBook);
        return newBook;
    }
}