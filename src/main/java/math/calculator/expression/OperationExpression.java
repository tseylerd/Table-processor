package math.calculator.expression;

import math.calculator.Lexer.Lexeme;
import math.calculator.Lexer.LexerValue;

/**
 * @author Dmitriy Tseyler
 */
public class OperationExpression implements Expression {
    private final Lexeme lexeme;
    private final Expression expression;

    public OperationExpression(Lexeme lexeme, Expression expression) {
        this.lexeme = lexeme;
        this.expression = expression;
    }

    @Override
    public LexerValue calculate() {
        return lexeme.getResult(expression.calculate());
    }
}
