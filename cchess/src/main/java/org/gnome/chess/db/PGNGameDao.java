package org.gnome.chess.db;

import static org.gnome.chess.util.Logging.warning;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.gnome.chess.lib.PGNGame;

public class PGNGameDao implements Dao<PGNGame> {

    private Connection conn;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            warning("%s", e.getMessage());
        }
    }

    public PGNGameDao() {
    }

    public PGNGameDao(String url) throws SQLException {
        connect(url);
    }

    @Override
    public boolean connect(String url) throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + url);
        return executeInsideTransaction(connectInsideTransaction, Void.TYPE);
    }

    private static SQLBiFunction<Connection, Class<Void>, Boolean> connectInsideTransaction = (Connection conn,
            Class<Void> empty) -> {
        String sql = "CREATE TABLE IF NOT EXISTS pgn (id INTEGER PRIMARY KEY AUTOINCREMENT, event TEXT NOT NULL, site TEXT NOT NULL, date TEXT NOT NULL, time TEXT, round TEXT NOT NULL, white TEXT NOT NULL, black TEXT NOT NULL, result TEXT NOT NULL, annotator TEXT, timeControl TEXT, whiteTimeLeft TEXT, blackTimeLeft TEXT, clockType TEXT, timerIncrement TEXT, setUp TEXT, fen TEXT, termination TEXT, whiteAi TEXT, whiteLevel TEXT, blackAi TEXT, blackLevel TEXT)";
        PreparedStatement smt = conn.prepareStatement(sql);
        return smt.execute();
    };

    @Override
    public Optional<PGNGame> get(int id) throws SQLException {
        return executeInsideTransaction(getInsideTransaction, id);
    }

    private static SQLBiFunction<Connection, Integer, Optional<PGNGame>> getInsideTransaction = (Connection conn,
            Integer id) -> {
        String sql = "SELECT * FROM pgn WHERE id = ?";
        PreparedStatement smt = conn.prepareStatement(sql);
        smt.setInt(1, id);
        ResultSet result = smt.executeQuery();
        Optional<PGNGame> game = Optional.empty();
        if (result.next()) {
            PGNGame pgnGame = new PGNGame(result.getInt("id"));
            game = Optional.of(pgnGame);
            pgnGame.setEvent(result.getString("event"));
            pgnGame.setSite(result.getString("site"));
            pgnGame.setDate(result.getString("date"));
            pgnGame.setTime(result.getString("time"));
            pgnGame.setRound(result.getString("round"));
            pgnGame.setWhite(result.getString("white"));
            pgnGame.setBlack(result.getString("black"));
            pgnGame.setResult(result.getString("result"));
            pgnGame.setAnnotator(result.getString("annotator"));
            pgnGame.setTimeControl(result.getString("timeControl"));
            pgnGame.setWhiteTimeLeft(result.getString("whiteTimeLeft"));
            pgnGame.setBlackTimeLeft(result.getString("blackTimeLeft"));
            pgnGame.setClockType(result.getString("clockType"));
            pgnGame.setTimerIncrement(result.getString("timerIncrement"));
            pgnGame.setSetUp(result.getString("setUp").equals("1") ? true : false);
            pgnGame.setFen(result.getString("fen"));
            pgnGame.setTermination(result.getString("termination"));
            pgnGame.setWhiteAi(result.getString("whiteAi"));
            pgnGame.setWhiteLevel(result.getString("whiteLevel"));
            pgnGame.setBlackAi(result.getString("blackAi"));
            pgnGame.setBlackLevel(result.getString("blackLevel"));
        }
        return game;
    };

    @Override
    public List<PGNGame> getAll() throws SQLException {
        return executeInsideTransaction(getAllInsideTransaction, Void.TYPE);
    }

    private static SQLBiFunction<Connection, Class<Void>, List<PGNGame>> getAllInsideTransaction = (Connection conn,
            Class<Void> empty) -> {
        String sql = "SELECT * FROM pgn";
        PreparedStatement smt = conn.prepareStatement(sql);
        ResultSet result = smt.executeQuery();
        List<PGNGame> games = new ArrayList<>();
        while (result.next()) {
            PGNGame pgnGame = new PGNGame(result.getInt("id"));
            games.add(pgnGame);
            pgnGame.setEvent(result.getString("event"));
            pgnGame.setSite(result.getString("site"));
            pgnGame.setDate(result.getString("date"));
            pgnGame.setTime(result.getString("time"));
            pgnGame.setRound(result.getString("round"));
            pgnGame.setWhite(result.getString("white"));
            pgnGame.setBlack(result.getString("black"));
            pgnGame.setResult(result.getString("result"));
            pgnGame.setAnnotator(result.getString("annotator"));
            pgnGame.setTimeControl(result.getString("timeControl"));
            pgnGame.setWhiteTimeLeft(result.getString("whiteTimeLeft"));
            pgnGame.setBlackTimeLeft(result.getString("blackTimeLeft"));
            pgnGame.setClockType(result.getString("clockType"));
            pgnGame.setTimerIncrement(result.getString("timerIncrement"));
            pgnGame.setSetUp(result.getString("setUp").equals("1") ? true : false);
            pgnGame.setFen(result.getString("fen"));
            pgnGame.setTermination(result.getString("termination"));
            pgnGame.setWhiteAi(result.getString("whiteAi"));
            pgnGame.setWhiteLevel(result.getString("whiteLevel"));
            pgnGame.setBlackAi(result.getString("blackAi"));
            pgnGame.setBlackLevel(result.getString("blackLevel"));
        }
        return games;
    };

    @Override
    public void save(PGNGame data) throws SQLException {
        executeInsideTransaction(saveInsideTransaction, data);
    }

    private static SQLBiFunction<Connection, PGNGame, Class<Void>> saveInsideTransaction = (Connection conn,
            PGNGame data) -> {
        String sql = "INSERT INTO pgn VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement smt = conn.prepareStatement(sql);
        smt.setInt(1, data.getId());
        smt.setString(2, data.getEvent());
        smt.setString(3, data.getSite());
        smt.setString(4, data.getDate());
        smt.setString(5, data.getTime());
        smt.setString(6, data.getRound());
        smt.setString(7, data.getWhite());
        smt.setString(8, data.getBlack());
        smt.setString(9, data.getResult());
        smt.setString(10, data.getAnnotator());
        smt.setString(11, data.getTimeControl());
        smt.setString(12, data.getWhiteTimeLeft());
        smt.setString(13, data.getBlackTimeLeft());
        smt.setString(14, data.getClockType());
        smt.setString(15, data.getTimerIncrement());
        smt.setString(16, data.getSetUp() ? "1" : "0");
        smt.setString(17, data.getFen());
        smt.setString(18, data.getTermination());
        smt.setString(19, data.getWhiteAi());
        smt.setString(20, data.getWhiteLevel());
        smt.setString(21, data.getBlackAi());
        smt.setString(22, data.getBlackLevel());
        smt.executeUpdate();
        return Void.TYPE;
    };

    @Override
    public void saveAll(List<PGNGame> data) throws SQLException {
        executeInsideTransaction(saveAllInsideTransaction, data);
    }

    private static SQLBiFunction<Connection, List<PGNGame>, Class<Void>> saveAllInsideTransaction = (Connection conn,
            List<PGNGame> data) -> {
        for (PGNGame pgnGame : data) {
            saveInsideTransaction.accept(conn, pgnGame);
        }
        return Void.TYPE;
    };

    @Override
    public void update(PGNGame data) throws SQLException {
        executeInsideTransaction(updateInsideTransaction, data);
    }

    private static SQLBiFunction<Connection, PGNGame, Class<Void>> updateInsideTransaction = (Connection conn,
            PGNGame data) -> {
        String sql = "UPDATE pgn SET event = ?, site = ?, date = ?, time = ?, round = ?, white = ?, black = ?, result = ?, annotator = ?, timeControl = ?, whiteTimeLeft = ?, blackTimeLeft = ?, clockType = ?, timerIncrement = ?, setUp = ?, fen = ?, termination = ?, whiteAi = ?, whiteLevel = ?. blackAi = ?, blackLevel = ? WHERE id = ?";
        PreparedStatement smt = conn.prepareStatement(sql);
        smt.setString(1, data.getEvent());
        smt.setString(2, data.getSite());
        smt.setString(3, data.getDate());
        smt.setString(4, data.getTime());
        smt.setString(5, data.getRound());
        smt.setString(6, data.getWhite());
        smt.setString(7, data.getBlack());
        smt.setString(8, data.getResult());
        smt.setString(9, data.getAnnotator());
        smt.setString(10, data.getTimeControl());
        smt.setString(11, data.getWhiteTimeLeft());
        smt.setString(12, data.getBlackTimeLeft());
        smt.setString(13, data.getClockType());
        smt.setString(14, data.getTimerIncrement());
        smt.setString(15, data.getSetUp() ? "1" : "0");
        smt.setString(16, data.getFen());
        smt.setString(17, data.getTermination());
        smt.setString(18, data.getWhiteAi());
        smt.setString(19, data.getWhiteLevel());
        smt.setString(20, data.getBlackAi());
        smt.setString(21, data.getBlackLevel());
        smt.setInt(22, data.getId());
        smt.executeUpdate();
        return Void.TYPE;
    };

    @Override
    public void delete(PGNGame data) throws SQLException {
        executeInsideTransaction(deleteInsideTransaction, data);
    }

    private static SQLBiFunction<Connection, PGNGame, Class<Void>> deleteInsideTransaction = (Connection conn,
            PGNGame data) -> {
        String sql = "DELETE FROM pgn WHERE id = ?";
        PreparedStatement smt = conn.prepareStatement(sql);
        smt.setInt(1, data.getId());
        smt.executeUpdate();
        return Void.TYPE;
    };

    private <T, R> R executeInsideTransaction(SQLBiFunction<Connection, T, R> action, T data) throws SQLException {
        R value = null;
        conn.setAutoCommit(false);
        try {
            value = action.accept(conn, data);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
        return value;
    }

}
