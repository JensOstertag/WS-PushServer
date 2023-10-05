package jensostertag.pushserver.main;

import jensostertag.pushserver.data.Config;
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

        // Start HTTP server
        HttpServer httpServer = new HttpServer(new InetSocketAddress("0.0.0.0", Config.PORT_S2S));
        httpServer.start();

        // Start WebSocket server
        WebSocketServer webSocketServer = new WebSocketServer(new InetSocketAddress("0.0.0.0", Config.PORT_C2S));
        webSocketServer.run();
    }
}
