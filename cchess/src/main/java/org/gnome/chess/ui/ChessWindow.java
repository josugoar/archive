
package org.gnome.chess.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.MutableComboBoxModel;

import org.gnome.chess.db.PGNGameDao;
import org.gnome.chess.lib.ChessClock;
import org.gnome.chess.lib.ChessGame;
import org.gnome.chess.lib.ChessMove;
import org.gnome.chess.lib.ChessState;
import org.gnome.chess.lib.PGNError;
import org.gnome.chess.lib.PGNGame;
import org.gnome.chess.util.ColorFactory;
import org.gnome.chess.util.Handler;
import org.gnome.chess.util.SignalSource;

public class ChessWindow extends JFrame {

    private static final long serialVersionUID = 1L;

    private PGNGameDao db = new PGNGameDao();

    private ChessView view;

    public ChessView getView(ChessView view) {
        return view;
    }

    public ChessScene scene;

    public ChessBoard board = new ChessBoard( this );

    public ChessScene getScene(ChessScene scene) {
        return scene;
    }

    private ChessGame game;

    private PGNGame pgnGame = new PGNGame(0); // TODO think of a way of increasing IDs

    public ChessGame getGame(ChessGame game) {
        return game;
    }

    public ChessApplication app;

    private long clockTickSignalId = 0;

    private JPanel mainBox;

    {
        mainBox = new JPanel(new BorderLayout(3, 3));
        mainBox.setFocusable(false);
        add(mainBox);
    }

    private JPanel infoBar;

    JButton newGameButton;
    JButton undoButton;
    JButton saveButton;

    public JComboBox<String> historyCombo;

