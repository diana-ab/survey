package org.example.config;

public final class Texts {
    private Texts() {}

    // Bot join/help
    public static final String HELP =
            "Send 'Hi' or press Start to join the community.";
    public static final String WELCOME_FMT =
            "Welcome, %s! Community size: %d.";
    public static final String NEW_MEMBER_BROADCAST_FMT =
            "New member: %s joined. Community size is now %d.";

    // Survey lifecycle
    public static final String SURVEY_TITLE_PREFIX = "[Survey] ";
    public static final String SURVEY_SENT = "Survey sent to community.";
    public static final String SURVEY_CLOSED_TIME = "Survey closed (timeout).";
    public static final String SURVEY_CLOSED_ALL  = "Survey closed (all answered).";
    public static final String SURVEY_CLOSED_FORCE= "Survey closed (forced by owner).";

    // Errors
    public static final String ERR_TOO_FEW_MEMBERS_FMT =
            "Need at least %d members to send a survey.";
    public static final String ERR_ACTIVE_SURVEY =
            "A survey is already active. Please wait until it closes.";
    public static final String ERR_INVALID_COUNTS =
            "Questions must be %d..%d; options must be %d..%d.";

    // Results
    public static final String RESULTS_HEADER = "Survey results:";
    public static final String RESULT_ROW_FMT = "- %s: %d (%.1f%%)";
    public static final String QUESTION_HEADER_FMT = "Q%d) %s";
}
