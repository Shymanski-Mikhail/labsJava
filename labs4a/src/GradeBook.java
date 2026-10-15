import org.json.JSONArray;
import org.json.JSONObject;

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

    public JSONObject toJson() {
        JSONObject root = new JSONObject();
        root.put("fullName", fullName);
        root.put("course", course);
        root.put("group", group);

        JSONArray sessionsArray = new JSONArray();
        for (Session session : sessions) {
            JSONObject sessionObj = new JSONObject();
            sessionObj.put("number", session.getNumber());

            JSONArray subjectsArray = new JSONArray();
            for (Subject subject : session.getSubjects()) {
                JSONObject subjectObj = new JSONObject();
                subjectObj.put("name", subject.getName());
                subjectObj.put("type", subject.getType());
                subjectObj.put("grade", subject.getGrade());
                subjectsArray.put(subjectObj);
            }
            sessionObj.put("subjects", subjectsArray);
            sessionsArray.put(sessionObj);
        }
        root.put("sessions", sessionsArray);
        return root;
    }

    public static class Session {

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
    }
}