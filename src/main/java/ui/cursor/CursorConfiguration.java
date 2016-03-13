package ui.cursor;

/**
 * @author Dmitriy Tseyler
 */
public enum CursorConfiguration {
    SPAN_DOWN("span_down"),
    SPAN_UP("span_up"),
    SPAN_LEFT("span_left"),
    SPAN_RIGHT("span_right");

    private final String name;

    CursorConfiguration(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
