package org.example.gui.cards;

import org.example.gui.SurveyBuiltListener;
import org.example.gui.components.QuestionEditorPanel;
import org.example.model.Question;
import org.example.model.Survey;
import org.example.util.Validate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManualSurveyCard extends JPanel {
    private static final int GAP = 8;
    private static final int PADDING = 12;
    private static final int MIN_QUESTIONS = 1;
    private static final int MAX_QUESTIONS = 3;

    private SurveyBuiltListener listener;
    private JTextField titleField;
    private JPanel editorsPanel;
    private List<QuestionEditorPanel> editors;
    private JButton addBtn;
    private JButton removeBtn;
    private JButton previewBtn;

    public ManualSurveyCard(SurveyBuiltListener listener) {
        super(new BorderLayout(GAP, GAP));
        this.listener = listener;

        this.titleField = new JTextField();
        this.editorsPanel = new JPanel(new GridLayout(0, 1, GAP, GAP));
        this.editors = new ArrayList<>();
        this.addBtn = new JButton("Add question");
        this.removeBtn = new JButton("Remove question");
        this.previewBtn = new JButton("Preview");

        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        JPanel top = new JPanel(new BorderLayout(GAP - 2, GAP - 2));
        top.add(new JLabel("Survey title:"), BorderLayout.WEST);
        top.add(titleField, BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, GAP - 2, 0));
        toolbar.add(addBtn);
        toolbar.add(removeBtn);
        toolbar.add(previewBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(editorsPanel), BorderLayout.CENTER);
        add(toolbar, BorderLayout.SOUTH);

        addQuestion();

        addBtn.addActionListener(e -> addQuestion());
        removeBtn.addActionListener(e -> removeQuestion());
        previewBtn.addActionListener(e -> buildSurvey());
        syncButtons();
    }

    private void addQuestion() {
        if (editors.size() >= MAX_QUESTIONS) return;
        QuestionEditorPanel panel = new QuestionEditorPanel(editors.size());
        editors.add(panel);
        editorsPanel.add(panel);
        refreshEditors();
        syncButtons();
    }

    private void removeQuestion() {
        if (editors.size() <= MIN_QUESTIONS) return;
        editorsPanel.remove(editors.remove(editors.size() - 1));
        refreshEditors();
        syncButtons();
    }

    private void syncButtons() {
        addBtn.setEnabled(editors.size() < MAX_QUESTIONS);
        removeBtn.setEnabled(editors.size() > MIN_QUESTIONS);
    }

    private void refreshEditors() {
        editorsPanel.revalidate();
        editorsPanel.repaint();
    }

    private void buildSurvey() {
        try {
            String title = Validate.requireText(titleField.getText(), "Title");
            List<Question> questions = new ArrayList<>();
            for (int i = 0; i < editors.size(); i++) {
                QuestionEditorPanel ed = editors.get(i);
                String qText = Validate.requireText(ed.getQuestionText(), "Question " + (i + 1));
                List<String> opts = ed.getOptionTexts();
                questions.add(new Question(i, qText, opts));
            }
            Survey survey = new Survey(System.currentTimeMillis(), title, questions);
            listener.onSurveyBuilt(survey);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
