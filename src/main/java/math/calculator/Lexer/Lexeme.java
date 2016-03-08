package math.calculator.Lexer;

import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public enum Lexeme {
    SUM(LexemeType.AGGREGATE_FUNCTION, "SUM"),
    MEAN(LexemeType.AGGREGATE_FUNCTION, "MEAN"),
    CELL(LexemeType.CELL_POINTER, "@"),
    MIN(LexemeType.AGGREGATE_FUNCTION, "MIN"),
    MAX(LexemeType.AGGREGATE_FUNCTION, "MAX"),
    CLOSE(null,")"),
    COS (LexemeType.FUNCTION,"COS"){
        @Override
        public LexerValue getResult(LexerValue value) {
            return Util.cos(value);
        }
    },
    SIN (LexemeType.FUNCTION,"SIN"){
        @Override
        public LexerValue getResult(LexerValue value) {
            return Util.sin(value);
        }
    },
    NUM (LexemeType.NUMBER, "0"),
    OPEN(LexemeType.FUNCTION, "("),
    POW(null, "^"),
    PLUS(LexemeType.OPERATION,"+"),
    MINUS(LexemeType.OPERATION,"-"){
        @Override
        public LexerValue getResult(LexerValue value){
            return Util.inverse(value);
        }
    },
    DIV(LexemeType.OPERATION,"/"){
        @Override
        public LexerValue getResult(LexerValue a, LexerValue b){
            return Util.div(a, b);
        }
    },
    MULT(LexemeType.OPERATION,"*"){
        @Override
        public LexerValue getResult(LexerValue a, LexerValue b){
            return Util.multiply(a, b);
        }
    },
    ABS(LexemeType.OPERATION, "ABS"){
        @Override
        public LexerValue getResult(LexerValue value){
            return Util.abs(value);
        }
    };

    private final String value;
    private final LexemeType type;

    Lexeme(LexemeType type, String value){
        this.value = value;
        this.type = type;
    }

    public static Lexeme getLexem(String value) {
        for (Lexeme lexeme : Lexeme.values()){
            if (lexeme.value.equals(value))
                return lexeme;
        }
        return null;
    }
    public LexemeType getType(){
        return type;
    }
    public LexerValue getResult(LexerValue value){
        return value;
    }

    public LexerValue getResult(LexerValue a, LexerValue b){
        return a;
    }
}
