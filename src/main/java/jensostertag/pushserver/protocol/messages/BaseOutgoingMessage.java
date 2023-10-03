package jensostertag.pushserver.protocol.messages;

import com.google.gson.annotations.Expose;
import jensostertag.pushserver.protocol.MessageType;

public class BaseOutgoingMessage {
    @Expose
    public String messageType;
    @Expose
    public int code;
    @Expose
    public String message;
    @Expose
    public Object data;

    public BaseOutgoingMessage(MessageType messageType, int code, String message, Object data) {
        this.messageType = messageType.toString();
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
