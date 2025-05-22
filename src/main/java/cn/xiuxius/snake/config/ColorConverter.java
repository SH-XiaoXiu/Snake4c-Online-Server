package cn.xiuxius.snake.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ColorConverter extends CompositeConverter<ILoggingEvent> {
    private static final Map<Integer, AnsiColor> LEVELS;
    static {
        Map<Integer, AnsiColor> map = new HashMap<>();
        map.put(Level.ERROR_INTEGER, AnsiColor.BRIGHT_RED);
        map.put(Level.WARN_INTEGER, AnsiColor.BRIGHT_YELLOW);
        map.put(Level.INFO_INTEGER, AnsiColor.BRIGHT_GREEN);
        map.put(Level.DEBUG_INTEGER, AnsiColor.BRIGHT_BLACK);
        map.put(Level.TRACE_INTEGER, AnsiColor.BRIGHT_BLACK);
        LEVELS = Collections.unmodifiableMap(map);
    }
    @Override
    protected String transform(ILoggingEvent event, String in) {
        String param = getFirstOption();
        AnsiColor color = null;
        if (param != null) {
            color = AnsiColor.fromName(param);
        }
        if (color == null) {
            color = LEVELS.get(event.getLevel().toInteger());
            if (color == null) {
                color = AnsiColor.WHITE;
            }
        }
        return toAnsiString(in, color);
    }
    protected String toAnsiString(String in, AnsiColor element) {
        return AnsiOutput.toString(element, in);
    }

    public static abstract class AnsiOutput {
        private static final String ENCODE_JOIN = ";";
        private static final String ENCODE_START = "\033[";
        private static final String ENCODE_END = "m";
        private static final String RESET = "0;" + AnsiColor.DEFAULT;
        public static String toString(Object... elements) {
            StringBuilder sb = new StringBuilder();
            buildEnabled(sb, elements);
            return sb.toString();
        }
        private static void buildEnabled(StringBuilder sb, Object[] elements) {
            boolean writingAnsi = false;
            boolean containsEncoding = false;
            for (Object element : elements) {
                if (element instanceof AnsiColor) {
                    containsEncoding = true;
                    if (!writingAnsi) {
                        sb.append(ENCODE_START);
                        writingAnsi = true;
                    } else {
                        sb.append(ENCODE_JOIN);
                    }
                } else {
                    if (writingAnsi) {
                        sb.append(ENCODE_END);
                        writingAnsi = false;
                    }
                }
                sb.append(element);
            }
            if (containsEncoding) {
                sb.append(writingAnsi ? ENCODE_JOIN : ENCODE_START);
                sb.append(RESET);
                sb.append(ENCODE_END);
            }
        }
    }

    public enum AnsiColor {

        DEFAULT("39"),
        BLACK("30"),
        RED("31"),
        GREEN("32"),
        YELLOW("33"),
        BLUE("34"),
        MAGENTA("35"),
        CYAN("36"),
        WHITE("37"),
        BRIGHT_BLACK("90"),
        BRIGHT_RED("91"),
        BRIGHT_GREEN("92"),
        BRIGHT_YELLOW("93"),
        BRIGHT_BLUE("94"),
        BRIGHT_MAGENTA("95"),
        BRIGHT_CYAN("96"),
        BRIGHT_WHITE("97");

        private final String code;

        AnsiColor(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return this.code;
        }

        public static AnsiColor fromName(String name) {
            if (name == null) return null;
            for (AnsiColor c : values()) {
                if (c.name().equalsIgnoreCase(name)) {
                    return c;
                }
            }
            return null;
        }
    }

}




