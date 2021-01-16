package org.gnome.chess.db;

import java.util.List;
import java.util.Optional;

import org.gnome.chess.lib.PGN;

public class PGNDao implements Dao<PGN> {

    @Override
    public Optional<PGN> get(long id) {
        return null;
    }

    @Override
    public List<PGN> getAll() {
        return null;
    }

    @Override
    public void save(PGN t) {
    }

    @Override
    public void update(PGN t, String[] params) {
    }

    @Override
    public void delete(PGN t) {
    }

}
