package math.calculator;

import cells.CellValue;
import ui.table.SpreadSheetModel;
import cells.CellPointer;
import cells.CellRange;
import util.Util;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dmitriy Tseyler
 */

public class ExpressionCalculator { // TODO: 06.03.16 remove multiple string conversions
    private Lexeme lexeme;
    private Lexer lexer;
    private final SpreadSheetModel model;
    private final Set<CellPointer> pointers;
    private final Set<CellRange> ranges;

    public ExpressionCalculator(SpreadSheetModel model) {
        pointers = new HashSet<>();
        ranges = new HashSet<>();
        this.model = model;
    }

    public String calculate(String expression) throws ParseException {
        try {
            lexer = new Lexer(expression.toUpperCase(), model);
            lexeme = lexer.nextLexem();
            return expression();
        } catch (NumberFormatException e) {
            throw new ParseException("", 0);
        }
    }

    private String expression() throws ParseException {
        String result = composed();

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

    private String composed() throws ParseException {
        String result = sign();
        while (lexeme == Lexeme.DIV || lexeme == Lexeme.MULT) {
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            result = tempLexeme.getResult(result, sign());
        }
        return result;
    }

    private String power() throws ParseException {
        String result = multiplier();
        while (lexeme == Lexeme.POW) {
            lexeme = lexer.nextLexem();
            result = Util.power(result, power());
        }
        return result;
    }

    private String sign() throws ParseException {
        if (lexeme == Lexeme.PLUS || lexeme == Lexeme.MINUS){
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            return tempLexeme.getResult(power());
        } else {
            return power();
        }
    }

    private String multiplier() throws ParseException {
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
                return value.getRendererValue();
            } case NUMBERS: {
                String num = lexer.getNumber();
                lexeme = lexer.nextLexem();
                return num;
            } case FUNC:{
                lexeme = lexer.nextLexem();
                String result = tempLexeme.getResult(expression());
                lexeme = lexer.nextLexem();
                return result;
            } case OPERATION:{
                lexeme = lexer.nextLexem();
                return tempLexeme.getResult(multiplier());
            }
        }
        return "0";
    }
}
