import cells.CellValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ui.table.SpreadSheetModel;

/**
 * @author Dmitriy Tseyler
 */
public class SpreadSheetModelTest extends AbstractExpressionTest {
    private static final int DEFAULT_ROWS = 40;
    private static final int DEFAULT_COLS = 40;

    private SpreadSheetModel sheetModel;

    @Before
    public void init() {
        sheetModel = new SpreadSheetModel(DEFAULT_ROWS, DEFAULT_COLS);
    }

    @Test
    public void testValueSet() {
        CellValue value = new CellValue("value");
        value.setExpression("value");
        sheetModel.setValueAt(value, 1, 1);
        CellValue after = (CellValue) sheetModel.getValueAt(1, 1);
        Assert.assertEquals(value, after);
    }

    @Test
    public void testExpression() {
        String expression = "=A1/A2+3+5+42";
        double result = 10./20 + 3 + 5 + 42;
        CellValue value = new CellValue();
        CellValue value11 = new CellValue();
        value11.setExpression("10");
        CellValue value21 = new CellValue();
        value21.setExpression("20");
        value.setExpression(expression);
        sheetModel.setValueAt(value11, 0, 0);
        sheetModel.setValueAt(value21, 1, 0);
        sheetModel.setValueAt(value, 3, 1);

        CellValue resultCellValue = (CellValue) sheetModel.getValueAt(3, 1);
        test(resultCellValue.getRendererValue(), result);

        CellValue value11New = new CellValue();
        value11New.setExpression("50");
        result = 50./20 + 3 + 5 + 42;
        sheetModel.setValueAt(value11New, 0, 0);
        test(resultCellValue.getRendererValue(), result);
    }

    @Test
    public void testRange() {
        String expression = "=SUM(A1:A2)";
        double result = 32 + 54;
        CellValue value = new CellValue(expression);
        CellValue valueA1 = new CellValue("32");
        CellValue valueA2 = new CellValue("54");
        sheetModel.setValueAt(value, 3, 1);

        Assert.assertTrue(((CellValue)sheetModel.getValueAt(3, 1)).isErrorState());

        sheetModel.setValueAt(valueA1, 0, 0);
        sheetModel.setValueAt(valueA2, 1, 0);
        CellValue resultValue = (CellValue) sheetModel.getValueAt(3, 1);
        test(resultValue.getRendererValue(), result);
    }
}
