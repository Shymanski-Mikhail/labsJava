import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class FileWriter {

    public static void writeExcellentStudents(List<GradeBook> books, String filePath)
            throws IOException {

        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));

        try {
            for (GradeBook book : books) {
                if (book.isExcellentStudent()) {
                    bw.write("ФИО: " + book.getFullName());
                    bw.newLine();
                    bw.write("Курс: " + book.getCourse()
                            + ", группа: " + book.getGroup());
                    bw.newLine();

                    for (GradeBook.Session session : book.getSessions()) {
                        bw.write("Сессия " + session.getNumber() + ":");
                        bw.newLine();

                        for (Subject subject : session.getSubjects()) {
                            if (subject.isExam()) {
                                bw.write(subject.getName()
                                        + " - " + subject.getGrade());
                                bw.newLine();
                            }
                        }
                    }
                    bw.newLine();
                }
            }
        } finally {
            bw.close();
        }
    }

    public static void writeReport(List<GradeBook> books, String filePath)
            throws IOException {

        BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));

        try {
            for (GradeBook book : books) {
                bw.write("ФИО: " + book.getFullName());
                bw.newLine();
                bw.write("Курс: " + book.getCourse()
                        + ", группа: " + book.getGroup());
                bw.newLine();

                for (GradeBook.Session session : book.getSessions()) {
                    bw.write("Сессия " + session.getNumber()
                            + ": средний балл = "
                            + String.format("%.2f", session.getAverageGrade()));
                    bw.newLine();
                }
                bw.newLine();
            }
        } finally {
            bw.close();
        }
    }
}