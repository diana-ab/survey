package org.example.gui.cards;

import javax.swing.*;
import java.awt.*;

public class ProgressCard extends JPanel {
    private static final int H_GAP = 8;
    private static final int V_GAP = 8;
    private static final int PAD_ALL = 12;

    private static final String LABEL_PROGRESS_TEXT = "Progress";
    private static final String STATUS_READY_TEXT = "Ready";

    private JLabel status;

    public ProgressCard() {
        super(new BorderLayout(H_GAP, V_GAP));

        this.status = new JLabel(STATUS_READY_TEXT);

        setBorder(BorderFactory.createEmptyBorder(PAD_ALL, PAD_ALL, PAD_ALL, PAD_ALL));
        add(new JLabel(LABEL_PROGRESS_TEXT), BorderLayout.NORTH);
        add(status, BorderLayout.CENTER);
    }

    public void setStatus(String text) {
        if (text == null) {
            text = STATUS_READY_TEXT;
        }
        status.setText(text);
    }
}
