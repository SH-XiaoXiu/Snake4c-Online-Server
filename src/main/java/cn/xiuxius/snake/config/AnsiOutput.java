package cn.xiuxius.snake.config;

import cn.hutool.core.lang.ansi.AnsiColor;

public abstract class AnsiOutput {

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
