package jensostertag.pushserver.objects;

public class WebSocketMessage {
    private final Client _client;
    private final String _message;

    public WebSocketMessage(Client client, String message) {
        this._client = client;
        this._message = message;
    }

    public String getMessage() {
        return this._message;
    }

    public Client getClient() {
        return this._client;
    }
}
