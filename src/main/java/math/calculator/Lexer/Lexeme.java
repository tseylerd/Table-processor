package math.calculator.Lexer;

import math.calculator.LexemeType;
import math.calculator.function.BinaryFunctionResolver;
import math.calculator.function.FunctionResolver;
import math.calculator.function.UnaryFunctionResolver;
import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public enum Lexeme {
    SUM(LexemeType.AGGREGATE_FUNCTION, "SUM(", null),
    MEAN(LexemeType.AGGREGATE_FUNCTION, "MEAN(", null),
    CELL(LexemeType.CELL_POINTER),
    MIN(LexemeType.AGGREGATE_FUNCTION, "MIN(", null),
    MAX(LexemeType.AGGREGATE_FUNCTION, "MAX(", null),
    CLOSE(null, ")", null),
    COS (LexemeType.FUNCTION, "COS(", new UnaryFunctionResolver(Util::cos)),
    SIN (LexemeType.FUNCTION, "SIN(", new UnaryFunctionResolver(Util::sin)),
    NUMBER(LexemeType.LITERAL),
    OPEN(LexemeType.FUNCTION, "(", lexerValues -> lexerValues[0]),
    POW(null, "^", null),
    PLUS(LexemeType.OPERATION, "+", new UnaryFunctionResolver(lexerValue -> lexerValue)),
    MINUS(LexemeType.OPERATION,"-", new UnaryFunctionResolver(Util::inverse)),
    DIV(LexemeType.OPERATION,"/", new BinaryFunctionResolver(Util::div)),
    MULT(LexemeType.OPERATION,"*", new BinaryFunctionResolver(Util::multiply)),
    ABS(LexemeType.OPERATION, "ABS", new UnaryFunctionResolver(Util::abs)),
    STRING(LexemeType.LITERAL);

    private final String value;
    private final LexemeType type;
    private final FunctionResolver functionResolver;

    Lexeme(LexemeType type) {
        this(type, null, null);
    }

    Lexeme(LexemeType type, String value, FunctionResolver functionResolver){
        this.value = value;
        this.type = type;
        this.functionResolver = functionResolver;
    }

    public static Lexeme getLexem(String value) {
        for (Lexeme lexeme : Lexeme.values()){
            if (value.equals(lexeme.value))
                return lexeme;
        }
        return null;
    }

    public LexemeType getType(){
        return type;
    }

    public LexerValue getResult(LexerValue... values){
        return functionResolver.getValue(values);
    }

    public String getValue() {
        return value;
    }
}
