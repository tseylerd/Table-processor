package util;

import math.calculator.AggregateFunction;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmitriy Tseyler
 */
public class Util {
    private static final Pattern CELL_PATTERN =  Pattern.compile("[A-Z]+\\d+");
    private static final Pattern AGGREGATE_FUNCTION_PATTERN;

    static {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        AggregateFunction[] functions = AggregateFunction.values();
        for (int i = 0; i < functions.length; i++) {
            if (i > 0) {
                builder.append("|");
            }
            builder.append(functions[i].getName());
        }
        builder.append(')');
        AGGREGATE_FUNCTION_PATTERN = Pattern.compile(builder.toString());
    }

    public static int getColumnFromString(String column) {
        return 1; //// TODO: 06.03.16
    }

    public static String addSpecialCharacters(String expression) { // TODO: 06.03.16 More effective
        String result = expression;
        Matcher matcher = CELL_PATTERN.matcher(expression);
        while (matcher.find()) {
            String group = matcher.group();
            result = result.replace(group, "@" + group);
        }
        matcher = AGGREGATE_FUNCTION_PATTERN.matcher(expression);
        while (matcher.find()) {
            String group = matcher.group();
            result = result.replace(group, "&" + group);
        }
        return result;
    }

    public static String stringAbs(String value) { //// TODO: 06.03.16 remove
        return String.valueOf(Math.abs(Double.parseDouble(value)));
    }

    public static String inverse(String value) {
        return String.valueOf(-Double.parseDouble(value));
    }

    public static String cos(String value) {
        return String.valueOf(Math.cos(Double.parseDouble(value)));
    }

    public static String sin(String value) {
        return String.valueOf(Math.sin(Double.parseDouble(value)));
    }

    public static String plus(String a, String b) {
        double result = Double.parseDouble(a) + Double.parseDouble(b);
        return String.valueOf(result);
    }

    public static String power(String e, String pow) {
        double result = Math.pow(Double.parseDouble(e), Double.parseDouble(pow));
        return String.valueOf(result);
    }

    public static String multiply(String a, String b) {
        double result = Double.parseDouble(a) * Double.parseDouble(b);
        return String.valueOf(result);
    }

    public static String div(String a, String b) {
        double result = Double.parseDouble(a) / Double.parseDouble(b);
        return String.valueOf(result);
    }
}
