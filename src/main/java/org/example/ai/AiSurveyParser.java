package org.example.ai;

import org.example.model.Question;
import org.example.util.Validate;

import java.util.ArrayList;
import java.util.List;

import static org.example.config.AppConst.*;

public class AiSurveyParser {
    private static final String FORMAT_INSTRUCTIONS = """
            Follow these rules. Output ONLY in the format below.
            
            RANDOMNESS:
            - Choose 1 to 3 questions.
            - For each question, choose 2 to 4 options.
            - Vary wording and avoid repeating structures.
            
            HARD RULES:
            - No extra text or explanations.
            - Exactly one blank line between questions.
            
            EXAMPLE FORMAT:
            Q1: <question text>
            - <option 1>
            - <option 2>
            - <option 3>
            - <option 4>
            
            Q2: <question text>
            - <option 1>
            - <option 2>
            
            Q3: <question text>
            - <option 1>
            - <option 2>
            - <option 3>
            """;
    private static final String ERR_NO_VALID_QUESTIONS =
            "No valid questions parsed. Expected format:\n" +
                    "Q1: <question text>\n" +
                    "- <option>\n- <option>\n\n" +
                    "(Use a blank line between questions.)";
    private static final String ERR_RESPONSE_LABEL = "AI response (extra)";
    private static final String BLANK_BLOCK_SPLIT_REGEX = "\\n\\s*\\n";
    private static final String LINE_SPLIT_REGEX = "\\n";
    private static final String NEWLINE_WIN = "\r\n";
    private static final String NEWLINE_UNIX = "\n";
    private static final String OPTION_PREFIX = "- ";
    private static final char HEADER_PREFIX_UPPER = 'Q';
    private static final char HEADER_PREFIX_LOWER = 'q';
    private static final char HEADER_SEPARATOR = ':';

    public static String getSystemFormatInstruction() {
        return FORMAT_INSTRUCTIONS;
    }

    public List<Question> parseToQuestions(String rawText) {
        String normalized = normalize(rawText);
        String[] blocks = splitIntoBlocks(normalized);

        List<Question> questions = new ArrayList<>(Math.min(blocks.length, MAX_QUESTIONS));
        int nextIndex = 0;

        for (String block : blocks) {
            if (questions.size() == MAX_QUESTIONS) {
                break;
            }
            Question q = parseBlock(block, nextIndex);
            if (q != null) {
                questions.add(q);
                nextIndex++;
            }
        }
        ensureAtLeastOneQuestion(questions);
        return questions;
    }

    private String normalize(String text) {
        String t = Validate.requireText(text, ERR_RESPONSE_LABEL);
        return t.replace(NEWLINE_WIN, NEWLINE_UNIX).trim();
    }

    private String[] splitIntoBlocks(String text) {
        return text.split(BLANK_BLOCK_SPLIT_REGEX);
    }

    private Question parseBlock(String block, int questionIndex) {
        String[] lines = block.split(LINE_SPLIT_REGEX);
        if (lines.length < 1 + MIN_OPTIONS) {
            return null;
        }

        String header = lines[0].trim();
        int sep = validHeaderSeparatorIndex(header);
        if (sep < 0) {
            return null;
        }

        String questionText = header.substring(sep + 1).trim();
        if (questionText.isEmpty()) {
            return null;
        }

        List<String> options = extractOptions(lines);
        int optionCount = options.size();
        if (optionCount < MIN_OPTIONS || optionCount > MAX_OPTIONS) {
            return null;
        }

        return new Question(questionIndex, questionText, options);
    }

    private int validHeaderSeparatorIndex(String header) {
        if (header == null || header.isEmpty()) {
            return -1;
        }
        char c0 = header.charAt(0);
        if (c0 != HEADER_PREFIX_UPPER && c0 != HEADER_PREFIX_LOWER) {
            return -1;
        }
        return header.indexOf(HEADER_SEPARATOR);
    }

    private List<String> extractOptions(String[] lines) {
        int capacity = Math.min(MAX_OPTIONS, Math.max(0, lines.length - 1));
        List<String> options = new ArrayList<>(capacity);

        for (int i = 1; i < lines.length && options.size() < capacity; i++) {
            String line = lines[i].trim();
            if (!line.startsWith(OPTION_PREFIX)) {
                continue;
            }
            String opt = line.substring(OPTION_PREFIX.length()).trim();
            if (!opt.isEmpty()) {
                options.add(opt);
            }
        }
        return options;
    }

    private void ensureAtLeastOneQuestion(List<Question> questions) {
        if (questions.size() < MIN_QUESTIONS) {
            throw new IllegalArgumentException(ERR_NO_VALID_QUESTIONS);
        }
    }
}
