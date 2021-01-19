package org.ccrew.cchess.db;

import static org.ccrew.cchess.util.Logging.warning;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.ccrew.cchess.lib.PGNGame;

public class PGNGameDao implements Dao<PGNGame> {

    public Connection conn;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            warning("%s", e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            new PGNGameDao("pgn.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PGNGameDao() {
    }

    public PGNGameDao(String url) throws SQLException {
        connect(url);
    }

    private String getSql(String name) {
        try {
            URL resource = getClass().getClassLoader().getResource("queries/" + name);
            return new String(resource.openStream().readAllBytes());
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean connect(String url) throws SQLException {
        conn = DriverManager.getConnection("jdbc:sqlite:" + url);
        return executeInsideTransaction(connectInsideTransaction, Void.TYPE, getSql("connect.sql"));
    }

    private static SQLBiFunction<Class<Void>, Boolean> connectInsideTransaction = (PreparedStatement smt,
            Class<Void> empty) -> {
        return smt.execute();
    };

    @Override
    public Optional<PGNGame> get(int id) throws SQLException {
        return executeInsideTransaction(getInsideTransaction, id, getSql("get.sql"));
    }

    private static SQLBiFunction<Integer, Optional<PGNGame>> getInsideTransaction = (PreparedStatement smt,
            Integer id) -> {
        smt.setInt(1, id);
        ResultSet result = smt.executeQuery();
        Optional<PGNGame> game = Optional.empty();
        if (result.next()) {
          //  System.out.println("sdAHJKL");
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
            for (String move : result.getString("movetext").split(" ")) {
                pgnGame.moves.add(move);
            }
        }
        return game;
    };

    @Override
    public List<PGNGame> getAll() throws SQLException {
        return executeInsideTransaction(getAllInsideTransaction, Void.TYPE, getSql("getAll.sql"));
    }

    private static SQLBiFunction<Class<Void>, List<PGNGame>> getAllInsideTransaction = (PreparedStatement smt,
            Class<Void> empty) -> {
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
            for (String move : result.getString("movetext").split(" ")) {
                pgnGame.moves.add(move);
            }
        }
        return games;
    };

    @Override
    public int save(PGNGame data) throws SQLException {
        return executeInsideTransaction(saveInsideTransaction, data, getSql("save.sql"));
    }

    private static SQLBiFunction<PGNGame, Integer> saveInsideTransaction = (PreparedStatement smt, PGNGame data) -> {
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
        smt.setString(23, String.join(" ", data.moves));
        return smt.executeUpdate();
    };

    @Override
    public int saveAll(List<PGNGame> data) throws SQLException {
        return executeInsideTransaction(saveAllInsideTransaction, data, getSql("saveAll.sql"));
    }

    private static SQLBiFunction<List<PGNGame>, Integer> saveAllInsideTransaction = (PreparedStatement smt,
            List<PGNGame> data) -> {
        int value = 0;
        for (PGNGame pgnGame : data) {
            value += saveInsideTransaction.accept(smt, pgnGame);
        }
        return value;
    };

    @Override
    public int update(PGNGame data) throws SQLException {
        return executeInsideTransaction(updateInsideTransaction, data, getSql("update.sql"));
    }

    private static SQLBiFunction<PGNGame, Integer> updateInsideTransaction = (PreparedStatement smt, PGNGame data) -> {
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
        smt.setString(22, String.join(" ", data.moves));
        smt.setInt(23, data.getId());
        return smt.executeUpdate();
    };

    @Override
    public int delete(PGNGame data) throws SQLException {
        return executeInsideTransaction(deleteInsideTransaction, data, getSql("delete.sql"));
    }

    private static SQLBiFunction<PGNGame, Integer> deleteInsideTransaction = (PreparedStatement smt, PGNGame data) -> {
        smt.setInt(1, data.getId());
        return smt.executeUpdate();
    };

    private <T, U> U executeInsideTransaction(SQLBiFunction<T, U> action, T data, String sql) throws SQLException {
        U value = null;
        conn.setAutoCommit(false);
        PreparedStatement smt = conn.prepareStatement(sql);
        try {
            value = action.accept(smt, data);
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
