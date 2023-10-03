package jensostertag.pushserver.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.protocol.MessageType;
import jensostertag.pushserver.protocol.MessageValidator;
import org.java_websocket.WebSocket;

public class MessageEvent {
    private final WebSocket _webSocket;
    private final String _message;
    private final MessageType _messageType;

    public MessageEvent(WebSocket webSocket, String message) throws InvalidMessageException, JsonProcessingException {
        this._webSocket = webSocket;
        this._message = message;
        this._messageType = MessageValidator.getMessageType(this._message);
    }

    public WebSocket getWebSocket() {
        return this._webSocket;
    }

    public String getMessage() {
        return this._message;
    }

    public MessageType getMessageType() {
        return this._messageType;
    }
}
