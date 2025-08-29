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
    private SurveyBuiltListener listener;
    private AiClient ai;
    private AiSurveyParser parser;
    private JTextField topicField;
    private JButton generateButton;
    private JTextArea previewArea;

    public ChatGptSurveyCard(SurveyBuiltListener listener, String id) {
        super(new BorderLayout(8, 8));
        this.listener = listener;
        this.ai = new AiClient(id);
        this.parser = new AiSurveyParser();
        this.topicField = new JTextField();
        this.generateButton = new JButton("Generate from AI");
        this.previewArea = new JTextArea(16, 60);

        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        previewArea.setEditable(false);

        JPanel top = new JPanel(new BorderLayout(6, 6));
        top.add(new JLabel("Topic:"), BorderLayout.WEST);
        top.add(topicField, BorderLayout.CENTER);
        top.add(generateButton, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(previewArea), BorderLayout.CENTER);

        generateButton.addActionListener(e -> generateFromAi());
    }

    private void generateFromAi() {
        try {
            String topic = Validate.requireText(topicField.getText(), "Topic");
            String prompt = "Create a short multiple-choice survey about: " + topic + "\n\n"
                    + AiSurveyParser.getSystemFormatInstruction();

            String aiText = ai.messageGpt(prompt);
            List<Question> questions = parser.parseToQuestions(aiText);

            Survey survey = new Survey(System.currentTimeMillis(), "AI: " + topic, questions);
            showPreview(survey);
            listener.onSurveyBuilt(survey);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPreview(Survey s) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Title: ").append(s.getTitle()).append("\n\n");
        for (Question question : s.getQuestions()) {
            stringBuilder.append("Q").append(question.getId() + 1).append(": ").append(question.getText()).append("\n");
            int i = 1;
            for (var opt : question.getOptions()) {
                stringBuilder.append("  ").append(i++).append(") ").append(opt.getText()).append("\n");
            }
            stringBuilder.append("\n");
        }
        previewArea.setText(stringBuilder.toString());
        previewArea.setCaretPosition(0);
    }
}
