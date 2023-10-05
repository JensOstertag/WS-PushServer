package jensostertag.pushserver.protocol.message.server;

import com.google.gson.annotations.Expose;
import jensostertag.pushserver.objects.WebSocketChannel;

public class ACK {
    @Expose
    public String channel;
    @Expose
    public String channelToken;

    public ACK(WebSocketChannel channel, boolean includeToken) {
        this.channel = channel.getName();
        this.channelToken = includeToken ? channel.getToken() : null;
    }
}