    {
        infoBar = new JPanel(new GridBagLayout());
        infoBar.setBorder(BorderFactory.createEmptyBorder(6, 3, 6, 3));
        infoBar.setFocusable(false);
        mainBox.add(infoBar, BorderLayout.NORTH);

        newGameButton = new JButton("New Game");
        GridBagConstraints gbcNewGame = new GridBagConstraints();
        gbcNewGame.fill = GridBagConstraints.BOTH;
        gbcNewGame.insets = new Insets(0, 3, 0, 3);
        gbcNewGame.gridx = 0;
        newGameButton.addActionListener((ActionEvent e) -> {
            try {
                // pgnGame = new PGNGame(pgnGame.getId() + 1);
                board.index = 0;
                board.setGame(new ChessGame());
                historyCombo.removeAllItems();
            } catch (PGNError e1) {
                e1.printStackTrace();
            }
        });
        // newGameButton.setIcon(defaultIcon); // TODO
        newGameButton.setVerticalAlignment(JButton.CENTER);
        infoBar.add(newGameButton, gbcNewGame);

        undoButton = new JButton("Undo");
        GridBagConstraints gbcUndo = new GridBagConstraints();
        gbcUndo.fill = GridBagConstraints.BOTH;
        gbcUndo.insets = new Insets(0, 3, 0, 3);
        gbcUndo.gridx = 2;
        undoButton.setToolTipText("Undo your most recent move");
        undoButton.setVerticalAlignment(JButton.CENTER);
        infoBar.add(undoButton, gbcUndo);

        // JButton appMenuButton = new JButton();
        GridBagConstraints gbcAppMenu = new GridBagConstraints();
        gbcAppMenu.fill = GridBagConstraints.BOTH;
        gbcAppMenu.insets = new Insets(0, 3, 0, 3);
        gbcAppMenu.gridx = 5;
        // appMenuButton.addActionListener(l); // TODO
        //infoBar.add(appMenuButton, gbcAppMenu);

        saveButton = new JButton("Save");
        saveButton.addActionListener((ActionEvent e) -> {
            JFileChooser fil = new JFileChooser();

            if (fil.showSaveDialog(saveButton) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fil.getSelectedFile();
                try {
                    db.connect(selectedFile.getAbsolutePath());
                    pgnGame.moves.clear();
                    for (ChessState move : board.game.moveStack) {
                        if (move.lastMove != null) {
                            pgnGame.moves.add(move.lastMove.getLan());
                        }
                    }
                    db.save(pgnGame);
                    db.conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }

        });

        infoBar.add(saveButton, gbcAppMenu);

        JButton openButton = new JButton("Open");
        openButton.addActionListener((ActionEvent e) -> {
            JFileChooser fil = new JFileChooser();
            if (fil.showOpenDialog(openButton) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fil.getSelectedFile();
                System.out.println(selectedFile);
                try {
                    db.connect(selectedFile.getAbsolutePath());
                    List<PGNGame> result = db.getAll();
                    db.conn.close();
                    System.out.println(result.get(0).moves);
                    if (result.size() > 0) {
                        ChessGame tempGame = new ChessGame(ChessGame.STANDARD_SETUP);
                        board.setGame(tempGame);
                        for (int i = result.get(0).moves.size() - 1; i >= 0; i--) {
                            board.game.getCurrentPlayer().doMove(result.get(0).moves.get(i), true);
                        }
                        historyCombo.removeAllItems();
                        for (ChessState state : board.game.moveStack) {
                            historyCombo.addItem(state.lastMove.getSan());
                        }
                    }
                    
                } catch (SQLException e1) {
                    e1.printStackTrace();
                } catch (PGNError e1) {
                    e1.printStackTrace();
                }
            }
        });

        infoBar.add(openButton);

    }

    private JLabel infoBarLabel;

    {
        infoBarLabel = new JLabel();
        GridBagConstraints gbcInfoBar = new GridBagConstraints();
        gbcInfoBar.fill = GridBagConstraints.BOTH;
        gbcInfoBar.weightx = 1;
        gbcInfoBar.insets = new Insets(0, 3, 0, 3);
        gbcInfoBar.gridx = 4;
        infoBarLabel.setHorizontalAlignment(JLabel.CENTER);
        infoBar.add(infoBarLabel, gbcInfoBar);
    }

    private JButton pauseResumeButton;

    {
        pauseResumeButton = new JButton();
        GridBagConstraints gbcPauseResume = new GridBagConstraints();
        gbcPauseResume.fill = GridBagConstraints.BOTH;
        gbcPauseResume.insets = new Insets(0, 3, 0, 3);
        gbcPauseResume.gridx = 3;
        // pauseResumeButton.setIcon(defaultIcon); // TODO
        pauseResumeButton.setVerticalAlignment(JButton.CENTER);
        //infoBar.add(pauseResumeButton, gbcPauseResume);
    }

    private JPanel navigationBox;

    private JPanel buttonPanel;
    private JButton firstMoveButton;
    private JButton prevMoveButton;
    private JButton nextMoveButton;
    private JButton lastMoveButton;

    {
        navigationBox = new JPanel(new GridBagLayout());
        navigationBox.setBorder(BorderFactory.createEmptyBorder(6, 3, 6, 3));
        mainBox.add(navigationBox, BorderLayout.SOUTH);

        buttonPanel = new JPanel(new GridLayout(1, 4));
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.fill = GridBagConstraints.BOTH;
        gbcButton.insets = new Insets(0, 3, 0, 3);
        gbcButton.gridx = 0;
        navigationBox.add(buttonPanel, gbcButton);

        firstMoveButton = new JButton();
        URL firstMoveIcon = getClass().getClassLoader().getResource("icons/material/arrow-collapse-left.png");
        firstMoveButton.setIcon(new ImageIcon(firstMoveIcon));
        firstMoveButton.setToolTipText("Rewind to the game start");
        buttonPanel.add(firstMoveButton);

        prevMoveButton = new JButton();
        URL prevMoveIcon = getClass().getClassLoader().getResource("icons/material/arrow-left.png");
        prevMoveButton.setIcon(new ImageIcon(prevMoveIcon));
        prevMoveButton.setToolTipText("Show the previous move");
        buttonPanel.add(prevMoveButton);

        nextMoveButton = new JButton();
        URL nextMoveIcon = getClass().getClassLoader().getResource("icons/material/arrow-right.png");
        nextMoveButton.setIcon(new ImageIcon(nextMoveIcon));
        nextMoveButton.setToolTipText("Show the next move");
        buttonPanel.add(nextMoveButton);

        lastMoveButton = new JButton();
        URL lastMoveIcon = getClass().getClassLoader().getResource("icons/material/arrow-collapse-right.png");
        lastMoveButton.setIcon(new ImageIcon(lastMoveIcon));
        lastMoveButton.setToolTipText("Show the current move");
        buttonPanel.add(lastMoveButton);
    }

    

    {
        historyCombo = new JComboBox<>();
        GridBagConstraints gbcHistory = new GridBagConstraints();
        gbcHistory.fill = GridBagConstraints.BOTH;
        gbcHistory.weightx = 1;
        gbcHistory.insets = new Insets(0, 3, 0, 3);
        gbcHistory.gridx = 1;
        historyCombo.setFocusable(false);
        navigationBox.add(historyCombo, gbcHistory);
    }

    private JPanel clockBox;

    {
        clockBox = new JPanel(new GridLayout(1, 2));
        GridBagConstraints gbcClock = new GridBagConstraints();
        gbcClock.fill = GridBagConstraints.BOTH;
        gbcClock.insets = new Insets(0, 3, 0, 3);
        gbcClock.gridx = 2;
        clockBox.setFocusable(false);
        navigationBox.add(clockBox, gbcClock);
    }

    private JLabel whiteTimeLabel;

    {
        whiteTimeLabel = new JLabel();
        whiteTimeLabel.setBackground(ColorFactory.createWhiteColor());
        whiteTimeLabel.setFocusable(false);
        whiteTimeLabel.setForeground(ColorFactory.createBlackColor());
        whiteTimeLabel.setHorizontalAlignment(JLabel.CENTER);
        whiteTimeLabel.setPreferredSize(new Dimension(80, 0));
        whiteTimeLabel.setOpaque(true);
        clockBox.add(whiteTimeLabel);
        board.game.getClock().tick.connect(new Handler<SignalSource<ChessClock>,Class<Void>>(){
			@Override
			public Class<Void> handle(SignalSource<ChessClock> e) {
                int remainingSeconds = e.getSource().getWhiteRemainingSeconds();
                whiteTimeLabel.setText(String.format("%d:%d",remainingSeconds/60, remainingSeconds%60));
                return null;
			}
        });
    }

    private JLabel blackTimeLabel;

    {
        blackTimeLabel = new JLabel();
        blackTimeLabel.setBackground(ColorFactory.createBlackColor());
        blackTimeLabel.setFocusable(false);
        blackTimeLabel.setForeground(ColorFactory.createWhiteColor());
        blackTimeLabel.setHorizontalAlignment(JLabel.CENTER);
        blackTimeLabel.setPreferredSize(new Dimension(80, 0));
        blackTimeLabel.setOpaque(true);
        clockBox.add(blackTimeLabel);
        board.game.getClock().tick.connect(new Handler<SignalSource<ChessClock>,Class<Void>>(){
			@Override
			public Class<Void> handle(SignalSource<ChessClock> e) {
                int remainingSeconds = e.getSource().getBlackRemainingSeconds();
                blackTimeLabel.setText(String.format("%d:%d",remainingSeconds/60, remainingSeconds%60));
                return null;
			}
        });
    }

    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFocusable(false);
        setLocationByPlatform(true);
        setPreferredSize(new Dimension(700, getPreferredSize().height));
        setTitle("Chess");
        add(mainBox);
        pack();
    }

