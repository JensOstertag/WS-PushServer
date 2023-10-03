package jensostertag.pushserver.objects;

import org.java_websocket.WebSocket;

public class UnregisteredWebSocketMessage extends WebSocketMessage {
    private final WebSocket _webSocket;

    public UnregisteredWebSocketMessage(WebSocket webSocket, String message) {
        super(null, message);
        this._webSocket = webSocket;
    }

    public WebSocket getWebSocket() {
        return this._webSocket;
    }
}
