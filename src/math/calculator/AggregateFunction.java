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
        public String calculate(CellRange range, SpreadSheetModel model) {
            double result = 0;
            for (CellPointer pointer : range) {
                result += model.getNumber(pointer.getRow(), pointer.getColumn());
            }
            return String.valueOf(result);
        }
    }, MEAN("MEAN") {
        @Override
        public String calculate(CellRange range, SpreadSheetModel model) {
            double result = 0;
            int count = 0;
            for (CellPointer cellPointer : range) {
                count++;
                result += model.getNumber(cellPointer.getRow(), cellPointer.getColumn());
            }
            result /= count;
            return String.valueOf(result);
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

    public abstract String calculate(CellRange range, SpreadSheetModel model);
}
