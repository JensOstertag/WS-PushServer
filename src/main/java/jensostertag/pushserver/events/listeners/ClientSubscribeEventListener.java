package jensostertag.pushserver.events.listeners;

import jensostertag.pushserver.events.ClientSubscribeEvent;

public interface ClientSubscribeEventListener {
    void onClientSubscribe(ClientSubscribeEvent event);
}
