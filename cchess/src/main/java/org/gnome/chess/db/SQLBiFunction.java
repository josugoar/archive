package org.gnome.chess.db;

import java.sql.SQLException;

@FunctionalInterface
public interface SQLBiFunction<T, U, R> {

    public R accept(T t, U u) throws SQLException;

}
