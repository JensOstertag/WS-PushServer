package jensostertag.pushserver.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import jensostertag.pushserver.exceptions.InvalidMessageException;
import jensostertag.pushserver.util.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MessageValidator {
    private static final String[][] SCHEMAS = {
        {"ERROR", "src/main/resources/schemas/error.schema.json"},
        {"CLIENT_SUBSCRIBE", "src/main/resources/schemas/clients/subscribe.schema.json"},
        {"CLIENT_UNSUBSCRIBE", "src/main/resources/schemas/clients/unsubscribe.schema.json"},
        {"CLIENT_ACK", "src/main/resources/schemas/clients/ack.schema.json"},
        {"CLIENT_PUSH", "src/main/resources/schemas/clients/push.schema.json"},
        {"SERVER_BROADCAST", "src/main/resources/schemas/servers/broadcast.schema.json"},
        {"SERVER_SEND_TO", "src/main/resources/schemas/servers/send_to.schema.json"}
    };

    public static MessageType getMessageType(String jsonString) throws JsonProcessingException, InvalidMessageException {
        for(String[] schema : MessageValidator.SCHEMAS) {
            if(MessageValidator.isValid(jsonString, schema[1])) {
                return MessageType.valueOf(schema[0]);
            }
        }

        throw new InvalidMessageException("Invalid message");
    }

    public static boolean isValid(String jsonString, String schemaPath) throws JsonProcessingException {
        JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);

        try {
            File schemaFile = new File(schemaPath);
            InputStream schemaInputStream = new FileInputStream(schemaFile);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            return schemaFactory.getSchema(schemaInputStream).validate(jsonNode).isEmpty();
        } catch(FileNotFoundException e) {
            new Logger("MessageValidator").error("Cannot find schema file: " + schemaPath);
            return false;
        }
    }
}
