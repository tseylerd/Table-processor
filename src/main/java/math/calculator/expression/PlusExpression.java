package math.calculator.expression;

import math.calculator.lexer.Lexeme;
import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public class PlusExpression extends BinaryExpression {
    public PlusExpression(Lexeme lexeme, Expression first, Expression second) {
        super(Util::plus, first, new UnaryExpression(lexeme::getResult, second));
    }
}
