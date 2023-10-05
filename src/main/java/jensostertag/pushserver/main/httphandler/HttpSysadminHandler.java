package jensostertag.pushserver.main.httphandler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.protocol.MessageCreator;

import java.io.IOException;
import java.io.OutputStream;

public class HttpSysadminHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        int responseCode = -1;

        try {
            response = MessageCreator.sysadmin(200, "OK");
            responseCode = 200;
        } catch(InvalidMessageException e) {
            try {
                response = MessageCreator.error(500, "Internal Server Error", "Internal Server Error");
                responseCode = 500;
            } catch(InvalidMessageException f) {
                response = "Internal Server Error";
                responseCode = 500;
            }
        }

        httpExchange.sendResponseHeaders(responseCode, response.length());

        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
