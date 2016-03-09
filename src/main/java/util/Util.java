package util;

import cells.CellPointer;
import cells.CellValue;
import math.calculator.Lexer.LexerValue;
import ui.table.exceptions.EmptyValueException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmitriy Tseyler
 */
public class Util {
    private static final int ENGLISH_CHARACTERS_COUNT = 26;
    private static final Pattern CELL_PATTERN =  Pattern.compile("[A-Z]+\\d+");

    public static void move(CellValue value, int rowOffset, int columnOffset) { // TODO: 07.03.16 build and not replace;
        String expression = value.getEditorValue();
        StringBuilder movedValue = new StringBuilder();
        Matcher matcher = CELL_PATTERN.matcher(value.getEditorValue());
        int beginIndex = 0;
        while (matcher.find()) {
            String group = matcher.group();
            CellPointer pointer = readCellPointer(group);
            CellPointer newPointer = new CellPointer(pointer, rowOffset, columnOffset);
            movedValue.append(expression.substring(beginIndex, matcher.start()));
            movedValue.append(newPointer);
            beginIndex = matcher.end();
        }
        movedValue.append(expression.substring(beginIndex, expression.length()));
        value.setExpression(movedValue.toString());
    }

    private static CellPointer readCellPointer(String full) {
        if (full.isEmpty()) {
            return null;
        }

        int index = 0;
        StringBuilder column = new StringBuilder();
        StringBuilder row = new StringBuilder();
        while (index < full.length() && Character.isLetter(full.charAt(index))) {
            column.append(full.charAt(index));
            index++;
        }
        while (index < full.length() && Character.isDigit(full.charAt(index))) {
            row.append(full.charAt(index));
            index++;
        }
        int rowIndex = Integer.parseInt(row.toString());
        int colIndex = indexByColumnName(column.toString());
        return new CellPointer(rowIndex, colIndex);
    }

    public static String columnNameByIndex(int column) {
        StringBuilder builder = new StringBuilder();
        column++;

        while (column > 0) {
            int mod = (column - 1) % ENGLISH_CHARACTERS_COUNT;
            builder.insert(0, (char)(65 + mod));
            column = (column - mod) / ENGLISH_CHARACTERS_COUNT;
        }

        return builder.toString();
    }

    public static int indexByColumnName(String columnName) {
        int result = 0;
        for (int i = 0; i < columnName.length(); i++) {
            result = result * ENGLISH_CHARACTERS_COUNT + columnName.charAt(i) - 'A' + 1;
        }
        return result - 1;
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

    public static String check(String value) {
        if (value.isEmpty()) {
            throw new EmptyValueException();
        }
        return value;
    }
}
