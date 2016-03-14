package math.calculator.expression;

import math.calculator.lexer.LexerValue;
import ui.table.exceptions.SpreadSheetException;

/**
 * @author Dmitriy Tseyler
 */
public interface Expression {
    LexerValue calculate() throws SpreadSheetException;
}
