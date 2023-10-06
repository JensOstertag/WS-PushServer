package jensostertag.pushserver.objects;

public class WebSocketMessage {
    private final Client _client;
    private final String _message;
    private final WebSocketChannel _webSocketChannel;

    public WebSocketMessage(Client client, String message, WebSocketChannel webSocketChannel) {
        this._client = client;
        this._message = message;
        this._webSocketChannel = webSocketChannel;
    }

    public String getMessage() {
        return this._message;
    }

    public Client getClient() {
        return this._client;
    }

    public WebSocketChannel getWebSocketChannel() {
        return this._webSocketChannel;
    }
}
