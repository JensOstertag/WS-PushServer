package jensostertag.pushserver.main;

import jensostertag.pushserver.data.Config;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        // Start push server
        PushServer pushServer = new PushServer(new InetSocketAddress("0.0.0.0", Config.PORT_C2S));
        pushServer.run();
    }
}
