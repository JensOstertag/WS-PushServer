package jensostertag.pushserver.protocol.messages.clients;

import com.google.gson.annotations.Expose;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.WebSocketChannel;

import java.util.List;

public class ACK {
    @Expose
    public String uuid;
    @Expose
    public List<String> subscribedChannels;

    public ACK(Client client) {
        this.uuid = client.getUuid().toString();
        this.subscribedChannels = client.getSubscribedChannels().stream().map(WebSocketChannel::getName).toList();
    }
}
