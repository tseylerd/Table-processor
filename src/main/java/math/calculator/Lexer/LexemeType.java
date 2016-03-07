package math.calculator.Lexer;

/**
 * @author Dmitriy Tseyler
 */
public enum LexemeType {
    AGGREGATE_FUNCTION, // TODO: 07.03.16 function and agregate function - one object
    FUNCTION,
    CELL_POINTER,
    OPERATION,
    NUMBER;
}
