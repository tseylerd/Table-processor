import math.calculator.lexer.LexerValue;
import org.junit.Assert;

/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractExpressionTest extends AbstractSwingTest {
    protected void test(String value, double doubleValue) {
        Assert.assertEquals(value, LexerValue.FORMAT.format(doubleValue));
    }
}
