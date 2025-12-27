package engine.util;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log {
    public enum Level { DEBUG, INFO, WARN, ERROR }

    private static Level minLevel = Level.INFO;
    private static boolean showThread = true;
    private static boolean showTime = true;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    private Log() {}

    public static void setMinLevel(Level level) {
        minLevel = level;
    }

    public static void configureFromSystemProperties() {
        String lvl = System.getProperty("log.level");
        if (lvl != null) {
            try {
                minLevel = Level.valueOf(lvl.trim().toUpperCase());
            }
            catch (IllegalArgumentException e) {
                warn("Log", "Unknown log.level='%s' (use DEBUG/INFO/WARN/ERROR)", lvl);
            }
        }

        String thread = System.getProperty("log.thread");
        if (thread != null) {
            showThread = Boolean.parseBoolean(thread);
        }

        String time = System.getProperty("log.time");
        if (time != null) {
            showTime = Boolean.parseBoolean(time);
        }
    }

    public static void debug(String tag, String msg, Object... args) { log(Level.DEBUG, tag, msg, args); }
    public static void  info(String tag, String msg, Object... args) { log(Level.INFO,  tag, msg, args); }
    public static void  warn(String tag, String msg, Object... args) { log(Level.WARN,  tag, msg, args); }
    public static void error(String tag, String msg, Object... args) { log(Level.ERROR, tag, msg, args); }

    public static void error(String tag, Throwable t, String msg, Object... args) {
        log(Level.ERROR, tag, msg, args);
        t.printStackTrace(System.err);
    }

    private static void log(Level level, String tag, String msg, Object... args) {
        if (level.ordinal() < minLevel.ordinal()) { return; }

        String text = (args == null || args.length == 0) ? msg : String.format(msg, args);

        StringBuilder sb = new StringBuilder();

        if (showTime) {
            sb.append(LocalTime.now().format(TIME_FMT)).append(" ");
        }
        sb.append("[").append(level).append("] ");
        sb.append("[").append(tag).append("] ");
        if (showThread) {
            sb.append("(").append(Thread.currentThread().getName()).append(") ");
        }
        sb.append(text);

        if (level == Level.ERROR) {
            System.err.println(sb);
        }
        else {
            System.out.println(sb);
        }
    }
}
