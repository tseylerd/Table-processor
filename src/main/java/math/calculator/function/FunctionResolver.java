package math.calculator.function;

import math.calculator.lexer.LexerValue;

/**
 * Created by dtseyler on 11.01.16.
 */
public interface FunctionResolver {
    LexerValue getValue(LexerValue... values);
}
