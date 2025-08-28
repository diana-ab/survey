package org.example.model;

import org.example.util.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Multiple-choice question with 2..4 options. */
public final class Question {
    private final int id;                    // 0..2 (we limit to 1..3 questions)
    private final String text;
    private final List<OptionForQuestion> options;

    public Question(int id, String text, List<String> optionTexts) {
        if (id < 0 || id > 100) throw new IllegalArgumentException("Question id must be >=0");
        this.id = id;
        this.text = Validate.requireText(text, "Question text");
        Validate.requireSizeBetween(optionTexts, 2, 4, "Options");
        List<OptionForQuestion> tmp = new ArrayList<>();
        for (int i = 0; i < optionTexts.size(); i++) {
            tmp.add(new OptionForQuestion(i, optionTexts.get(i)));
        }
        this.options = Collections.unmodifiableList(tmp);
    }

    public int getId() { return id; }
    public String getText() { return text; }
    public List<OptionForQuestion> getOptions() { return options; }
}
