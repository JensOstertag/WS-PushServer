package jensostertag.pushserver.events.listeners;

import jensostertag.pushserver.events.ClientSubscribeEvent;

public interface ClientUnsubscribeEventListener {
    void onClientSubscribe(ClientSubscribeEvent event);
}
