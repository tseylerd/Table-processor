import math.calculator.Lexer.LexerValue;
import org.junit.Assert;

/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractExpressionTest {
    protected void test(String value, double doubleValue) {
        Assert.assertEquals(value, LexerValue.FORMAT.format(doubleValue));
    }
}
