package org.gnome.Chess.chess;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import javax.lang.model.type.NullType;

import org.gnome.Chess.chess.ChessClock.ChessClockEvent;
import org.gnome.Chess.chess.ChessPiece.ChessPieceEvent;
import org.gnome.Chess.chess.ChessPlayer.ChessPlayerEvent;
import org.gnome.Chess.chess.ChessPlayer.DoMoveEvent;
import org.gnome.Chess.util.Handler;
import org.gnome.Chess.util.Out;
import org.gnome.Chess.util.Signal;

public class ChessGame {

    public static class ChessGameEvent extends EventObject {

        private static final long serialVersionUID = 1L;

        public ChessGameEvent(ChessGame source) {
            super(source);
        }

        @Override
        public ChessGame getSource() {
            return (ChessGame) super.getSource();
        }

    }

    public static class TurnStartedEvent extends ChessGameEvent {

        private static final long serialVersionUID = 1L;

        private ChessPlayer player;

        public TurnStartedEvent(ChessGame source, ChessPlayer player) {
            super(source);
            this.player = player;
        }

        public ChessPlayer getPlayer() {
            return player;
        }

    }

    public static class MovedEvent extends ChessGameEvent {

        private static final long serialVersionUID = 1L;

        private ChessMove move;

        public MovedEvent(ChessGame source, ChessMove move) {
            super(source);
            this.move = move;
        }

        public ChessMove getMove() {
            return move;
        }

    }

    public boolean isStarted;
    public ChessResult result;
    public ChessRule rule;
    public List<ChessState> moveStack = new ArrayList<>();

    private int holdCount = 0;

    public static final String STANDARD_SETUP = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public Signal<TurnStartedEvent, NullType> turnStarted = new Signal<>();
    public Signal<MovedEvent, NullType> moved = new Signal<>();
    public Signal<ChessGameEvent, NullType> paused = new Signal<>();
    public Signal<ChessGameEvent, NullType> unpaused = new Signal<>();
    public Signal<ChessGameEvent, NullType> undo = new Signal<>();
    public Signal<ChessGameEvent, NullType> ended = new Signal<>();

    private boolean isPaused;

    public boolean isPaused() {
        return isPaused;
    }

    private void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    {
        setPaused(false);
    }

    private boolean shouldShowPausedOverlay;

    public boolean shouldShowPausedOverlay() {
        return shouldShowPausedOverlay;
    }

    private void setShowPausedOverlay(boolean shouldShowPausedOverlay) {
        this.shouldShowPausedOverlay = shouldShowPausedOverlay;
    }

    {
        setShowPausedOverlay(false);
    }

    public ChessState getCurrentState() {
        return moveStack.get(moveStack.size() - 1);
    }

    public ChessPlayer getWhite() {
        return getCurrentState().players[Color.WHITE.ordinal()];
    }

    public ChessPlayer getBlack() {
        return getCurrentState().players[Color.BLACK.ordinal()];
    }

    public ChessPlayer getCurrentPlayer() {
        return getCurrentState().currentPlayer;
    }

    public ChessPlayer getOpponent() {
        return getCurrentState().getOpponent();
    }

    private ChessClock clock;

    public ChessClock getClock() {
        return clock;
    }

    public void setClock(ChessClock clock) {
        if (isStarted) {
            return;
        }
        this.clock = clock;
    }

    public ChessGame() throws PGNError {
        this(STANDARD_SETUP, null);
    }

    public ChessGame(String fen) throws PGNError {
        this(fen, null);
    }

    public ChessGame(String[] moves) throws PGNError {
        this(STANDARD_SETUP, moves);
    }

