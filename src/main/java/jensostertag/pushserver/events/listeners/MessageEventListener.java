package jensostertag.pushserver.events.listeners;

import jensostertag.pushserver.events.MessageEvent;

public interface MessageEventListener {
    void onMessage(MessageEvent event);
}
