import jensostertag.pushserver.exceptions.NoUuidAvailableException;
import jensostertag.pushserver.exceptions.WebSocketChannelNotFoundException;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.WebSocketChannel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebSocketChannelTest {
    @Test
    public void testChannel() throws WebSocketChannelNotFoundException, NoUuidAvailableException {
        TestSetup ts = new TestSetup();

        // Test whether the channel doesn't exist
        Assert.assertThrows(WebSocketChannelNotFoundException.class, () -> WebSocketChannel.getWebSocketChannel("WebSocketChannelTest:testChannel"));
        Assert.assertNull(WebSocketChannel.getWebSocketChannelNullable("WebSocketChannelTest:testChannel"));

        // Create a new WebSocketChannel object
        WebSocketChannel webSocketChannel = new WebSocketChannel("WebSocketChannelTest:testChannel");

        // Test whether the channel exists
        Assert.assertEquals(webSocketChannel, WebSocketChannel.getWebSocketChannel("WebSocketChannelTest:testChannel"));
        Assert.assertEquals(webSocketChannel, WebSocketChannel.getWebSocketChannelNullable("WebSocketChannelTest:testChannel"));
        Assert.assertTrue(WebSocketChannel.getWebSocketChannels().contains(webSocketChannel));

        // Create a new Client object and subscribe him to the channel
        Client client = new Client(null);
        client.subscribeToChannel(webSocketChannel);

        // Test whether the client is subscribed to the channel
        Assert.assertTrue(webSocketChannel.getClients().contains(client));
        Assert.assertTrue(client.getSubscribedChannels().contains(webSocketChannel));

        // Destroy the channel
        webSocketChannel.destroy();

        // Test whether the channel doesn't exist anymore
        Assert.assertThrows(WebSocketChannelNotFoundException.class, () -> WebSocketChannel.getWebSocketChannel("WebSocketChannelTest:testChannel"));
        Assert.assertNull(WebSocketChannel.getWebSocketChannelNullable("WebSocketChannelTest:testChannel"));
        Assert.assertFalse(WebSocketChannel.getWebSocketChannels().contains(webSocketChannel));

        // Test whether the client isn't subscribed to the channel anymore
        Assert.assertFalse(webSocketChannel.getClients().contains(client));
        Assert.assertFalse(client.getSubscribedChannels().contains(webSocketChannel));
    }
}
