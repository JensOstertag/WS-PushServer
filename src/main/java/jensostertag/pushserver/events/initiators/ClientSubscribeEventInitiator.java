package jensostertag.pushserver.events.initiators;

import jensostertag.pushserver.events.ClientSubscribeEvent;
import jensostertag.pushserver.events.listeners.ClientSubscribeEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientSubscribeEventInitiator {
    private static final List<ClientSubscribeEventListener> LISTENERS = new ArrayList<>();

    public static void registerListener(ClientSubscribeEventListener listener) {
        LISTENERS.add(listener);
    }

    public static void trigger(ClientSubscribeEvent event) {
        for(ClientSubscribeEventListener listener : LISTENERS) {
            listener.onClientSubscribe(event);
        }
    }
}
