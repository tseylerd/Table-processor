package cells;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * @author Dmitriy Tseyler
 */
public class CellValue implements Transferable {
    private String value;
    private String expression;
    private boolean errorState;

    public CellValue() {
        this("");
    }

    public CellValue(String value) {
        this(value, "");
    }

    public CellValue(String value, String expression) {
        this.value = value;
        this.expression = expression;
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

    /* ================== Transferable ================== */

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[1];
        flavors[0] = new DataFlavor(this.getClass(), getRendererValue());
        return flavors;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return this;
    }
}
