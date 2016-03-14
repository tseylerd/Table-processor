package math.calculator.expression;

import math.calculator.lexer.LexerValue;

/**
 * @author Dmitriy Tseyler
 */
public class StringExpression implements Expression {
    private final String string;

    public StringExpression(String string) {
        this.string = string;
    }

    @Override
    public LexerValue calculate() {
        return new LexerValue(string);
    }
}
