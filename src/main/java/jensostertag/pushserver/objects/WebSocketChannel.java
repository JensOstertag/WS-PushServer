package jensostertag.pushserver.objects;

import java.util.HashMap;

public class WebSocketChannel {
    private static final HashMap<String, WebSocketChannel> CHANNELS = new HashMap<>();

    private final String _name;

    public WebSocketChannel(String name) {
        this._name = name;
    }

    public String getName() {
        return this._name;
    }

    public static WebSocketChannel getWebSocketChannel(String name) {
        if(!WebSocketChannel.CHANNELS.containsKey(name)) {
            WebSocketChannel.CHANNELS.put(name, new WebSocketChannel(name));
        }

        return WebSocketChannel.CHANNELS.get(name);
    }
}
