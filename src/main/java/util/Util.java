package util;

import math.calculator.AggregateFunction;
import math.calculator.LexerValue;

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

    public static LexerValue abs(LexerValue value) {
        return new LexerValue(Math.abs(value.getDoubleValue()));
    }

    public static LexerValue inverse(LexerValue value) {
        return new LexerValue(-value.getDoubleValue());
    }

    public static LexerValue cos(LexerValue value) {
        return new LexerValue(Math.cos(value.getDoubleValue()));
    }

    public static LexerValue sin(LexerValue value) {
        return new LexerValue(Math.sin(value.getDoubleValue()));
    }

    public static LexerValue plus(LexerValue a, LexerValue b) {
        return new LexerValue(a.getDoubleValue() + b.getDoubleValue());
    }

    public static LexerValue power(LexerValue e, LexerValue pow) {
        return new LexerValue(Math.pow(e.getDoubleValue(), pow.getDoubleValue()));
    }

    public static LexerValue multiply(LexerValue a, LexerValue b) {
        return new LexerValue(a.getDoubleValue() * b.getDoubleValue());
    }

    public static LexerValue div(LexerValue a, LexerValue b) {
        return new LexerValue(a.getDoubleValue() / b.getDoubleValue());
    }
}
