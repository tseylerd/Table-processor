package math.calculator.expression;

import cells.pointer.CellPointer;
import cells.CellValue;
import math.calculator.Lexer.LexerValue;
import ui.table.SpreadSheetModel;

/**
 * @author Dmitriy Tseyler
 */
public class CellPointerExpression implements Expression {
    private final CellPointer pointer;
    private final SpreadSheetModel model;

    public CellPointerExpression(CellPointer pointer, SpreadSheetModel model) {
        this.model = model;
        this.pointer = pointer;
    }

    @Override
    public LexerValue calculate() {
        CellValue value = (CellValue)model.getValueAt(pointer.getRow(), pointer.getColumn());
        return new LexerValue(value.getRendererValue());
    }
}
