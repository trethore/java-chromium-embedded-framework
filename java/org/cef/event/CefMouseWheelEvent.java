package org.cef.event;

/**
 * Lightweight mouse-wheel event for direct injection without AWT conversion.
 * Uses double delta for high-resolution trackpads.
 */
public class CefMouseWheelEvent {
    public static final int WHEEL_UNIT_SCROLL = 0;
    public static final int WHEEL_BLOCK_SCROLL = 1;

    private int id;
    private double delta;
    private int x;
    private int y;
    private int modifiers;
    /** Units to scroll for WHEEL_UNIT_SCROLL; default approximates typical AWT value. */
    private int amount = 32;

    public CefMouseWheelEvent(int id, int x, int y, double delta, int modifiers) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.delta = delta;
        this.modifiers = modifiers;
    }

    public int getScrollType() {
        return id;
    }

    public double getWheelRotation() {
        return delta;
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

    public double getUnitsToScroll() {
        return amount * delta;
    }

    public void setScrollType(int id) {
        this.id = id;
    }

    public void setWheelRotation(double delta) {
        this.delta = delta;
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

    public void setUnitsToScroll(int amount) {
        this.amount = amount;
    }
}
