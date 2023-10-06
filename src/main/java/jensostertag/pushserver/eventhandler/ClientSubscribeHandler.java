package jensostertag.pushserver.eventhandler;

import jensostertag.pushserver.event.EventHandler;
import jensostertag.pushserver.event.Listener;
import jensostertag.pushserver.event.websocket.ClientSubscribeEvent;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.main.WebSocketMessageQueue;
import jensostertag.pushserver.protocol.MessageCreator;

public class ClientSubscribeHandler implements Listener {
    @EventHandler
    public void onSubscribe(ClientSubscribeEvent event) {
        event.getClient().subscribeToChannel(event.getWebSocketChannel());

        try {
            String jsonMessage = MessageCreator.clientAck(event.getClient(), 200, "Subscribed");
            WebSocketMessageQueue.getInstance().queueMessage(event.getClient(), jsonMessage, null);
        } catch(InvalidMessageException e) {
            // Catch block left empty on purpose
            // This exception is already handled by the MessageCreator
        }
    }
}