    public ChessWindow(ChessApplication app) {

        newGameButton.addActionListener(app.actionEntries.get(ChessApplication.NEW_GAME_ACTION_NAME));
        undoButton.addActionListener(app.actionEntries.get(ChessApplication.UNDO_MOVE_ACTION_NAME));
        pauseResumeButton.addActionListener(app.actionEntries.get(ChessApplication.PAUSE_RESUME_ACTION_NAME));
        firstMoveButton.addActionListener(app.actionEntries.get(ChessApplication.HISTORY_GO_FIRST_ACTION_NAME));
        prevMoveButton.addActionListener(app.actionEntries.get(ChessApplication.HISTORY_GO_PREVIOUS_ACTION_NAME));
        nextMoveButton.addActionListener(app.actionEntries.get(ChessApplication.HISTORY_GO_NEXT_ACTION_NAME));
        lastMoveButton.addActionListener(app.actionEntries.get(ChessApplication.HISTORY_GO_LAST_ACTION_NAME));

        historyCombo.addItemListener(historyComboChangedCb);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                Font f = new Font("Sans-Serif", Font.PLAIN, (int) (ChessWindow.this.getHeight() * 0.03));
                updateFont(ChessWindow.this, f);
            }
        });

        this.app = app;

        // var scene = new ChessScene();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JPanel secondPanel = new JPanel();
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(secondPanel);
        mainPanel.add(Box.createVerticalGlue());
        secondPanel.add(board);
        secondPanel.setAlignmentX(CENTER_ALIGNMENT);
        secondPanel.setAlignmentY(CENTER_ALIGNMENT);
        mainBox.add(mainPanel);

        // TODO
    }

    public void setClockVisible(boolean visible) {
        clockBox.setVisible(visible);
    }

    public void startGame() {
        var model = (MutableComboBoxModel<String>) historyCombo.getModel();
        model.setSelectedItem(null);
        /* Move History Combo: Go to the start of the game */
        model.addElement("Game Start");
        historyCombo.setSelectedItem(model.getSize() - 1);

        whiteTimeLabel.repaint();
        blackTimeLabel.repaint();

        if (clockTickSignalId != 0) {
            game.getClock().tick.disconnect(clockTickSignalId);
            clockTickSignalId = 0;
        }

        if (game.getClock() != null) {
            clockTickSignalId = game.getClock().tick.connect((SignalSource<ChessClock> e) -> {
                whiteTimeLabel.repaint();
                blackTimeLabel.repaint();
                return Void.TYPE;
            });
        }
    }

    private ItemListener historyComboChangedCb = (ItemEvent e) -> {
    };

    public void setMoveText(MutableComboBoxModel<String> model, ChessMove move) {
        /*
         * Note there are no move formats for pieces taking kings and this is not
         * allowed in Chess rules
         */
        // final String[] humanDescription = { /*
        // * Human Move String: Description of a white pawn moving from %1$s to %2s,
        // * e.g. 'c2 to c4'
        // */
        // "White pawn moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a white pawn at %1$s capturing a pawn at
        // * %2$s
        // */
        // "White pawn at %1$s takes the black pawn at %2$s",
        // /*
        // * Human Move String: Description of a white pawn at %1$s capturing a rook at
        // * %2$s
        // */
        // "White pawn at %1$s takes the black rook at %2$s",
        // /*
        // * Human Move String: Description of a white pawn at %1$s capturing a knight
        // at
        // * %2$s
        // */
        // "White pawn at %1$s takes the black knight at %2$s",
        // /*
        // * Human Move String: Description of a white pawn at %1$s capturing a bishop
        // at
        // * %2$s
        // */
        // "White pawn at %1$s takes the black bishop at %2$s",
        // /*
        // * Human Move String: Description of a white pawn at %1$s capturing a queen at
        // * %2$s
        // */
        // "White pawn at %1$s takes the black queen at %2$s",
        // /*
        // * Human Move String: Description of a white rook moving from %1$s to %2$s,
        // e.g.
        // * 'a1 to a5'
        // */
        // "White rook moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a white rook at %1$s capturing a pawn at
        // * %2$s
        // */
        // "White rook at %1$s takes the black pawn at %2$s",
        // /*
        // * Human Move String: Description of a white rook at %1$s capturing a rook at
        // * %2$s
        // */
        // "White rook at %1$s takes the black rook at %2$s",
        // /*
        // * Human Move String: Description of a white rook at %1$s capturing a knight
        // at
        // * %2$s
        // */
        // "White rook at %1$s takes the black knight at %2$s",
        // /*
        // * Human Move String: Description of a white rook at %1$s capturing a bishop
        // at
        // * %2$s
        // */
        // "White rook at %1$s takes the black bishop at %2$s",
        // /*
        // * Human Move String: Description of a white rook at %1$s capturing a queen at
        // * %2$s"
        // */
        // "White rook at %1$s takes the black queen at %2$s",
        // /*
        // * Human Move String: Description of a white knight moving from %1$s to %2$s,
        // * e.g. 'b1 to c3'
        // */
        // "White knight moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a white knight at %1$s capturing a pawn
        // at
        // * %2$s
        // */
        // "White knight at %1$s takes the black pawn at %2$s",
        // /*
        // * Human Move String: Description of a white knight at %1$s capturing a rook
        // at
        // * %2$s
        // */
        // "White knight at %1$s takes the black rook at %2$s",
        // /*
        // * Human Move String: Description of a white knight at %1$s capturing a knight
        // * at %2$s
        // */
        // "White knight at %1$s takes the black knight at %2$s",
        // /*
        // * Human Move String: Description of a white knight at %1$s capturing a bishop
        // * at %2$s
        // */
        // "White knight at %1$s takes the black bishop at %2$s",
        // /*
        // * Human Move String: Description of a white knight at %1$s capturing a queen
        // at
        // * %2$s
        // */
        // "White knight at %1$s takes the black queen at %2$s",
        // /*
        // * Human Move String: Description of a white bishop moving from %1$s to %2$s,
        // * e.g. 'f1 to b5'
        // */
        // "White bishop moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a white bishop at %1$s capturing a pawn
        // at
        // * %2$s
        // */
        // "White bishop at %1$s takes the black pawn at %2$s",
        // /*
        // * Human Move String: Description of a white bishop at %1$s capturing a rook
        // at
        // * %2$s
        // */
        // "White bishop at %1$s takes the black rook at %2$s",
        // /*
        // * Human Move String: Description of a white bishop at %1$s capturing a knight
        // * at %2$s
        // */
        // "White bishop at %1$s takes the black knight at %2$s",
        // /*
        // * Human Move String: Description of a white bishop at %1$s capturing a bishop
        // * at %2$s
        // */
        // "White bishop at %1$s takes the black bishop at %2$s",
        // /*
        // * Human Move String: Description of a white bishop at %1$s capturing a queen
        // at
        // * %2$s
        // */
        // "White bishop at %1$s takes the black queen at %2$s",
        // /*
        // * Human Move String: Description of a white queen moving from %1$s to %2$s,
        // * e.g. 'd1 to d4'
        // */
        // "White queen moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a white queen at %1$s capturing a pawn at
        // * %2$s
        // */
        // "White queen at %1$s takes the black pawn at %2$s",
        // /*
        // * Human Move String: Description of a white queen at %1$s capturing a rook at
        // * %2$s
        // */
        // "White queen at %1$s takes the black rook at %2$s",
        // /*
        // * Human Move String: Description of a white queen at %1$s capturing a knight
        // at
        // * %2$s
        // */
        // "White queen at %1$s takes the black knight at %2$s",
        // /*
        // * Human Move String: Description of a white queen at %1$s capturing a bishop
        // at
        // * %2$s
        // */
        // "White queen at %1$s takes the black bishop at %2$s",
        // /*
        // * Human Move String: Description of a white queen at %1$s capturing a queen
        // at
        // * %2$s
        // */
        // "White queen at %1$s takes the black queen at %2$s",
        // /*
        // * Human Move String: Description of a white king moving from %1$s to %2$s,
        // e.g.
        // * 'e1 to f1'
        // */
        // "White king moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a white king at %1$s capturing a pawn at
        // * %2$s
        // */
        // "White king at %1$s takes the black pawn at %2$s",
        // /*
        // * Human Move String: Description of a white king at %1$s capturing a rook at
        // * %2$s
        // */
        // "White king at %1$s takes the black rook at %2$s",
        // /*
        // * Human Move String: Description of a white king at %1$s capturing a knight
        // at
        // * %2$s
        // */
        // "White king at %1$s takes the black knight at %2$s",
        // /*
        // * Human Move String: Description of a white king at %1$s capturing a bishop
        // at
        // * %2$s
        // */
        // "White king at %1$s takes the black bishop at %2$s",
        // /*
        // * Human Move String: Description of a white king at %1$s capturing a queen at
        // * %2$s
        // */
        // "White king at %1$s takes the black queen at %2$s",
        // /*
        // * Human Move String: Description of a black pawn moving from %1$s to %2$s,
        // e.g.
        // * 'c8 to c6'
        // */
        // "Black pawn moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a black pawn at %1$s capturing a pawn at
        // * %2$s
        // */
        // "Black pawn at %1$s takes the white pawn at %2$s",
        // /*
        // * Human Move String: Description of a black pawn at %1$s capturing a rook at
        // * %2$s
        // */
        // "Black pawn at %1$s takes the white rook at %2$s",
        // /*
        // * Human Move String: Description of a black pawn at %1$s capturing a knight
        // at
        // * %2$s
        // */
        // "Black pawn at %1$s takes the white knight at %2$s",
        // /*
        // * Human Move String: Description of a black pawn at %1$s capturing a bishop
        // at
        // * %2$s
        // */
        // "Black pawn at %1$s takes the white bishop at %2$s",
        // /*
        // * Human Move String: Description of a black pawn at %1$s capturing a queen at
        // * %2$s
        // */
        // "Black pawn at %1$s takes the white queen at %2$s",
        // /*
        // * Human Move String: Description of a black rook moving from %1$s to %2$s,
        // e.g.
        // * 'a8 to a4'
        // */
        // "Black rook moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a black rook at %1$s capturing a pawn at
        // * %2$s
        // */
        // "Black rook at %1$s takes the white pawn at %2$s",
        // /*
        // * Human Move String: Description of a black rook at %1$s capturing a rook at
        // * %2$s
        // */
        // "Black rook at %1$s takes the white rook at %2$s",
        // /*
        // * Human Move String: Description of a black rook at %1$s capturing a knight
        // at
        // * %2$s
        // */
        // "Black rook at %1$s takes the white knight at %2$s",
        // /*
        // * Human Move String: Description of a black rook at %1$s capturing a bishop
        // at
        // * %2$s
        // */
        // "Black rook at %1$s takes the white bishop at %2$s",
        // /*
        // * Human Move String: Description of a black rook at %1$s capturing a queen at
        // * %2$s
        // */
        // "Black rook at %1$s takes the white queen at %2$s",
        // /*
        // * Human Move String: Description of a black knight moving from %1$s to %2$s,
        // * e.g. 'b8 to c6'
        // */
        // "Black knight moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a black knight at %1$s capturing a pawn
        // at
        // * %2$s
        // */
        // "Black knight at %1$s takes the white pawn at %2$s",
        // /*
        // * Human Move String: Description of a black knight at %1$s capturing a rook
        // at
        // * %2$s
        // */
        // "Black knight at %1$s takes the white rook at %2$s",
        // /*
        // * Human Move String: Description of a black knight at %1$s capturing a knight
        // * at %2$s
        // */
        // "Black knight at %1$s takes the white knight at %2$s",
        // /*
        // * Human Move String: Description of a black knight at %1$s capturing a bishop
        // * at %2$s
        // */
        // "Black knight at %1$s takes the white bishop at %2$s",
        // /*
        // * Human Move String: Description of a black knight at %1$s capturing a queen
        // at
        // * %2$s
        // */
        // "Black knight at %1$s takes the white queen at %2$s",
        // /*
        // * Human Move String: Description of a black bishop moving from %1$s to %2$s,
        // * e.g. 'f8 to b3'
        // */
        // "Black bishop moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a black bishop at %1$s capturing a pawn
        // at
        // * %2$s
        // */
        // "Black bishop at %1$s takes the white pawn at %2$s",
        // /*
        // * Human Move String: Description of a black bishop at %1$s capturing a rook
        // at
        // * %2$s
        // */
        // "Black bishop at %1$s takes the white rook at %2$s",
        // /*
        // * Human Move String: Description of a black bishop at %1$s capturing a knight
        // * at %2$s
        // */
        // "Black bishop at %1$s takes the white knight at %2$s",
        // /*
        // * Human Move String: Description of a black bishop at %1$s capturing a bishop
        // * at %2$s
        // */
        // "Black bishop at %1$s takes the white bishop at %2$s",
        // /*
        // * Human Move String: Description of a black bishop at %1$s capturing a queen
        // at
        // * %2$s
        // */
        // "Black bishop at %1$s takes the white queen at %2$s",
        // /*
        // * Human Move String: Description of a black queen moving from %1$s to %2$s,
        // * e.g. 'd8 to d5'
        // */
        // "Black queen moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a black queen at %1$s capturing a pawn at
        // * %2$s
        // */
        // "Black queen at %1$s takes the white pawn at %2$s",
        // /*
        // * Human Move String: Description of a black queen at %1$s capturing a rook at
        // * %2$s
        // */
        // "Black queen at %1$s takes the white rook at %2$s",
        // /*
        // * Human Move String: Description of a black queen at %1$s capturing a knight
        // at
        // * %2$s
        // */
        // "Black queen at %1$s takes the white knight at %2$s",
        // /*
        // * Human Move String: Description of a black queen at %1$s capturing a bishop
        // at
        // * %2$s
        // */
        // "Black queen at %1$s takes the white bishop at %2$s",
        // /*
        // * Human Move String: Description of a black queen at %1$s capturing a queen
        // at
        // * %2$s
        // */
        // "Black queen at %1$s takes the white queen at %2$s",
        // /*
        // * Human Move String: Description of a black king moving from %1$s to %2$s,
        // e.g.
        // * 'e8 to f8'
        // */
        // "Black king moves from %1$s to %2$s",
        // /*
        // * Human Move String: Description of a black king at %1$s capturing a pawn at
        // * %2$s
        // */
        // "Black king at %1$s takes the white pawn at %2$s",
        // /*
        // * Human Move String: Description of a black king at %1$s capturing a rook at
        // * %2$s
        // */
        // "Black king at %1$s takes the white rook at %2$s",
        // /*
        // * Human Move String: Description of a black king at %1$s capturing a knight
        // at
        // * %2$s
        // */
        // "Black king at %1$s takes the white knight at %2$s",
        // /*
        // * Human Move String: Description of a black king at %1$s capturing a bishop
        // at
        // * %2$s
        // */
        // "Black king at %1$s takes the white bishop at %2$s",
        // /*
        // * Human Move String: Description of a black king at %1$s capturing a queen at
        // * %2$s"
        // */
        // "Black king at %1$s takes the white queen at %2$s" };

        // var moveText = "";
        // switch (scene.moveFormat) {
        // case "human":
        // if (move.castlingRook != null) {
        // if (move.f0 < move.f1 && move.r0 == 0) {
        // moveText = "White castles kingside";
        // } else if (move.f1 < move.f0 && move.r0 == 0) {
        // moveText = "White castles queenside";
        // } else if (move.f0 < move.f1 && move.r0 == 7) {
        // moveText = "Black castles kingside";
        // } else if (move.f1 < move.f0 && move.r0 == 7) {
        // moveText = "Black castles queenside";
        // } else {
        // throw assertNotReached();
        // }
        // } else {
        // int index;
        // if (move.victim == null) {
        // index = 0;
        // } else {
        // index = move.victim.type.ordinal() + 1;
        // }
        // index += move.piece.type.ordinal() * 6;
        // if (move.piece.player.color == Color.BLACK) {
        // index += 36;
        // }

        // var start = String.format("%c%d", 'a' + move.f0, move.r0 + 1);
        // var end = String.format("%c%d", 'a' + move.f1, move.r1 + 1);
        // var template = humanDescription[index];
        // if (move.enPassant) {
        // if (move.r0 < move.r1) { /*
        // * Human Move String: Description of a white pawn at %1$s capturing a
        // * pawn at %2$s en passant
        // */
        // template = "White pawn at %1$s takes the black pawn at %2$s en passant";
        // } else { /*
        // * Human Move String: Description of a black pawn at %1$s capturing a pawn at
        // * %2$s en passant
        // */
        // template = "Black pawn at %1$s takes white pawn at %2$s en passant";
        // }
        // }
        // moveText = String.format(template, start, end);
        // }

        // break;

        // case "san":
        // moveText = move.getSan();
        // break;

        // case "fan":
        // moveText = move.getFan();
        // break;

        // case "lan":
        // // Fall through
        // default:
        // moveText = move.getLan();
        // break;
        // }
    }

    public void move(ChessMove m) {
        /* Automatically return view to the present */
        // scene.moveNumber = -1;

        var model = (MutableComboBoxModel<String>) historyCombo.getModel();
        setMoveText(model, m);

        /* Follow the latest move */
        if (m.number == game.getNMoves()) {
            historyCombo.setSelectedIndex(model.getSize() - 1);
        }
    }

    public void undo() {
        /* Remove from the history */
        var model = (MutableComboBoxModel<String>) historyCombo.getModel();
        model.removeElementAt(model.getSize() - 1);

        /* Always undo from the most recent move */
        // scene.moveNumber = -1;

        /* Go back one */
        historyCombo.setSelectedIndex(model.getSize() - 1);
        view.repaint();
    }

    public void end_game() {
        whiteTimeLabel.repaint();
        blackTimeLabel.repaint();
    }

    /**
     * Constant text scaling for the window
     *
     * @param comp the window
     * @param font the font to update
     */
    public void updateFont(Component comp, Font font) {
        comp.setFont(font);
        if (comp instanceof Container) {
            for (Component comp2 : ((Container) comp).getComponents()) {
                updateFont(comp2, font);
            }
        }
    }
}
