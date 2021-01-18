package org.gnome.chess.ui;

import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import static org.gnome.chess.util.Logging.warning;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ChessApplication extends Thread {

    public static String NEW_GAME_ACTION_NAME = "new";
    public static String OPEN_GAME_ACTION_NAME = "open";
    public static String SAVE_GAME_ACTION_NAME = "save";
    public static String SAVE_GAME_AS_ACTION_NAME = "save-as";
    public static String UNDO_MOVE_ACTION_NAME = "undo";
    public static String RESIGN_ACTION_NAME = "resign";
    public static String PAUSE_RESUME_ACTION_NAME = "pause-resume";
    public static String HISTORY_GO_FIRST_ACTION_NAME = "go-first";
    public static String HISTORY_GO_PREVIOUS_ACTION_NAME = "go-previous";
    public static String HISTORY_GO_NEXT_ACTION_NAME = "go-next";
    public static String HISTORY_GO_LAST_ACTION_NAME = "go-last";
    public static String PREFERENCES_ACTION_NAME = "preferences";
    public static String HELP_ACTION_NAME = "help";
    public static String ABOUT_ACTION_NAME = "about";
    public static String QUIT_ACTION_NAME = "quit";

    private ChessWindow window;

    public ChessWindow getWindow() {
        return window;
    }

    public Map<String, ActionListener> actionEntries;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            warning("%s", e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            SwingUtilities.invokeAndWait(() -> {
                new ChessApplication().start();
            });
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ChessApplication() {
    }

    @Override
    public void run() {
        actionEntries = new HashMap<>();
        actionEntries.put(NEW_GAME_ACTION_NAME, newGameCb);
        actionEntries.put(OPEN_GAME_ACTION_NAME, openGameCb);
        actionEntries.put(SAVE_GAME_ACTION_NAME, saveGameCb);
        actionEntries.put(SAVE_GAME_AS_ACTION_NAME, saveGameAsCb);
        actionEntries.put(UNDO_MOVE_ACTION_NAME, undoMoveCb);
        actionEntries.put(RESIGN_ACTION_NAME, resignCb);
        actionEntries.put(PAUSE_RESUME_ACTION_NAME, pauseResumeCb);
        actionEntries.put(HISTORY_GO_FIRST_ACTION_NAME, historyGoFirstCb);
        actionEntries.put(HISTORY_GO_PREVIOUS_ACTION_NAME, historyGoPreviousCb);
        actionEntries.put(HISTORY_GO_NEXT_ACTION_NAME, historyGoNextCb);
        actionEntries.put(HISTORY_GO_LAST_ACTION_NAME, historyGoLastCb);
        actionEntries.put(PREFERENCES_ACTION_NAME, preferencesCb);
        actionEntries.put(HELP_ACTION_NAME, helpCb);
        actionEntries.put(ABOUT_ACTION_NAME, aboutCb);
        actionEntries.put(QUIT_ACTION_NAME, quitCb);

        window = new ChessWindow(this);
        window.setVisible(true);
    }

    public ActionListener newGameCb = (ActionEvent e) -> {
    };

    public ActionListener resignCb = (ActionEvent e) -> {
    };

    public ActionListener undoMoveCb = (ActionEvent e) -> {
    };

    public ActionListener pauseResumeCb = (ActionEvent e) -> {
    };

    public ActionListener quitCb = (ActionEvent e) -> {
    };

    private ActionListener historyGoFirstCb = (ActionEvent e) -> {
    };

    private ActionListener historyGoPreviousCb = (ActionEvent e) -> {
    };

    private ActionListener historyGoNextCb = (ActionEvent e) -> {
    };

    private ActionListener historyGoLastCb = (ActionEvent e) -> {
    };

    private ActionListener preferencesCb = (ActionEvent e) -> {
    };

    public ActionListener helpCb = (ActionEvent e) -> {
    };

    public ActionListener aboutCb = (ActionEvent e) -> {
    };

    public ActionListener saveGameCb = (ActionEvent e) -> {
    };

    public ActionListener saveGameAsCb = (ActionEvent e) -> {
    };

    public ActionListener openGameCb = (ActionEvent e) -> {
    };

}
