package jensostertag.pushserver.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.protocol.messages.BaseOutgoingMessage;
import jensostertag.pushserver.protocol.messages.Error;
import jensostertag.pushserver.protocol.messages.clients.ACK;
import jensostertag.pushserver.protocol.messages.clients.Push;
import jensostertag.pushserver.util.Logger;

public class MessageCreator {
    public static String generateMessage(Object message) throws InvalidMessageException {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = gson.toJson(message);

        try {
            MessageValidator.getMessageType(json);
        } catch(JsonProcessingException e) {
            new Logger("MessageCreator").error("Generated invalid message:");
            new Logger("MessageCreator").error(json);
            throw new InvalidMessageException("Invalid message");
        }

        return json;
    }

    public static String error(int code, String message, String errorDetails) throws InvalidMessageException {
        Object data = new Error(errorDetails);
        Object outgoingMessage = new BaseOutgoingMessage(MessageType.ERROR, code, message, data);
        return MessageCreator.generateMessage(outgoingMessage);
    }

    public static String clientAck(Client client, int code, String message) throws InvalidMessageException {
        Object data = new ACK(client);
        Object outgoingMessage = new BaseOutgoingMessage(MessageType.CLIENT_ACK, code, message, data);
        return MessageCreator.generateMessage(outgoingMessage);
    }

    public static String clientPush(Client client, int code, String message, Object pushMessage) throws InvalidMessageException {
        Object data = new Push(client, pushMessage);
        Object outgoingMessage = new BaseOutgoingMessage(MessageType.CLIENT_PUSH, code, message, data);
        return MessageCreator.generateMessage(outgoingMessage);
    }
}
