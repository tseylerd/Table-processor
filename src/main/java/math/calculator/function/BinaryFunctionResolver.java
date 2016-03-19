package math.calculator.function;

import math.calculator.ParserException;
import math.calculator.lexer.LexerValue;

import java.util.function.BiFunction;

/**
 * Used for applying binary function to values
 * @author Dmitriy Tseyler
 */
public class BinaryFunctionResolver implements FunctionResolver {
    private final BiFunction<LexerValue, LexerValue, LexerValue> biFunction;

    public BinaryFunctionResolver(BiFunction<LexerValue, LexerValue, LexerValue> biFunction) {
        this.biFunction = biFunction;
    }

    @Override
    public LexerValue getValue(LexerValue... values) {
        if (values.length != 2) {
            throw new ParserException();
        }
        return biFunction.apply(values[0], values[1]);
    }
}
