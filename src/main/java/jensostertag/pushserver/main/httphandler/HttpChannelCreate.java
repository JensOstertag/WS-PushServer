package jensostertag.pushserver.main.httphandler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.objects.WebSocketChannel;
import jensostertag.pushserver.protocol.MessageCreator;
import jensostertag.pushserver.protocol.MessageType;
import jensostertag.pushserver.protocol.MessageValidator;

import java.io.IOException;
import java.io.OutputStream;

public class HttpChannelCreate implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        int responseCode = -1;

        StringBuilder requestBodyBuilder = new StringBuilder();
        int i;
        while((i = httpExchange.getRequestBody().read()) != -1) {
            requestBodyBuilder.append((char) i);
        }

        String requestBody = requestBodyBuilder.toString();

        try {
            MessageType messageType = MessageValidator.getMessageType(requestBody);

            if(messageType == MessageType.SERVER_CHANNEL_CREATE) {
                JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);
                String channel = jsonObject.get("data").getAsJsonObject().get("channel").getAsString();
                WebSocketChannel webSocketChannel = WebSocketChannel.getWebSocketChannelNullable(channel);

                if(webSocketChannel != null) {
                    response = MessageCreator.error(409, "Conflict", "A WebSocketChannel called \"" + channel + "\" already exists");
                    responseCode = 409;
                } else {
                    webSocketChannel = new WebSocketChannel(channel);
                    response = MessageCreator.serverAck(webSocketChannel, true, 200, "Created");
                    responseCode = 200;
                }
            } else {
                response = MessageCreator.error(400, "Bad Request", "Invalid message type");
                responseCode = 400;
            }
        } catch(InvalidMessageException e) {
            try {
                response = MessageCreator.error(400, "Bad Request", "Invalid message");
                responseCode = 400;
            } catch(InvalidMessageException f) {
                response = "Internal Server Error";
                responseCode = 500;
            }
        }

        httpExchange.sendResponseHeaders(responseCode, response.length());

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
