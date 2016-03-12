package math.calculator.Lexer;

import cells.pointer.CellPointer;
import cells.CellRange;
import math.calculator.AggregateFunction;
import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public class Lexer {
    private final String expression;
    private int pointer;
    private String number;
    private CellPointer cellPointer;
    private CellRange range;
    private AggregateFunction function;

    public Lexer(String expression){
        this.expression = expression.substring(1).toUpperCase();
        pointer = 0;
        number = "";
    }

    private boolean notEnd() {
        return pointer < expression.length();
    }

    private char currentChar() {
        return expression.charAt(pointer);
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

    public Lexeme nextLexeme() {
        if (pointer < expression.length()) {
            StringBuilder builder = new StringBuilder();
            while (notEnd() && needReadDigit(builder)) {
                builder.append(currentChar());
                incrementPointer();
            }
            if (builder.length() > 0) {
                number = builder.toString();
                return Lexeme.NUM;
            }
            if (currentChar() == '$') {
                incrementPointer();
            }
            while (notEnd() && Character.isLetter(currentChar())) {
                builder.append(currentChar());
                incrementPointer();
            }
            if (currentChar() == '$') {
                incrementPointer();
            }
            if (notEnd() && Character.isDigit(currentChar())) {
                cellPointer = readCellPointer(builder.toString());
                return Lexeme.CELL;
            }
            Lexeme lexeme = Lexeme.getLexem(builder.toString());
            while (lexeme == null && notEnd()) {
                builder.append(currentChar());
                incrementPointer();
                lexeme = Lexeme.getLexem(builder.toString());
            }
            if (lexeme != null && lexeme.getType() == LexemeType.AGGREGATE_FUNCTION) {
                CellPointer begin = readCellPointer();
                incrementPointer();
                CellPointer end = readCellPointer();
                range = new CellRange(begin, end);
                function = AggregateFunction.getFunction(lexeme);
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
        if (currentChar() == '$') {
            incrementPointer();
        }
        String column = readLiteral();
        if (currentChar() == '$') {
            incrementPointer();
        }
        String row = readNumber();
        return CellPointer.getPointer(Integer.parseInt(row) - 1, Util.indexByColumnName(column));
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
