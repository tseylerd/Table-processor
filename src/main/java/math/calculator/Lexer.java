package math.calculator;

import cells.CellPointer;
import cells.CellRange;
import util.Util;

import java.text.ParseException;

/**
 * @author Dmitriy Tseyler
 */
public class Lexer {
    private String expression;
    private int pointer;
    private String number;
    private CellPointer cellPointer;
    private CellRange range;
    private AggregateFunction function;

    public Lexer(String expression){
        this.expression = expression.replaceAll(" ", "");
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
                current == 'E' ||
                !(builder.length() == 0) && builder.charAt(builder.length() - 1) == 'E' && (current == '-' || current == '+');
    }

    public Lexeme nextLexem() throws ParseException {
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
            while (notEnd() && Character.isAlphabetic(currentChar())) {
                builder.append(currentChar());
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
            if (lexeme.getType() == LexemeType.AGGREGATE_FUNCTION) {
                incrementPointer();
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
        return new CellPointer(Integer.parseInt(readNumber()), Util.indexByColumnName(column));
    }

    private CellPointer readCellPointer() {
        String column = readLiteral();
        String row = readNumber();
        return new CellPointer(Integer.parseInt(row), Util.indexByColumnName(column));
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
