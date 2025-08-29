package org.example.model;

import org.example.util.Validate;

public class OptionForQuestion {
    private static final int MIN_ID = 0;
    private static final int MAX_ID = 100;
    private static final String ERR_ID_RANGE_FMT = "Option id must be between %d and %d";
    private static final String ERR_TEXT_LABEL = "Option text";

    private int id;
    private String text;

    public OptionForQuestion(int id, String text) {
        if (id < MIN_ID || id > MAX_ID) {
            throw new IllegalArgumentException(String.format(ERR_ID_RANGE_FMT, MIN_ID, MAX_ID));
        }
        this.id = id;
        this.text = Validate.requireText(text, ERR_TEXT_LABEL);
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }
}
