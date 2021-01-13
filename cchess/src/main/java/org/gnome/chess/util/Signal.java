package org.gnome.chess.util;

import java.util.EventObject;
import java.util.HashSet;

public class Signal<T extends EventObject, S> {

    private HashSet<Handler<T, S>> handlers = new HashSet<>();

    public void connect(Handler<T, S> handler) {
        handlers.add(handler);
    }

    public void disconnect(Handler<T, S> handler) {
        handlers.remove(handler);
    }

    public S emit(T e) {
        S value = null;
        for (Handler<T, S> handler : handlers) {
            value = handler.handle(e);
        }
        return value;
    }

}
