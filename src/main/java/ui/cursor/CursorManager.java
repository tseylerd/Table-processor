package ui.cursor;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Dmitriy Tseyler
 */
public class CursorManager {
    private static CursorManager instance;

    private final Map<CursorConfiguration, Cursor> cursorMap;

    private CursorManager() {
        cursorMap = new EnumMap<>(CursorConfiguration.class);
    }

    public Cursor getCursor(CursorConfiguration configuration) {
        Cursor cursor = cursorMap.get(configuration);
        if (cursor == null) {
            String fileName = "/cursors/" + configuration.getName() + ".png";
            Point hotspot = new Point(16, 16);
            Image image = new ImageIcon(getClass().getResource(fileName)).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, hotspot, configuration.getName());
            cursorMap.put(configuration, cursor);
        }
        return cursor;
    }

    public static CursorManager getInstance() {
        if (instance == null) {
            instance = new CursorManager();
        }
        return instance;
    }
}
