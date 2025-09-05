package org.ccrew.cchess.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLBiFunction<T, U> {

    public U accept(PreparedStatement smt, T data) throws SQLException;

}
