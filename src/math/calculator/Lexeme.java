package math.calculator;

/**
 * @author Dmitriy Tseyler
 */
public enum Lexeme {
    FUNC(),
    NUMBERS(),
    AGGREGATE_FUNCTION,
    CELL_POINTER,
    AMPERSAND(AGGREGATE_FUNCTION, '&', 1),
    CELL(CELL_POINTER, '@', 1),
    CLOSE(null,')',1),
    COS (FUNC,'C', 4){
        @Override
        public double getResult(double value){
            double result = Math.cos(value);
            return result;
        }
    },
    SIN (FUNC,'S',4){
        @Override
        public double getResult(double value){
            double result = Math.sin(value);
            return result;
        }
    },
    NUM (NUMBERS, '0', 0),
    OPEN(FUNC, '(',1),
    POW(null, '^',1),
    OPERATION,
    PLUS(OPERATION,'+',1){
        @Override
        public double getResult(double value){
            return value;
        }
    },
    MINUS(OPERATION,'-',1){
        @Override
        public double getResult(double value){
            return -value;
        }
    },
    DIV(OPERATION,'/',1){
        @Override
        public double getResult(double a, double b){
            return a/b;
        }
    },
    MULT(OPERATION,'*',1){
        @Override
        public double getResult(double a, double b){
            return a*b;
        }
    },
    ABS(FUNC, 'A',4){
        @Override
        public double getResult(double value){
            double result = Math.abs(value);
            return result;
        }
    };
    private char value;
    private int pointerPlus=1;
    private Lexeme type = null;
    private Lexeme(Lexeme parent, char value, int pointerPlus){
        this.value = value;
        this.pointerPlus = pointerPlus;
        this.type = parent;
    }
    private Lexeme(){
        this.value = 'R';
        this.pointerPlus = 0;
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
    public Lexeme getType(){
        return type;
    }
    public int getOffset(){
        return pointerPlus;
    }
    public  double getResult(double value){
        return value;
    }

    public  double getResult(double a, double b){
        return a;
    };
}
