package jensostertag.pushserver.eventhandler;

import jensostertag.pushserver.event.EventHandler;
import jensostertag.pushserver.event.Listener;
import jensostertag.pushserver.event.websocket.ClientUnsubscribeEvent;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.main.WebSocketMessageQueue;
import jensostertag.pushserver.protocol.MessageCreator;

public class ClientUnsubscribeHandler implements Listener {
    @EventHandler
    public void onUnsubscribe(ClientUnsubscribeEvent event) {
        event.getClient().unsubscribeFromChannel(event.getWebSocketChannel());

        try {
            String jsonMessage = MessageCreator.clientAck(event.getClient(), 200, "Unsubscribed");
            WebSocketMessageQueue.getInstance().queueMessage(event.getClient(), jsonMessage, null);
        } catch(InvalidMessageException e) {
            // Catch block left empty on purpose
            // This exception is already handled by the MessageCreator
        }
    }
}
