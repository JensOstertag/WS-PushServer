import jensostertag.pushserver.event.EventInitiator;
import jensostertag.pushserver.event.websocket.PushMessageEvent;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.exceptions.NoUuidAvailableException;
import jensostertag.pushserver.main.WebSocketMessageQueue;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.WebSocketChannel;
import jensostertag.pushserver.protocol.MessageCreator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageTest {
    @Test
    public void sendToAllInChannel() throws NoUuidAvailableException, InvalidMessageException {
        TestSetup.testSetup();

        // Create a new WebSocketChannel object
        WebSocketChannel webSocketChannel = new WebSocketChannel("MessageTest:sendToAllInChannel");

        // Create new Client objects and subscribe them to the channel
        int clientsAmount = 5;
        Client[] clients = new Client[clientsAmount];
        List<UUID> recipients = new ArrayList<>();
        for(int i = 0; i < clientsAmount; i++) {
            clients[i] = new Client(null);
            clients[i].subscribeToChannel(webSocketChannel);
        }

        // Dispatch a PushMessageEvent
        String messageContent = "message";
        PushMessageEvent event = new PushMessageEvent(webSocketChannel, messageContent, recipients);
        EventInitiator.trigger(event);

        // Test whether the WebSocketMessageQueue contains the message for all clients
        for(Client client : clients) {
            String jsonMessage = MessageCreator.clientPush(client, 200, "Push Message", event.getWebSocketChannel().getName(), event.getMessage());
            Assert.assertTrue(WebSocketMessageQueue.getInstance().isReceivingMessage(client, jsonMessage));
        }
    }

    @Test
    public void sendToSpecificInChannel() throws NoUuidAvailableException, InvalidMessageException {
        TestSetup.testSetup();

        // Create a new WebSocketChannel object
        WebSocketChannel webSocketChannel = new WebSocketChannel("MessageTest:sendToSpecificInChannel");

        // Create new Client objects and subscribe them to the channel
        // Only the clients with even index will be added to the recipients list
        int clientsAmount = 5;
        Client[] clients = new Client[clientsAmount];
        List<UUID> recipients = new ArrayList<>();
        for(int i = 0; i < clientsAmount; i++) {
            clients[i] = new Client(null);
            clients[i].subscribeToChannel(webSocketChannel);
            if(i % 2 == 0) {
                recipients.add(clients[i].getUuid());
            }
        }

        // Dispatch a PushMessageEvent
        String messageContent = "message";
        PushMessageEvent event = new PushMessageEvent(webSocketChannel, messageContent, recipients);
        EventInitiator.trigger(event);

        // Test whether the WebSocketMessageQueue contains the message for the recipients
        for(Client client : clients) {
            String jsonMessage = MessageCreator.clientPush(client, 200, "Push Message", event.getWebSocketChannel().getName(), event.getMessage());
            if(recipients.contains(client.getUuid())) {
                Assert.assertTrue(WebSocketMessageQueue.getInstance().isReceivingMessage(client, jsonMessage));
            } else {
                Assert.assertFalse(WebSocketMessageQueue.getInstance().isReceivingMessage(client, jsonMessage));
            }
        }
    }
}