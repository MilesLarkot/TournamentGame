package edu.pidev3a32.tools;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Toast {
    public static void showToast(String message) {
        Notifications.create()
                .title("Info")
                .text(message)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_RIGHT)
                .showInformation();
    }

    public static void showToast(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_RIGHT)
                .showInformation();
    }

    public static void showWarning(String message) {
        Notifications.create()
                .title("Warning")
                .text(message)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_RIGHT)
                .showWarning();
    }

    public static void showWarning(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_RIGHT)
                .showWarning();
    }
}
