package math.calculator;

import math.calculator.Lexer.Lexeme;
import math.calculator.Lexer.Lexer;
import math.calculator.Lexer.LexerValue;
import math.calculator.expression.*;
import ui.table.SpreadSheetModel;
import cells.pointer.CellPointer;
import cells.CellRange;
import ui.table.exceptions.ParserException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dmitriy Tseyler
 */

public class ExpressionParser {
    private final SpreadSheetModel model;
    private final Set<CellPointer> pointers;
    private final Set<CellRange> ranges;

    private Lexeme lexeme;
    private Lexer lexer;

    public ExpressionParser(SpreadSheetModel model) {
        pointers = new HashSet<>();
        ranges = new HashSet<>();
        this.model = model;
    }

    public Expression parse(String expression) throws ParserException{
        pointers.clear();
        ranges.clear();
        expression = expression.replaceAll(" ", "");
        if (expression.isEmpty() || expression.charAt(0) != '=') {
            return new StringExpression(expression);
        }
        if (expression.length() == 1) {
            return new StringExpression("");
        }
        lexer = new Lexer(expression);
        lexeme = lexer.nextLexeme();
        return expression();
    }

    private Expression expression() throws ParserException {
        Expression result = composed();

        while (lexeme == Lexeme.PLUS || lexeme == Lexeme.MINUS) {
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexeme();
            result = new PlusExpression(tempLexeme, result, composed());
        }
        return result;
    }

    public Set<CellPointer> getPointers() {
        return pointers;
    }

    public Set<CellRange> getRanges() {
        return ranges;
    }

    private Expression composed() throws ParserException {
        Expression result = sign();
        while (lexeme == Lexeme.DIV || lexeme == Lexeme.MULT) {
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexeme();
            result = new BinaryExpression(tempLexeme::getResult, result, sign());
        }
        return result;
    }

    private Expression power() throws ParserException {
        Expression result = multiplier();
        while (lexeme == Lexeme.POW) {
            lexeme = lexer.nextLexeme();
            result = new PowerExpression(result, power());
        }
        return result;
    }

    private Expression sign() throws ParserException {
        if (lexeme == Lexeme.PLUS || lexeme == Lexeme.MINUS){
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexeme();
            return new UnaryExpression(tempLexeme::getResult, power());
        } else {
            return power();
        }
    }

    private Expression multiplier() throws ParserException {
        return lexeme.getType().getExpression(this);
    }

    public Expression onAgregateFunction() throws ParserException {
        CellRange range = lexer.getRange();
        ranges.add(range);
        AggregateFunction function = lexer.getFunction();
        lexeme = lexer.nextLexeme();
        return new AggregateExpression(function, range, model);
    }

    Expression onCellPointer() throws ParserException {
        CellPointer pointer = lexer.getCellPointer();
        pointers.add(pointer);
        lexeme = lexer.nextLexeme();
        return new CellPointerExpression(pointer, model);
    }

    Expression onLiteral() throws ParserException {
        String num = lexer.getNumber();
        lexeme = lexer.nextLexeme();
        return new NumberExpression(new LexerValue(num));
    }

    Expression onFunction() throws ParserException {
        Lexeme previous = lexeme;
        lexeme = lexer.nextLexeme();
        Expression result = new UnaryExpression(previous::getResult, expression());
        lexeme = lexer.nextLexeme();
        return result;
    }

    Expression onOperation() throws ParserException {
        Lexeme previous = lexeme;
        lexeme = lexer.nextLexeme();
        return new UnaryExpression(previous::getResult, multiplier());
    }
}
