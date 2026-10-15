import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonSerializer {

    public void saveToFile(GradeBook book, String filePath) throws IOException {
        JSONObject json = book.toJson();
        try (FileWriter fw = new FileWriter(filePath)) {
            fw.write(json.toString(4));
        }
    }

    public void printWithDescriptions(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
        JSONObject json = new JSONObject(content);
        printNode(json, 0);
    }

    private void printNode(JSONObject json, int indent) {
        String pad = "  ".repeat(indent);

        String[] order = {"fullName", "course", "group",
                "number", "name", "type", "grade",
                "sessions", "subjects"};

        for (String key : order) {
            if (!json.has(key)) continue;

            Object value = json.get(key);
            String description = describeKey(key);

            if (value instanceof JSONObject) {
                System.out.println(pad + description + ":");
                printNode((JSONObject) value, indent + 1);
            } else if (value instanceof JSONArray) {
                System.out.println(pad + description + ":");
                JSONArray arr = (JSONArray) value;
                for (int i = 0; i < arr.length(); i++) {
                    printNode(arr.getJSONObject(i), indent + 1);
                }
            } else {
                System.out.println(pad + description + ": " + value);
            }
        }
    }

    private String describeKey(String key) {
        switch (key) {
            case "fullName": return "ФИО";
            case "course": return "Курс";
            case "group": return "Группа";
            case "sessions": return "Сессии";
            case "number": return "Номер сессии";
            case "subjects": return "Предметы";
            case "name": return "Название предмета";
            case "type": return "Тип";
            case "grade": return "Оценка";
            default: return key;
        }
    }
}