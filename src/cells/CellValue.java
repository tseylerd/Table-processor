package cells;

/**
 * @author Dmitriy Tseyler
 */
public class CellValue {
    private String value;
    private String expression;

    public CellValue() {
        this("");
    }

    public CellValue(String value) {
        this.value = value;
    }

    public String getRendererValue() {
        return value;
    }

    public String getEditorValue() {
        return expression;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
