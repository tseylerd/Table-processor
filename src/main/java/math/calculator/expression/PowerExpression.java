package math.calculator.expression;

import math.calculator.Lexer.LexerValue;
import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public class PowerExpression implements Expression {
    private final Expression exponent;
    private final Expression power;

    public PowerExpression(Expression exponent, Expression power) {
        this.exponent = exponent;
        this.power = power;
    }

    @Override
    public LexerValue calculate() {
        return Util.power(exponent.calculate(), power.calculate());
    }
}
