package org.example.model;

import org.example.util.Validate;

/** One option in a multiple-choice question. */
public final class OptionForQuestion {
    private final int id;          // 0..n-1
    private final String text;     // shown to users

    public OptionForQuestion(int id, String text) {
        if (id < 0 || id > 100) throw new IllegalArgumentException("Option id must be >=0");
        this.id = id;
        this.text = Validate.requireText(text, "Option text");
    }

    public int getId() { return id; }
    public String getText() { return text; }

    @Override public String toString() { return text; }
}
