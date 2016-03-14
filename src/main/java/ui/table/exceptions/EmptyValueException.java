package ui.table.exceptions;

import ui.table.error.Error;

/**
 * @author Dmitriy Tseyler
 */
public class EmptyValueException extends SpreadSheetException {
    public EmptyValueException() {
        super(Error.EMPTY_VALUE, 5, "");
    }
}
