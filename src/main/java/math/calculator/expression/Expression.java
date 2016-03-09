package math.calculator.expression;

import math.calculator.Lexer.LexerValue;

/**
 * @author Dmitriy Tseyler
 */
public interface Expression {
    LexerValue calculate();
}
