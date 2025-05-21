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
        map.put(Level.ERROR_INTEGER, AnsiColor.RED);
        map.put(Level.WARN_INTEGER, AnsiColor.MAGENTA);
        map.put(Level.INFO_INTEGER, AnsiColor.GREEN);
        map.put(Level.DEBUG_INTEGER, AnsiColor.BRIGHT_YELLOW);
        map.put(Level.TRACE_INTEGER, AnsiColor.BRIGHT_BLACK);
        LEVELS = Collections.unmodifiableMap(map);
    }

    @Override
    protected String transform(ILoggingEvent event, String in) {
        String param = getFirstOption();  // 读取{}里的参数，比如 RED, BLUE 等

        AnsiColor color = null;
        if (param != null) {
            color = AnsiColor.fromName(param);
        }
        if (color == null) {
            // 参数为空时，使用日志级别对应颜色
            color = LEVELS.get(event.getLevel().toInteger());
            if (color == null) {
                color = AnsiColor.WHITE;
            }
        }

        return toAnsiString(in, color);
    }

    protected String toAnsiString(String in, AnsiColor color) {
        return AnsiOutput.toString(color, in);
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


