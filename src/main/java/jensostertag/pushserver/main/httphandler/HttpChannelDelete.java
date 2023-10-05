package jensostertag.pushserver.main.httphandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.main.PermissionHandler;
import jensostertag.pushserver.objects.WebSocketChannel;
import jensostertag.pushserver.protocol.MessageCreator;
import jensostertag.pushserver.protocol.MessageType;
import jensostertag.pushserver.protocol.MessageValidator;

import java.io.IOException;
import java.io.OutputStream;

public class HttpChannelDelete implements HttpHandler {
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
            MessageType messageType = null;
            try {
                messageType = MessageValidator.getMessageType(requestBody);

                if(messageType != MessageType.SERVER_CHANNEL_DELETE) {
                    throw new InvalidMessageException("Bad Request");
                }
            } catch(InvalidMessageException e) {
                response = MessageCreator.error(400, "Bad Request", "Invalid message");
                responseCode = 400;
            } catch(JsonProcessingException e) {
                response = MessageCreator.error(400, "Bad Request", requestBody);
                responseCode = 400;
            }

            if(messageType == MessageType.SERVER_CHANNEL_DELETE) {
                JsonObject jsonObject = new Gson().fromJson(requestBody, JsonObject.class);
                String channel = jsonObject.get("data").getAsJsonObject().get("channel").getAsString();
                WebSocketChannel webSocketChannel = WebSocketChannel.getWebSocketChannelNullable(channel);
                String channelToken;
                if(jsonObject.get("data").getAsJsonObject().get("channelToken").isJsonNull()) {
                    channelToken = null;
                } else {
                    channelToken = jsonObject.get("data").getAsJsonObject().get("channelToken").getAsString();
                }

                if(webSocketChannel == null) {
                    response = MessageCreator.error(404, "Not found", "Could not find a WebSocketChannel called \"" + channel + "\"");
                    responseCode = 404;
                } else if(!PermissionHandler.hasPermission(channelToken, webSocketChannel)) {
                    response = MessageCreator.error(403, "Forbidden", "Channel token is invalid");
                    responseCode = 403;
                } else {
                    webSocketChannel.destroy();
                    response = MessageCreator.serverAck(webSocketChannel, false, 200, "Deleted");
                    responseCode = 200;
                }
            }
        } catch(InvalidMessageException e) {
            try {
                response = MessageCreator.error(500, "Internal Server Error", null);
                responseCode = 500;
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
