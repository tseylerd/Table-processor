package math.calculator.function;

import math.calculator.lexer.LexerValue;

/**
 * Used for applying different function types from the same interface
 * @author Dmitriy Tseyler
 */
public interface FunctionResolver {
    LexerValue getValue(LexerValue... values);
}
