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
        {"ERROR",                      "src/resources/schema/error.schema.json"},

        {"CLIENT_SUBSCRIBE_CHANNEL",   "src/resources/schema/client/subscribeChannel.schema.json"},
        {"CLIENT_UNSUBSCRIBE_CHANNEL", "src/resources/schema/client/unsubscribeChannel.schema.json"},
        {"CLIENT_ACK",                 "src/resources/schema/client/ack.schema.json"},
        {"CLIENT_PUSH",                "src/resources/schema/client/push.schema.json"},

        {"SERVER_CHANNEL_CREATE",      "src/resources/schema/server/createChannel.schema.json"},
        {"SERVER_CHANNEL_DELETE",      "src/resources/schema/server/deleteChannel.schema.json"},
        {"SERVER_CHANNEL_PING",        "src/resources/schema/server/pingChannel.schema.json"},
        {"SERVER_PUSH_MESSAGE",        "src/resources/schema/server/pushMessage.schema.json"},
        {"SERVER_ACK",                 "src/resources/schema/server/ack.schema.json"},
        {"SERVER_SYSADMIN",            "src/resources/schema/server/sysadmin.schema.json"}
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
