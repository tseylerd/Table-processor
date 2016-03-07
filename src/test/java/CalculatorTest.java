import math.calculator.ExpressionCalculator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;

/**
 * @author Dmitriy Tseyler
 */
public class CalculatorTest {
    private ExpressionCalculator calculator;

    @Before
    public void init() {
        calculator = new ExpressionCalculator(null);
    }

    private void test(String expression, double result) throws Exception {
        double calculatorResult = Double.parseDouble(calculator.calculate(expression));
        Assert.assertTrue(Double.compare(calculatorResult, result) == 0);
    }

    @Test
    public void testNumber() throws Exception {
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(calculator.calculate(String.valueOf(i)), String.valueOf(i));
        }
    }

    @Test
    public void testAdditionAndSubstitution() throws Exception {
        String expression = "1+2+3+4+5+6+(2+1)";
        double result = 1+2+3+4+5+6+(2+1);
        test(expression, result);

        expression = "1-2-3";
        result = 1 - 2 - 3;
        test(expression, result);
    }

    @Test
    public void testMultiplyDivision() throws Exception {
        String expression = " 2 * 3 * 4 * 5";
        double result = 2 * 3 * 4 * 5;
        test(expression, result);

        expression = "1/5/4";
        result = 1. / 5. / 4.;
        test(expression, result);
    }

    @Test
    public void testPower() throws Exception {
        String expression = "3^5";
        double result = Math.pow(3, 5);
        test(expression, result);
    }

}
