package jensostertag.pushserver.main;

import jensostertag.pushserver.main.httphandler.*;
import jensostertag.pushserver.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServer extends Thread {
    private final InetSocketAddress _inetSocketAddress;

    private HttpServer() {
        this._inetSocketAddress = null;
    }

    public HttpServer(InetSocketAddress inetSocketAddress) {
        this._inetSocketAddress = inetSocketAddress;
    }

    @Override
    public void run() {
        new Logger("HTTP-Server").log("Starting server...");

        try {
            com.sun.net.httpserver.HttpServer httpServer = com.sun.net.httpserver.HttpServer.create(this._inetSocketAddress, 0);

            new Logger("HTTP-Server").log("Setting up routes");
            httpServer.createContext("/sysadmin", new HttpSysadminHandler());
            httpServer.createContext("/channel/ping", new HttpChannelPing());
            httpServer.createContext("/channel/create", new HttpChannelCreate());
            httpServer.createContext("/channel/delete", new HttpChannelDelete());
            httpServer.createContext("/push", new HttpPushMessageHandler());
            httpServer.createContext("/js/pushserver.js", new HttpJsScript());

            httpServer.start();
        } catch(IOException e) {
            new Logger("HTTP-Server").log("Failed to start HTTP server");
            System.exit(1);
        }

        new Logger("HTTP-Server").log("Server started");
    }
}
