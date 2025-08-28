package org.example.gui.cards;

import org.example.model.Question;
import org.example.model.Survey;

import javax.swing.*;
import java.awt.*;

/** Shows a read-only preview of the current survey. */
public final class PreviewCard extends JPanel {
    private final JTextArea area = new JTextArea(20, 60);

    public PreviewCard() {
        super(new BorderLayout(8,8));
        area.setEditable(false);
        add(new JLabel("Preview"), BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    public void bindSurvey(Survey survey) {
        if (survey == null) { area.setText(""); return; }
        StringBuilder sb = new StringBuilder();
        sb.append("Title: ").append(survey.getTitle()).append("\n\n");
        for (Question q : survey.getQuestions()) {
            sb.append("Q").append(q.getId()+1).append(": ").append(q.getText()).append("\n");
            int i = 1;
            for (var opt : q.getOptions()) {
                sb.append("  ").append(i++).append(") ").append(opt.getText()).append("\n");
            }
            sb.append("\n");
        }
        area.setText(sb.toString());
        area.setCaretPosition(0);
    }
}
