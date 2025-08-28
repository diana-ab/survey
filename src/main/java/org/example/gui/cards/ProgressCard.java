package org.example.gui.cards;

import javax.swing.*;
import java.awt.*;

/** Shows a one-line status while sending/closing. */
public final class ProgressCard extends JPanel {
    private final JLabel status = new JLabel("Ready");

    public ProgressCard() {
        super(new BorderLayout(8,8));
        add(new JLabel("Progress"), BorderLayout.NORTH);
        add(status, BorderLayout.CENTER);
    }

    public void setStatus(String text) { status.setText(text); }
}
