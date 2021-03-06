package math.calculator.expression;

import cells.pointer.CellPointer;
import cells.CellValue;
import math.calculator.lexer.LexerValue;
import ui.table.SpreadSheetModel;
import ui.table.exceptions.CorruptedReferenceException;
import ui.table.exceptions.EmptyValueException;

/**
 * Expression for access table cell value
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
        CellValue value = model.getValueAt(pointer);
        if (value.getRendererValue().isEmpty()) {
            if (value.getError() == null) {
                throw new EmptyValueException();
            } else {
                throw new CorruptedReferenceException();
            }
        }
        return new LexerValue(value.getRendererValue());
    }
}
