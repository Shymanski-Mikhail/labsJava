public class Subject {

    private String name;
    private String type;
    private String grade;

    public Subject() {
    }

    public Subject(String name, String type, String grade) {
        this.name = name;
        this.type = type;
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getGrade() {
        return grade;
    }
}