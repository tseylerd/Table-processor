package math.calculator;

import ui.table.SpreadSheetModel;
import cells.CellPointer;
import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public enum AggregateFunction {
    SUM("SUM") {
        @Override
        public LexerValue calculate(CellRange range, SpreadSheetModel model) {
            double result = 0;
            for (CellPointer pointer : range) {
                result += model.getNumber(pointer.getRow(), pointer.getColumn());
            }
            return new LexerValue(result);
        }
    }, MEAN("MEAN") {
        @Override
        public LexerValue calculate(CellRange range, SpreadSheetModel model) {
            double result = 0;
            int count = 0;
            for (CellPointer cellPointer : range) {
                count++;
                result += model.getNumber(cellPointer.getRow(), cellPointer.getColumn());
            }
            result /= count;
            return new LexerValue(result);
        }
    };

    private final String name;

    AggregateFunction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static AggregateFunction getFunction(String name) {
        for (AggregateFunction aggregateFunction : AggregateFunction.values()) {
            if (aggregateFunction.name.equals(name))
                return aggregateFunction;
        }
        throw new IllegalArgumentException();
    }

    public abstract LexerValue calculate(CellRange range, SpreadSheetModel model);
}
