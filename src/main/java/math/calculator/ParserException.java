package math.calculator;

import ui.table.error.Error;
import ui.table.exceptions.SpreadSheetException;

/**
 * @author Dmitriy Tseyler
 */
public class ParserException extends SpreadSheetException {
    public ParserException() {
        super(Error.PARSE, 1, "");
    }
}
