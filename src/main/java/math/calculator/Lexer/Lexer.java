package math.calculator.lexer;

import cells.pointer.CellPointer;
import cells.CellRange;
import math.calculator.AggregateFunction;
import math.calculator.LexemeType;
import math.calculator.ParserException;
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

    private void decrement() {
        pointer--;
        builder.setLength(builder.length() - 1);
        originalBuilder.setLength(originalBuilder.length() - 1);
    }

    public Lexeme nextLexeme() throws ParserException {
        builder.setLength(0);
        originalBuilder.setLength(0);
        if (pointer < upperCaseExpression.length()) {
            while (notEnd() && needReadDigit(builder)) {
                append();
                incrementPointer();
            }
            if (builder.length() > 0) {
                number = builder.toString();
                return Lexeme.NUMBER;
            }
            checkFixCharacter();
            while (notEnd() && Character.isLetter(currentChar())) {
                append();
                incrementPointer();
            }
            checkFixCharacter();
            if (notEnd() && Character.isDigit(currentChar())) {
                cellPointer = readCellPointer(builder.toString());
                assertNextOperation();
                return Lexeme.CELL;
            }
            Lexeme lexeme = Lexeme.getLexem(builder.toString());

            if (lexeme == null && notEnd()) {
                append();
                incrementPointer();
                lexeme = Lexeme.getLexem(builder.toString());
                if (lexeme == null) {
                    decrement();
                } else if (lexeme == Lexeme.CLOSE) {
                    assertAfterClose();
                } else {
                    assertFunction();
                }
            }
            if (lexeme != null && lexeme.getType() == LexemeType.AGGREGATE_FUNCTION) {
                CellPointer begin = readCellPointer();
                assertRangeDelimiter();
                incrementPointer();
                CellPointer end = readCellPointer();
                range = new CellRange(begin, end);
                function = AggregateFunction.getFunction(lexeme);
                assertEndFunction();
            }
            if (lexeme == null) {
                throw new ParserException();
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

    private void assertNextOperation() throws ParserException {
        if (pointer == upperCaseExpression.length())
            return;

        String current = String.valueOf(currentChar());
        for (Lexeme lexeme : Lexeme.values()) {
            if (lexeme.getType() == LexemeType.OPERATION) {
                if (lexeme.getValue().equals(current)) {
                    return;
                }
            }
        }
        if (Lexeme.CLOSE.getValue().equals(current) || Lexeme.POW.getValue().equals(current)) {
            return;
        }

        throw new ParserException();
    }

    private void assertFunction() throws ParserException {
        assertNotEnds();
        boolean isDigit = Character.isDigit(currentChar());
        boolean isLetter = Character.isLetter(currentChar());
        boolean isOpen = currentChar() == '(';
        if (!isDigit && !isLetter && !isOpen) {
            throw new ParserException();
        }
    }

    private void assertAfterClose() throws ParserException {
        if (notEnd()) {
            assertNextOperation();
        }
    }

    private void assertRangeDelimiter() throws ParserException {
        assertNotEnds();
        if (currentChar() != ':') {
            throw new ParserException();
        }
    }

    private void assertEndFunction() throws ParserException {
        assertNotEnds();
        if (currentChar() != ')') {
            throw new ParserException();
        }
    }

    private void assertNotEnds() throws ParserException {
        if (!notEnd()) {
            throw new ParserException();
        }
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
