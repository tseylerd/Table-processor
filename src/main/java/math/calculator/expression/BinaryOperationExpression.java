package math.calculator.expression;

import math.calculator.Lexer.Lexeme;
import math.calculator.Lexer.LexerValue;

/**
 * @author Dmitriy Tseyler
 */
public class BinaryOperationExpression implements Expression {
    private final Expression first;
    private final Expression second;
    private final Lexeme lexeme;

    public BinaryOperationExpression(Lexeme lexeme, Expression first, Expression second) {
        this.lexeme = lexeme;
        this.first = first;
        this.second = second;
    }

    @Override
    public LexerValue calculate() {
        return lexeme.getResult(first.calculate(), second.calculate());
    }
}
