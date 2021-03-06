package math.calculator;

import math.calculator.expression.Expression;

/**
 * Type of lexem
 * @author Dmitriy Tseyler
 */
public enum LexemeType {
    AGGREGATE_FUNCTION() {
        @Override
        Expression getExpression(ExpressionParser parser) throws ParserException {
            return parser.onAggregateFunction();
        }
    },
    FUNCTION() {
        @Override
        Expression getExpression(ExpressionParser parser) throws ParserException {
            return parser.onFunction();
        }
    },
    CELL_POINTER() {
        @Override
        Expression getExpression(ExpressionParser parser) throws ParserException {
            return parser.onCellPointer();
        }
    },
    OPERATION() {
        @Override
        Expression getExpression(ExpressionParser parser) throws ParserException {
            return parser.onOperation();
        }
    },
    LITERAL() {
        @Override
        Expression getExpression(ExpressionParser parser) throws ParserException {
            return parser.onLiteral();
        }
    };

    abstract Expression getExpression(ExpressionParser parser) throws ParserException;
}
