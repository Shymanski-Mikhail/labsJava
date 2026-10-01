public class Subject {

    private final String name;
    private final String type;
    private final String grade;

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

    public boolean isExam() {
        return "Экзамен".equalsIgnoreCase(type);
    }

    public boolean isCredit() {
        return "Зачет".equalsIgnoreCase(type);
    }

    public int getNumericGrade() {
        if (isExam()) {
            return Integer.parseInt(grade);
        }
        return 0;
    }

    public boolean isExcellent() {
        if (isExam()) {
            return getNumericGrade() >= 9;
        }
        return "Зачет".equalsIgnoreCase(grade);
    }

    @Override
    public String toString() {
        return name + " (" + type + "): " + grade;
    }
}