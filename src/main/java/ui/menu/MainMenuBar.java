package ui.menu;

import ui.menu.save.SaveAsMenuItem;
import ui.tabbedpane.MainTabbedPane;
import ui.menu.save.SaveMenuItem;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author Dmitriy Tseyler
 */
public class MainMenuBar extends JMenuBar {
    private final MainTabbedPane tabbedPane;
    private final JFrame mainFrame;

    public MainMenuBar(MainTabbedPane tabbedPane, JFrame mainFrame) {
        this.tabbedPane = tabbedPane;
        this.mainFrame = mainFrame;
        add(createFileMenu());
    }

    private JMenu createFileMenu() {
        JMenu file = new JMenu("File");
        file.add(new CreateTableMenuItem(tabbedPane));
        file.add(new SaveMenuItem(tabbedPane));
        file.add(new SaveAsMenuItem(tabbedPane));
        file.add(new JSeparator());

        file.add(new OpenMenuItem(tabbedPane));
        file.add(new JSeparator());

        file.add(new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
            }
        }));
        return file;
    }
}