    public ChessGame(String fen, String[] moves) throws PGNError {
        isStarted = false;
        moveStack.add(0, new ChessState(fen));
        result = ChessResult.IN_PROGRESS;

        if (moves != null) {
            for (var i = 0; i < moves.length; i++) {
                if (!doMove(getCurrentPlayer(), moves[i], true)) {
                    /* Message when the game cannot be loaded due to an invalid move in the file. */
                    throw new PGNError.LOAD_ERROR(String.format("Failed to load PGN: move %s is invalid.", moves[i]));
                }
            }
        }

        getWhite().doMove.connect(moveCb);
        getWhite().doUndo.connect(undoCb);
        getWhite().doResign.connect(resignCb);
        getWhite().doClaimDraw.connect(claimDrawCb);
        getBlack().doMove.connect(moveCb);
        getBlack().doUndo.connect(undoCb);
        getBlack().doResign.connect(resignCb);
        getBlack().doClaimDraw.connect(claimDrawCb);
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (clock != null) {
                clock.stop();
            }
        } finally {
            super.finalize();
        }
    }

    private Handler<DoMoveEvent, Boolean> moveCb = (DoMoveEvent e) -> {
        if (!isStarted) {
            return false;
        }

        return doMove(e.getSource(), e.getMove(), e.shouldApply());
    };

    private boolean doMove(ChessPlayer player, String move, boolean apply) {
        if (player != getCurrentPlayer()) {
            return false;
        }

        var state = getCurrentState().clone();
        state.number++;
        if (!state.move(move, apply)) {
            return false;
        }

        if (!apply) {
            return true;
        }

        moveStack.add(0, state);
        if (state.lastMove.victim != null) {
            state.lastMove.victim.died.emit(new ChessPieceEvent(state.lastMove.victim));
        }
        state.lastMove.piece.moved.emit(new ChessPieceEvent(state.lastMove.piece));
        if (state.lastMove.castlingRook != null) {
            state.lastMove.castlingRook.moved.emit(new ChessPieceEvent(state.lastMove.castlingRook));
        }
        moved.emit(new MovedEvent(this, state.lastMove));
        completeMove();

        return true;
    }

    public void addHold() {
        holdCount++;
    }

    public void removeHold() {
        if (holdCount > 0) {
            return;
        }

        holdCount--;
        if (holdCount == 0) {
            completeMove();
        }
    }

    private void completeMove() {
        /* Wait until the hold is removed */
        if (holdCount > 0) {
            return;
        }

        if (!isStarted) {
            return;
        }

        Out<ChessRule> rule = new Out<>();
        var result = getCurrentState().getResult(rule);
        if (result != ChessResult.IN_PROGRESS) {
            stop(result, rule.value);
            return;
        }

        if (isFiveFoldRepeat()) {
            stop(ChessResult.DRAW, ChessRule.FIVE_FOLD_REPETITION);
            return;
        }

        /*
         * Note this test must occur after the test for checkmate in
         * current_state.get_result ().
         */
        if (isSeventyFiveMoveRuleFulfilled()) {
            stop(ChessResult.DRAW, ChessRule.SEVENTY_FIVE_MOVES);
            return;
        }

        if (clock != null) {
            clock.setActiveColor(getCurrentPlayer().color);
        }
        turnStarted.emit(new TurnStartedEvent(this, getCurrentPlayer()));
    }

    private Handler<ChessPlayerEvent, NullType> undoCb = (ChessPlayerEvent e) -> {
        undo(e.getSource());
        return null;
    };

    private void undo(ChessPlayer player) {
        /* If this players turn undo their opponents move first */
        if (player == getCurrentPlayer()) {
            undo(getOpponent());
        }

        /* Don't pop off starting move */
        if (moveStack.size() == 1) {
            return;
        }

        /* Pop off the move state */
        moveStack.remove(moveStack.get(moveStack.size() - 1));

        /* Restart the game if undo was done after end of the game */
        if (result != ChessResult.IN_PROGRESS) {
            result = ChessResult.IN_PROGRESS;
            start();
        }

        /* Notify */
        undo.emit(new ChessGameEvent(this));
    }

    private Handler<ChessPlayerEvent, Boolean> resignCb = (ChessPlayerEvent e) -> {
        if (!isStarted) {
            return false;
        }

        if (e.getSource().color == Color.WHITE) {
            stop(ChessResult.BLACK_WON, ChessRule.RESIGN);
        } else {
            stop(ChessResult.WHITE_WON, ChessRule.RESIGN);
        }

        return true;
    };

    private int stateRepeatedTimes(ChessState s1) {
        var count = 1;

        for (var s2 : moveStack) {
            if (s1 != s2 && s1.equals(s2)) {
                count++;
            }
        }

        return count;
    }

    public boolean isThreeFoldRepeat() {
        var repeated = stateRepeatedTimes(getCurrentState());
        return repeated == 3 || repeated == 4;
    }

    public boolean isFiveFoldRepeat() {
        return stateRepeatedTimes(getCurrentState()) >= 5;
    }

    public boolean isFiftyMoveRuleFulfilled() {
        /* Fifty moves *per player* without capture or pawn advancement */
        return getCurrentState().halfmoveClock >= 100 && getCurrentState().halfmoveClock < 150;
    }

    public boolean isSeventyFiveMoveRuleFulfilled() {
        /* 75 moves *per player* without capture or pawn advancement */
        return getCurrentState().halfmoveClock >= 150;
    }

    public boolean canClaimDraw() {
        return isFiftyMoveRuleFulfilled() || isThreeFoldRepeat();
    }

    private Handler<ChessPlayerEvent, NullType> claimDrawCb = (ChessPlayerEvent e) -> {
        if (!canClaimDraw()) {
            return null;
        }

        if (isFiftyMoveRuleFulfilled()) {
            stop(ChessResult.DRAW, ChessRule.FIFTY_MOVES);
        } else if (isThreeFoldRepeat()) {
            stop(ChessResult.DRAW, ChessRule.THREE_FOLD_REPETITION);
        }
        return null;
    };

    public void start() {
        if (result != ChessResult.IN_PROGRESS) {
            return;
        }

        if (isStarted) {
            return;
        }
        isStarted = true;

        if (clock != null) {
            clock.expired.connect(clockExpiredCb);
            clock.setActiveColor(getCurrentPlayer().color);
        }

        turnStarted.emit(new TurnStartedEvent(this, getCurrentPlayer()));
    }

    private Handler<ChessClockEvent, NullType> clockExpiredCb = (ChessClockEvent e) -> {
        if (clock.getWhiteRemainingSeconds() == 0) {
            stop(ChessResult.BLACK_WON, ChessRule.TIMEOUT);
        } else if (clock.getBlackRemainingSeconds() <= 0) {
            stop(ChessResult.WHITE_WON, ChessRule.TIMEOUT);
        }
        return null;
    };

    public ChessPiece getPiece(int rank, int file) {
        return getPiece(rank, file, -1);
    }

    public ChessPiece getPiece(int rank, int file, int moveNumber) {
        if (moveNumber < 0) {
            moveNumber += (int) moveStack.size();
        }

        var state = moveStack.get(moveStack.size() - moveNumber - 1);

        return state.board[ChessState.getIndex(rank, file)];
    }

    public int getNMoves() {
        return moveStack.size() - 1;
    }

    public void pause() {
        pause(true);
    }

    public void pause(boolean showOverlay) {
        if (clock != null && result == ChessResult.IN_PROGRESS && !isPaused) {
            clock.pause();
            isPaused = true;
            shouldShowPausedOverlay = showOverlay;
            paused.emit(new ChessGameEvent(this));
        }
    }

    public void unpause() {
        if (clock != null && result == ChessResult.IN_PROGRESS && isPaused) {
            clock.unpause();
            isPaused = false;
            shouldShowPausedOverlay = false;
            unpaused.emit(new ChessGameEvent(this));
        }
    }

    public void stop(ChessResult result, ChessRule rule) {
        if (!isStarted) {
            return;
        }
        this.result = result;
        this.rule = rule;
        isStarted = false;
        if (clock != null) {
            clock.stop();
        }
        ended.emit(new ChessGameEvent(this));
    }

}
