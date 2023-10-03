package jensostertag.pushserver.events.initiators;

import jensostertag.pushserver.events.MessageEvent;
import jensostertag.pushserver.events.listeners.MessageEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageEventInitiator {
    private static final List<MessageEventListener> LISTENERS = new ArrayList<>();

    public static void registerListener(MessageEventListener listener) {
        LISTENERS.add(listener);
    }

    public static void trigger(MessageEvent event) {
        for(MessageEventListener listener : LISTENERS) {
            listener.onMessage(event);
        }
    }
}
