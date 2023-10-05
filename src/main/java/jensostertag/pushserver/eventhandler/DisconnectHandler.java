package jensostertag.pushserver.eventhandler;

import jensostertag.pushserver.event.EventHandler;
import jensostertag.pushserver.event.Listener;
import jensostertag.pushserver.event.websocket.ConnectEvent;
import jensostertag.pushserver.exceptions.ClientNotFoundException;
import jensostertag.pushserver.objects.Client;

public class DisconnectHandler implements Listener {
    @EventHandler
    public void onDisconnect(ConnectEvent event) {
        try {
            Client client = Client.getClient(event.getWebSocket());
            client.destroy();
        } catch(ClientNotFoundException e) {
            // Catch block left empty on purpose
            // If no client was found, there is no need to destroy one
        }
    }
}
