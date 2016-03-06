package main.java.math.calculator;

import main.java.cells.CellValue;
import main.java.ui.table.SpreadSheetModel;
import main.java.cells.CellPointer;
import main.java.cells.CellRange;
import main.java.util.Util;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dmitriy Tseyler
 */

public class ExpressionCalculator {
    private final SpreadSheetModel model;
    private final Set<CellPointer> pointers;
    private final Set<CellRange> ranges;

    private Lexeme lexeme;
    private Lexer lexer;

    public ExpressionCalculator(SpreadSheetModel model) {
        pointers = new HashSet<>();
        ranges = new HashSet<>();
        this.model = model;
    }

    public String calculate(String expression) throws ParseException {
        try {
            lexer = new Lexer(expression.toUpperCase());
            lexeme = lexer.nextLexem();
            return expression().toString();
        } catch (Exception e) {
            throw new ParseException("", 0);
        }
    }

    private LexerValue expression() throws ParseException {
        LexerValue result = composed();

        while (lexeme == Lexeme.PLUS || lexeme == Lexeme.MINUS) {
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            result = Util.plus(result, tempLexeme.getResult(composed()));
        }
        return result;
    }

    public Set<CellPointer> getPointers() {
        return pointers;
    }

    public void reset() {
        pointers.clear();
        ranges.clear();
    }

    public Set<CellRange> getRanges() {
        return ranges;
    }

    private LexerValue composed() throws ParseException {
        LexerValue result = sign();
        while (lexeme == Lexeme.DIV || lexeme == Lexeme.MULT) {
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            result = tempLexeme.getResult(result, sign());
        }
        return result;
    }

    private LexerValue power() throws ParseException {
        LexerValue result = multiplier();
        while (lexeme == Lexeme.POW) {
            lexeme = lexer.nextLexem();
            result = Util.power(result, power());
        }
        return result;
    }

    private LexerValue sign() throws ParseException {
        if (lexeme == Lexeme.PLUS || lexeme == Lexeme.MINUS){
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            return tempLexeme.getResult(power());
        } else {
            return power();
        }
    }

    private LexerValue multiplier() throws ParseException {
        Lexeme tempLexeme = lexeme;
        switch (lexeme.getType()) {
            case AGGREGATE_FUNCTION: {
                CellRange range = lexer.getRange();
                ranges.add(range);
                AggregateFunction function = lexer.getFunction();
                lexeme = lexer.nextLexem();
                return function.calculate(range, model);
            } case CELL_POINTER: {
                CellPointer pointer = lexer.getCellPointer();
                pointers.add(pointer);
                lexeme = lexer.nextLexem();
                CellValue value = (CellValue)model.getValueAt(pointer.getRow(), pointer.getColumn());
                return new LexerValue(value.getRendererValue());
            } case NUMBERS: {
                String num = lexer.getNumber();
                lexeme = lexer.nextLexem();
                return new LexerValue(num);
            } case FUNC:{
                lexeme = lexer.nextLexem();
                LexerValue result = tempLexeme.getResult(expression());
                lexeme = lexer.nextLexem();
                return result;
            } case OPERATION:{
                lexeme = lexer.nextLexem();
                return tempLexeme.getResult(multiplier());
            }
        }
        return LexerValue.NOTHING;
    }
}
