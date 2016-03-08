package math.calculator.Lexer;

/**
 * @author Dmitriy Tseyler
 * For lazy conversions
 */
public class LexerValue {
    public static final LexerValue NOTHING = new LexerValue(0);

    private String stringValue;
    private Double doubleValue;

    public LexerValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public LexerValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public String getStringValue() {
        if (stringValue == null) {
            stringValue = String.valueOf(doubleValue);
        }
        return stringValue;
    }

    public double getDoubleValue() {
        if (doubleValue == null) {
            doubleValue = Double.parseDouble(stringValue);
        }
        return doubleValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }
}
