package jensostertag.pushserver.main;

import jensostertag.pushserver.data.DockerVariables;
import jensostertag.pushserver.event.EventInitiator;
import jensostertag.pushserver.eventhandler.*;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        // Event handlers
        EventInitiator.registerListener(new ConnectHandler());
        EventInitiator.registerListener(new DisconnectHandler());
        EventInitiator.registerListener(new MessageHandler());
        EventInitiator.registerListener(new ClientSubscribeHandler());
        EventInitiator.registerListener(new ClientUnsubscribeHandler());
        EventInitiator.registerListener(new PushMessageHandler());

        // Start HTTP server
        HttpServer httpServer = new HttpServer(new InetSocketAddress("0.0.0.0", DockerVariables.getPortHttp()));
        httpServer.start();

        // Start WebSocket server
        WebSocketServer webSocketServer = new WebSocketServer(new InetSocketAddress("0.0.0.0", DockerVariables.getPortWs()));
        webSocketServer.run();
    }
}
