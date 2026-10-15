import org.json.JSONArray;
import org.json.JSONObject;

public class JsonValidator {

    private String errorMessage;

    public boolean validate(JSONObject json) {
        errorMessage = null;

        if (!json.has("fullName")) {
            errorMessage = "Отсутствует поле fullName";
            return false;
        }
        if (json.getString("fullName").isEmpty()) {
            errorMessage = "Поле fullName пустое";
            return false;
        }

        if (!json.has("course")) {
            errorMessage = "Отсутствует поле course";
            return false;
        }
        int course = json.getInt("course");
        if (course < 1 || course > 6) {
            errorMessage = new StringBuilder()
                    .append("course должен быть от 1 до 6, получено: ")
                    .append(course)
                    .toString();
            return false;
        }

        if (!json.has("group")) {
            errorMessage = "Отсутствует поле group";
            return false;
        }
        if (json.getString("group").isEmpty()) {
            errorMessage = "Поле group пустое";
            return false;
        }

        if (!json.has("sessions")) {
            errorMessage = "Отсутствует поле sessions";
            return false;
        }

        JSONArray sessions = json.getJSONArray("sessions");
        for (int i = 0; i < sessions.length(); i++) {
            JSONObject session = sessions.getJSONObject(i);
            if (!validateSession(session, i)) {
                return false;
            }
        }

        return true;
    }

    private boolean validateSession(JSONObject session, int index) {
        if (!session.has("number")) {
            errorMessage = new StringBuilder()
                    .append("Сессия ")
                    .append(index)
                    .append(": отсутствует number")
                    .toString();
            return false;
        }
        int number = session.getInt("number");
        if (number < 1 || number > 9) {
            errorMessage = new StringBuilder()
                    .append("Сессия ")
                    .append(index)
                    .append(": number должен быть 1-9, получено: ")
                    .append(number)
                    .toString();
            return false;
        }

        if (!session.has("subjects")) {
            errorMessage = new StringBuilder()
                    .append("Сессия ")
                    .append(index)
                    .append(": отсутствует subjects")
                    .toString();
            return false;
        }

        JSONArray subjects = session.getJSONArray("subjects");
        for (int i = 0; i < subjects.length(); i++) {
            JSONObject subject = subjects.getJSONObject(i);
            if (!validateSubject(subject, index, i)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateSubject(JSONObject subject, int sIndex, int subjIndex) {
        if (!subject.has("name")) {
            errorMessage = new StringBuilder()
                    .append("Сессия ")
                    .append(sIndex)
                    .append(", предмет ")
                    .append(subjIndex)
                    .append(": нет name")
                    .toString();
            return false;
        }
        if (!subject.has("type")) {
            errorMessage = new StringBuilder()
                    .append("Сессия ")
                    .append(sIndex)
                    .append(", предмет ")
                    .append(subjIndex)
                    .append(": нет type")
                    .toString();
            return false;
        }
        String type = subject.getString("type");
        if (!type.equals("Экзамен") && !type.equals("Зачет")) {
            errorMessage = new StringBuilder()
                    .append("Сессия ")
                    .append(sIndex)
                    .append(", предмет ")
                    .append(subjIndex)
                    .append(": type должен быть 'Экзамен' или 'Зачет', получено: ")
                    .append(type)
                    .toString();
            return false;
        }
        if (!subject.has("grade")) {
            errorMessage = new StringBuilder()
                    .append("Сессия ")
                    .append(sIndex)
                    .append(", предмет ")
                    .append(subjIndex)
                    .append(": нет grade")
                    .toString();
            return false;
        }
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}