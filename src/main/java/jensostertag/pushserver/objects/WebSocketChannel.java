package jensostertag.pushserver.objects;

import jensostertag.pushserver.util.Token;

import java.util.HashMap;
import java.util.List;

public class WebSocketChannel {
    private static final HashMap<String, WebSocketChannel> CHANNELS = new HashMap<>();

    private final String _name;
    private final String _token;

    public WebSocketChannel(String name) {
        this._name = name;
        this._token = Token.randomToken();
    }

    public String getName() {
        return this._name;
    }

    public String getToken() {
        return this._token;
    }

    public static WebSocketChannel getWebSocketChannel(String name) {
        if(!WebSocketChannel.CHANNELS.containsKey(name)) {
            WebSocketChannel.CHANNELS.put(name, new WebSocketChannel(name));
        }

        return WebSocketChannel.CHANNELS.get(name);
    }

    public static List<WebSocketChannel> getWebSocketChannels() {
        return WebSocketChannel.CHANNELS.values().stream().toList();
    }
}
