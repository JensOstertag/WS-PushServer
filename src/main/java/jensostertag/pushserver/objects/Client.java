package jensostertag.pushserver.objects;

import jensostertag.pushserver.data.Config;
import jensostertag.pushserver.exceptions.ClientNotFoundException;
import jensostertag.pushserver.exceptions.NoUuidAvailableException;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Client {
    private static final HashMap<UUID, Client> CLIENTS = new HashMap<>();
    private final UUID _uuid;
    private final WebSocket _webSocket;
    private final ArrayList<WebSocketChannel> _subscribedChannels = new ArrayList<>();

    public Client(WebSocket webSocket) throws NoUuidAvailableException {
        UUID uuid = null;
        int i = 0;
        do {
            uuid = UUID.randomUUID();
            if(!Client.CLIENTS.containsKey(uuid)) {
                break;
            }

            i++;
        } while(i < Config.RETRY_GENERATE_UUID);
        if(Client.CLIENTS.containsKey(uuid)) {
            throw new NoUuidAvailableException("Cannot generate UUID for the client");
        }

        this._uuid = uuid;
        this._webSocket = webSocket;

        Client.CLIENTS.put(this._uuid, this);
    }

    public UUID getUuid() {
        return this._uuid;
    }

    public WebSocket getWebSocket() {
        return this._webSocket;
    }

    public boolean hasSubscribedToChannel(WebSocketChannel channel) {
        return this._subscribedChannels.contains(channel);
    }

    public void subscribeToChannel(WebSocketChannel channel) {
        this._subscribedChannels.add(channel);
    }

    public void unsubscribeFromChannel(WebSocketChannel channel) {
        this._subscribedChannels.remove(channel);
    }

    public List<WebSocketChannel> getSubscribedChannels() {
        return this._subscribedChannels.stream().toList();
    }

    public void destroy() {
        Client.CLIENTS.put(this._uuid, null);
    }

    public static Client getClient(UUID uuid) throws ClientNotFoundException {
        if(!Client.CLIENTS.containsKey(uuid)) {
            throw new ClientNotFoundException("Client with UUID " + uuid.toString() + " not found");
        }

        return Client.CLIENTS.get(uuid);
    }

    public static Client getClient(WebSocket webSocket) throws ClientNotFoundException {
        for(Client client : Client.CLIENTS.values()) {
            if(client.getWebSocket().equals(webSocket)) {
                return client;
            }
        }

        throw new ClientNotFoundException("Client with WebSocket " + webSocket.toString() + " not found");
    }

    public static List<Client> getClients() {
        return Client.CLIENTS.values().stream().toList();
    }
}
