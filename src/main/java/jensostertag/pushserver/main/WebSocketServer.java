package jensostertag.pushserver.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import jensostertag.pushserver.event.EventInitiator;
import jensostertag.pushserver.event.websocket.ConnectEvent;
import jensostertag.pushserver.event.websocket.DisconnectEvent;
import jensostertag.pushserver.event.websocket.MessageEvent;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.objects.UnregisteredWebSocketMessage;
import jensostertag.pushserver.objects.WebSocketMessage;
import jensostertag.pushserver.protocol.MessageCreator;
import jensostertag.pushserver.util.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;

public class WebSocketServer extends org.java_websocket.server.WebSocketServer {
    public WebSocketServer(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake handshake) {
        ConnectEvent event = new ConnectEvent(webSocket);
        EventInitiator.trigger(event);

        new Logger("Server").log("Client connected");
    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {
        DisconnectEvent event = new DisconnectEvent(webSocket);
        EventInitiator.trigger(event);

        new Logger("Server").log("Client disconnected");
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        try {
            MessageEvent event = new MessageEvent(webSocket, message);
            EventInitiator.trigger(event);
        } catch(InvalidMessageException | JsonProcessingException e) {
            try {
                String responseMessage = (e instanceof InvalidMessageException) ? "Invalid message" : "Bad JSON";
                String jsonMessage = MessageCreator.error(400, responseMessage, message);
                WebSocketMessage webSocketMessage = new UnregisteredWebSocketMessage(webSocket, jsonMessage);
                WebSocketMessageQueue.getInstance().queueMessage(webSocketMessage);
            } catch(InvalidMessageException f) {
                // Catch block left empty on purpose
                // This exception is already handled by the MessageCreator
            }
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception exception) {

    }

    @Override
    public void onStart() {
        new Logger("WebSocket-Server").log("Starting server...");

        // Start WebSocketMessageQueue for outgoing messages
        new Logger("WebSocket-Server").log("Starting WebSocketMessageQueue");
        WebSocketMessageQueue.getInstance().start();

        new Logger("WebSocket-Server").log("Server started");
    }
}
