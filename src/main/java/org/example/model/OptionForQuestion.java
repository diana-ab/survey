package org.example.model;

import org.example.util.Validate;

public class OptionForQuestion {
    private int id;
    private String text;

    public OptionForQuestion(int id, String text) {
        if (id < 0 || id > 100) throw new IllegalArgumentException("Option id must be >=0");
        this.id = id;
        this.text = Validate.requireText(text, "Option text");
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
