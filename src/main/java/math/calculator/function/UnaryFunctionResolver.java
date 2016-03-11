package math.calculator.function;

import math.calculator.Lexer.LexerValue;

import java.util.function.Function;

/**
 * Created by dtseyler on 11.01.16.
 */
public class UnaryFunctionResolver implements FunctionResolver {
    private final Function<LexerValue, LexerValue> function;

    public UnaryFunctionResolver(Function<LexerValue, LexerValue> function) {
        this.function = function;
    }

    @Override
    public LexerValue getValue(LexerValue... values) {
        if (values.length != 1) {
            throw new IllegalArgumentException("Must be one value");
        }
        return function.apply(values[0]);
    }
}
