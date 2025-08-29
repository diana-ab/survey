package org.example.gui.cards;

import javax.swing.*;
import java.awt.*;

public class WelcomeCard extends JPanel {
    public WelcomeCard() {
        super(new BorderLayout());
        JLabel l = new JLabel("Welcome! Build a survey using the toolbar.", SwingConstants.CENTER);
        l.setFont(l.getFont().deriveFont(18f));
        add(l, BorderLayout.CENTER);
    }
}
