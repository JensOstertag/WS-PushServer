import jensostertag.pushserver.main.PermissionHandler;
import jensostertag.pushserver.objects.WebSocketChannel;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PermissionHandlerTest {
    @Test
    public void testChannelPermission() {
        WebSocketChannel webSocketChannel = new WebSocketChannel("PermissionHandlerTest:testChannelPermission");
        String channelToken = webSocketChannel.getToken();

        Assert.assertFalse(PermissionHandler.hasPermission(null, webSocketChannel));
        Assert.assertFalse(PermissionHandler.hasPermission("", webSocketChannel));
        Assert.assertFalse(PermissionHandler.hasPermission("wrongToken", webSocketChannel));
        Assert.assertTrue(PermissionHandler.hasPermission(channelToken, webSocketChannel));
    }
}
