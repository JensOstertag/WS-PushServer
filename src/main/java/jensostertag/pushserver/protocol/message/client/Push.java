package jensostertag.pushserver.protocol.message.client;

import com.google.gson.annotations.Expose;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.WebSocketChannel;

import java.util.List;

public class Push {
    @Expose
    public String uuid;
    @Expose
    public List<String> subscribedChannels;
    @Expose
    public Object pushMessage;

    public Push(Client client, Object pushMessage) {
        this.uuid = client.getUuid().toString();
        this.subscribedChannels = client.getSubscribedChannels().stream().map(WebSocketChannel::getName).toList();
        this.pushMessage = pushMessage;
    }
}
