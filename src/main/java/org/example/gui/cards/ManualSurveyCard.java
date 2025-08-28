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

/** Manual survey builder card. */
public final class ManualSurveyCard extends JPanel {

    private static final int MIN_QUESTIONS = 1;
    private static final int MAX_QUESTIONS = 3;

    private final JTextField titleField = new JTextField();
    private final JPanel editorHolder = new JPanel(new GridLayout(0,1,8,8));
    private final java.util.List<QuestionEditorPanel> editors = new ArrayList<>();
    private final JButton addQBtn = new JButton("Add question");
    private final JButton remQBtn = new JButton("Remove question");
    private final JButton previewBtn = new JButton("Preview");

    private final SurveyBuiltListener listener;

    public ManualSurveyCard(SurveyBuiltListener listener) {
        super(new BorderLayout(8,8));
        this.listener = listener;
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JPanel top = new JPanel(new BorderLayout(6,6));
        top.add(new JLabel("Survey title:"), BorderLayout.WEST);
        top.add(titleField, BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        toolbar.add(addQBtn);
        toolbar.add(remQBtn);
        toolbar.add(previewBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(editorHolder), BorderLayout.CENTER);
        add(toolbar, BorderLayout.SOUTH);

        // one question to start
        addQuestionEditor();

        addQBtn.addActionListener(e -> addQuestionEditor());
        remQBtn.addActionListener(e -> removeQuestionEditor());
        previewBtn.addActionListener(e -> buildSurveyAndNotify());
        updateButtonsState();
    }

    private void addQuestionEditor() {
        if (editors.size() >= MAX_QUESTIONS) return;
        QuestionEditorPanel panel = new QuestionEditorPanel(editors.size());
        editors.add(panel);
        editorHolder.add(panel);
        revalidate();
        repaint();
        updateButtonsState();
    }

    private void removeQuestionEditor() {
        if (editors.size() <= MIN_QUESTIONS) return;
        QuestionEditorPanel p = editors.remove(editors.size()-1);
        editorHolder.remove(p);
        revalidate();
        repaint();
        updateButtonsState();
    }

    private void updateButtonsState() {
        addQBtn.setEnabled(editors.size() < MAX_QUESTIONS);
        remQBtn.setEnabled(editors.size() > MIN_QUESTIONS);
    }

    private void buildSurveyAndNotify() {
        try {
            String title = Validate.requireText(titleField.getText(), "Title");
            List<Question> qs = new ArrayList<>();
            for (int i = 0; i < editors.size(); i++) {
                QuestionEditorPanel ed = editors.get(i);
                String qText = Validate.requireText(ed.getQuestionText(), "Question " + (i+1));
                List<String> opts = ed.getOptionTexts();
                qs.add(new Question(i, qText, opts)); // validates 2..4 in constructor
            }
            Survey s = new Survey(System.currentTimeMillis(), title, qs); // validates 1..3 in constructor
            listener.onSurveyBuilt(s);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
