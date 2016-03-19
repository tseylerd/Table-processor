import math.calculator.lexer.LexerValue;
import org.junit.Assert;

/**
 * @author Dmitriy Tseyler
 */
abstract class AbstractExpressionTest extends AbstractSwingTest {
    void test(String value, double doubleValue) {
        Assert.assertEquals(value, LexerValue.FORMAT.format(doubleValue));
    }
}
