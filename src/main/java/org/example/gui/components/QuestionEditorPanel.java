package org.example.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple editor for a single multiple-choice question (2..4 options).
 */
public final class QuestionEditorPanel extends JPanel {
    private static final int MIN_OPTIONS = 2;
    private static final int MAX_OPTIONS = 4;

    private final int questionIndex;
    private final JTextField questionField = new JTextField();
    private final JPanel optionsPanel = new JPanel(new GridLayout(0, 1, 6, 6));
    private final java.util.List<JTextField> optionFields = new ArrayList<>();
    private final JButton addOptBtn = new JButton("Add option");
    private final JButton remOptBtn = new JButton("Remove option");

    public QuestionEditorPanel(int index) {
        super(new BorderLayout(8,8));
        this.questionIndex = index;

        JPanel top = new JPanel(new BorderLayout(6,6));
        top.add(new JLabel("Q" + (index+1) + ":"), BorderLayout.WEST);
        top.add(questionField, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        btns.add(addOptBtn);
        btns.add(remOptBtn);

        JPanel center = new JPanel(new BorderLayout(6,6));
        center.add(new JLabel("Options (2..4):"), BorderLayout.NORTH);
        center.add(optionsPanel, BorderLayout.CENTER);
        center.add(btns, BorderLayout.SOUTH);

        setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        // start with 2 options
        addOptionField();
        addOptionField();
        updateButtons();

        addOptBtn.addActionListener(e -> { addOptionField(); updateButtons(); });
        remOptBtn.addActionListener(e -> { removeOptionField(); updateButtons(); });
    }

    private void addOptionField() {
        if (optionFields.size() >= MAX_OPTIONS) return;
        JTextField f = new JTextField();
        optionFields.add(f);
        optionsPanel.add(f);
        revalidate();
        repaint();
    }

    private void removeOptionField() {
        if (optionFields.size() <= MIN_OPTIONS) return;
        JTextField f = optionFields.remove(optionFields.size()-1);
        optionsPanel.remove(f);
        revalidate();
        repaint();
    }

    private void updateButtons() {
        addOptBtn.setEnabled(optionFields.size() < MAX_OPTIONS);
        remOptBtn.setEnabled(optionFields.size() > MIN_OPTIONS);
    }

    public String getQuestionText() { return questionField.getText().trim(); }

    public List<String> getOptionTexts() {
        List<String> out = new ArrayList<>();
        for (JTextField f : optionFields) {
            String t = f.getText().trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }

    public int getQuestionIndex() { return questionIndex; }
}
