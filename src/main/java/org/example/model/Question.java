package org.example.model;

import org.example.config.AppConst;
import org.example.util.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {

    private static final int MIN_ID = 0;
    private static final int MAX_ID = 100;
    private static final String ERR_ID_RANGE_FMT = "Question id must be between %d and %d";
    private static final String ERR_TEXT_LABEL = "Question text";
    private static final String ERR_OPTIONS_LABEL = "Options";
    private static final int OPTION_ID_START = 0;

    private  int id;
    private  String text;
    private  List<OptionForQuestion> options;

    public Question(int id, String text, List<String> optionTexts) {
        if (id < MIN_ID || id > MAX_ID) {
            throw new IllegalArgumentException(String.format(ERR_ID_RANGE_FMT, MIN_ID, MAX_ID));
        }

        this.id = id;
        this.text = Validate.requireText(text, ERR_TEXT_LABEL);
        Validate.requireSizeBetween(optionTexts, AppConst.MIN_OPTIONS, AppConst.MAX_OPTIONS, ERR_OPTIONS_LABEL);

        List<OptionForQuestion> tmp = new ArrayList<>(optionTexts.size());
        int nextOptionId = OPTION_ID_START;
        for (String optText : optionTexts) {
            tmp.add(new OptionForQuestion(nextOptionId++, optText));
        }
        this.options = Collections.unmodifiableList(tmp);
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<OptionForQuestion> getOptions() {
        return options;
    }
}
