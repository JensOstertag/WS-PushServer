package jensostertag.pushserver.protocol.message.server;

import com.google.gson.annotations.Expose;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.WebSocketChannel;

import java.util.List;

public class Sysadmin {
    @Expose
    public List<String> channels;
    @Expose
    public int channelsAmount;
    @Expose
    public int clientsAmount;

    public Sysadmin() {
        this.channels = WebSocketChannel.getWebSocketChannels().stream().map(WebSocketChannel::getName).toList();
        this.channelsAmount = this.channels.size();
        this.clientsAmount = Client.getClients().size();
    }
}
