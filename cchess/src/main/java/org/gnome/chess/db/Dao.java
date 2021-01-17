package org.gnome.chess.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    public boolean connect(String url) throws SQLException;

    public Optional<T> get(int id) throws SQLException;

    public List<T> getAll() throws SQLException;

    public int save(T data) throws SQLException;

    public int saveAll(List<T> data) throws SQLException;

    public int update(T data) throws SQLException;

    public int delete(T data) throws SQLException;

}
