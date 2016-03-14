package math.calculator.Lexer;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * @author Dmitriy Tseyler
 * For lazy conversions
 */
public class LexerValue {
    public static final DecimalFormat FORMAT = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    static {
        FORMAT.setMaximumFractionDigits(10);
    }

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
            stringValue = FORMAT.format(doubleValue);
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
