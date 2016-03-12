package cells;

import math.calculator.expression.Expression;
import math.calculator.expression.StringExpression;

/**
 * @author Dmitriy Tseyler
 */
public class CellValue {
    private String rendererValue;
    private String editorValue;
    private Expression expression;
    private boolean errorState;

    public CellValue(CellValue rendererValue) {
        this(rendererValue.rendererValue, rendererValue.editorValue);
    }

    public CellValue() {
        this("");
    }

    public CellValue(String expression) {
        this(expression, expression);
    }

    public CellValue(String rendererValue, String expression) {
        this.rendererValue = rendererValue;
        this.editorValue = expression;
    }

    public String getRendererValue() {
        return isErrorState() ? "Error" : rendererValue;
    }

    public String getEditorValue() {
        return editorValue;
    }

    public void setRendererValue(String valueString) {
        this.rendererValue = valueString;
    }

    public void setEditorValue(String expressionString) {
        this.editorValue = expressionString;
    }

    public void setErrorState(boolean errorState) {
        this.errorState = errorState;
    }

    public boolean isErrorState() {
        return errorState;
    }

    public boolean containsExpression() {
        return !(expression instanceof StringExpression);
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CellValue)) {
            return false;
        }

        CellValue toCompare = (CellValue)obj;
        return this.rendererValue.equals(toCompare.rendererValue) && this.editorValue.equals(toCompare.editorValue);
    }

    @Override
    public int hashCode() {
        return rendererValue.hashCode() + editorValue.hashCode();
    }
}
