package com.pet.animation.enviroment;

import com.sun.jna.Pointer;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.RECT;
import com.sun.jna.platform.win32.WinUser;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Scans top-level windows and returns those that overlap a given area.
 */
public class WindowsScanner {

    public static List<NativeWindow> getWindowsUnder(Rectangle area) {
        List<NativeWindow> result = new ArrayList<>();

        User32.INSTANCE.EnumWindows(new WinUser.WNDENUMPROC() {
            @Override
            public boolean callback(HWND hWnd, Pointer data) {
                try {
                    if (!User32.INSTANCE.IsWindowVisible(hWnd)) {
                        return true; // continue
                    }

                    RECT rect = new RECT();
                    if (!User32.INSTANCE.GetWindowRect(hWnd, rect)) {
                        return true;
                    }

                    Rectangle winRect = new Rectangle(rect.left, rect.top, rect.right - rect.left, rect.bottom - rect.top);

                    if (!area.intersects(winRect)) {
                        return true; // not overlapping
                    }

                    // Get class name
                    char[] classBuf = new char[512];
                    User32.INSTANCE.GetClassName(hWnd, classBuf, 512);
                    String className = Native.toString(classBuf);

                    // Get window title
                    char[] titleBuf = new char[512];
                    User32.INSTANCE.GetWindowText(hWnd, titleBuf, 512);
                    String title = Native.toString(titleBuf);

                    NativeWindow.WindowType type = NativeWindow.WindowType.UNKNOWN;
                    if ("Shell_TrayWnd".equalsIgnoreCase(className)) {
                        type = NativeWindow.WindowType.TASKBAR;
                    } else if ("Progman".equalsIgnoreCase(className) || "WorkerW".equalsIgnoreCase(className)) {
                        type = NativeWindow.WindowType.DESKTOP;
                    } else {
                        // Treat other visible overlapped windows with a title as apps
                        type = (title != null && title.trim().length() > 0) ? NativeWindow.WindowType.APP : NativeWindow.WindowType.UNKNOWN;
                    }

                    result.add(new NativeWindow(winRect, title, className, type));
                } catch (Throwable t) {
                    // keep scanning even if one window caused trouble
                    t.printStackTrace();
                }
                return true;
            }
        }, null);

        return result;
    }
}
