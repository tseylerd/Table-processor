package math.calculator.expression;

import math.calculator.Lexer.Lexeme;
import math.calculator.Lexer.LexerValue;
import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public class PlusExpression implements Expression {
    private final Lexeme lexeme;
    private final Expression first;
    private final Expression second;

    public PlusExpression(Lexeme lexeme, Expression first, Expression second) {
        this.lexeme = lexeme;
        this.first = first;
        this.second = second;
    }

    @Override
    public LexerValue calculate() {
        return Util.plus(first.calculate(), lexeme.getResult(second.calculate()));
    }
}
