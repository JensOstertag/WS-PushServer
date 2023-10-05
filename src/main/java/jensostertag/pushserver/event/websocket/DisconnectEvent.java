package jensostertag.pushserver.event.websocket;

import jensostertag.pushserver.event.Event;
import org.java_websocket.WebSocket;

public class DisconnectEvent extends Event {
    private final WebSocket _webSocket;

    public DisconnectEvent(WebSocket webSocket) {
        this._webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return this._webSocket;
    }
}
