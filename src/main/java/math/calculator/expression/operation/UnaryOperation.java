package math.calculator.expression.operation;

import math.calculator.lexer.LexerValue;

/**
 * @author Dmitriy Tseyler
 */
public interface UnaryOperation {
    LexerValue getValue(LexerValue value);
}
