import org.junit.Before;
import ui.laf.ProcessorLookAndFeel;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Dmitriy Tseyler
 */
public class AbstractSwingTest {

    @Before
    public void init() {
        installLaf();
    }

    protected void installLaf() {
        try {
            SwingUtilities.invokeAndWait(this::install);
        } catch (InterruptedException | InvocationTargetException ignored) {
        }
    }

    private void install() {
        try {
            UIManager.setLookAndFeel(new ProcessorLookAndFeel());
        } catch (UnsupportedLookAndFeelException ignore) {
        }
    }
}
