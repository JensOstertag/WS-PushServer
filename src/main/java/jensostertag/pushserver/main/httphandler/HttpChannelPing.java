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

public class HttpChannelPing implements HttpHandler {
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

            if(messageType == MessageType.SERVER_CHANNEL_PING) {
                JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);
                String channel = jsonObject.get("data").getAsJsonObject().get("channel").getAsString();
                WebSocketChannel webSocketChannel = WebSocketChannel.getWebSocketChannel(channel);
                String channelToken = jsonObject.get("data").getAsJsonObject().get("channelToken").getAsString();

                if(webSocketChannel == null) {
                    response = MessageCreator.error(404, "Not found", "Could not find a channel called \"" + channel + "\"");
                    responseCode = 404;
                } else if(channelToken == null) {
                    response = MessageCreator.error(401, "Unauthorized", "No channel token provided");
                    responseCode = 401;
                } else if(channelToken.equals(webSocketChannel.getToken())) {
                    response = MessageCreator.serverAck(webSocketChannel, false, 200, "OK");
                    responseCode = 200;
                } else {
                    response = MessageCreator.error(403, "Forbidden", "WebSocketChannel token is invalid");
                    responseCode = 403;
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
