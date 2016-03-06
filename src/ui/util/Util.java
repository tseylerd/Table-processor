package ui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmitriy Tseyler
 */
public class Util {
    private static final Pattern CELL_PATTERN =  Pattern.compile("[A-Z]+\\d+");

    public static int getColumnFromString(String column) {
        return 1; //// TODO: 06.03.16
    }

    public static String addCellSymbol(String expression) { // TODO: 06.03.16 More effective
        String result = expression;
        Matcher matcher = CELL_PATTERN.matcher(expression);
        while (matcher.find()) {
            String group = matcher.group();
            result = result.replace(group, "@" + group);
        }
        return result;
    }
}
