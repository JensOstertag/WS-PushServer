package jensostertag.pushserver.eventhandler;

import jensostertag.pushserver.event.EventHandler;
import jensostertag.pushserver.event.Listener;
import jensostertag.pushserver.event.websocket.ConnectEvent;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.exceptions.NoUuidAvailableException;
import jensostertag.pushserver.main.WebSocketMessageQueue;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.UnregisteredWebSocketMessage;
import jensostertag.pushserver.objects.WebSocketMessage;
import jensostertag.pushserver.protocol.MessageCreator;

public class ConnectHandler implements Listener {
    @EventHandler
    public void onConnect(ConnectEvent event) {
        try {
            Client client = new Client(event.getWebSocket());

            String jsonMessage = MessageCreator.clientAck(client, 200, "Connected");
            WebSocketMessageQueue.getInstance().queueMessage(client, jsonMessage, null);
        } catch(NoUuidAvailableException e) {
            try {
                String jsonMessage = MessageCreator.error(503, "No UUID available", null);
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
