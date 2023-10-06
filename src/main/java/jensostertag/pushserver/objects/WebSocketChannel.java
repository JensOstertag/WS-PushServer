package jensostertag.pushserver.objects;

import jensostertag.pushserver.exceptions.WebSocketChannelNotFoundException;
import jensostertag.pushserver.util.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WebSocketChannel {
    private static final HashMap<String, WebSocketChannel> CHANNELS = new HashMap<>();

    private final String _name;
    private final String _token;
    private final List<Client> _clients;

    public WebSocketChannel(String name) {
        this._name = name;
        this._token = Token.randomToken();
        this._clients = new ArrayList<>();

        WebSocketChannel.CHANNELS.put(name, this);
    }

    public String getName() {
        return this._name;
    }

    public String getToken() {
        return this._token;
    }

    public List<Client> getClients() {
        return this._clients.stream().toList();
    }

    public void subscribe(Client client) {
        this._clients.add(client);
    }

    public void unsubscribe(Client client) {
        this._clients.remove(client);
    }

    public void destroy() {
        this._clients.stream().toList().forEach(client -> client.unsubscribeFromChannel(this));

        WebSocketChannel.CHANNELS.remove(this._name);
    }

    public static WebSocketChannel getWebSocketChannel(String name) throws WebSocketChannelNotFoundException {
        if(!WebSocketChannel.CHANNELS.containsKey(name)) {
            throw new WebSocketChannelNotFoundException("Could not find a WebSocketChannel called \"" + name + "\"");
        }

        return WebSocketChannel.CHANNELS.get(name);
    }

    public static WebSocketChannel getWebSocketChannelNullable(String name) {
        try {
            return WebSocketChannel.getWebSocketChannel(name);
        } catch(WebSocketChannelNotFoundException e) {
            return null;
        }
    }

    public static List<WebSocketChannel> getWebSocketChannels() {
        return WebSocketChannel.CHANNELS.values().stream().toList();
    }
}
