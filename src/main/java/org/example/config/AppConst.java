package org.example.config;

public final class AppConst {
    // Community & Survey limits
    public static final int MIN_COMMUNITY_SIZE = 3;       // ← שנה ל-1 לבדיקות
    public static final int MAX_ACTIVE_SURVEYS = 1;

    public static final int MIN_QUESTIONS = 1;
    public static final int MAX_QUESTIONS = 3;
    public static final int MIN_OPTIONS   = 2;
    public static final int MAX_OPTIONS   = 4;

    // Timing (ms)
    public static final long POLL_TIMEOUT_MS = 5 * 60_000L; // 5 דקות
    public static final long SEND_DELAY_MS_DEFAULT = 0L;    // מידי
    public static final long RETRY_SLEEP_MS = 800L;
    public static final int  SEND_RETRY_COUNT = 2;
    public static final long UI_REFRESH_MS = 250L;          // רענון קל ל-progress

    // Threads
    public static final long WORKER_SLEEP_MS = 25L;         // Polling קל
}
