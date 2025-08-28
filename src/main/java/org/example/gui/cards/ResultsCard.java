package org.example.gui.cards;

import javax.swing.*;
import java.awt.*;

/** Shows final results text. */
public final class ResultsCard extends JPanel {
    private final JTextArea area = new JTextArea(20, 60);

    public ResultsCard() {
        super(new BorderLayout(8,8));
        area.setEditable(false);
        add(new JLabel("Results"), BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    public void showResults(String text) {
        area.setText(text == null ? "" : text);
        area.setCaretPosition(0);
    }
}
