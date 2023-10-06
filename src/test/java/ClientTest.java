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
        TestSetup.testSetup();

        // Create a new Client object
        Client client = new Client(null);
        UUID uuid = client.getUuid();

        // Test whether the client can be found
        Assert.assertEquals(client, Client.getClient(uuid));

        // Create a new WebSocketChannel object
        WebSocketChannel webSocketChannel = new WebSocketChannel("channelName");

        // Test whether the client isn't subscribed to the channel
        Assert.assertFalse(webSocketChannel.getClients().contains(client));
        Assert.assertFalse(client.getSubscribedChannels().contains(webSocketChannel));

        // Subscribe the client to the channel
        client.subscribeToChannel(webSocketChannel);

        // Test whether the client is subscribed to the channel
        Assert.assertTrue(webSocketChannel.getClients().contains(client));
        Assert.assertTrue(client.getSubscribedChannels().contains(webSocketChannel));

        // Unsubscribe the client from the channel
        client.unsubscribeFromChannel(webSocketChannel);

        // Test whether the client isn't subscribed to the channel
        Assert.assertFalse(webSocketChannel.getClients().contains(client));
        Assert.assertFalse(client.getSubscribedChannels().contains(webSocketChannel));
    }
}