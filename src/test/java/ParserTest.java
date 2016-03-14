import math.calculator.ExpressionParser;
import math.calculator.ParserException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.text.html.parser.Parser;
import java.text.ParseException;


/**
 * @author Dmitriy Tseyler
 */
public class ParserTest extends AbstractExpressionTest {
    private ExpressionParser calculator;

    @Before
    public void init() {
        calculator = new ExpressionParser(null);
    }

    private String calculate(String expression) throws Exception {
        return calculator.parse(expression).calculate().getStringValue();
    }

    @Test
    public void testNumber() throws Exception {
        for (int i = 0; i < 10; i++) {
            test(calculate('=' + String.valueOf(i)), i);
        }
    }

    @Test
    public void testAdditionAndSubstitution() throws Exception {
        String expression = "=1+2+3+4+5+6+(2+1)";
        double result = 1+2+3+4+5+6+(2+1);
        test(calculate(expression), result);

        expression = "=1-2-3";
        result = 1 - 2 - 3;
        test(calculate(expression), result);
    }

    @Test
    public void testMultiplyDivision() throws Exception {
        String expression = " = 2 * 3 * 4 * 5";
        double result = 2 * 3 * 4 * 5;
        test(calculate(expression), result);

        expression = "= 1/5/4";
        result = 1. / 5. / 4.;
        test(calculate(expression), result);
    }

    @Test
    public void testPower() throws Exception {
        String expression = "= 3^5";
        double result = Math.pow(3, 5);
        test(calculate(expression), result);
    }

    @Test
    public void testBraces() throws Exception {
        String expression = "= (1+452+(600+4)*2^5/10)*50";
        double result = (1+452+(600+4)*Math.pow(2, 5)/10)*50;
        test(calculate(expression), result);
    }

    @Test
    public void testFunc() throws Exception {
        String expression = "= sin(1)^4";
        double result = Math.pow(Math.sin(1), 4);
        test(calculate(expression), result);
    }

    @Test
    public void testStringExpression() throws Exception {
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
