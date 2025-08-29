package org.example.config;

import java.awt.*;

public final class UiConst {
    // Gaps & Insets
    public static final int GAP_S = 6;
    public static final int GAP_M = 10;
    public static final int GAP_L = 16;

    public static final Insets PAD_S = new Insets(GAP_S, GAP_S, GAP_S, GAP_S);
    public static final Insets PAD_M = new Insets(GAP_M, GAP_M, GAP_M, GAP_M);
    public static final Insets PAD_L = new Insets(GAP_L, GAP_L, GAP_L, GAP_L);

    // Preferred sizes
    public static final Dimension FIELD_WIDE = new Dimension(320, 28);
    public static final Dimension BTN_STD    = new Dimension(140, 32);

    // Card names (כדי שלא יהיו “קסומים” בקוד)
    public static final String CARD_WELCOME  = "WELCOME";
    public static final String CARD_MANUAL   = "MANUAL";
    public static final String CARD_PREVIEW  = "PREVIEW";
    public static final String CARD_PROGRESS = "PROGRESS";
    public static final String CARD_RESULTS  = "RESULTS";
}
