package util;

import cells.pointer.CellPointer;
import cells.CellValue;
import math.calculator.Lexer.LexerValue;
import ui.table.exceptions.EmptyValueException;
import ui.table.exceptions.InvalidCellPointerException;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

/**
 * @author Dmitriy Tseyler
 */
public class Util {
    private static final Logger log = Logger.getLogger(Util.class.getName());
    private static final int COLOR_MAX = (255 * 3) / 2;

    private static final int ENGLISH_CHARACTERS_COUNT = 26;
    private static final Pattern CELL_PATTERN =  Pattern.compile("\\$?[A-Z]+\\$?\\d+");

    public static CellValue moveImmutably(CellValue value, int rowOffset, int columnOffset) {
        CellValue toMove = new CellValue(value);
        move(toMove, rowOffset, columnOffset);
        return toMove;
    }

    public static void move(CellValue value, int rowOffset, int columnOffset) {
        String expression = value.getEditorValue();
        if (expression.isEmpty() || expression.charAt(0) != '=')
            return;

        StringBuilder movedValue = new StringBuilder();
        Matcher matcher = CELL_PATTERN.matcher(value.getEditorValue());
        int beginIndex = 0;
        while (matcher.find()) {
            String group = matcher.group();
            PointerMovingExpression movingExpression = readMovingExpression(group);
            PointerMovingExpression moved = movingExpression;
            try {
                moved = movingExpression.moveAndGet(rowOffset, columnOffset);
            } catch (InvalidCellPointerException e) {
                log.warning(String.format("Can't move pointer %s, column offset = %s, row offst = %s.", movingExpression, columnOffset, rowOffset));
            }
            movedValue.append(expression.substring(beginIndex, matcher.start()));
            movedValue.append(moved);
            beginIndex = matcher.end();
        }
        movedValue.append(expression.substring(beginIndex, expression.length()));
        value.setEditorValue(movedValue.toString());
        value.setExpression(null);
    }

    private static PointerMovingExpression readMovingExpression(String stringValue) {
        int index = 0;
        StringBuilder column = new StringBuilder();
        StringBuilder row = new StringBuilder();
        boolean rowFixed = false;
        boolean columnFixed = false;
        if (stringValue.charAt(index) == '$') {
            columnFixed = true;
            index++;
        }
        while (index < stringValue.length() && Character.isLetter(stringValue.charAt(index))) {
            column.append(stringValue.charAt(index));
            index++;
        }
        if (stringValue.charAt(index) == '$') {
            rowFixed = true;
            index++;
        }
        while (index < stringValue.length() && Character.isDigit(stringValue.charAt(index))) {
            row.append(stringValue.charAt(index));
            index++;
        }
        int rowIndex = Integer.parseInt(row.toString());
        int colIndex = indexByColumnName(column.toString());
        return new PointerMovingExpression(columnFixed, rowFixed, CellPointer.getPointer(rowIndex, colIndex));
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

    public static Color mix(Color first, Color second) {
        float ratio  = 0.5f;
        float rgb1[] = new float[3];
        float rgb2[] = new float[3];
        first.getColorComponents(rgb1);
        second.getColorComponents(rgb2);
        float red = rgb1[0] * ratio + rgb2[0] * ratio;
        float green = rgb1[1] * ratio + rgb2[1] * ratio;
        float blue = rgb1[2] * ratio + rgb2[2] * ratio;
        return new Color(red, green, blue);
    }

    public static Color inverse(Color toInverse) {
        int red = toInverse.getRed();
        int green = toInverse.getGreen();
        int blue = toInverse.getBlue();
        return red + green + blue < COLOR_MAX ? Color.WHITE : Color.BLACK;
    }
}
