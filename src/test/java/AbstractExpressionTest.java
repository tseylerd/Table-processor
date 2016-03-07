import org.junit.Assert;

/**
 * @author Dmitriy Tseyler
 */
public abstract class AbstractExpressionTest {
    protected void test(String value, double doubleValue) {
        double result = Double.parseDouble(value);
        Assert.assertEquals(Double.compare(result, doubleValue), 0);
    }
}
