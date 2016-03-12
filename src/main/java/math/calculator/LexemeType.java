package math.calculator;

import math.calculator.expression.Expression;

import java.util.function.Function;

/**
 * @author Dmitriy Tseyler
 */
public enum LexemeType {
    AGGREGATE_FUNCTION(ExpressionParser::onAgregateFunction),
    FUNCTION(ExpressionParser::onFunction),
    CELL_POINTER(ExpressionParser::onCellPointer),
    OPERATION(ExpressionParser::onOperation),
    LITERAL(ExpressionParser::onLiteral);

    private final Function<ExpressionParser, Expression> expressionGetter;

    LexemeType(Function<ExpressionParser, Expression> expressionGetter) {
        this.expressionGetter = expressionGetter;
    }

    Expression getExpression(ExpressionParser parser) {
        return expressionGetter.apply(parser);
    }
}
