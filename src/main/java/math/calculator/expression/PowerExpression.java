package math.calculator.expression;

import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public class PowerExpression extends BinaryExpression {
    public PowerExpression(Expression exponent, Expression power) {
        super(Util::power, exponent, power);
    }
}
