package org.gnome.Chess.util;

import java.io.Serializable;
import java.util.EventListener;
import java.util.EventObject;

@FunctionalInterface
public interface Handler<T extends EventObject, S> extends EventListener, Serializable {

    public S handle(T e);

}
