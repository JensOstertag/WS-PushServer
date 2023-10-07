import jensostertag.pushserver.main.WebSocketMessageQueue;
import jensostertag.pushserver.objects.UnregisteredWebSocketMessage;
import jensostertag.pushserver.util.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WebSocketMessageQueueTest {
    @Test
    @SuppressWarnings("deprecation")
    public void testMessageQueue() throws InterruptedException {
        Logger.setLogLevel(2);

        String messageContent = "message";

        WebSocketMessageQueue messageQueue = new WebSocketMessageQueue();

        UnregisteredWebSocketMessage unregisteredWebSocketMessage = new UnregisteredWebSocketMessage(null, messageContent);
        messageQueue.queueMessage(unregisteredWebSocketMessage);

        Assert.assertTrue(messageQueue.isReceivingMessage(null, messageContent));

        Assert.assertFalse(messageQueue.isReceivingMessage(null, "wrongMessage"));

        messageQueue.start();

        // Wait for the message to be sent
        Thread.sleep(100);

        Assert.assertFalse(messageQueue.isReceivingMessage(null, messageContent));
    }
}
