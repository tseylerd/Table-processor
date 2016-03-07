package math.calculator;

import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public enum Lexeme {
    AMPERSAND(LexemeType.AGGREGATE_FUNCTION, '&', 1),
    CELL(LexemeType.CELL_POINTER, '@', 1),
    CLOSE(null,')',1),
    COS (LexemeType.FUNCTION,'C', 4){
        @Override
        public LexerValue getResult(LexerValue value) {
            return Util.cos(value);
        }
    },
    SIN (LexemeType.FUNCTION,'S',4){
        @Override
        public LexerValue getResult(LexerValue value) {
            return Util.sin(value);
        }
    },
    NUM (LexemeType.NUMBER, '0', 0),
    OPEN(LexemeType.FUNCTION, '(',1),
    POW(null, '^',1),
    PLUS(LexemeType.OPERATION,'+',1),
    MINUS(LexemeType.OPERATION,'-',1){
        @Override
        public LexerValue getResult(LexerValue value){
            return Util.inverse(value);
        }
    },
    DIV(LexemeType.OPERATION,'/',1){
        @Override
        public LexerValue getResult(LexerValue a, LexerValue b){
            return Util.div(a, b);
        }
    },
    MULT(LexemeType.OPERATION,'*',1){
        @Override
        public LexerValue getResult(LexerValue a, LexerValue b){
            return Util.multiply(a, b);
        }
    },
    ABS(LexemeType.OPERATION, 'A',4){
        @Override
        public LexerValue getResult(LexerValue value){
            return Util.abs(value);
        }
    };

    private char value;
    private int offset;
    private LexemeType type = null;

    Lexeme(LexemeType type, char value, int offset){
        this.value = value;
        this.offset = offset;
        this.type = type;
    }

    public static Lexeme getLexem(char value) {
        if (Character.isDigit(value)) {
            return NUM;
        } else {
            for (Lexeme lexeme : Lexeme.values()){
                if (lexeme.value == value)
                    return lexeme;
            }
        }
        return null;
    }
    public LexemeType getType(){
        return type;
    }
    public int getOffset(){
        return offset;
    }
    public LexerValue getResult(LexerValue value){
        return value;
    }

    public LexerValue getResult(LexerValue a, LexerValue b){
        return a;
    };
}
