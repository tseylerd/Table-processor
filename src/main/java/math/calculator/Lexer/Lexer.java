package math.calculator.Lexer;

import cells.pointer.CellPointer;
import cells.CellRange;
import math.calculator.AggregateFunction;
import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public class Lexer {
    public static final char FIX_CHARACTER = '$';

    private final String upperCaseExpression;
    private final String originalExpression;
    private final StringBuilder builder;
    private final StringBuilder originalBuilder;

    private int pointer;
    private String number;
    private CellPointer cellPointer;
    private CellRange range;
    private AggregateFunction function;

    public Lexer(String expression){
        this.originalExpression = expression.substring(1);
        this.upperCaseExpression = originalExpression.toUpperCase();

        builder = new StringBuilder();
        originalBuilder = new StringBuilder();

        pointer = 0;
        number = "";
    }

    private void append() {
        builder.append(currentChar());
        originalBuilder.append(originalChar());
    }

    private boolean notEnd() {
        return pointer < upperCaseExpression.length();
    }

    private char currentChar() {
        return upperCaseExpression.charAt(pointer);
    }

    private char originalChar() {
        return originalExpression.charAt(pointer);
    }

    private void incrementPointer() {
        pointer++;
    }

    private boolean needReadDigit(StringBuilder builder) {
        char current = currentChar();
        return Character.isDigit(current) ||
                current == '.' ||
                (current == 'E' && builder.length() > 0 && (Character.isDigit(builder.charAt(builder.length() - 1)) || builder.charAt(builder.length() - 1) == '.')) ||
                !(builder.length() == 0) && builder.charAt(builder.length() - 1) == 'E' && (current == '-' || current == '+');
    }

    private boolean needReadLetter() {
        Lexeme lexeme = Lexeme.getLexem(String.valueOf(currentChar()));
        return lexeme != null && lexeme.getType() == LexemeType.OPERATION && lexeme != Lexeme.OPEN;
    }

    private void decrement() {
        pointer--;
        builder.setLength(builder.length() - 1);
        originalBuilder.setLength(originalBuilder.length() - 1);
    }

    public Lexeme nextLexeme() {
        builder.setLength(0);
        originalBuilder.setLength(0);
        if (pointer < upperCaseExpression.length()) {
            while (notEnd() && needReadDigit(builder)) {
                append();
                incrementPointer();
            }
            if (builder.length() > 0) {
                number = builder.toString();
                return Lexeme.NUM;
            }
            checkFixCharacter();
            while (notEnd() && Character.isLetter(currentChar())) {
                append();
                incrementPointer();
            }
            checkFixCharacter();
            if (notEnd() && Character.isDigit(currentChar())) {
                cellPointer = readCellPointer(builder.toString());
                return Lexeme.CELL;
            }
            Lexeme lexeme = Lexeme.getLexem(builder.toString());

            if (lexeme == null && notEnd()) {
                append();
                incrementPointer();
                lexeme = Lexeme.getLexem(builder.toString());
                if (lexeme == null) {
                    decrement();
                }
            }

            if (lexeme != null && lexeme.getType() == LexemeType.AGGREGATE_FUNCTION) {
                CellPointer begin = readCellPointer();
                incrementPointer();
                CellPointer end = readCellPointer();
                range = new CellRange(begin, end);
                function = AggregateFunction.getFunction(lexeme);
            }
            if (lexeme == null) {
                number = originalBuilder.toString();
                lexeme = Lexeme.STRING;
            }
            return lexeme;
        }
        return null;
    }

    public CellRange getRange() {
        return range;
    }

    public AggregateFunction getFunction() {
        return function;
    }

    private CellPointer readCellPointer(String column) {
        return CellPointer.getPointer(Integer.parseInt(readNumber()) - 1, Util.indexByColumnName(column));
    }

    private CellPointer readCellPointer() {
        checkFixCharacter();
        String column = readLiteral();
        checkFixCharacter();
        String row = readNumber();
        return CellPointer.getPointer(Integer.parseInt(row) - 1, Util.indexByColumnName(column));
    }

    private void checkFixCharacter() {
        if (notEnd() && currentChar() == FIX_CHARACTER) {
            originalBuilder.append(FIX_CHARACTER);
            incrementPointer();
        }
    }

    private String readLiteral() {
        StringBuilder builder = new StringBuilder();
        while (notEnd() && Character.isAlphabetic(currentChar())) {
            builder.append(currentChar());
            incrementPointer();
        }
        return builder.toString();
    }

    private String readNumber() {
        StringBuilder builder = new StringBuilder();
        while (notEnd() && Character.isDigit(currentChar())) {
            builder.append(currentChar());
            incrementPointer();
        }
        return builder.toString();
    }

    public CellPointer getCellPointer() {
        return cellPointer;
    }

    public String getNumber(){
        return number;
    }
}
