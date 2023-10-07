package jensostertag.pushserver.main;

import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.UnregisteredWebSocketMessage;
import jensostertag.pushserver.objects.WebSocketChannel;
import jensostertag.pushserver.objects.WebSocketMessage;
import jensostertag.pushserver.util.Logger;
import org.java_websocket.WebSocket;

import java.util.LinkedList;
import java.util.Queue;

public class WebSocketMessageQueue extends Thread {
    private static final WebSocketMessageQueue INSTANCE = new WebSocketMessageQueue();

    public static WebSocketMessageQueue getInstance() {
        return INSTANCE;
    }

    private final Queue<WebSocketMessage> _messageQueue = new LinkedList<>();

    private WebSocketMessageQueue() {

    }

    @Override
    public void run() {
        while(true) {
            if(this._messageQueue.peek() == null) {
                try {
                    Thread.sleep(50);
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }

            WebSocketMessage message = this._messageQueue.poll();
            this.sendWebSocketMessage(message);
        }
    }

    private void sendWebSocketMessage(WebSocketMessage message) {
        WebSocket webSocket = null;
        if(message instanceof UnregisteredWebSocketMessage) {
            webSocket = ((UnregisteredWebSocketMessage) message).getWebSocket();
        } else {
            Client client = message.getClient();
            webSocket = client.getWebSocket();

            if(message.getWebSocketChannel() != null && !client.hasSubscribedToChannel(message.getWebSocketChannel())) {
                return;
            }
        }

        if(webSocket == null) {
            return;
        }

        if(!webSocket.isOpen() || webSocket.isClosing() || webSocket.isClosed()) {
            return;
        }

        webSocket.send(message.getMessage());
    }

    public void queueMessage(WebSocketMessage message) {
        this._messageQueue.add(message);
    }

    public void queueMessage(Client client, String message, WebSocketChannel webSocketChannel) {
        this.queueMessage(new WebSocketMessage(client, message, webSocketChannel));
    }

    public boolean isReceivingMessage(Client client, String message) {
        new Logger("WebSocketMessageQueue").debug("Pending WebSocketMessages for client " + (client != null ? client.getUuid() : "null"));
        this._messageQueue.stream().filter(webSocketMessage -> webSocketMessage.getClient() == client).forEach(webSocketMessage -> new Logger("WebSocketMessageQueue").debug(webSocketMessage.getMessage()));
        new Logger("WebSocketMessageQueue").debug("End of pending WebSocketMessages for client " + (client != null ? client.getUuid() : "null"));
        return this._messageQueue.stream().anyMatch(webSocketMessage -> webSocketMessage.getClient() == client && webSocketMessage.getMessage().equals(message));
    }
}
