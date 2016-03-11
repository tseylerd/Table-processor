package math.calculator.function;

import math.calculator.Lexer.LexerValue;

import java.util.function.BiFunction;

/**
 * Created by dtseyler on 11.01.16.
 */
public class BinaryFunctionResolver implements FunctionResolver {
    private final BiFunction<LexerValue, LexerValue, LexerValue> biFunction;

    public BinaryFunctionResolver(BiFunction<LexerValue, LexerValue, LexerValue> biFunction) {
        this.biFunction = biFunction;
    }

    @Override
    public LexerValue getValue(LexerValue... values) {
        if (values.length != 2) {
            throw new IllegalArgumentException("Must be two values");
        }
        return biFunction.apply(values[0], values[1]);
    }
}
