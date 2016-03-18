package math.calculator.lexer;

import cells.pointer.CellPointer;
import cells.CellRange;
import math.calculator.AggregateFunction;
import math.calculator.LexemeType;
import math.calculator.ParserException;
import util.Util;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Lexer implementation
 * @author Dmitriy Tseyler
 */
public class Lexer {
    public static final char FIX_CHARACTER = '$';

    private final String upperCaseExpression;
    private final StringBuilder builder;

    private int bracesCount;
    private int pointer;
    private String number;
    private CellPointer cellPointer;
    private CellRange range;
    private AggregateFunction function;

    public Lexer(String expression){
        this.upperCaseExpression = expression.substring(1).toUpperCase();

        builder = new StringBuilder();

        pointer = 0;
        bracesCount = 0;
        number = "";
    }

    private void append() {
        builder.append(currentChar());
    }

    private boolean notEnd() {
        return pointer < upperCaseExpression.length();
    }

    private char currentChar() {
        return upperCaseExpression.charAt(pointer);
    }

    private void incrementPointer() {
        if (currentChar() == '(') {
            bracesCount++;
        } else if (currentChar() == ')') {
            bracesCount--;
        }
        pointer++;
    }

    private boolean needReadDigit(StringBuilder builder) {
        char current = currentChar();
        boolean isDigit = Character.isDigit(current);
        boolean isFullStop = current == '.';
        boolean builderNotEmpty = builder.length() > 0;
        boolean isExp = current == 'E';
        boolean isExpAllowed = isExp && builderNotEmpty && (Character.isDigit(builder.charAt(builder.length() - 1)) || builder.charAt(builder.length() - 1) == '.');
        boolean isOperatorAllowed = builderNotEmpty && builder.charAt(builder.length() - 1) == 'E' && (current == '-' || current == '+');
        return isDigit ||
                isFullStop ||
                isExpAllowed ||
                isOperatorAllowed;
    }

    private void decrement() {
        pointer--;
        builder.setLength(builder.length() - 1);
    }

    public Lexeme nextLexeme() throws ParserException {
        builder.setLength(0);
        if (pointer < upperCaseExpression.length()) {
            while (notEnd() && needReadDigit(builder)) { // try read number
                append();
                incrementPointer();
            }
            if (builder.length() > 0) {
                number = builder.toString();
                return Lexeme.NUMBER;
            }
            checkFixCharacter(Character::isLetter); // try read CellPointer column
            while (notEnd() && Character.isLetter(currentChar())) {
                append();
                incrementPointer();
            }
            checkFixCharacter(Character::isDigit); // try read CellPointer row
            if (notEnd() && Character.isDigit(currentChar())) {
                cellPointer = readCellPointer(builder.toString());
                assertNextOperation();
                return Lexeme.CELL;
            }
            Lexeme lexeme = Lexeme.getLexem(builder.toString());

            if (lexeme == null && notEnd()) { // try get operation lexem
                append();
                incrementPointer();
                lexeme = Lexeme.getLexem(builder.toString());
                if (lexeme == null) {
                    decrement();
                } else if (lexeme == Lexeme.CLOSE) {
                    assertAfterClose();
                } else if (lexeme.getType() == LexemeType.FUNCTION && lexeme != Lexeme.OPEN) {
                    assertFunction();
                }
            }
            if (lexeme != null && lexeme.getType() == LexemeType.AGGREGATE_FUNCTION) { // try get cell range
                CellPointer begin = readCellPointer();
                assertRangeDelimiter();
                incrementPointer();
                CellPointer end = readCellPointer();
                range = new CellRange(begin, end);
                function = AggregateFunction.getFunction(lexeme);
                assertEndFunction();
                incrementPointer();
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
        boolean isOperation = currentChar() == '-' || currentChar() == '+';
        boolean isDigit = Character.isDigit(currentChar());
        boolean isLetter = Character.isLetter(currentChar());
        boolean isOpen = currentChar() == '(';
        if (!isDigit && !isLetter && !isOpen && !isOperation) {
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
        CellPointer pointer;
        try {
            pointer = CellPointer.getPointer(Integer.parseInt(readNumber()) - 1, Util.indexByColumnName(column));
        } catch (NumberFormatException e) {
            throw new ParserException();
        }
        return pointer;
    }

    private CellPointer readCellPointer() {
        checkFixCharacter(Character::isLetter);
        String column = readLiteral();
        checkFixCharacter(Character::isDigit);
        String row = readNumber();
        CellPointer pointer;
        try {
             pointer = CellPointer.getPointer(Integer.parseInt(row) - 1, Util.indexByColumnName(column));
        } catch (NumberFormatException e) {
            throw new ParserException();
        }
        return pointer;
    }

    private void checkFixCharacter(Predicate<Character> check) {
        if (notEnd() && currentChar() == FIX_CHARACTER) {
            incrementPointer();
            if (notEnd() && !check.test(currentChar())) {
                throw new ParserException();
            }
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

    public int getBracesCount() {
        return bracesCount;
    }
}
