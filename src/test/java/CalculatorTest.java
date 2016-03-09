import math.calculator.ExpressionParser;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Dmitriy Tseyler
 */
public class CalculatorTest extends AbstractExpressionTest {
    private ExpressionParser calculator;

    @Before
    public void init() {
        calculator = new ExpressionParser(null);
    }

    private String calculate(String expression) throws Exception {
        return calculator.calculate(expression).calculate().getStringValue();
    }

    @Test
    public void testNumber() throws Exception {
        for (int i = 0; i < 10; i++) {
            test(String.valueOf(i), i);
        }
    }

    @Test
    public void testAdditionAndSubstitution() throws Exception {
        String expression = "1+2+3+4+5+6+(2+1)";
        double result = 1+2+3+4+5+6+(2+1);
        test(calculate(expression), result);

        expression = "1-2-3";
        result = 1 - 2 - 3;
        test(calculate(expression), result);
    }

    @Test
    public void testMultiplyDivision() throws Exception {
        String expression = " 2 * 3 * 4 * 5";
        double result = 2 * 3 * 4 * 5;
        test(calculate(expression), result);

        expression = "1/5/4";
        result = 1. / 5. / 4.;
        test(calculate(expression), result);
    }

    @Test
    public void testPower() throws Exception {
        String expression = "3^5";
        double result = Math.pow(3, 5);
        test(calculate(expression), result);
    }

    @Test
    public void testBraces() throws Exception {
        String expression = "(1+452+(600+4)*2^5/10)*50";
        double result = (1+452+(600+4)*Math.pow(2, 5)/10)*50;
        test(calculate(expression), result);
    }

}
