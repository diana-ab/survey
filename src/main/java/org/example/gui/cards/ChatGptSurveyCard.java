package org.example.gui.cards;

import org.example.ai.AiClient;
import org.example.ai.AiSurveyParser;
import org.example.gui.SurveyBuiltListener;
import org.example.model.Question;
import org.example.model.Survey;
import org.example.util.Validate;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class ChatGptSurveyCard extends JPanel {
    private static final int GAP = 8;
    private static final int ROWS = 16;
    private static final int COLUMNS = 60;

    private SurveyBuiltListener surveyListener;
    private JTextField topicField;
    private JButton generateButton;
    private JTextArea previewArea;
    private AiClient aiClient;
    private AiSurveyParser parser;

    public ChatGptSurveyCard(SurveyBuiltListener listener, String id) {
        super(new BorderLayout(GAP, GAP));
        this.surveyListener = listener;
        this.aiClient = new AiClient(id);
        this.topicField = new JTextField();
        this.generateButton = new JButton("Generate from AI");
        this.previewArea = new JTextArea(ROWS, COLUMNS);
        this.parser = new AiSurveyParser();

        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        previewArea.setEditable(false);

        JPanel top = new JPanel(new BorderLayout(6, 6));
        top.add(new JLabel("Topic:"), BorderLayout.WEST);
        top.add(topicField, BorderLayout.CENTER);
        top.add(generateButton, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(previewArea), BorderLayout.CENTER);

        generateButton.addActionListener(e -> onGenerate());
    }

    private void onGenerate() {
        try {
            String topic = Validate.requireText(topicField.getText(), "Topic");
            String prompt = buildPrompt(topic);
            setBusy(true, "Generating survey from AI...");
            new SwingWorker<Survey, Void>() {
                private String error;

                @Override
                protected Survey doInBackground() {
                    try {
                        String aiExtra = aiClient.messageGpt(prompt);
                        List<Question> qs = parser.parseToQuestions(aiExtra);
                        return new Survey(System.currentTimeMillis(), "AI: " + topic, qs);
                    } catch (Exception ex) {
                        error = ex.getMessage();
                        return null;
                    }
                }

                @Override
                protected void done() {
                    try {
                        Survey s = get();
                        if (s == null) showError("AI Error", error != null ? error : "Unknown error");
                        else {
                            showPreview(s);
                            surveyListener.onSurveyBuilt(s);
                        }
                    } catch (Exception ex) {
                        showError("AI Error", ex.getMessage());
                    } finally {
                        setBusy(false, null);
                    }
                }
            }.execute();
        } catch (Exception ex) {
            showError("Validation error", ex.getMessage());
        }
    }

    private String buildPrompt(String topic) {
        return "Create a short multiple-choice survey about: " + topic + "\n\n"
                + AiSurveyParser.getSystemFormatInstruction();
    }

    private void showPreview(Survey survey) {
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(survey.getTitle()).append("\n\n");
        for (Question q : survey.getQuestions()) {
            sb.append("Q").append(q.getId() + 1).append(": ").append(q.getText()).append("\n");
            int i = 1;
            for (var opt : q.getOptions()) {
                sb.append("  ").append(i++).append(") ").append(opt.getText()).append("\n");
            }
            sb.append("\n");
        }
        previewArea.setText(sb.toString());
        previewArea.setCaretPosition(0);
    }

    private void showError(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void setBusy(boolean busy, String message) {
        generateButton.setEnabled(!busy);
        if (busy && message != null) previewArea.setText(message + "\n");
    }
}
