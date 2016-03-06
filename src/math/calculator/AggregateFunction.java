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
        public double calculate(CellRange range, SpreadSheetModel model) {
            double result = 0;
            for (CellPointer pointer : range) {
                result += model.getNumber(pointer.getRow(), pointer.getColumn());
            }
            return result;
        }
    }, MEAN("MEAN") {
        @Override
        public double calculate(CellRange range, SpreadSheetModel model) {
            double result = 0;
            int count = 0;
            for (CellPointer cellPointer : range) {
                count++;
                result += model.getNumber(cellPointer.getRow(), cellPointer.getColumn());
            }
            result /= count;
            return result;
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

    public abstract double calculate(CellRange range, SpreadSheetModel model);
}
