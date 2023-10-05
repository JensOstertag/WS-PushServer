package jensostertag.pushserver.eventhandler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jensostertag.pushserver.event.Event;
import jensostertag.pushserver.event.EventHandler;
import jensostertag.pushserver.event.EventInitiator;
import jensostertag.pushserver.event.Listener;
import jensostertag.pushserver.event.websocket.ClientSubscribeEvent;
import jensostertag.pushserver.event.websocket.ClientUnsubscribeEvent;
import jensostertag.pushserver.event.websocket.MessageEvent;
import jensostertag.pushserver.exceptions.ClientNotFoundException;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.main.PermissionHandler;
import jensostertag.pushserver.main.WebSocketMessageQueue;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.UnregisteredWebSocketMessage;
import jensostertag.pushserver.objects.WebSocketChannel;
import jensostertag.pushserver.objects.WebSocketMessage;
import jensostertag.pushserver.protocol.MessageCreator;
import jensostertag.pushserver.protocol.MessageType;

import java.util.UUID;

public class MessageHandler implements Listener {
    @EventHandler
    public void onMessage(MessageEvent event) {
        JsonObject json = new Gson().fromJson(event.getMessage(), JsonObject.class);

        try {
            switch(event.getMessageType()) {
                case ERROR, CLIENT_ACK, CLIENT_PUSH -> {}
                case CLIENT_SUBSCRIBE_CHANNEL, CLIENT_UNSUBSCRIBE_CHANNEL -> {
                    String uuidString = json.get("uuid").getAsString();
                    UUID uuid = UUID.fromString(uuidString);
                    String channel = json.get("data").getAsJsonObject().get("channel").getAsString();
                    WebSocketChannel webSocketChannel = WebSocketChannel.getWebSocketChannel(channel);

                    if(!PermissionHandler.hasPermission(uuid, event.getWebSocket())) {
                        String jsonMessage = MessageCreator.error(403, "Forbidden", "The given UUID does not belong to this WebSocket connection");
                        WebSocketMessage webSocketMessage = new UnregisteredWebSocketMessage(event.getWebSocket(), jsonMessage);
                        WebSocketMessageQueue.getInstance().queueMessage(webSocketMessage);
                    }

                    Client client = Client.getClient(uuid);

                    Event delegateEvent;
                    if(event.getMessageType() == MessageType.CLIENT_SUBSCRIBE_CHANNEL) {
                        delegateEvent = new ClientSubscribeEvent(client, webSocketChannel);
                    } else {
                        delegateEvent = new ClientUnsubscribeEvent(client, webSocketChannel);
                    }
                    EventInitiator.trigger(delegateEvent);
                }
            }
        } catch(ClientNotFoundException e) {
            try {
                String jsonMessage = MessageCreator.error(403, "Forbidden", "The given UUID does not belong to any WebSocket connection");
                WebSocketMessage webSocketMessage = new UnregisteredWebSocketMessage(event.getWebSocket(), jsonMessage);
                WebSocketMessageQueue.getInstance().queueMessage(webSocketMessage);
            } catch(InvalidMessageException f) {
                // Catch block left empty on purpose
                // This exception is already handled by the MessageCreator
            }
        } catch(InvalidMessageException e) {
            // Catch block left empty on purpose
            // This exception is already handled by the MessageCreator
        }
    }
}
