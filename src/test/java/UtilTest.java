import cells.CellValue;
import org.junit.Assert;
import org.junit.Test;
import util.Util;

/**
 * @author Dmitriy Tseyler
 */
public class UtilTest {
    @Test
    public void testMoveExpression() {
        String expression = "=A1";
        CellValue value = new CellValue(expression);
        Util.move(value, 1, 1);
        Assert.assertEquals(value.getEditorValue(), "=B2");
    }

    @Test
    public void testMoveString() {
        String s = "A1";
        CellValue value = new CellValue(s);
        Util.move(value, 1, 1);
        Assert.assertEquals(value.getEditorValue(), "A1");
    }

    @Test
    public void testMoveImmutable() {
        CellValue value = new CellValue("=A1");
        CellValue moved = Util.moveImmutably(value, 1, 1);
        Assert.assertEquals(value.getEditorValue(), "=A1");
        Assert.assertEquals(moved.getEditorValue(), "=B2");
    }

    @Test
    public void testMoveFixedExpression() {
        CellValue fixed = new CellValue("=$A$1");
        CellValue columnFixed = new CellValue("=$A1");
        CellValue rowFixed = new CellValue("=A$1");
        Util.move(fixed, 1, 1);
        Util.move(columnFixed, 1, 1);
        Util.move(rowFixed, 1, 1);
        Assert.assertEquals(fixed.getEditorValue(), "=$A$1");
        Assert.assertEquals(rowFixed.getEditorValue(), "=B$1");
        Assert.assertEquals(columnFixed.getEditorValue(), "=$A2");
    }
}
