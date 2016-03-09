package math.calculator.expression;

import math.calculator.Lexer.LexerValue;
import math.calculator.expression.operation.BinaryOperation;

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
        return operation.getValue(first.calculate(), second.calculate());
    }
}
