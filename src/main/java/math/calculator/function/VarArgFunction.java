package math.calculator.function;

import math.calculator.Lexer.LexerValue;

import java.util.function.Function;

/**
 * Created by dtseyler on 11.01.16.
 */
public interface VarArgFunction {
    LexerValue apply(LexerValue... lexerValue);
}
