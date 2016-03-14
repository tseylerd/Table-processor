package math.calculator.expression;

import math.calculator.lexer.LexerValue;

/**
 * @author Dmitriy Tseyler
 */
public class NumberExpression implements Expression {
    private final LexerValue value;

    public NumberExpression(LexerValue value) {
        this.value = value;
    }

    @Override
    public LexerValue calculate() {
        return value;
    }
}
