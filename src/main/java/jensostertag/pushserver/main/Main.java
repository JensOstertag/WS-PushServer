package jensostertag.pushserver.main;

import jensostertag.pushserver.data.Config;
import jensostertag.pushserver.event.EventInitiator;
import jensostertag.pushserver.eventhandler.ClientSubscribeHandler;
import jensostertag.pushserver.eventhandler.ClientUnsubscribeHandler;
import jensostertag.pushserver.eventhandler.ConnectHandler;
import jensostertag.pushserver.eventhandler.MessageHandler;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        // Event handlers
        EventInitiator.registerListener(new ConnectHandler());
        EventInitiator.registerListener(new MessageHandler());
        EventInitiator.registerListener(new ClientSubscribeHandler());
        EventInitiator.registerListener(new ClientUnsubscribeHandler());

        // Start push server
        PushServer pushServer = new PushServer(new InetSocketAddress("0.0.0.0", Config.PORT_C2S));
        pushServer.run();
    }
}
