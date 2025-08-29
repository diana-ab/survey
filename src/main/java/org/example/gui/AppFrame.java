package org.example.gui;

import org.example.core.SurveySender;
import org.example.gui.cards.*;
import org.example.model.Survey;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;

/**
 * Main application frame using CardLayout.
 */
public final class AppFrame extends JFrame implements SurveyBuiltListener {

    private final CardLayout layout = new CardLayout();
    private final JPanel root = new JPanel(layout);

    private final WelcomeCard welcome = new WelcomeCard();
    private final ManualSurveyCard manual;
    private final ChatGptSurveyCard aiCard;
    private final PreviewCard preview = new PreviewCard();
    private final ProgressCard progress = new ProgressCard();
    private final ResultsCard results = new ResultsCard();

    private final SurveySender sender;
    private Survey lastSurvey;

    public AppFrame(SurveySender sender) {
        super("Survey Builder");
        this.sender = sender;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        manual = new ManualSurveyCard(this);
        aiCard = new ChatGptSurveyCard(this, "209202985");

        JToolBar tb = new JToolBar();
        JButton goWelcome = new JButton("Home");
        JButton goManual = new JButton("Create (Manual)");
        JButton goAI = new JButton("Create (AI)");
        JButton goPreview = new JButton("Preview");
        JButton sendBtn = new JButton("Send");
        JButton closeBtn = new JButton("Close");
        tb.add(goWelcome);
        tb.add(goManual);
        tb.add(goAI);
        tb.add(goPreview);
        tb.addSeparator();
        tb.add(sendBtn);
        tb.add(closeBtn);

        goWelcome.addActionListener(e -> showCard("WELCOME"));
        goManual.addActionListener(e -> showCard("MANUAL"));
        goAI.addActionListener(e -> showCard("AI"));
        goPreview.addActionListener(e -> {
            if (lastSurvey != null) preview.bindSurvey(lastSurvey);
            showCard("PREVIEW");
        });
        sendBtn.addActionListener(e -> doSend());
        closeBtn.addActionListener(e -> doClose());

        setLayout(new BorderLayout());
        add(tb, BorderLayout.NORTH);

        root.add(welcome, "WELCOME");
        root.add(manual, "MANUAL");
        root.add(aiCard, "AI");
        root.add(preview, "PREVIEW");
        root.add(progress, "PROGRESS");
        root.add(results, "RESULTS");
        add(root, BorderLayout.CENTER);

        showCard("WELCOME");
    }

    private void showCard(String name) {
        layout.show(root, name);
    }

    @Override
    public void onSurveyBuilt(Survey survey) {
        this.lastSurvey = survey;
        preview.bindSurvey(survey);
        showCard("PREVIEW");
    }

    private void doSend() {
        if (lastSurvey == null) {
            JOptionPane.showMessageDialog(this, "Build a survey first.", "No survey", JOptionPane.WARNING_MESSAGE);
            return;
        }
        progress.setStatus("Sending to Telegram...");
        showCard("PROGRESS");

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                sender.sendSurvey(lastSurvey);
                SwingUtilities.invokeLater(() -> progress.setStatus("Sent. Waiting for votes..."));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, ex.getMessage(), "Send failed", JOptionPane.ERROR_MESSAGE));
            }
        });
    }

    private void doClose() {
        if (lastSurvey == null) return;
        progress.setStatus("Closing...");
        showCard("PROGRESS");

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                sender.closeSurvey(lastSurvey.getId());
                SwingUtilities.invokeLater(() -> {
                    progress.setStatus("Closed.");
                    results.showResults("Results placeholder. (Implement aggregation if desired.)");
                    showCard("RESULTS");
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, ex.getMessage(), "Close failed", JOptionPane.ERROR_MESSAGE));
            }
        });
    }
}
