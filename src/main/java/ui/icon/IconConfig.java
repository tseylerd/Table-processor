package main.java.ui.icon;

/**
 * @author Dmitriy Tseyler
 */
public enum IconConfig {
    WARNING("warning");

    private final String name;

    IconConfig(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
