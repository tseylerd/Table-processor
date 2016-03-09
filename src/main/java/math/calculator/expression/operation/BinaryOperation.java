package math.calculator.expression.operation;

import math.calculator.Lexer.LexerValue;

/**
 * @author Dmitriy Tseyler
 */
public interface BinaryOperation {
    LexerValue getValue(LexerValue first, LexerValue second);
}
