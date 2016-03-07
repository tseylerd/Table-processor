package cells;

/**
 * @author Dmitriy Tseyler
 */
public class CellValue {
    private String value;
    private String expression;
    private boolean errorState;

    public CellValue() {
        this("");
    }

    public CellValue(String value) {
        this.value = value;
    }

    public String getRendererValue() {
        return isErrorState() ? "Error" : value;
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

    public void setErrorState(boolean errorState) {
        this.errorState = errorState;
    }

    public boolean isErrorState() {
        return errorState;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CellValue)) {
            return false;
        }

        CellValue toCompare = (CellValue)obj;
        return this.value.equals(toCompare.value) && this.expression.equals(toCompare.expression);
    }
}
