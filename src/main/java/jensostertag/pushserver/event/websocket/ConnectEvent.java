package jensostertag.pushserver.event.websocket;

import jensostertag.pushserver.event.Event;
import org.java_websocket.WebSocket;

public class ConnectEvent extends Event {
    private final WebSocket _webSocket;

    public ConnectEvent(WebSocket webSocket) {
        this._webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return this._webSocket;
    }
}
