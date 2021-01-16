
package org.gnome.chess.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class ChessWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    public enum LayoutMode {
        NORMAL, NARROW
    }

    private JPanel bottomPanel;
    private JPanel topPanel;
    private Font componentFont;
    private String gameState = "White move"; // TODO Change dynamically as gamestate changes

    private ArrayList<int[]> resolutions; // Access by settings menu

    public ChessWindow() {

        componentFont = new Font("Times New Roman", Font.BOLD, 30);

        setTitle("lol");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 960);

        prepBottomPanel();
        prepTopPanel();

        updateFont(this, componentFont);

        setVisible(true);

    }

    private void prepBottomPanel() {

        bottomPanel = new JPanel(new BorderLayout());

        // Game state control buttons ( bottom left )
        // TODO Replace text with icons
        // TODO Make buttons change the selected state
        // TODO ?? Dimm (block) buttons that can't be used (If selected state == newest
        // state, then dimm ">|" and ">" buttons )
        JPanel buttonPane = new JPanel();
        JButton toFirst = new JButton("|<");
        JButton back = new JButton("<");
        JButton forward = new JButton(">");
        JButton toLast = new JButton(">|");

        buttonPane.add(toFirst);
        buttonPane.add(back);
        buttonPane.add(forward);
        buttonPane.add(toLast);

        bottomPanel.add(buttonPane, BorderLayout.WEST);

        // ComboBox for selecting specific states (Stages) of an ongoing game
        // TODO Placeholder text to test ComboBox, dynamically add gamestates to Box
        // TODO Update center panel to draw selected state
        // TODO Change selected item as left pannel buttons are used
        String[] testStrings = { "One", "Two", "Three", "Four", "Five" };
        JComboBox stateSelector = new JComboBox(testStrings);
        bottomPanel.add(stateSelector, BorderLayout.CENTER);

        // Panel with timers for both players
        // TODO Change placeholder text to actual fluent changing timer
        JPanel timerPane = new JPanel();
        JLabel whiteTimerLabel = new JLabel("white");
        JLabel blackTimerlLabel = new JLabel("black");
        whiteTimerLabel.setOpaque(true);
        whiteTimerLabel.setBackground(Color.WHITE);
        blackTimerlLabel.setOpaque(true);
        blackTimerlLabel.setForeground(Color.WHITE);
        blackTimerlLabel.setBackground(Color.BLACK);
        timerPane.add(whiteTimerLabel);
        timerPane.add(blackTimerlLabel);
        bottomPanel.add(timerPane, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void prepTopPanel() {

        topPanel = new JPanel(new BorderLayout());

        // Top left button panel
        // TODO Make button create a new game
        // TODO Prompt user to save or discard the ongoing game before creating a new
        // one
        // TODO Erase newest state when selecting second button, update bottom comboBox
        // and Buttons accordingly
        // TODO Dimm reverse button if selected state != newest state???
        JPanel leftButtonPane = new JPanel();
        JButton newGame = new JButton("New Game");
        JButton undoMove = new JButton("<^^]");
        leftButtonPane.add(newGame);
        leftButtonPane.add(undoMove);
        topPanel.add(leftButtonPane, BorderLayout.WEST);

        // Game state bar, shows current ply player, if game ended and how it ended??
        // TODO Update when gamestate changes
        JLabel stateLabel = new JLabel(gameState, SwingConstants.CENTER);
        topPanel.add(stateLabel, BorderLayout.CENTER);

        // Top right button panel, File control and settings
        // TODO Change button text for icons
        // TODO Method to rescale icons as updateFont() does for fonts
        // TODO Make buttons act accordingly
        // TODO Setting button pops up a settings menu, settings might be stored in a
        // .config file??
        JPanel rightButtonPane = new JPanel();
        JButton open = new JButton("Open");
        JButton save = new JButton("Save");
        JButton settings = new JButton("Settings");
        rightButtonPane.add(open);
        rightButtonPane.add(save);
        rightButtonPane.add(settings);
        topPanel.add(rightButtonPane, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

    }

    /**
     * Method to change font in a component and all its child components
     *
     * @param component Top parent component
     * @param font      Font to replace previous one
     */
    private void updateFont(Component component, Font font) {

        component.setFont(font);
        if (component instanceof Container) {
            for (Component c : ((Container) component).getComponents()) {
                updateFont(c, font);
            }
        }
    }

}
