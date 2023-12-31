package jensostertag.pushserver.event.websocket;

import jensostertag.pushserver.event.Event;
import jensostertag.pushserver.main.WebSocketMessageQueue;
import jensostertag.pushserver.objects.WebSocketChannel;

import java.util.List;
import java.util.UUID;

public class PushMessageEvent extends Event {
    private final WebSocketChannel _webSocketChannel;
    private final String _message;
    private final List<UUID> _recipients;
    private WebSocketMessageQueue _webSocketMessageQueue = null;

    public PushMessageEvent(WebSocketChannel webSocketChannel, String message, List<UUID> recipients) {
        this._webSocketChannel = webSocketChannel;
        this._message = message;
        this._recipients = recipients;
    }

    public WebSocketChannel getWebSocketChannel() {
        return _webSocketChannel;
    }

    public String getMessage() {
        return _message;
    }

    public List<UUID> getRecipients() {
        return _recipients;
    }

    public WebSocketMessageQueue getWebSocketMessageQueue() {
        return _webSocketMessageQueue;
    }

    @Deprecated
    public void setWebSocketMessageQueue(WebSocketMessageQueue webSocketMessageQueue) {
        this._webSocketMessageQueue = webSocketMessageQueue;
    }
}
