package math.calculator;

import cells.CellValue;
import ui.table.SpreadSheetModel;
import cells.CellPointer;
import cells.CellRange;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy Tseyler
 */

public class ExpressionCalculator {
    private Lexeme lexeme;
    private Lexer lexer;
    private final SpreadSheetModel model;
    private final List<CellPointer> pointers;
    private final List<CellRange> ranges;

    public ExpressionCalculator(SpreadSheetModel model) {
        pointers = new ArrayList<>();
        ranges = new ArrayList<>();
        this.model = model;
    }

    public double calculate(String expression) throws ParseException {
        try {
            lexer = new Lexer(expression.toUpperCase(), model);
            lexeme = lexer.nextLexem();
            return expression();
        } catch (Exception e) {
            throw new ParseException("", 0);
        }
    }

    private double expression() throws ParseException {
        double result = composed();

        while (lexeme == Lexeme.PLUS || lexeme == Lexeme.MINUS) {
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            result += tempLexeme.getResult(composed());
        }
        return result;
    }

    public List<CellPointer> getPointers() {
        return pointers;
    }

    public void reset() {
        pointers.clear();
        ranges.clear();
    }

    public List<CellRange> getRanges() {
        return ranges;
    }

    private double composed() throws ParseException {
        double result = sign();
        while (lexeme == Lexeme.DIV || lexeme == Lexeme.MULT) {
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            result = tempLexeme.getResult(result, sign());
        }
        return result;
    }

    private double power() throws ParseException {
        double result = multiplier();
        while (lexeme == Lexeme.POW) {
            lexeme = lexer.nextLexem();
            result = Math.pow(result, power());
        }
        return result;
    }

    private double sign() throws ParseException {
        if (lexeme == Lexeme.PLUS || lexeme == Lexeme.MINUS){
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            return tempLexeme.getResult(power());
        } else {
            return power();
        }
    }

    private double multiplier() throws ParseException {
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
                return Double.parseDouble(value.getRendererValue());
            } case NUMBERS: {
                double num = lexer.getNumber();
                lexeme = lexer.nextLexem();
                return num;
            } case FUNC:{
                lexeme = lexer.nextLexem();
                double temp = tempLexeme.getResult(expression());
                lexeme = lexer.nextLexem();
                return temp;
            } case OPERATION:{
                lexeme = lexer.nextLexem();
                return tempLexeme.getResult(multiplier());
            }
        }
        return 0;
    }
}
