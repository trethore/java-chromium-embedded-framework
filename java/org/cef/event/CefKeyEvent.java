package org.cef.event;

/**
 * Lightweight key event used for direct input injection without converting
 * through AWT. Values mirror GLFW constants used by LWJGL (PRESS=1, RELEASE=0,
 * REPEAT/TYPE=2) so existing LWJGL code can forward events as-is.
 */
public class CefKeyEvent {
    /** Matches GLFW_RELEASE. */
    public static final int KEY_RELEASE = 0;
    /** Matches GLFW_PRESS. */
    public static final int KEY_PRESS = 1;
    /** Matches GLFW_REPEAT. */
    public static final int KEY_TYPE = 2;

    private int id;
    private int keyCode;
    private char keyChar;
    private int modifiers;
    /** Optional native scancode (e.g. from GLFW). */
    private long scancode;

    public CefKeyEvent(int id, int keyCode, char keyChar, int modifiers) {
        this.id = id;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
        this.modifiers = modifiers;
    }

    public int getID() {
        return id;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public char getKeyChar() {
        return keyChar;
    }

    public int getModifiers() {
        return modifiers;
    }

    public long getScancode() {
        return scancode;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public void setKeyChar(char keyChar) {
        this.keyChar = keyChar;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public void setScancode(long scancode) {
        this.scancode = scancode;
    }
}
