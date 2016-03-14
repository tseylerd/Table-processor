package math.calculator.expression;

import math.calculator.lexer.LexerValue;
import math.calculator.expression.operation.BinaryOperation;
import ui.table.exceptions.SpreadSheetException;

/**
 * @author Dmitriy Tseyler
 */
public class BinaryExpression implements Expression {
    private final Expression first;
    private final Expression second;
    private final BinaryOperation operation;

    public BinaryExpression(BinaryOperation operation, Expression first, Expression second) {
        this.first = first;
        this.second = second;
        this.operation = operation;
    }

    @Override
    public LexerValue calculate() {
        SpreadSheetException firstException = null;
        SpreadSheetException secondException = null;
        SpreadSheetException thirdException = null;
        LexerValue firstValue;
        LexerValue secondValue;
        LexerValue result = null;
        try {
            firstValue = first.calculate();
        } catch (SpreadSheetException e) {
            firstException = e;
            firstValue = new LexerValue(e.getValue());
        }
        try {
            secondValue = second.calculate();
        } catch (SpreadSheetException e) {
            secondException = e;
            secondValue = new LexerValue(e.getValue());
        }
        try {
            result = operation.getValue(firstValue, secondValue);
        } catch (SpreadSheetException e) {
            thirdException = e;
        }
        SpreadSheetException.throwIfNeeded(firstException, secondException, thirdException);
        return result;
    }
}
