package org.example.model;

import org.example.ai.AiSurveyParser;
import org.example.config.AppConst;
import org.example.util.Validate;

import java.util.Collections;
import java.util.List;


public class Survey {
    private long id;
    private String title;
    private List<Question> questions;

    public Survey(long id, String title, List<Question> questions) {
        this.id = id;
        this.title = Validate.requireText(title, "Survey title");
        Validate.requireSizeBetween(questions, AppConst.MIN_QUESTIONS , AppConst.MAX_QUESTIONS, "Questions");
        this.questions = Collections.unmodifiableList(questions);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "Survey{" + id + ", '" + title + "', q=" + questions.size() + "}";
    }
}
