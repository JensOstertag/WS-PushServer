import jensostertag.pushserver.main.WebSocketMessageQueue;
import jensostertag.pushserver.objects.UnregisteredWebSocketMessage;
import jensostertag.pushserver.util.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebSocketMessageQueueTest {
    @Test
    public void testMessageQueue() throws InterruptedException {
        Logger.setLogLevel(2);

        String messageContent = "message";

        UnregisteredWebSocketMessage unregisteredWebSocketMessage = new UnregisteredWebSocketMessage(null, messageContent);
        WebSocketMessageQueue.getInstance().queueMessage(unregisteredWebSocketMessage);

        Assert.assertTrue(WebSocketMessageQueue.getInstance().isReceivingMessage(null, messageContent));

        Assert.assertFalse(WebSocketMessageQueue.getInstance().isReceivingMessage(null, "wrongMessage"));

        WebSocketMessageQueue.getInstance().start();

        // Wait for the message to be sent
        Thread.sleep(100);

        Assert.assertFalse(WebSocketMessageQueue.getInstance().isReceivingMessage(null, messageContent));
    }
}
