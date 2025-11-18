package org.cef.event;

/**
 * Lightweight mouse event for direct injection. Button mask values mirror AWT's
 * BUTTON{1,2,3}_DOWN_MASK so existing modifier flags stay compatible.
 */
public class CefMouseEvent {
    // Event ids (aligned with AWT for convenience)
    public static final int MOUSE_MOVED = 503;
    public static final int MOUSE_EXIT = 505;
    public static final int MOUSE_ENTER = 504;
    public static final int MOUSE_PRESSED = 501;
    public static final int MOUSE_RELEASED = 502;
    public static final int MOUSE_DRAGGED = 506;

    // Modifier masks (aligned with java.awt.event.InputEvent)
    public static final int BUTTON1_MASK = 0x10;
    public static final int BUTTON2_MASK = 0x20;
    public static final int BUTTON3_MASK = 0x40;

    private int id;
    private int x;
    private int y;
    private int modifiers;
    private int clickCount;
    /** Button number (1/2/3) for press/release events. */
    private int button;

    public CefMouseEvent(int id, int x, int y, int clickCount, int button, int modifiers) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.clickCount = clickCount;
        this.button = button;
        this.modifiers = modifiers;
    }

    public int getID() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getModifiers() {
        return modifiers;
    }

    public int getClickCount() {
        return clickCount;
    }

    public int getButton() {
        return button;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public void setButton(int button) {
        this.button = button;
    }
}
