package math.calculator.expression;

import math.calculator.lexer.LexerValue;
import math.calculator.expression.operation.UnaryOperation;

/**
 * @author Dmitriy Tseyler
 */
public class UnaryExpression implements Expression {
    private final UnaryOperation lexeme;
    private final Expression expression;

    public UnaryExpression(UnaryOperation operation, Expression expression) {
        this.lexeme = operation;
        this.expression = expression;
    }

    @Override
    public LexerValue calculate() {
        return lexeme.getValue(expression.calculate());
    }
}
