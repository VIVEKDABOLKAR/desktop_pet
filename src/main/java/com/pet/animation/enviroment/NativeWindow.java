package com.pet.animation.enviroment;

import java.awt.Rectangle;

public class NativeWindow {
    public enum WindowType {
        APP,
        TASKBAR,
        DESKTOP,
        UNKNOWN
    }

    private final Rectangle boundRectangle;
    private final String title;
    private final String className;
    private final WindowType type;

    public NativeWindow(Rectangle boundRectangle, String title, String className, WindowType type) {
        this.boundRectangle = boundRectangle;
        this.title = title == null ? "" : title;
        this.className = className == null ? "" : className;
        this.type = type == null ? WindowType.UNKNOWN : type;
    }

    public Rectangle getBoundRectangle() {
        return boundRectangle;
    }

    public String getTitle() {
        return title;
    }

    public String getClassName() {
        return className;
    }

    public WindowType getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("NativeWindow[type=%s, class=%s, title=%s, bounds=%s]", type, className, title, boundRectangle);
    }
}