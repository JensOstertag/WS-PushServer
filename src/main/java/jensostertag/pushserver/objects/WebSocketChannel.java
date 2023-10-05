package jensostertag.pushserver.objects;

import jensostertag.pushserver.exceptions.WebSocketChannelNotFoundException;
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
