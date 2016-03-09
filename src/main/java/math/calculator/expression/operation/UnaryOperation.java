package math.calculator.expression.operation;

import math.calculator.Lexer.LexerValue;

/**
 * @author Dmitriy Tseyler
 */
public interface UnaryOperation {
    LexerValue getValue(LexerValue value);
}
