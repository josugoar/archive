package org.gnome.chess.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    public boolean connect(String url) throws SQLException;

    public Optional<T> get(int id) throws SQLException;

    public List<T> getAll() throws SQLException;

    public void save(T data) throws SQLException;

    public void saveAll(List<T> data) throws SQLException;

    public void update(T data) throws SQLException;

    public void delete(T data) throws SQLException;

}
