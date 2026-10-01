import java.util.ArrayList;
import java.util.List;

public class GradeBook {

    private final String fullName;
    private final int course;
    private final String group;
    private final List<Session> sessions;

    public GradeBook(String fullName, int course, String group) {
        this.fullName = fullName;
        this.course = course;
        this.group = group;
        this.sessions = new ArrayList<Session>();
    }

    public String getFullName() {
        return fullName;
    }

    public int getCourse() {
        return course;
    }

    public String getGroup() {
        return group;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public Session getOrCreateSession(int number) {
        for (Session s : sessions) {
            if (s.getNumber() == number) {
                return s;
            }
        }
        Session newSession = new Session(number);
        sessions.add(newSession);
        return newSession;
    }

    public boolean isExcellentStudent() {
        if (sessions.isEmpty()) {
            return false;
        }
        for (Session session : sessions) {
            for (Subject subject : session.getSubjects()) {
                if (!subject.isExcellent()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return fullName + " (" + course + " курс, группа " + group + ")";
    }

    public class Session {

        private final int number;
        private final List<Subject> subjects;

        public Session(int number) {
            this.number = number;
            this.subjects = new ArrayList<Subject>();
        }

        public int getNumber() {
            return number;
        }

        public List<Subject> getSubjects() {
            return subjects;
        }

        public void addSubject(Subject subject) {
            subjects.add(subject);
        }

        public double getAverageGrade() {
            int sum = 0;
            int count = 0;
            for (Subject subject : subjects) {
                if (subject.isExam()) {
                    sum += subject.getNumericGrade();
                    count++;
                }
            }
            if (count == 0) {
                return 0.0;
            }
            return (double) sum / count;
        }

        @Override
        public String toString() {
            return "Сессия " + number + ": " + subjects;
        }
    }
}