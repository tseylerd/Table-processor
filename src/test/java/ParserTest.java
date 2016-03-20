import math.calculator.ExpressionParser;
import math.calculator.ParserException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Dmitriy Tseyler
 */
public class ParserTest extends AbstractExpressionTest {
    private ExpressionParser calculator;

    @Before
    public void init() {
        calculator = new ExpressionParser(null);
    }

    private String calculate(String expression) {
        return calculator.parse(expression).calculate().getStringValue();
    }

    @Test
    public void testNumber() {
        for (int i = 0; i < 10; i++) {
            test(calculate('=' + String.valueOf(i)), i);
        }
    }

    @Test
    public void testAdditionAndSubstitution() {
        String expression = "=1+2+3+4+5+6+(2+1)";
        double result = 1+2+3+4+5+6+(2+1);
        test(calculate(expression), result);

        expression = "=1-2-3";
        result = 1 - 2 - 3;
        test(calculate(expression), result);
    }

    @Test
    public void testMultiplyDivision() {
        String expression = " = 2 * 3 * 4 * 5";
        double result = 2 * 3 * 4 * 5;
        test(calculate(expression), result);

        expression = "= 1/5/4";
        result = 1. / 5. / 4.;
        test(calculate(expression), result);
    }

    @Test
    public void testPower() {
        String expression = "= 3^5";
        double result = Math.pow(3, 5);
        test(calculate(expression), result);
    }

    @Test
    public void testBraces() {
        String expression = "= (1+452+(600+4)*2^5/10)*50";
        double result = (1+452+(600+4)*Math.pow(2, 5)/10)*50;
        test(calculate(expression), result);
    }

    @Test
    public void testFunc() {
        String expression = "= sin(1)^4";
        double result = Math.pow(Math.sin(1), 4);
        test(calculate(expression), result);
    }

    @Test
    public void testStringExpression() {
        String expression;
        try {
            expression = "=abdfgggg+1";
            calculate(expression);
        } catch (ParserException e) {
            try {
                expression = "= 1 + abdfgggg";
                calculate(expression);
            } catch (ParserException inner) {
                Assert.assertTrue(true);
                return;
            }
        }
        Assert.assertTrue(false);
    }
}
