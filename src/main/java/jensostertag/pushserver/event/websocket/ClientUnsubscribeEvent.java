package jensostertag.pushserver.event.websocket;

import jensostertag.pushserver.event.Event;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.WebSocketChannel;

public class ClientUnsubscribeEvent extends Event {
    private final Client _client;
    private final WebSocketChannel _webSocketChannel;

    public ClientUnsubscribeEvent(Client client, WebSocketChannel webSocketChannel) {
        this._client = client;
        this._webSocketChannel = webSocketChannel;
    }

    public Client getClient() {
        return this._client;
    }

    public WebSocketChannel getWebSocketChannel() {
        return this._webSocketChannel;
    }
}
