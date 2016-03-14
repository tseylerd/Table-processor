package math.calculator.expression;

import cells.CellRange;
import math.calculator.AggregateFunction;
import math.calculator.lexer.LexerValue;
import ui.table.SpreadSheetModel;

/**
 * @author Dmitriy Tseyler
 */
public class AggregateExpression implements Expression {
    private final CellRange range;
    private final SpreadSheetModel model;
    private final AggregateFunction function;

    public AggregateExpression(AggregateFunction function, CellRange range, SpreadSheetModel model) {
        this.function = function;
        this.range = range;
        this.model = model;
    }

    @Override
    public LexerValue calculate() {
        return function.calculate(range, model);
    }
}
