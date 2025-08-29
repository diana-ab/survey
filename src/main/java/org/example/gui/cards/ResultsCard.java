package org.example.gui.cards;

import javax.swing.*;
import java.awt.*;

public class ResultsCard extends JPanel {
    private static final int H_GAP = 8;
    private static final int V_GAP = 8;
    private static final int PAD_ALL = 12;
    private static final int RESULTS_ROWS = 20;
    private static final int RESULTS_COLS = 60;

    private static final String LABEL_RESULTS_TEXT = "Results";
    private static final String DEFAULT_TEXT = "";

    private final JTextArea area;

    public ResultsCard() {
        super(new BorderLayout(H_GAP, V_GAP));

        this.area = new JTextArea(RESULTS_ROWS, RESULTS_COLS);
        area.setEditable(false);

        setBorder(BorderFactory.createEmptyBorder(PAD_ALL, PAD_ALL, PAD_ALL, PAD_ALL));
        add(new JLabel(LABEL_RESULTS_TEXT), BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);
    }

    public void showResults(String text) {
        if (text == null) {
            area.setText(DEFAULT_TEXT);
        } else {
            area.setText(text);
        }
        area.setCaretPosition(0);
    }
}
