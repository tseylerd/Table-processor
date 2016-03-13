package ui;

import ui.laf.ProcessorLookAndFeel;
import ui.menu.MainMenuBar;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dmitriy Tseyler
 */
public class MainFrame extends JFrame {
    private static final Dimension DEFAULT_SIZE = new Dimension(1024, 768);
    private static final String TITLE = "Table processor";

    public MainFrame() {
        MainTabbedPane tabbedPane = new MainTabbedPane();
        setJMenuBar(new MainMenuBar(tabbedPane, this));
        setLayout(new BorderLayout());
        setTitle(TITLE);
        add(tabbedPane, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(DEFAULT_SIZE);
        pack();
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(new ProcessorLookAndFeel());
        MainFrame frame = new MainFrame();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            JOptionPane.showMessageDialog(frame, e);
            e.printStackTrace();
        });
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }
}
