import jensostertag.pushserver.event.EventInitiator;
import jensostertag.pushserver.event.websocket.ClientSubscribeEvent;
import jensostertag.pushserver.event.websocket.ClientUnsubscribeEvent;
import jensostertag.pushserver.exceptions.ClientNotFoundException;
import jensostertag.pushserver.exceptions.NoUuidAvailableException;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.WebSocketChannel;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

public class ClientTest {
    @Test
    public void clientConnect() throws NoUuidAvailableException, ClientNotFoundException {
        TestSetup ts = new TestSetup();

        Assert.assertThrows(ClientNotFoundException.class, () -> Client.getClient(UUID.randomUUID()));

        // Create a new Client object
        Client client = new Client(null);
        UUID uuid = client.getUuid();

        // Test whether the client can be found
        Assert.assertEquals(client, Client.getClient(uuid));

        // Create a new WebSocketChannel object
        WebSocketChannel webSocketChannel = new WebSocketChannel("ClientTest:clientConnect");

        // Test whether the client isn't subscribed to the channel
        Assert.assertFalse(webSocketChannel.getClients().contains(client));
        Assert.assertFalse(client.getSubscribedChannels().contains(webSocketChannel));

        // Subscribe the client to the channel
        ClientSubscribeEvent subscribeEvent = new ClientSubscribeEvent(client, webSocketChannel);
        EventInitiator.trigger(subscribeEvent);

        // Test whether the client is subscribed to the channel
        Assert.assertTrue(webSocketChannel.getClients().contains(client));
        Assert.assertTrue(client.getSubscribedChannels().contains(webSocketChannel));

        // Unsubscribe the client from the channel
        ClientUnsubscribeEvent unsubscribeEvent = new ClientUnsubscribeEvent(client, webSocketChannel);
        EventInitiator.trigger(unsubscribeEvent);

        // Test whether the client isn't subscribed to the channel
        Assert.assertFalse(webSocketChannel.getClients().contains(client));
        Assert.assertFalse(client.getSubscribedChannels().contains(webSocketChannel));

        // Subscribe the client to the channel again
        EventInitiator.trigger(subscribeEvent);

        // Destroy the client
        client.destroy();

        // Test whether the client can't be found anymore
        Assert.assertThrows(ClientNotFoundException.class, () -> Client.getClient(uuid));

        // Test whether the client isn't subscribed to the channel anymore
        Assert.assertFalse(webSocketChannel.getClients().contains(client));
        Assert.assertFalse(client.getSubscribedChannels().contains(webSocketChannel));
    }
}