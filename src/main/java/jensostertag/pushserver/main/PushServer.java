package jensostertag.pushserver.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import jensostertag.pushserver.data.Config;
import jensostertag.pushserver.events.MessageEvent;
import jensostertag.pushserver.events.initiators.MessageEventInitiator;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.exceptions.NoUuidAvailableException;
import jensostertag.pushserver.objects.Client;
import jensostertag.pushserver.objects.UnregisteredWebSocketMessage;
import jensostertag.pushserver.objects.WebSocketMessage;
import jensostertag.pushserver.protocol.MessageCreator;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class PushServer extends WebSocketServer {
    public PushServer(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake handshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int code, String reason, boolean remote) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        try {
            MessageEvent event = new MessageEvent(webSocket, message);
            MessageEventInitiator.trigger(event);
        } catch(InvalidMessageException | JsonProcessingException e) {
            try {
                String responseMessage = (e instanceof InvalidMessageException) ? "Invalid message" : "Bad JSON";
                String jsonMessage = MessageCreator.error(400, responseMessage, message);
                WebSocketMessage webSocketMessage = new UnregisteredWebSocketMessage(webSocket, jsonMessage);
                WebSocketMessageQueue.getInstance().queueMessage(webSocketMessage);
            } catch (InvalidMessageException f) {
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

    }

    public static void main(String[] args) {
        PushServer pushServer = new PushServer(new InetSocketAddress("0.0.0.0", Config.PORT_C2S));
        pushServer.run();
    }
}
