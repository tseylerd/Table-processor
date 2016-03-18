package math.calculator.function;

import math.calculator.ParserException;
import math.calculator.lexer.LexerValue;

import java.util.function.Function;

/**
 * Used for applying unary function to value
 * @author Dmitriy Tseyler
 */
public class UnaryFunctionResolver implements FunctionResolver {
    private final Function<LexerValue, LexerValue> function;

    public UnaryFunctionResolver(Function<LexerValue, LexerValue> function) {
        this.function = function;
    }

    @Override
    public LexerValue getValue(LexerValue... values) {
        if (values.length != 1) {
            throw new ParserException();
        }
        return function.apply(values[0]);
    }
}
