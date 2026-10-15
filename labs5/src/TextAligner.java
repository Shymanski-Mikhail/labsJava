import java.util.ArrayList;
import java.util.List;

public class TextAligner {

    private final int width;

    public TextAligner(int width) {
        this.width = width;
    }

    public List<String> align(List<String> lines) {
        List<String> result = new ArrayList<String>();
        List<String> paragraph = new ArrayList<String>();

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                if (!paragraph.isEmpty()) {
                    result.addAll(alignParagraph(paragraph));
                    paragraph.clear();
                }
                result.add("");
            } else {
                boolean startsWithIndent = line.length() > 0 && line.charAt(0) == ' ';

                if (startsWithIndent && !paragraph.isEmpty()) {
                    result.addAll(alignParagraph(paragraph));
                    paragraph.clear();
                }

                paragraph.add(line);
            }
        }

        if (!paragraph.isEmpty()) {
            result.addAll(alignParagraph(paragraph));
        }

        return result;
    }

    private List<String> alignParagraph(List<String> paragraphLines) {
        List<String> result = new ArrayList<String>();

        String leadingSpaces = getLeadingSpaces(paragraphLines.get(0));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paragraphLines.size(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(paragraphLines.get(i).trim());
        }
        String paragraph = sb.toString();

        String[] parts = paragraph.split("\\s+");
        List<String> words = new ArrayList<String>();
        for (String p : parts) {
            if (!p.isEmpty()) {
                words.add(p);
            }
        }

        if (words.isEmpty()) {
            return result;
        }

        List<String> wrapped = wrapWords(words, leadingSpaces);

        for (int i = 0; i < wrapped.size() - 1; i++) {
            result.add(justify(wrapped.get(i), leadingSpaces));
        }
        result.add(wrapped.get(wrapped.size() - 1));

        return result;
    }

    private String getLeadingSpaces(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') {
            i++;
        }
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < i; j++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    private List<String> wrapWords(List<String> words, String leadingSpaces) {
        List<String> lines = new ArrayList<String>();
        StringBuilder current = new StringBuilder();
        int leadingLen = leadingSpaces.length();

        for (String word : words) {
            int currentLen = current.length();
            int addLen = (currentLen == 0 ? 0 : 1) + word.length();

            if (currentLen + addLen + leadingLen <= width) {
                if (currentLen > 0) {
                    current.append(' ');
                }
                current.append(word);
            } else {
                if (current.length() > 0) {
                    lines.add(current.toString());
                }
                current = new StringBuilder(word);
            }
        }

        if (current.length() > 0) {
            lines.add(current.toString());
        }

        return lines;
    }

    private String justify(String line, String leadingSpaces) {
        String[] parts = line.trim().split("\\s+");

        if (parts.length <= 1) {
            return leadingSpaces + line.trim();
        }

        int textLength = 0;
        for (String p : parts) {
            textLength += p.length();
        }

        int availableSpace = width - textLength - leadingSpaces.length();

        if (availableSpace <= 0) {
            return leadingSpaces + line.trim();
        }

        int gaps = parts.length - 1;
        int baseSpaces = availableSpace / gaps;
        int extraSpaces = availableSpace % gaps;

        StringBuilder sb = new StringBuilder(leadingSpaces);
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]);
            if (i < gaps) {
                int spaces = baseSpaces + (i < extraSpaces ? 1 : 0);
                for (int s = 0; s < spaces; s++) {
                    sb.append(' ');
                }
            }
        }

        return sb.toString();
    }
}