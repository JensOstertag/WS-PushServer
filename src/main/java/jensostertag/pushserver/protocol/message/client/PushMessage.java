package jensostertag.pushserver.protocol.message.client;

import com.google.gson.annotations.Expose;

public class PushMessage {
    @Expose
    public String channel;
    @Expose
    public String message;

    public PushMessage(String channel, String message) {
        this.channel = channel;
        this.message = message;
    }
}
