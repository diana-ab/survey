package org.example.gui;

import org.example.core.SurveySender;
import org.example.gui.cards.*;
import org.example.model.Survey;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AppFrame extends JFrame implements SurveyBuiltListener {
    private static final String APP_TITLE = "Survey Builder";
    private static final int FRAME_WIDTH = 900;
    private static final int FRAME_HEIGHT = 700;
    private static final int H_GAP = 0;
    private static final int V_GAP = 0;
    private static final String CARD_WELCOME = "WELCOME";
    private static final String CARD_MANUAL = "MANUAL";
    private static final String CARD_AI = "AI";
    private static final String CARD_PREVIEW = "PREVIEW";
    private static final String CARD_PROGRESS = "PROGRESS";
    private static final String CARD_RESULTS = "RESULTS";
    private static final String BTN_HOME_TEXT = "Home";
    private static final String BTN_MANUAL_TEXT = "Create (Manual)";
    private static final String BTN_AI_TEXT = "Create (AI)";
    private static final String BTN_PREVIEW_TEXT = "Preview";
    private static final String BTN_SEND_TEXT = "Send";
    private static final String BTN_CLOSE_TEXT = "Close";
    private static final String MSG_NO_SURVEY = "Build a survey first.";
    private static final String DIALOG_NO_SURVEY_TITLE = "No survey";
    private static final String STATUS_SENDING = "Sending to Telegram...";
    private static final String STATUS_SENT_WAITING = "Sent. Waiting for votes...";
    private static final String STATUS_CLOSING = "Closing...";
    private static final String STATUS_CLOSED = "Closed.";
    private static final String DIALOG_SEND_FAILED_TITLE = "Send failed";
    private static final String DIALOG_CLOSE_FAILED_TITLE = "Close failed";
    private static final String RESULTS_PLACEHOLDER = "Results placeholder. (Implement aggregation if desired.)";

    private static final String AI_CLIENT_ID = "209202985";
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    private CardLayout layout = new CardLayout();
    private JPanel root = new JPanel(layout);
    private WelcomeCard welcome = new WelcomeCard();
    private ManualSurveyCard manual;
    private ChatGptSurveyCard aiCard;
    private PreviewCard preview = new PreviewCard();
    private ProgressCard progress = new ProgressCard();
    private ResultsCard results = new ResultsCard();
    private SurveySender sender;
    private Survey lastSurvey;

    public AppFrame(SurveySender sender) {
        super(APP_TITLE);
        this.sender = sender;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);

        this.manual = new ManualSurveyCard(this);
        this.aiCard = new ChatGptSurveyCard(this, AI_CLIENT_ID);


        JToolBar tb = new JToolBar();
        JButton goWelcome = new JButton(BTN_HOME_TEXT);
        JButton goManual = new JButton(BTN_MANUAL_TEXT);
        JButton goAI = new JButton(BTN_AI_TEXT);
        JButton goPreview = new JButton(BTN_PREVIEW_TEXT);
        JButton sendBtn = new JButton(BTN_SEND_TEXT);
        JButton closeBtn = new JButton(BTN_CLOSE_TEXT);

        tb.add(goWelcome);
        tb.add(goManual);
        tb.add(goAI);
        tb.add(goPreview);
        tb.addSeparator();
        tb.add(sendBtn);
        tb.add(closeBtn);

        goWelcome.addActionListener(e -> {
            showCard(CARD_WELCOME);
        });
        goManual.addActionListener(e -> {
            showCard(CARD_MANUAL);
        });
        goAI.addActionListener(e -> {
            showCard(CARD_AI);
        });
        goPreview.addActionListener(e -> {
            if (lastSurvey != null) {
                preview.bindSurvey(lastSurvey);
            }
            showCard(CARD_PREVIEW);
        });
        sendBtn.addActionListener(e -> {
            doSend();
        });
        closeBtn.addActionListener(e -> {
            doClose();
        });

        setLayout(new BorderLayout(H_GAP, V_GAP));
        add(tb, BorderLayout.NORTH);

        root.add(welcome, CARD_WELCOME);
        root.add(manual, CARD_MANUAL);
        root.add(aiCard, CARD_AI);
        root.add(preview, CARD_PREVIEW);
        root.add(progress, CARD_PROGRESS);
        root.add(results, CARD_RESULTS);
        add(root, BorderLayout.CENTER);

        showCard(CARD_WELCOME);
    }

    private void showCard(String name) {
        layout.show(root, name);
    }

    @Override
    public void onSurveyBuilt(Survey survey) {
        this.lastSurvey = survey;
        preview.bindSurvey(survey);
        showCard(CARD_PREVIEW);
    }

    private void doSend() {
        if (lastSurvey == null) {
            JOptionPane.showMessageDialog(this, MSG_NO_SURVEY, DIALOG_NO_SURVEY_TITLE, JOptionPane.WARNING_MESSAGE);
            return;
        }

        progress.setStatus(STATUS_SENDING);
        showCard(CARD_PROGRESS);

        EXECUTOR.submit(() -> {
            try {
                sender.sendSurvey(lastSurvey);
                SwingUtilities.invokeLater(() -> {
                    progress.setStatus(STATUS_SENT_WAITING);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), DIALOG_SEND_FAILED_TITLE, JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }

    private void doClose() {
        if (lastSurvey == null) {
            return;
        }

        progress.setStatus(STATUS_CLOSING);
        showCard(CARD_PROGRESS);

        EXECUTOR.submit(() -> {
            try {
                sender.closeSurvey(lastSurvey.getId());
                SwingUtilities.invokeLater(() -> {
                    progress.setStatus(STATUS_CLOSED);
                    results.showResults(RESULTS_PLACEHOLDER);
                    showCard(CARD_RESULTS);
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), DIALOG_CLOSE_FAILED_TITLE, JOptionPane.ERROR_MESSAGE);
                });
            }
        });
    }
}
