package jensostertag.pushserver.eventhandler;

import jensostertag.pushserver.event.EventHandler;
import jensostertag.pushserver.event.Listener;
import jensostertag.pushserver.event.websocket.PushMessageEvent;
import jensostertag.pushserver.exceptions.ClientNotFoundException;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.main.WebSocketMessageQueue;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.protocol.MessageCreator;
import jensostertag.pushserver.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PushMessageHandler implements Listener {
    @EventHandler
    public void onPushMessage(PushMessageEvent event) {
        List<Client> receivingClients = new ArrayList<>();
        if(event.getRecipients().isEmpty()) {
            receivingClients = event.getWebSocketChannel().getClients();
            new Logger("PushMessageHandler").log("Sending push message to all clients in channel \"" + event.getWebSocketChannel().getName() + "\"");
        }

        for(UUID uuid : event.getRecipients()) {
            try {
                Client client = Client.getClient(uuid);
                receivingClients.add(client);
            } catch(ClientNotFoundException e) {
                // Catch block left empty on purpose
                // If the client could not be found, he will not receive the message
            }
        }

        new Logger("PushMessageHandler").log("Sending push message to " + receivingClients.size() + " clients in channel \"" + event.getWebSocketChannel().getName() + "\"");

        receivingClients.forEach(client -> {
            try {
                String jsonMessage = MessageCreator.clientPush(client, 200, "Push Message", event.getWebSocketChannel().getName(), event.getMessage());
                WebSocketMessageQueue.getInstance().queueMessage(client, jsonMessage, event.getWebSocketChannel());
            } catch(InvalidMessageException e) {
                // Catch block left empty on purpose
                // If the message is invalid, the client will not receive the message
            }
        });
    }
}
