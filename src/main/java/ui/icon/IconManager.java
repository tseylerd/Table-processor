package ui.icon;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Dmitriy Tseyler
 */
public class IconManager {
    private static IconManager instance = new IconManager();
    private static final String ICON_PATH = "/icons/%s.png";

    private final Map<IconConfiguration, Icon> icons;

    private IconManager() {
        icons = new EnumMap<>(IconConfiguration.class);
    }

    public static IconManager getInstance() {
        if (instance == null) {
            instance = new IconManager();
        }
        return instance;
    }

    public Icon getIcon(IconConfiguration config) {
        Icon icon = icons.get(config);
        if (icon == null) {
            String fileName = String.format(ICON_PATH, config.getName());
            icon = new ImageIcon(new ImageIcon(getClass().getResource(fileName)).getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH));
            icons.put(config, icon);
        }
        return icon;
    }
}
