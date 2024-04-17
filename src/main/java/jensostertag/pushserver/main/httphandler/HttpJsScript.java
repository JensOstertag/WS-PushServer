package jensostertag.pushserver.main.httphandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import jensostertag.pushserver.data.DockerVariables;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.main.PermissionHandler;
import jensostertag.pushserver.objects.WebSocketChannel;
import jensostertag.pushserver.protocol.MessageCreator;
import jensostertag.pushserver.protocol.MessageType;
import jensostertag.pushserver.protocol.MessageValidator;

import java.io.*;

public class HttpJsScript implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "";
        int responseCode = -1;

        File jsScript = new File(DockerVariables.scriptsBaseDirectory() + "js/pushserver.js");
        try(FileInputStream jsScriptReader = new FileInputStream(jsScript)) {
            if(!jsScript.exists()) {
                response = MessageCreator.error(500, "Internal Server Error", null);
                responseCode = 500;
            } else {
                StringBuilder scriptBuilder = new StringBuilder();
                int i;
                while((i = jsScriptReader.read()) != -1) {
                    scriptBuilder.append((char) i);
                }
                response = scriptBuilder.toString();
                responseCode = 200;
            }
        } catch(InvalidMessageException e) {
            try {
                response = MessageCreator.error(500, "Internal Server Error", null);
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
