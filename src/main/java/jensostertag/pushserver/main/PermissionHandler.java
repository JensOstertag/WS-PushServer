package jensostertag.pushserver.main;

import jensostertag.pushserver.exceptions.ClientNotFoundException;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.WebSocketChannel;
import org.java_websocket.WebSocket;

import java.util.UUID;

public class PermissionHandler {
    public static boolean hasPermission(UUID targetUuid, WebSocket webSocket) throws ClientNotFoundException {
        Client client = Client.getClient(targetUuid);

        return client.getWebSocket() == webSocket;
    }

    public static boolean hasPermission(String channelToken, WebSocketChannel webSocketChannel) {
        if(channelToken == null) {
            return false;
        }

        return channelToken.equals(webSocketChannel.getToken());
    }
}
