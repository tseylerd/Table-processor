package math.calculator.expression.operation;

import math.calculator.lexer.LexerValue;

/**
 * @author Dmitriy Tseyler
 */
public interface BinaryOperation {
    LexerValue getValue(LexerValue first, LexerValue second);
}
