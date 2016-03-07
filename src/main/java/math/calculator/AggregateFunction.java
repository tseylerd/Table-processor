package math.calculator;

import ui.table.SpreadSheetModel;
import cells.CellPointer;
import cells.CellRange;

/**
 * @author Dmitriy Tseyler
 */
public enum AggregateFunction {
    SUM(Lexeme.SUM) {
        @Override
        public LexerValue calculate(CellRange range, SpreadSheetModel model) {
            double result = 0;
            for (CellPointer pointer : range) {
                result += model.getNumber(pointer.getRow(), pointer.getColumn());
            }
            return new LexerValue(result);
        }
    }, MEAN(Lexeme.MEAN) {
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

    private final Lexeme lexeme;

    AggregateFunction(Lexeme lexeme) {
        this.lexeme = lexeme;
    }

    public static AggregateFunction getFunction(Lexeme lexeme) {
        for (AggregateFunction aggregateFunction : AggregateFunction.values()) {
            if (aggregateFunction.lexeme == lexeme)
                return aggregateFunction;
        }
        throw new IllegalArgumentException();
    }

    public abstract LexerValue calculate(CellRange range, SpreadSheetModel model);
}
